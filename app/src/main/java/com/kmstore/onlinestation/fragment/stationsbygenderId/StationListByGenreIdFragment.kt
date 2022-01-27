package com.kmstore.onlinestation.fragment.stationsbygenderId

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kmstore.onlinestation.appbase.FragmentBaseMVVM
import com.kmstore.onlinestation.activity.MainActivity
import com.kmstore.onlinestation.appbase.utils.viewBinding
import com.kmstore.onlinestation.databinding.FragmentStationListByGenreIdBinding
import com.kmstore.onlinestation.domain.entities.StationItem
import com.kmstore.onlinestation.fragment.stationsbygenderId.viewmodel.StationListByGenreIdViewModel
import com.kmstore.onlinestation.service.PlayingRadioLibrary
import com.kmstore.onlinestation.utils.gone
import com.kmstore.onlinestation.utils.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class StationListByGenreIdFragment :
    FragmentBaseMVVM<StationListByGenreIdViewModel, FragmentStationListByGenreIdBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    override val viewModel: StationListByGenreIdViewModel by viewModel()
    override val binding: FragmentStationListByGenreIdBinding by viewBinding()

    private lateinit var stationAdapter: StationListByGenreIdAdapter
    private val playingRadioLibrary: PlayingRadioLibrary by inject()
    private var genreId: Long = 0

    private val layoutManager = LinearLayoutManager(context)

    private var enableLoading = false

    private var lastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        genreId = arguments?.getLong("genderId") ?: -1
    }

    override fun onView() {

        with(binding) {
            swipeRefresh.setOnRefreshListener(this@StationListByGenreIdFragment)
            swipeRefresh.isRefreshing = true

            stationAdapter = StationListByGenreIdAdapter(
                { item ->
                    viewModel.addRemoveStationItem(item)
                },
                { stationId ->
                    viewModel.loadData(stationId.toLong())
                }
            )
            stationsByGenreIdRV.adapter = stationAdapter
            stationsByGenreIdRV.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
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
                            viewModel.getStationsByGenreIdList(genreId)
                        }
                    }
                }
            })
        }
        viewModel.getStationsByGenreIdList(genreId)
    }

    override fun onEach() {
        with(viewModel) {

            onEach(isShowEmptyDataContainer) { isShow ->
                with(binding) {
                    if (isShow == true) {
                        dataNotFound.show()
                        stationsByGenreIdRV.gone()
                    } else {
                        dataNotFound.gone()
                        stationsByGenreIdRV.show()
                    }
                }
            }

            onEach(isLastPage) {
                Log.d("Scroll", "onScrolled:IsLastePage $it ")
                lastPage = it
                enableLoading = false
                stationAdapter.onLoadingState(!it)
            }
            onEach(swipeRefreshState) {
                binding.swipeRefresh.isRefreshing = it
            }

            onEach(addedOrRemovedFavorite) {
                shearViewModel?.sendFavoriteStation(it)
            }

            onEach(loadStation) {
                playingRadioLibrary.updateLibraryStation(
                    viewModel.getStationsList.value!!,
                    this@StationListByGenreIdFragment::class.java.simpleName
                )
                (context as MainActivity).mMediaBrowserHelper?.getTransportControls()
                    ?.playFromMediaId(it, null)
            }

            onEach(getStationsList) {
                stationAdapter.submitList(it)
            }

            onEach(errorAddStation) {
                Toast.makeText(
                    requireContext(),
                    "Can not save radio",
                    Toast.LENGTH_SHORT
                ).show()
            }

            onEach(errorLoadStations) {
                Toast.makeText(
                    context,
                    "Can't load data",
                    Toast.LENGTH_SHORT
                ).show()
            }

            onEach(errorNotBalance) {
                Toast.makeText(
                    context,
                    "Your balance is empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun addRemoveStation(item: StationItem) {
        viewModel.addRemoveStationItem(item)
    }

    override fun onRefresh() {
        viewModel.getStationsByGenreIdList(genreId, true)
        stationAdapter.onLoadingState(true)
    }

}
