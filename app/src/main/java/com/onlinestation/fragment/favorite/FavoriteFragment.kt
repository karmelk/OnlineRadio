package com.onlinestation.fragment.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.onlinestation.R
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import com.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private lateinit var favoriteStationAdapter: FavoriteListStationAdapter
    private val myViewModel: FavoriteViewModel by viewModel()
    private val stationList: MutableList<StationItemLocal> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModel.getFavoriteStationList()
     //   initFragmentView()
        initViewModel()
    }

  /*  private fun initFragmentView() {
        favoriteStationRV.layoutManager = LinearLayoutManager(requireContext())
        favoriteStationAdapter = FavoriteListStationAdapter(stationList){itemId ->
            myViewModel.removeFavoriteItem(itemId)
        }
        favoriteStationRV.adapter = favoriteStationAdapter
        favoriteStationRV.scheduleLayoutAnimation()
    }*/

    private fun initViewModel() {
        myViewModel.getStationsListData.observe(viewLifecycleOwner, Observer {
           // favoriteStationAdapter.updateList(it)
            favoriteStationRV.also {
                it.layoutManager=LinearLayoutManager(requireContext())
                it.adapter = FavoriteListStationAdapter(stationList){itemId ->
                    myViewModel.removeFavoriteItem(itemId)
                }
                it.scheduleLayoutAnimation()
            }
        })
    }
}
