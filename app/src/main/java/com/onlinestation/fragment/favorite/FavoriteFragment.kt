package com.onlinestation.fragment.favorite

import com.kmworks.appbase.FragmentBaseMVVM
import com.kmworks.appbase.utils.viewBinding
import com.onlinestation.activity.MainActivity
import com.onlinestation.databinding.FragmentFavoriteBinding
import com.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel
import com.onlinestation.service.PlayingRadioLibrary
import kotlinx.android.synthetic.main.fragment_station_list_by_genre_id.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : FragmentBaseMVVM<FavoriteViewModel, FragmentFavoriteBinding>() {

    private lateinit var stationAdapter: FavoriteListStationAdapter
    private val playingRadioLibrary: PlayingRadioLibrary by inject()

    override val viewModel: FavoriteViewModel by viewModel()
    override val binding: FragmentFavoriteBinding by viewBinding()


    override fun onView() {
        stationAdapter = FavoriteListStationAdapter{
            viewModel.removeFavoriteItem(it)
        }
        binding.favoriteStationRV.adapter = stationAdapter
    }

    override fun observes() {
        observe(viewModel.loadStation) {
            playingRadioLibrary.updateLibraryStation(
                viewModel.getStationsListData.value!!,
                this@FavoriteFragment::class.java.simpleName
            )
            (context as MainActivity).mMediaBrowserHelper?.getTransportControls()
                ?.playFromMediaId(it, null)
        }
        observe(viewModel.getStationsListData){
            stationAdapter.submitList(it)

        }
    }

    override fun navigateUp() {
        navigateBackStack()
    }

}
