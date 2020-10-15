package com.onlinestation.activity

import android.content.Context
import android.net.Uri
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
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util.getUserAgent
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.kmworks.appbase.Constants.Companion.defaultUserBalanceCount
import com.kmworks.appbase.Constants.Companion.defaultUserID
import com.kmworks.appbase.command.Command.PlayStation
import com.onlinestation.R
import com.onlinestation.adapter.PopUpGenderAdapter
import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.fragment.searchradios.SearchFragment
import com.onlinestation.service.MediaBrowserHelper
import com.onlinestation.service.PlayingRadioLibrary
import com.onlinestation.service.RadioService
import com.onlinestation.utils.getCurrentFragment
import com.onlinestation.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var navHostFragment: NavHostFragment
    lateinit var nav: NavController
    private lateinit var navOptions: NavOptions
    private var searchKeyword: String? = null
    private var selectedGenderName: String? = null
    private var genreList: MutableList<GenderItem>? = null
    private lateinit var mMediaBrowserHelper: MediaBrowserHelper
    private var mIsPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initObserver()
        initData()
        setupView()
        PlayingRadioLibrary.init()
        setupMediaBrowserHelper()
        initClickListener()
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object :AdListener(){
            override fun onAdClosed() {
                Log.i(TAG, "onAdClosed: ")
            }

            override fun onAdFailedToLoad(p0: Int) {
                Log.i(TAG, "onAdFailedToLoad: ")
            }

            override fun onAdLeftApplication() {
                Log.i(TAG, "onAdLeftApplication: ")
            }

            override fun onAdOpened() {
                Log.i(TAG, "onAdOpened: ")
            }

            override fun onAdLoaded() {
                Log.i(TAG, "onAdLoaded: ")
            }

            override fun onAdClicked() {
                Log.i(TAG, "onAdClicked: ")   
            }
        }
    }



    private fun initObserver() {
        mainViewModel.primaryGenreDB.observe(this, Observer(::showSearchView))
        mainViewModel.command.observe(this, {
            when (it) {
                is PlayStation -> {
                    if (mIsPlaying) {
                        mMediaBrowserHelper.getTransportControls()!!.pause()
                    } else {
                        mMediaBrowserHelper.getTransportControls()!!.play()
                    }
                }
            }
        })
    }

    private fun showSearchView(genreList: MutableList<GenderItem>) {
        this.genreList = genreList
        icSearchIcon.visibility = GONE
        appName.visibility = GONE
        searchContainer.visibility = VISIBLE
        tabLayout.visibility = GONE
        editSearch.requestFocus()
        editSearch.hint = resources.getString(R.string.search)
        nav.navigate(R.id.navigation_search_radios)
    }

    private fun initClickListener() {
        appName.setOnClickListener {
            mainViewModel.loadRadio(this)
        }
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
            hideKeyboard(this, window.decorView)
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

    private fun selectCategory(
        it: View,
        mutableList: MutableList<GenderItem>
    ) {

        val lInflater: LayoutInflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = lInflater.inflate(R.layout.popup_category_dialog, null)
        val rvGenderr = view.findViewById<ListView>(R.id.rvGender)
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
        val balance = mainViewModel.getBalance()
        if (balance == null) {
            setInitBalance()
        }
    }

    private fun setInitBalance() {
        mainViewModel.setDefaultBalance(defaultUserID, defaultUserBalanceCount)
    }

    private fun setupView() {
        nav = Navigation.findNavController(this@MainActivity, R.id.navigationHostFragment)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigationHostFragment) as NavHostFragment

        navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(nav.graph.startDestination, false)
            .build()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

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
        if (searchContainer.isVisible) {
            searchContainer.visibility = GONE
            icSearchIcon.visibility = VISIBLE
            appName.visibility = VISIBLE
            editSearch.text?.clear()
            tabLayout.visibility = VISIBLE
            hideKeyboard(this, window.decorView)
            tabLayout?.touchables?.forEach { it.isEnabled = true }
        } else {
            tabLayout.getTabAt(0)?.select()
        }
        super.onBackPressed()
    }


    private fun extractMediaSourceFromUri(uri: Uri): MediaSource {
        val userAgent = getUserAgent(this, "Exo")
        return ExtractorMediaSource.Factory(DefaultDataSourceFactory(this, userAgent))
            .setExtractorsFactory(DefaultExtractorsFactory()).createMediaSource(uri)
    }

    private fun setupMediaBrowserHelper() {
        mMediaBrowserHelper = MediaBrowserConnection(this)
        mMediaBrowserHelper.registerCallback(MediaBrowserListener())
    }
    override fun onStart() {
        super.onStart()
        mMediaBrowserHelper.onStart()
    }

    override fun onStop() {
        super.onStop()
        // mSeekBarAudio.disconnectController()
        mMediaBrowserHelper.onStop()
    }

    private class MediaBrowserConnection(context: Context) : MediaBrowserHelper(
        context,
        RadioService::class.java
    ) {

        override fun onConnected(mediaController: MediaControllerCompat) {
            // mSeekBarAudio.setMediaController(mediaController)
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
            mIsPlaying = playbackState != null &&
                    playbackState.state == PlaybackStateCompat.STATE_PLAYING
            media_controls.isPressed = mIsPlaying
        }

        override fun onMetadataChanged(mediaMetadata: MediaMetadataCompat?) {
            if (mediaMetadata == null) {
                return
            }
           /* song_title.text = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
            song_artist.text = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
            album_art.setImageBitmap(
                PlayingRadioLibrary.getAlbumBitmap(
                    this@MainActivity,
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
                )
            )*/
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
        }

        override fun onQueueChanged(queue: List<MediaSessionCompat.QueueItem>) {
            super.onQueueChanged(queue)
        }

    }
}
