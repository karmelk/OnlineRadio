package com.onlinestation.fragment.searchradios

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.onlinestation.R
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import com.onlinestation.fragment.searchradios.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

/**
 * A simple [Fragment] subclass.
 */

class SearchFragment : Fragment() {
    private lateinit var searchStationAdapter: SearchStationAdapter
    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var balanceCount: OwnerUserBalance
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initFragmentView()
        initViewModel()
    }

    private fun initData() {
        searchViewModel.getBalanceData()?.apply {
            balanceCount = this
        }
    }

    private fun initFragmentView() {
        searchStationRV.layoutManager = LinearLayoutManager(requireContext())
        searchStationAdapter = SearchStationAdapter(
            mutableListOf(),
            { item ->
                searchViewModel.addStationLocalDB(item)
            },
            { item ->
                searchStationAdapter.removeFavoriteItem(item)
                searchViewModel.removeStationLocalDB(item.id)
            })
        searchStationRV.adapter = searchStationAdapter
        searchStationRV.scheduleLayoutAnimation()
    }

    private fun initViewModel() {
        searchViewModel.getSearchStationLD.observe(viewLifecycleOwner, Observer {
            searchStationAdapter.updateList(it)
        })
        searchViewModel.successAddStationLD.observe(viewLifecycleOwner, Observer {
            searchStationAdapter.updateSuccessItem(it)
        })
        searchViewModel.errorAddStationLD.observe(viewLifecycleOwner, Observer {
            searchStationAdapter.updateErrorItem(it)
            Toast.makeText(
                requireContext(),
                "Can not save radio",
                Toast.LENGTH_SHORT
            ).show()
        })
        searchViewModel.errorNotBalanceLD.observe(viewLifecycleOwner, Observer {
            searchStationAdapter.updateErrorItem(it)
            Toast.makeText(
                requireContext(),
                "Your balance is empty",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    fun searchRadio(searchKeyword: String, genre: String?) {
        searchViewModel.searchStation(searchKeyword, genre)
    }
}
