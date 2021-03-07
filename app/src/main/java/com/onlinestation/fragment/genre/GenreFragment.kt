package com.onlinestation.fragment.genre

import com.kmworks.appbase.FragmentBaseMVVM
import com.kmworks.appbase.utils.viewBinding
import com.onlinestation.activity.MainActivity
import com.onlinestation.databinding.FragmentGenreBinding
import com.onlinestation.fragment.genre.viewmodel.GenreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
class GenreFragment : FragmentBaseMVVM<GenreViewModel, FragmentGenreBinding>() {

    private lateinit var mAdapter: GenreAdapter
    override val viewModel: GenreViewModel by viewModel()
    override val binding: FragmentGenreBinding by viewBinding()
    override fun onView() {
        binding.run {
            mAdapter = GenreAdapter() {
//                navigateFragment(
//                    R.id.action_navigation_home_to_navigation_stations_by_genre_id,
//                    bundleOf(StationListByGenreIdFragment.GENRE_ID to id)
//                )

            }
            rvGenre.adapter = mAdapter
        }
    }

    override fun observes() {
        observe(viewModel.getGenderData) {
            mAdapter.submitList(it)
        }
    }

    override fun navigateUp() {
        (activity as MainActivity).finish()
    }

}

