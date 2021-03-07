package com.kmworks.appbase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.kmworks.appbase.viewmodel.BaseViewModel


abstract class FragmentBaseMVVM<ViewModel : BaseViewModel, ViewBind : ViewBinding> :
    Fragment() {
    abstract val viewModel: ViewModel
    abstract val binding: ViewBind
    private lateinit var navControler: NavController
    private val navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        initData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navControler = Navigation.findNavController(view)
        observes()
        onView()
        onViewClick()
    }

    protected open fun onView(){}
    protected open fun initData(){}
    protected open fun onViewClick(){}
    protected open fun observes(){}

    protected fun <T> observe(liveData: LiveData<T>, action: (T) -> Unit) = view?.run {
        if (!this@FragmentBaseMVVM.isAdded) return@run
        liveData.observe(viewLifecycleOwner, Observer { action(it ?: return@Observer) })
    }
    protected open fun navigateUp() {}
    protected open fun navigateBackStack() {
        navControler.popBackStack()
    }
    protected fun navigateFragment(destinationId: Int, arg:Bundle?=null) {
        navControler.navigate(destinationId, arg, navOptions)
    }

}