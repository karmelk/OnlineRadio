package com.nextidea.onlinestation.fragment.favorite

import android.widget.Toast
import com.nextidea.onlinestation.appbase.FragmentBaseMVVM
import com.nextidea.onlinestation.activity.MainActivity
import com.nextidea.onlinestation.appbase.utils.viewBinding
import com.nextidea.onlinestation.databinding.FragmentFavoriteBinding
import com.nextidea.onlinestation.domain.entities.StationItem
import com.nextidea.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel
import com.nextidea.onlinestation.service.PlayingRadioLibrary
import com.nextidea.onlinestation.utils.gone
import com.nextidea.onlinestation.utils.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : FragmentBaseMVVM<FavoriteViewModel, FragmentFavoriteBinding>() {

    private lateinit var stationAdapter: FavoriteListStationAdapter
    private val playingRadioLibrary: PlayingRadioLibrary by inject()

    override val viewModel: FavoriteViewModel by viewModel()
    override val binding: FragmentFavoriteBinding by viewBinding()

    override fun onView() {
        stationAdapter = FavoriteListStationAdapter(
            { addRemoveStation(it) },
            { stationId -> viewModel.loadData(stationId.toLong()) }
        )
        binding.favoriteStationRV.adapter = stationAdapter
    }

    override fun onEach() {
        with(viewModel){
            onEach(loadStation){
                playingRadioLibrary.updateLibraryStation(
                    viewModel.getStationsListData.value!!,
                    this@FavoriteFragment::class.java.simpleName
                )
                (context as MainActivity).mMediaBrowserHelper?.getTransportControls()
                    ?.playFromMediaId(it, null)
            }

            onEach(isShowEmptyDataContainer){ isShow ->
                with(binding) {
                    if (isShow == true) {
                        favoriteStationRV.gone()
                        dataNotFound.show()
                    } else {
                        favoriteStationRV.show()
                        dataNotFound.gone()
                    }
                }
            }

            onEach(addedOrRemovedFavorite){
                shearViewModel?.sendFavoriteStation(it)
            }

            onEach(getStationsListData){
                stationAdapter.submitList(it)
            }

            onEach(errorAddStationLD){
                Toast.makeText(
                    requireContext(),
                    "Can not save radio",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun addRemoveStation(item: StationItem) {
        viewModel.addRemoveStationItem(item)
    }

}