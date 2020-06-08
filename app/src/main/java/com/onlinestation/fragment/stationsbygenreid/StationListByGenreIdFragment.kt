package com.onlinestation.fragment.stationsbygenreid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.onlinestation.R
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import com.onlinestation.fragment.stationsbygenreid.viewmodel.StationListByGenreIdViewModel
import kotlinx.android.synthetic.main.fragment_station_list_by_genre_id.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class StationListByGenreIdFragment : Fragment() {

    private lateinit var stationAdapter: StationListByGenreIdAdapter
    private val stationsListViewModel: StationListByGenreIdViewModel by viewModel()

    private var genreId: Int = 0
    private lateinit var balanceCount: OwnerUserBalance
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        genreId = arguments?.getInt("genreId", 0) ?: 0
        return inflater.inflate(R.layout.fragment_station_list_by_genre_id, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initFragmentView()
        initViewModel()
        stationsListViewModel.getStationsByGenreIdList(genreId)
    }

    private fun initData() {
        stationsListViewModel.getBalanceData()?.apply {
            balanceCount = this
        }
    }

    private fun initFragmentView() {
        stationsByGenreIdRV.layoutManager = LinearLayoutManager(requireContext())
        stationAdapter =
            StationListByGenreIdAdapter(mutableListOf(),
                { item ->
                    stationsListViewModel.addStationLocalDB(item)
                },
                { item ->
                    stationAdapter.removeFavoriteItem(item)
                    stationsListViewModel.removeStationLocalDB(item.id)
                })
        stationsByGenreIdRV.adapter = stationAdapter
        stationsByGenreIdRV.scheduleLayoutAnimation()
    }

    private fun initViewModel() {
        stationsListViewModel.getStationsListLD.observe(viewLifecycleOwner, Observer {
            stationAdapter.updateList(it)
        })

        stationsListViewModel.successAddStationLD.observe(viewLifecycleOwner, Observer {
            stationAdapter.updateSuccessItem(it)
        })
        stationsListViewModel.errorAddStationLD.observe(viewLifecycleOwner, Observer {
            stationAdapter.updateErrorItem(it)
            Toast.makeText(
                requireContext(),
                "Can not save radio",
                Toast.LENGTH_SHORT
            ).show()
        })
        stationsListViewModel.errorNotBalanceLD.observe(viewLifecycleOwner, Observer {
            stationAdapter.updateErrorItem(it)
            Toast.makeText(
                requireContext(),
                "Your balance is empty",
                Toast.LENGTH_SHORT
            ).show()
        })
    }
}
