package com.onlinestation.fragment.topstations

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kmworks.appbase.FragmentBaseMVVM
import com.kmworks.appbase.utils.viewBinding
import com.onlinestation.databinding.FragmentTopStationBinding
import com.onlinestation.fragment.topstations.viewmodel.TopStationViewModel
import com.onlinestation.service.PlayingRadioLibrary
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopStationsFragment : FragmentBaseMVVM<TopStationViewModel, FragmentTopStationBinding>() {
    private lateinit var topStationAdapter: TopStationAdapter
    override val viewModel: TopStationViewModel by viewModel()
    override val binding: FragmentTopStationBinding by viewBinding()
    private val playingRadioLibrary: PlayingRadioLibrary by inject()

    override fun onView() {
        with(binding) {
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
    }

    override fun observes() {
        with(viewModel) {
            observe(getTopStationLD) {
                topStationAdapter.submitList(it)
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

}
