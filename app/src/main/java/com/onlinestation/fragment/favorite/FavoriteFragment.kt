package com.onlinestation.fragment.favorite

import android.widget.Toast
import com.onlinestation.appbase.FragmentBaseMVVM
import com.onlinestation.activity.MainActivity
import com.onlinestation.appbase.utils.viewBinding
import com.onlinestation.databinding.FragmentFavoriteBinding
import com.onlinestation.domain.entities.StationItem
import com.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel
import com.onlinestation.service.PlayingRadioLibrary
import com.onlinestation.utils.gone
import com.onlinestation.utils.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : FragmentBaseMVVM<FavoriteViewModel, FragmentFavoriteBinding>() {

    private lateinit var stationAdapter: FavoriteListStationAdapter
    private val playingRadioLibrary: PlayingRadioLibrary by inject()

    override val viewModel: FavoriteViewModel by viewModel()
    override val binding: FragmentFavoriteBinding by viewBinding()

    override fun onView() {
        stationAdapter = FavoriteListStationAdapter(
            {
                addRemoveStation(it)
            },
            { stationId ->
                viewModel.loadData(stationId.toLong())
            }
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

            onEach(showEmptyDataContainer){
                binding.dataNotFound.show()
            }

            onEach(addedOrRemovedIsFavorite){
                shearViewModel?.sendFavoriteStation(it)
            }

            onEach(getStationsListData){
                binding.dataNotFound.gone()
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
