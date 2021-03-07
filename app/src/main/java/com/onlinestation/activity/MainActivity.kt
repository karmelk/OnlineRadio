package com.onlinestation.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ListView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.kmworks.appbase.utils.viewBinding
import com.onlinestation.R
import com.onlinestation.adapter.PopUpGenderAdapter
import com.onlinestation.databinding.ActivityMainBinding
import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.fragment.searchradios.SearchFragment
import com.onlinestation.service.MediaBrowserHelper
import com.onlinestation.service.PlayingRadioLibrary
import com.onlinestation.service.RadioService
import com.onlinestation.utils.getCurrentFragment
import com.onlinestation.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    val mainViewModel: MainViewModel by viewModel()
    private val binding: ActivityMainBinding by viewBinding()
    private val playingRadioLibrary: PlayingRadioLibrary by inject()
    private lateinit var navHostFragment: NavHostFragment
    lateinit var nav: NavController
    private lateinit var navOptions: NavOptions
    private var searchKeyword: String? = null
    private var selectedGenderName: String? = null
    private var genreList: MutableList<GenderItem>? = null
    var mMediaBrowserHelper: MediaBrowserHelper? = null
    private var mIsPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        initObserves()
        initData()
        initClickListener()
        mMediaBrowserHelper = MediaBrowserConnection(this)
        mMediaBrowserHelper?.registerCallback(MediaBrowserListener())
    }

    private fun initObserves() {
        with(mainViewModel) {
            primaryGenreDB.observe(this@MainActivity, Observer(::showSearchView))
            nextStation.observe(this@MainActivity, Observer {
                mMediaBrowserHelper?.getTransportControls()?.skipToNext()
            })
            previewStation.observe(this@MainActivity, Observer {
                mMediaBrowserHelper?.getTransportControls()?.skipToPrevious()
            })
            playPause.observe(this@MainActivity, Observer {
                if (mIsPlaying) {
                    mMediaBrowserHelper?.getTransportControls()?.stop()
                } else {
                    mMediaBrowserHelper?.getTransportControls()?.play()
                }
            })
        }
    }

    private fun showSearchView(genreList: MutableList<GenderItem>) {
        this.genreList = genreList
        with(binding) {
            icSearchIcon.visibility = GONE
            appName.visibility = GONE
            searchContainer.visibility = VISIBLE
            tabLayout.visibility = GONE
            editSearch.requestFocus()
            editSearch.hint = resources.getString(R.string.search)
            nav.navigate(R.id.navigation_search_radios)
        }
    }

    private fun initClickListener() {
        with(binding) {
            icSearchIcon.setOnClickListener {
                mainViewModel.getGenderListDB()
            }
            icCloseSearch.setOnClickListener {
                searchContainer.visibility = GONE
                icSearchIcon.visibility = VISIBLE
                appName.visibility = VISIBLE
                editSearch.text?.clear()
                tabLayout.visibility = VISIBLE
                selectedGenderName = null
                hideKeyboard(this@MainActivity, window.decorView)
                nav.navigateUp()
            }
            icSettings.setOnClickListener {
                genreList?.apply {
                    selectCategory(it, this)
                }
            }
            icSearch.setOnClickListener {
                getCurrentFragment<SearchFragment>(navHostFragment)?.run {
                    searchKeyword?.apply {
                        if (length >= 2) {
                            this@run.searchRadio(this, selectedGenderName)
                        }
                    }
                }
            }
            editSearch.doOnTextChanged { text, _, _, _ ->
                text?.run {
                    searchKeyword = if (length >= 2) {
                        this.toString()
                    } else {
                        null
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mMediaBrowserHelper?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMediaBrowserHelper?.onStop()
    }

    private fun selectCategory(
        it: View,
        mutableList: MutableList<GenderItem>
    ) {

        val lInflater: LayoutInflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = lInflater.inflate(R.layout.popup_category_dialog, null)
        val rvGenderr = view.findViewById<ListView>(R.id.rvGenre)
        val myPopupWindow = PopupWindow(
            view,
            resources.getDimensionPixelSize(R.dimen.dp_180),
            resources.getDimensionPixelSize(R.dimen.dp_240),
            true
        )
        val customAdapter = PopUpGenderAdapter(
            this,
            mutableList
        ) { genderName ->
            selectedGenderName = genderName
        }
        rvGenderr.adapter = customAdapter
        myPopupWindow.showAsDropDown(it)
    }

    private fun initData() {
        mainViewModel.initBalance()
    }

    private fun setupView() {
        nav = Navigation.findNavController(this, R.id.navHost)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment

        navOptions = NavOptions.Builder()
            .setLaunchSingleTop(false)
            .setPopUpTo(nav.graph.startDestination, false)
            .build()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.apply {
                    when (position) {
                        0 -> nav.navigate(R.id.navigation_home, null, navOptions)
                        1 -> nav.navigate(R.id.navigation_random_station, null, navOptions)
                        2 -> nav.navigate(R.id.navigation_favorite, null, navOptions)
                        3 -> nav.navigate(R.id.navigation_settings, null, navOptions)
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        with(binding) {
            if (searchContainer.isVisible) {
                searchContainer.visibility = GONE
                icSearchIcon.visibility = VISIBLE
                appName.visibility = VISIBLE
                editSearch.text?.clear()
                tabLayout.visibility = VISIBLE
                hideKeyboard(this@MainActivity, window.decorView)
                tabLayout.touchables?.forEach { it.isEnabled = true }
            } else {
                tabLayout.getTabAt(0)?.select()
            }
        }
        super.onBackPressed()
    }


    private class MediaBrowserConnection(context: Context) : MediaBrowserHelper(
        context,
        RadioService::class.java
    ) {

        override fun onConnected(mediaController: MediaControllerCompat) {
            Log.i("MainActivityDIS", "onConnected: ")
        }

        override fun onDisconnected() {
            Log.i("MainActivityDIS", "onDisconnected: ")
        }

        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem?>
        ) {
            super.onChildrenLoaded(parentId, children)
            val mediaController = getMediaController()

            // Queue up all media items for this simple sample.
            for (mediaItem in children) {
                mediaController.addQueueItem(mediaItem!!.description)
            }

            // Call prepare now so pressing play just works.
            mediaController.transportControls.prepare()
        }
    }

    private inner class MediaBrowserListener : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(playbackState: PlaybackStateCompat?) {

            playbackState?.let {
                if (playbackState.state == PlaybackStateCompat.STATE_PLAYING) {
                    it.state == PlaybackStateCompat.STATE_PLAYING
                    mainViewModel.playPauseIcon.value = true
                    mIsPlaying = true
                } else {
                    it.state == PlaybackStateCompat.STATE_STOPPED
                    mainViewModel.playPauseIcon.value = false
                    mIsPlaying = false
                }
            } ?: kotlin.run {
                mainViewModel.playPauseIcon.value = false
                mIsPlaying = false
            }
        }

        override fun onMetadataChanged(mediaMetadata: MediaMetadataCompat?) {
            if (mediaMetadata == null) {
                return
            }
            val mediaId = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
            mainViewModel.checkStationInDB(mediaId.toInt())
            val url = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI)

            Glide.with(this@MainActivity)
                .load(url)
                .circleCrop()
                .placeholder(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_default_station
                    )
                )
                .into(vStationIcon)
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
        }

        override fun onQueueChanged(queue: List<MediaSessionCompat.QueueItem>) {
            super.onQueueChanged(queue)
        }

    }
}
