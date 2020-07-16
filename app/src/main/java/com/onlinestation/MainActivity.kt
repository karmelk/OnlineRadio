package com.onlinestation

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
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
import com.google.android.material.tabs.TabLayout
import com.onlinestation.adapter.PopUpGenderAdapter
import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.fragment.searchradios.SearchFragment
import com.onlinestation.onlineradioapp.MainViewModel
import com.onlinestation.utils.Constants.Companion.defaultUserBalanceCount
import com.onlinestation.utils.Constants.Companion.defaultUserID
import com.onlinestation.utils.getCurrentFragment
import com.onlinestation.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var navHostFragment: NavHostFragment
    lateinit var nav: NavController
    private lateinit var navOptions: NavOptions
    private var searchKeyword: String? = null
    private var selectedGenderName: String? = null
    private var genreList: MutableList<GenderItem>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        initData()
        setupView()
        initClickListener()
    }

    private fun initViewModel() {
        mainViewModel.primaryGenreDB.observe(this, Observer(::showSearchView))
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

}
