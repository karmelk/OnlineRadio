package com.kmstore.onlinestation.fragment.searchradios

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kmstore.onlinestation.appbase.FragmentBaseMVVM
import com.kmstore.onlinestation.activity.MainActivity
import com.kmstore.onlinestation.appbase.utils.viewBinding
import com.kmstore.onlinestation.databinding.FragmentSearchBinding
import com.kmstore.onlinestation.domain.entities.StationItem
import com.kmstore.onlinestation.fragment.searchradios.viewmodel.SearchViewModel
import com.kmstore.onlinestation.service.PlayingRadioLibrary
import com.kmstore.onlinestation.utils.gone
import com.kmstore.onlinestation.utils.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : FragmentBaseMVVM<SearchViewModel, FragmentSearchBinding>() {

    override val viewModel: SearchViewModel by viewModel()
    override val binding: FragmentSearchBinding by viewBinding()

    private val playingRadioLibrary: PlayingRadioLibrary by inject()
    private lateinit var searchStationAdapter: SearchStationAdapter

    override fun onView() {
        with(binding) {
            searchStationRV.layoutManager = LinearLayoutManager(requireContext())
            searchStationAdapter = SearchStationAdapter(
                { item ->
                    addRemoveStation(item)
                },
                { stationId ->
                    viewModel.loadData(stationId.toLong())
                })
            searchStationRV.adapter = searchStationAdapter
            searchStationRV.scheduleLayoutAnimation()
        }
    }

    override fun onEach() {
        with(viewModel) {
            onEach(getSearchStationLD) {
                binding.dataNotFound.gone()
                searchStationAdapter.submitList(it)
            }
            onEach(errorAddStationLD) {
                context?.let {
                    Toast.makeText(
                        it,
                        "Can not save radio",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            onEach(errorNotBalanceLD) {
                context?.let {
                    Toast.makeText(
                        it,
                        "Your balance is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            onEach(loadStation) {
                playingRadioLibrary.updateLibraryStation(
                    viewModel.getSearchStationLD.value!!,
                    this@SearchFragment::class.java.simpleName
                )
                (context as MainActivity).mMediaBrowserHelper?.getTransportControls()
                    ?.playFromMediaId(it, null)
            }
            onEach(isShowEmptyDataContainer) {
                binding.dataNotFound.show()
            }
            onEach(addedOrRemovedFavorite) {
                shearViewModel?.sendFavoriteStation(it)
            }
        }
    }

    fun searchRadio(keyword: String) {
        viewModel.searchStation(keyword)
    }

    fun addRemoveStation(item: StationItem) {
        viewModel.addRemoveStation(item)
    }

}
