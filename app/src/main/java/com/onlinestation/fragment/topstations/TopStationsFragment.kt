package com.onlinestation.fragment.topstations

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.onlinestation.appbase.FragmentBaseMVVM
import com.onlinestation.activity.MainActivity
import com.onlinestation.appbase.utils.viewBinding
import com.onlinestation.databinding.FragmentTopStationBinding
import com.onlinestation.domain.entities.StationItem
import com.onlinestation.service.PlayingRadioLibrary
import com.onlinestation.utils.gone
import com.onlinestation.utils.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopStationsFragment : FragmentBaseMVVM<TopStationViewModel, FragmentTopStationBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    override val viewModel: TopStationViewModel by viewModel()

    override val binding: FragmentTopStationBinding by viewBinding()

    private lateinit var topStationAdapter: TopStationAdapter

    private val playingRadioLibrary: PlayingRadioLibrary by inject()

    override fun onView() {

        with(binding) {
            swipeRefresh.setOnRefreshListener(this@TopStationsFragment)
            swipeRefresh.isRefreshing = true
            topStationRV.layoutManager = LinearLayoutManager(requireContext())
            topStationAdapter = TopStationAdapter(
                { item ->
                    viewModel.addStationLocalDB(item)
                },
                { stationId ->
                    viewModel.loadData(stationId.toLong())
                })
            topStationRV.adapter = topStationAdapter
            topStationRV.scheduleLayoutAnimation()
        }
        viewModel.getTopStationList()
    }

    override fun onEach() {
        with(viewModel){

            onEach(loadStation){
                playingRadioLibrary.updateLibraryStation(
                    viewModel.getTopStation.value!!,
                    this@TopStationsFragment::class.java.simpleName
                )
                (context as? MainActivity)?.mMediaBrowserHelper?.getTransportControls()
                    ?.playFromMediaId(it, null)
            }

            onEach(addedOrRemovedIsFavorite){
                shearViewModel?.sendFavoriteStation(it)
            }

            onEach(showEmptyDataContainer){
                with(binding) {
                    topStationRV.gone()
                    dataNotFound.show()
                }
            }

            onEach(getTopStation){
                with(binding) {
                    dataNotFound.gone()
                }
                topStationAdapter.submitList(it)
                binding.swipeRefresh.isRefreshing = false
            }

            onEach(errorStationsList){
                with(binding) {
                    swipeRefresh.isRefreshing = false
                }
                Toast.makeText(
                    requireContext(),
                    "Can't load data",
                    Toast.LENGTH_SHORT
                ).show()
            }

            onEach(errorAddStation){
                Toast.makeText(
                    requireContext(),
                    "Can not save radio",
                    Toast.LENGTH_SHORT
                ).show()
            }

            onEach(errorNotBalance){
                Toast.makeText(
                    requireContext(),
                    "Your balance is empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun addRemoveStation(item: StationItem) {
        viewModel.addStationLocalDB(item)
    }

    override fun onRefresh() {
        viewModel.getTopStationList()
    }
}
