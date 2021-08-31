package com.onlinestation.fragment.genre

import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.onlinestation.appbase.FragmentBaseMVVM
import com.onlinestation.appbase.utils.viewBinding
import com.onlinestation.databinding.FragmentGenreBinding
import com.onlinestation.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class GenreFragment : FragmentBaseMVVM<GenreViewModel, FragmentGenreBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    override val viewModel: GenreViewModel by viewModel()
    override val binding: FragmentGenreBinding by viewBinding()

    private lateinit var mAdapter: GenreAdapter

    override fun onView() {
        requireContext()
        binding.swipeRefresh.setOnRefreshListener(this)
        with(binding) {
            mAdapter = GenreAdapter {
                val directions =
                    GenreFragmentDirections.actionNavigationHomeToNavigationStationsByGenreId()
                navigateFragment(directions)
            }
            rvGenre.adapter = mAdapter
        }
        binding.swipeRefresh.isRefreshing = true
        viewModel.getGenreList()
    }


    override fun onEach() {
        with(viewModel) {
            onEach(showEmptyDataContainer) {
                binding.dataNotFound.show()
            }

            onEach(getGenderData) {
                mAdapter.submitList(it)
                binding.swipeRefresh.isRefreshing = false
            }

            onEach(errorGenderData) {
                binding.swipeRefresh.isRefreshing = false
                Toast.makeText(
                    requireContext(),
                    "Can't load data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    override fun onRefresh() {
        viewModel.getGenreList()
    }

}

