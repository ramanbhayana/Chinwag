package com.app.chinwag.mvvm


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.app.chinwag.R
import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.commonUtils.common.LoadingDialog
import com.app.chinwag.commonUtils.utility.extension.showSnackBar
import com.app.chinwag.dagger.components.DaggerFragmentComponent
import com.app.chinwag.dagger.components.FragmentComponent
import com.app.chinwag.dagger.modules.FragmentModule
import com.app.chinwag.dataclasses.generics.Settings
import com.hb.logger.Logger
import com.hb.logger.msc.MSCGenerator
import javax.inject.Inject

/**
 *
 * @author Ramandeep Bhayana
 */
abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    @Inject
    lateinit var viewModel: VM
    val logger by lazy {
        Logger(this::class.java.simpleName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildFragmentComponent())
        super.onCreate(savedInstanceState)
        setDataBindingLayout()
        setupObservers()
        viewModel.onCreate()

        MSCGenerator.addLineComment(this::class.java.simpleName)
    }

    private fun buildFragmentComponent() =
        DaggerFragmentComponent
            .builder()
            .applicationComponent((context?.applicationContext as AppineersApplication).applicationComponent)
            .fragmentModule(FragmentModule(this))
            .build()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(provideLayoutId(), container, false)

    protected open fun setupObservers() {
        viewModel.messageString.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })
        viewModel.showDialog.observe(this, Observer {
            if (it) {
                LoadingDialog.showDialog()
            } else {
                LoadingDialog.dismissDialog()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    fun showMessage(message: CharSequence) {
        message.toString().showSnackBar(context = activity)
    }

    fun showMessage(message: CharSequence, type: Int) {
        message.toString().showSnackBar(context = activity, type = type)
    }

    fun goBack() {
        if (activity is BaseActivity<*>) (activity as BaseActivity<*>).goBack()
    }

    fun checkInternet(): Boolean {
        if ((activity as BaseActivity<*>).checkInternet()) {
            return true
        } else {
            showMessage(getString(R.string.network_connection_error))
            return false
        }
    }

    protected abstract fun setDataBindingLayout()

    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    protected abstract fun injectDependencies(fragmentComponent: FragmentComponent)

    protected abstract fun setupView(view: View)

    fun setFireBaseAnalyticsData(id: String, name: String, contentType: String) {
        (activity as BaseActivity<*>).setFireBaseAnalyticsData(id, name, contentType)
    }

    fun handleApiError(settings: Settings?) = (activity as BaseActivity<*>).handleApiError(settings)

}