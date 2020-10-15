package com.onlinestation.fragment.primarygenre

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.onlinestation.activity.MainActivity

import com.onlinestation.R
import com.onlinestation.entities.responcemodels.gendermodels.PrimaryGenreItem
import com.onlinestation.fragment.primarygenre.viewmodel.PrimaryGenderViewModel
import kotlinx.android.synthetic.main.fragment_primary_genre.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PrimaryGenreFragment : Fragment() {
    private lateinit var genreAdapter: PrimaryGenreAdapter
    private val myViewModel: PrimaryGenderViewModel by viewModel()
    private val genreList: MutableList<PrimaryGenreItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_primary_genre, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModel.getPrimaryGenderList()
        initFragmentView()
        initViewModel()

    }

    private fun initFragmentView() {
        primaryGenreRV.layoutManager = LinearLayoutManager(requireContext())
        genreAdapter = PrimaryGenreAdapter(genreList) { itemGenreId ->
            val bundle = bundleOf("secondaryGenreID" to itemGenreId)
            (requireActivity() as MainActivity).nav.navigate(
                R.id.action_navigation_home_to_navigation_secondary_genre,
                bundle
            )
        }
        primaryGenreRV.adapter = genreAdapter
        primaryGenreRV.scheduleLayoutAnimation()
    }

    private fun initViewModel() {
        myViewModel.getPrimaryGenreData.observe(viewLifecycleOwner,  {
            genreAdapter.updateList(it)
        })
    }

}

