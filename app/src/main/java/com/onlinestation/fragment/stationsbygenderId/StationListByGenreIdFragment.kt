package com.onlinestation.fragment.stationsbygenderId

import android.widget.*
import androidx.lifecycle.Observer
import com.kmworks.appbase.FragmentBaseMVVM
import com.kmworks.appbase.utils.viewBinding

import com.onlinestation.activity.MainActivity
import com.onlinestation.databinding.FragmentStationListByGenreIdBinding
import com.onlinestation.fragment.stationsbygenderId.viewmodel.StationListByGenreIdViewModel
import com.onlinestation.service.PlayingRadioLibrary
import kotlinx.android.synthetic.main.fragment_station_list_by_genre_id.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class StationListByGenreIdFragment :
    FragmentBaseMVVM<StationListByGenreIdViewModel, FragmentStationListByGenreIdBinding>() {

    override val viewModel: StationListByGenreIdViewModel by viewModel()
    override val binding: FragmentStationListByGenreIdBinding by viewBinding()

    private lateinit var stationAdapter: StationListByGenreIdAdapter
    private val playingRadioLibrary: PlayingRadioLibrary by inject()
    private var genreId: Long = 0

    companion object {
        const val GENRE_ID = "genreId"
    }

    override fun initData() {
        genreId = arguments?.getLong(GENRE_ID, 0) ?: 0
    }

    override fun onView() {
        initFragmentView()
        initViewModel()
        viewModel.getStationsByGenreIdList(genreId)
    }

    override fun observes() {
        observe(viewModel.getStationsListLD) {
            stationAdapter.submitList(it)
        }
        observe(viewModel.errorNotBalanceLD) {
            Toast.makeText(
                requireContext(),
                "Your balance is empty",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun initFragmentView() {
        stationAdapter = StationListByGenreIdAdapter()
        stationsByGenreIdRV.adapter = stationAdapter
    }

    private fun initViewModel() {
        viewModel.loadStation.observe(viewLifecycleOwner, Observer {
            playingRadioLibrary.updateLibraryStation(
                viewModel.getStationsListLD.value!!,
                this@StationListByGenreIdFragment::class.java.simpleName
            )
            (context as MainActivity).mMediaBrowserHelper?.getTransportControls()
                ?.playFromMediaId(it, null)
        })
    }

    override fun navigateUp() {
        navigateBackStack()
    }
}