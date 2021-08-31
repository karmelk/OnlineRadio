package com.onlinestation.fragment.stationsbygenderId

import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.onlinestation.appbase.FragmentBaseMVVM
import com.onlinestation.activity.MainActivity
import com.onlinestation.appbase.utils.viewBinding
import com.onlinestation.databinding.FragmentStationListByGenreIdBinding
import com.onlinestation.domain.entities.StationItem
import com.onlinestation.fragment.genre.GenreFragmentArgs
import com.onlinestation.fragment.stationsbygenderId.viewmodel.StationListByGenreIdViewModel
import com.onlinestation.service.PlayingRadioLibrary
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class StationListByGenreIdFragment :
    FragmentBaseMVVM<StationListByGenreIdViewModel, FragmentStationListByGenreIdBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    override val viewModel: StationListByGenreIdViewModel by viewModel()
    override val binding: FragmentStationListByGenreIdBinding by viewBinding()

    private lateinit var stationAdapter: StationListByGenreIdAdapter
    private val playingRadioLibrary: PlayingRadioLibrary by inject()

    //private val args: GenreFragmentArgs by navArgs()
    private var genreId: Long = 0

    override fun onView() {
        binding.swipeRefresh.setOnRefreshListener(this)
        genreId = 3
        initView()
        viewModel.getStationsByGenreIdList(genreId)

    }

    override fun onEach() {
        with(viewModel) {
            onEach(addedOrRemovedIsFavorite) {
                shearViewModel?.sendFavoriteStation(it)
            }

            onEach(loadStation) {
                playingRadioLibrary.updateLibraryStation(
                    viewModel.getStationsList.value!!,
                    this@StationListByGenreIdFragment::class.java.simpleName
                )
                (context as MainActivity).mMediaBrowserHelper?.getTransportControls()
                    ?.playFromMediaId(it, null)
            }

            onEach(getStationsList) {
                stationAdapter.submitList(it)
                binding.swipeRefresh.isRefreshing = false
            }

            onEach(errorLoadStations) {
                binding.swipeRefresh.isRefreshing = false
            }

            onEach(errorAddStation) {
                Toast.makeText(
                    requireContext(),
                    "Can not save radio",
                    Toast.LENGTH_SHORT
                ).show()
            }

            onEach(errorNotBalance) {
                Toast.makeText(
                    context,
                    "Your balance is empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initView() {
        stationAdapter = StationListByGenreIdAdapter(
            { item ->
                viewModel.addRemoveStationItem(item)
            },
            { stationId ->
                viewModel.loadData(stationId.toLong())
            }
        )
        binding.stationsByGenreIdRV.adapter = stationAdapter

    }

//    private fun initViewModel() {
//        viewModel.loadStation.observe(viewLifecycleOwner, Observer {
//            playingRadioLibrary.updateLibraryStation(
//                viewModel.getStationsList.value!!,
//                this@StationListByGenreIdFragment::class.java.simpleName
//            )
//            (context as MainActivity).mMediaBrowserHelper?.getTransportControls()
//                ?.playFromMediaId(it, null)
//        })
//    }

    fun addRemoveStation(item: StationItem) {
        viewModel.addRemoveStationItem(item)
    }

    override fun onRefresh() {
        viewModel.getStationsByGenreIdList(genreId)
    }

}
