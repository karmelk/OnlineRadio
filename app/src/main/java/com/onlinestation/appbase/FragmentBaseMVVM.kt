package com.onlinestation.appbase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.viewbinding.ViewBinding
import com.onlinestation.R
import com.onlinestation.activity.ShearViewModel
import com.onlinestation.appbase.utils.observeInLifecycle
import com.onlinestation.appbase.viewmodel.BaseViewModel
import com.onlinestation.dialogs.NotNetworkDialog
import com.onlinestation.fragment.genre.GenreFragmentDirections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach


abstract class FragmentBaseMVVM<ViewModel : BaseViewModel, ViewBind : ViewBinding> : Fragment() {

    abstract val viewModel: ViewModel
    protected var shearViewModel: ShearViewModel?=null
    abstract val binding: ViewBind
    private lateinit var navControler: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            shearViewModel = ViewModelProvider(it)[ShearViewModel::class.java]
        }
        navControler = Navigation.findNavController(view)

        onEach(viewModel.notNetworkConnection){
            NotNetworkDialog.newInstance().show(parentFragmentManager, NotNetworkDialog.DIALOG_TAG)
        }
        onEach()
        onView()
        onViewClick()
    }

    protected open fun onView() {}

    protected open fun onViewClick() {}

    protected open fun onEach() {}

    protected inline fun <reified T> onEach(flow: Flow<T>, crossinline action: (T) -> Unit) = view?.run {
        if (!this@FragmentBaseMVVM.isAdded) return@run
        flow.onEach { action(it ?: return@onEach) }.observeInLifecycle(viewLifecycleOwner)
    }

    protected fun popBackStack() {
        navControler.popBackStack()
    }

    protected fun navigateFragment(destinationId: Int, arg: Bundle? = null) {
        navControler.navigate(destinationId, arg)
    }

    protected fun navigateFragment(directions: NavDirections) {
        navControler.navigate(directions)
    }
}