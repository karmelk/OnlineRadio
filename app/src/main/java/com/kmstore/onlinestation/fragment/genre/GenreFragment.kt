package com.kmstore.onlinestation.fragment.genre

import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kmstore.onlinestation.R
import com.kmstore.onlinestation.appbase.FragmentBaseMVVM
import com.kmstore.onlinestation.appbase.utils.viewBinding
import com.kmstore.onlinestation.databinding.FragmentGenreBinding
import com.kmstore.onlinestation.utils.gone
import com.kmstore.onlinestation.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class GenreFragment : FragmentBaseMVVM<GenreViewModel, FragmentGenreBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    override val viewModel: GenreViewModel by viewModel()
    override val binding: FragmentGenreBinding by viewBinding()

    private lateinit var mAdapter: GenreAdapter

    private val layoutManager = LinearLayoutManager(context)

    private var enableLoading = false

    private var lastPage = false

    override fun onView() {

        with(binding) {
            swipeRefresh.setOnRefreshListener(this@GenreFragment)
            swipeRefresh.isRefreshing = true
            mAdapter = GenreAdapter {
                navigateFragment(R.id.navigation_stations_by_genre_id, bundleOf("genderId" to it))
            }
            rvGenre.adapter = mAdapter
            rvGenre.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    Log.d("Scroll", "onScrolled: $enableLoading $lastPage")
                    if (!enableLoading && !lastPage) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            // Log.d("Scroll", "onScrolled: $enableLoading $lastPage")
                            enableLoading = true
                            viewModel.getGenreList()
                        }
                    }
                }
            })
        }
    }

    override fun onEach() {
        with(viewModel) {
            onEach(isShowEmptyDataContainer) { isShow ->
                with(binding) {
                    if (isShow == true) {
                        dataNotFound.show()
                        rvGenre.gone()
                    } else {
                        dataNotFound.gone()
                        rvGenre.show()
                    }
                }
            }

            onEach(getGender) {
                mAdapter.submitList(it)
            }

            onEach(swipeRefreshState) {
                binding.swipeRefresh.isRefreshing = it
            }

            onEach(errorGenderData) {
                Toast.makeText(
                    requireContext(),
                    "Can't load data",
                    Toast.LENGTH_SHORT
                ).show()
            }

            onEach(isLastPage) {
                Log.d("Scroll", "onScrolled:IsLastePage $it ")
                lastPage = it
                enableLoading = false
            }

        }
    }

    override fun onRefresh() {
        viewModel.getGenreList(true)
    }

}

