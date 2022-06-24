package com.nextidea.onlinestation.fragment.topstations

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nextidea.onlinestation.appbase.FragmentBaseMVVM
import com.nextidea.onlinestation.activity.MainActivity
import com.nextidea.onlinestation.appbase.utils.viewBinding
import com.nextidea.onlinestation.databinding.FragmentTopStationBinding
import com.nextidea.onlinestation.domain.entities.StationItem
import com.nextidea.onlinestation.service.PlayingRadioLibrary
import com.nextidea.onlinestation.utils.gone
import com.nextidea.onlinestation.utils.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopStationsFragment : FragmentBaseMVVM<TopStationViewModel, FragmentTopStationBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    override val viewModel: TopStationViewModel by viewModel()

    override val binding: FragmentTopStationBinding by viewBinding()

    private lateinit var topStationAdapter: TopStationAdapter

    private val playingRadioLibrary: PlayingRadioLibrary by inject()

    private val layoutManager = LinearLayoutManager(context)

    private var enableLoading = false

    private var lastPage = false

    override fun onView() {

        with(binding) {
            swipeRefresh.setOnRefreshListener(this@TopStationsFragment)
            swipeRefresh.isRefreshing = true
            topStationRV.layoutManager = layoutManager
            topStationAdapter = TopStationAdapter(
                { item ->
                    viewModel.addStationLocalDB(item)
                },
                { stationId ->
                    viewModel.loadData(stationId.toLong())
                })
            topStationRV.adapter = topStationAdapter
            topStationRV.scheduleLayoutAnimation()
            topStationRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if (!enableLoading && !lastPage) {

                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            Log.d("Scroll", "onScrolled: $enableLoading $lastPage")
                            enableLoading = true
                           // topStationAdapter.onProgress(true)
                            viewModel.getTopStationList()
                        }
                    }
                }
            })
        }
    }

    override fun onEach() {
        with(viewModel) {

            onEach(loadStation) {
                playingRadioLibrary.updateLibraryStation(
                    viewModel.getTopStation.value!!,
                    this@TopStationsFragment::class.java.simpleName
                )
                (context as? MainActivity)?.mMediaBrowserHelper?.getTransportControls()
                    ?.playFromMediaId(it, null)
            }

            onEach(addedOrRemovedFavorite) {
                shearViewModel?.sendFavoriteStation(it)
            }

            onEach(isShowEmptyDataContainer) { isShow ->
                with(binding) {
                    if (isShow == true) {
                        topStationRV.gone()
                        dataNotFound.show()
                    } else {
                        topStationRV.show()
                        dataNotFound.gone()
                    }
                }
            }

            onEach(getTopStation) {
                topStationAdapter.submitList(it)
            }

            onEach(isLastPage) {
                Log.d("Scroll", "onScrolled:IsLastePage $it ")
                lastPage = it
                enableLoading = false
                topStationAdapter.onLoadingState(!it)
            }
            onEach(swipeRefreshState) {
                binding.swipeRefresh.isRefreshing = it
            }
            onEach(errorStationsList) {
                Toast.makeText(
                    context,
                    "Can't load data",
                    Toast.LENGTH_SHORT
                ).show()
            }

            onEach(errorAddStation) {
                Toast.makeText(
                    context,
                    "Can not save radio",
                    Toast.LENGTH_SHORT
                ).show()
            }

            onEach(errorNotBalance) {
                Toast.makeText(
                    requireContext(),
                    "Your balance is empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun addRemoveStation(item: StationItem) {
        viewModel.addStationLocalDB(item)
    }

    override fun onRefresh() {
        viewModel.getTopStationList(true)
        topStationAdapter.onLoadingState(true)
    }
}
