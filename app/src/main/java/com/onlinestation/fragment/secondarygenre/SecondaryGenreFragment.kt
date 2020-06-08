package com.onlinestation.fragment.secondarygenre

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.onlinestation.MainActivity

import com.onlinestation.R
import com.onlinestation.entities.responcemodels.gendermodels.SecondaryGenreItem
import com.onlinestation.fragment.secondarygenre.viewmodel.SecondaryGenreViewModel
import kotlinx.android.synthetic.main.fragment_secondary_genre.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class SecondaryGenreFragment : Fragment() {
    private lateinit var genreAdapter: SecondaryGenreAdapter
    private val genreList: MutableList<SecondaryGenreItem> = mutableListOf()
    private val myViewModel: SecondaryGenreViewModel by viewModel()
    private var secondaryGenre: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        secondaryGenre = arguments?.getInt("secondaryGenreID") ?: 0
        return inflater.inflate(R.layout.fragment_secondary_genre, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragmentView()
        initViewModel()

        myViewModel.getSecondaryGenderList(secondaryGenre)

    }

    private fun initFragmentView() {
        secondaryGenreRV.layoutManager = LinearLayoutManager(requireContext())
        genreAdapter = SecondaryGenreAdapter(genreList) { itemGenreId ->
            val bundle = bundleOf("genreId" to itemGenreId)
            (requireActivity() as MainActivity).nav.navigate(
                R.id.action_navigation_secondary_genre_to_navigation_stations_by_genre_id,
                bundle
            )
        }
        secondaryGenreRV.adapter = genreAdapter
        secondaryGenreRV.scheduleLayoutAnimation()
    }

    private fun initViewModel() {
        myViewModel.getSecondaryGenreData.observe(viewLifecycleOwner, Observer {
            genreAdapter.updateList(it)
        })
    }
}
