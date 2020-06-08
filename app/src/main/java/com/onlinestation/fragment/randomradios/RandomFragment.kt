package com.onlinestation.fragment.randomradios

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

import com.onlinestation.fragment.randomradios.viewmodel.RandomStationViewModel
import kotlinx.android.synthetic.main.fragment_random.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class RandomFragment : Fragment() {
    private lateinit var randomStationAdapter: RandomStationAdapter
    private val randomViewModel: RandomStationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_random, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragmentView()
        initViewModel()
        randomViewModel.getPrimaryGenderList()
    }

    private fun initFragmentView() {
        randomStationRV.layoutManager = LinearLayoutManager(requireContext())
        randomStationAdapter = RandomStationAdapter(mutableListOf(),
            { item ->
                randomViewModel.addStationLocalDB(item)
            },
            { item ->
                randomStationAdapter.removeFavoriteItem(item)
                randomViewModel.removeStationLocalDB(item.id)
            })
        randomStationRV.adapter = randomStationAdapter
        randomStationRV.scheduleLayoutAnimation()
    }

    private fun initViewModel() {
        randomViewModel.getRandomStationLD.observe(viewLifecycleOwner, Observer {
            randomStationAdapter.updateList(it)
        })
        randomViewModel.successAddStationLD.observe(viewLifecycleOwner, Observer {
            randomStationAdapter.updateSuccessItem(it)
        })
        randomViewModel.errorAddStationLD.observe(viewLifecycleOwner, Observer {
            randomStationAdapter.updateErrorItem(it)
            Toast.makeText(
                requireContext(),
                "Can not save radio",
                Toast.LENGTH_SHORT
            ).show()
        })
        randomViewModel.errorNotBalanceLD.observe(viewLifecycleOwner, Observer {
            randomStationAdapter.updateErrorItem(it)
            Toast.makeText(
                requireContext(),
                "Your balance is empty",
                Toast.LENGTH_SHORT
            ).show()
        })

    }

}
