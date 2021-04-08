package com.onlinestation.fragment.searchradios

import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kmworks.appbase.FragmentBaseMVVM
import com.kmworks.appbase.utils.viewBinding
import com.onlinestation.databinding.FragmentSearchBinding
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.fragment.searchradios.viewmodel.SearchViewModel
import com.onlinestation.service.PlayingRadioLibrary
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */

class SearchFragment : FragmentBaseMVVM<SearchViewModel, FragmentSearchBinding>() {


    override val viewModel: SearchViewModel by viewModel()
    override val binding: FragmentSearchBinding by viewBinding()
    private val playingRadioLibrary: PlayingRadioLibrary by inject()
    private lateinit var searchStationAdapter: SearchStationAdapter
    private lateinit var balanceCount: OwnerUserBalance

    override fun onView() {
        with(binding) {
            searchStationRV.layoutManager = LinearLayoutManager(requireContext())
            searchStationAdapter = SearchStationAdapter(
                { item ->
                    viewModel.addStationLocalDB(item)
                },
                { stationId ->
                    viewModel.loadData(stationId.toLong())
                })
            searchStationRV.adapter = searchStationAdapter
            searchStationRV.scheduleLayoutAnimation()
        }
    }

    override fun observes() {
        with(viewModel) {
            observe(getSearchStationLD) {
                searchStationAdapter.submitList(it)
            }
            observe(errorAddStationLD) {
                Toast.makeText(
                    requireContext(),
                    "Can not save radio",
                    Toast.LENGTH_SHORT
                ).show()
            }
            observe(errorNotBalanceLD) {
                Toast.makeText(
                    requireContext(),
                    "Your balance is empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
            observe(loadStation) {
//                playingRadioLibrary.updateLibraryStation(randomViewModel.getRandomStationLD.value!!,this@RandomFragment::class.java.simpleName)
//                (context as MainActivity).mMediaBrowserHelper?.getTransportControls()
//                    ?.playFromMediaId(it, null)
            }

        }
    }
    fun searchRadio(keyword: String) {
        viewModel.searchStation(keyword)
    }

}
