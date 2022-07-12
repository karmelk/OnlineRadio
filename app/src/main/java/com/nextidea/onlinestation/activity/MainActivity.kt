package com.nextidea.onlinestation.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.nextidea.onlinestation.R
import com.nextidea.onlinestation.appbase.utils.observeInLifecycle
import com.nextidea.onlinestation.appbase.utils.viewBinding
import com.nextidea.onlinestation.databinding.ActivityMainBinding
import com.nextidea.onlinestation.fragment.favorite.FavoriteFragment
import com.nextidea.onlinestation.fragment.searchradios.SearchFragment
import com.nextidea.onlinestation.fragment.stationsbygenderId.StationListByGenreIdFragment
import com.nextidea.onlinestation.fragment.topstations.TopStationsFragment
import com.nextidea.onlinestation.service.MediaBrowserHelper
import com.nextidea.onlinestation.service.PlayingRadioLibrary
import com.nextidea.onlinestation.service.RadioService
import com.nextidea.onlinestation.utils.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private val binding: ActivityMainBinding by viewBinding()
    private val playingRadioLibrary: PlayingRadioLibrary by inject()
    private val shearViewModel: ShearViewModel by viewModel()
    private lateinit var navHostFragment: NavHostFragment
    lateinit var nav: NavController
    private lateinit var navOptions: NavOptions

    var mMediaBrowserHelper: MediaBrowserHelper? = null
    private var isPlaying = false

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
                if (isPlaying) {
                    mMediaBrowserHelper?.getTransportControls()?.stop()
                } else {
                    mMediaBrowserHelper?.getTransportControls()?.play()
                }
            }.observeInLifecycle(this@MainActivity)

            playPauseIcon.onEach {
                //binding.steamingStation.gone()

                if (it) {
                  //  binding.steamingStation.show()
                    binding.playPause.setImageResource(R.drawable.ic_pause_bottom_panel)
                } else {
                    //binding.steamingStation.gone()
                    binding.playPause.setImageResource(R.drawable.ic_play_bottom_panel)
                }
            }.observeInLifecycle(this@MainActivity)

            isFavorite.onEach {
                onFavoriteIcon(it)
            }.observeInLifecycle(this@MainActivity)

            getCurrentFragment<SearchFragment>(navHostFragment)?.run {
                isShowEmptyDataContainer.onEach {
                    Toast.makeText(
                        this@MainActivity,
                        "not found data",
                        Toast.LENGTH_SHORT
                    ).show()
                }.observeInLifecycle(this@MainActivity)
            }
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
            playPause.setOnClickListener { mainViewModel.playPause(!isPlaying) }
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
//            share.setOnClickListener {
//                val sendIntent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
//                    type = "text/plain"
//                }
//                val shareIntent = Intent.createChooser(sendIntent, null)
//                startActivity(shareIntent)
//            }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.actionSearch)
        val actionShare: MenuItem? = menu?.findItem(R.id.actionShare)
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
        actionShare?.setOnMenuItemClickListener {
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupView() {
        nav = Navigation.findNavController(this, R.id.navHost)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment

        navOptions = NavOptions.Builder()
            .setPopUpTo(nav.graph.startDestinationId, false)
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
            if (playbackState == null) {
                mainViewModel.playPauseIcon(false)
                binding.steamingStation.gone()
                isPlaying = false
            } else {
                when (playbackState.state) {
                    PlaybackStateCompat.STATE_BUFFERING -> {
                        // it.state == PlaybackStateCompat.STATE_PLAYING
                        mainViewModel.playPauseIcon(true)
                        binding.steamingStation.show()

                    }
                    PlaybackStateCompat.STATE_PLAYING -> {
                        mainViewModel.playPauseIcon(true)
                        binding.steamingStation.gone()
                        isPlaying = true
                    }
                    PlaybackStateCompat.STATE_STOPPED -> {
                        mainViewModel.playPauseIcon(false)
                        binding.steamingStation.gone()
                        isPlaying = false
                    }

//                    else -> {
//                        //  it.state == PlaybackStateCompat.STATE_STOPPED
//                        mainViewModel.playPauseIcon(false)
//                        binding.steamingStation.gone()
//                        isPlaying = false
//                    }
                }
            }
        }

        override fun onMetadataChanged(mediaMetadata: MediaMetadataCompat?) {
            if (mediaMetadata == null) {
                return
            }
            mainViewModel.playPauseIcon(true)
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

    }
}
