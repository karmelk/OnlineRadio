package com.onlinestation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.onlinestation.R
import com.onlinestation.appbase.utils.observeInLifecycle
import com.onlinestation.appbase.utils.viewBinding
import com.onlinestation.databinding.ActivityMainBinding
import com.onlinestation.fragment.favorite.FavoriteFragment
import com.onlinestation.fragment.searchradios.SearchFragment
import com.onlinestation.fragment.stationsbygenderId.StationListByGenreIdFragment
import com.onlinestation.fragment.topstations.TopStationsFragment
import com.onlinestation.service.MediaBrowserHelper
import com.onlinestation.service.PlayingRadioLibrary
import com.onlinestation.service.RadioService
import com.onlinestation.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private val binding: ActivityMainBinding by viewBinding()
    private val playingRadioLibrary: PlayingRadioLibrary by inject()
    private val shearViewModel: ShearViewModel by viewModel()
    private lateinit var navHostFragment: NavHostFragment
    lateinit var nav: NavController
    private lateinit var navOptions: NavOptions

    var mMediaBrowserHelper: MediaBrowserHelper? = null
    private var mIsPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        initObserves()
        initData()
        initClick()
        mMediaBrowserHelper = MediaBrowserConnection(this)
        mMediaBrowserHelper?.registerCallback(MediaBrowserListener())
    }

    private fun initObserves() {
        shearViewModel.favoriteStation.onEach {
            playingRadioLibrary.getCurrentStation?.let { item ->
                if (item.id == it.id) {
                    playingRadioLibrary.updateCurrentPlayedStation(it)
                    onFavoriteIcon(it.isFavorite)
                }
            }
        }.observeInLifecycle(this@MainActivity)

        with(mainViewModel) {

            errorNotBalanceLD.onEach {
                it ?: return@onEach
                Toast.makeText(
                    this@MainActivity,
                    "Your balance is emptya",
                    Toast.LENGTH_SHORT
                ).show()
            }.observeInLifecycle(this@MainActivity)

            nextStation.onEach {

                mMediaBrowserHelper?.getTransportControls()?.skipToNext()
            }.observeInLifecycle(this@MainActivity)

            previewStation.onEach {
                mMediaBrowserHelper?.getTransportControls()?.skipToPrevious()
            }.observeInLifecycle(this@MainActivity)

            playPause.onEach {
                if (mIsPlaying) {
                    mMediaBrowserHelper?.getTransportControls()?.stop()
                } else {
                    mMediaBrowserHelper?.getTransportControls()?.play()
                }
            }.observeInLifecycle(this@MainActivity)

            playPauseIcon.onEach {
                binding.steamingStation.gone()
                if (it) {
                    binding.playPause.setImageResource(R.drawable.ic_pause_bottom_panel)
                } else {
                    binding.playPause.setImageResource(R.drawable.ic_play_bottom_panel)
                }
            }.observeInLifecycle(this@MainActivity)

            isFavorite.onEach {
                onFavoriteIcon(it)
            }.observeInLifecycle(this@MainActivity)

//            errorNotBalanceLD.observe(this@MainActivity, {
//                Toast.makeText(
//                    this@MainActivity,
//                    "Your balance is empty",
//                    Toast.LENGTH_SHORT
//                ).show()
//            })
            getCurrentFragment<SearchFragment>(navHostFragment)?.run {
//                showEmptyDataContainer.observe(this, {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "not found data",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                })
                showEmptyDataContainer.onEach {
                    Toast.makeText(
                        this@MainActivity,
                        "not found data",
                        Toast.LENGTH_SHORT
                    ).show()
                }.observeInLifecycle(this@MainActivity)
            }

//            nextStation.observe(this@MainActivity, {
//                mMediaBrowserHelper?.getTransportControls()?.skipToNext()
//            })
//            previewStation.observe(this@MainActivity, {
//                mMediaBrowserHelper?.getTransportControls()?.skipToPrevious()
//            })
//            playPause.observe(this@MainActivity, {
//                if (mIsPlaying) {
//                    mMediaBrowserHelper?.getTransportControls()?.stop()
//                } else {
//                    mMediaBrowserHelper?.getTransportControls()?.play()
//                }
//            })
//            playPauseIcon.observe(this@MainActivity, {
//                binding.steamingStation.gone()
//                if (it) {
//                    binding.playPause.setImageResource(R.drawable.ic_pause_bottom_panel)
//                } else {
//                    binding.playPause.setImageResource(R.drawable.ic_play_bottom_panel)
//                }
//            })
//            isFavorite.observe(this@MainActivity, {
//                onFavoriteIcon(it)
//            })
        }
    }

    private fun onFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.favorite.setImageResource(R.drawable.ic_favorite_selected_24dp)
        } else {
            binding.favorite.setImageResource(R.drawable.ic_favorite_unselected_24dp)
        }
    }

    private fun initClick() {
        with(binding) {
            playPause.setOnClickListener { mainViewModel.playPause() }
            next.setOnClickListener { mainViewModel.skipNextStation() }
            previous.setOnClickListener { mainViewModel.skipPreviousStation() }
            favorite.setOnClickListener {
                getCurrentFragment<SearchFragment>(navHostFragment)?.run {
                    it?.apply {
                        playingRadioLibrary.getCurrentStation?.let { item ->
                            this@run.addRemoveStation(item)
                        }
                    }
                }
                getCurrentFragment<TopStationsFragment>(navHostFragment)?.run {
                    it?.apply {
                        playingRadioLibrary.getCurrentStation?.let { item ->
                            this@run.addRemoveStation(item)
                        }
                    }
                }
                getCurrentFragment<StationListByGenreIdFragment>(navHostFragment)?.run {
                    it?.apply {
                        playingRadioLibrary.getCurrentStation?.let { item ->
                            this@run.addRemoveStation(item)
                        }
                    }
                }
                getCurrentFragment<FavoriteFragment>(navHostFragment)?.run {
                    it?.apply {
                        playingRadioLibrary.getCurrentStation?.let { item ->
                            this@run.addRemoveStation(item)
                        }
                    }
                }
            }
            share.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
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

    private fun initData() {
        mainViewModel.initBalance()
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        searchView.textChanges()
            .debounce(300)
            .onEach {
                getCurrentFragment<SearchFragment>(navHostFragment)?.run {
                    it?.apply {
                        this@run.searchRadio(this.toString())
                    }
                }
            }.launchIn(lifecycleScope)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                binding.tabLayout.gone()
                nav.navigate(R.id.navigation_search_radios)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                binding.tabLayout.show()
                nav.popBackStack()
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupView() {
        nav = Navigation.findNavController(this, R.id.navHost)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment

        navOptions = NavOptions.Builder()
          //  .setLaunchSingleTop(true)
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
                    // it.state == PlaybackStateCompat.STATE_PLAYING
                    mainViewModel.playPauseIcon.value = true
                    mIsPlaying = true
                } else {
                    //  it.state == PlaybackStateCompat.STATE_STOPPED
                    mainViewModel.playPauseIcon.value = false
                    mIsPlaying = false
                }
            } ?: run {
                mainViewModel.playPauseIcon.value = false
                mIsPlaying = false
            }
        }

        override fun onMetadataChanged(mediaMetadata: MediaMetadataCompat?) {
            if (mediaMetadata == null) {
                return
            }
            binding.steamingStation.show()
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
                .into(binding.stationIcon)
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
        }

        override fun onQueueChanged(queue: List<MediaSessionCompat.QueueItem>) {
            super.onQueueChanged(queue)
        }

    }
}
