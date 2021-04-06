package com.app.chinwag.view.message

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.chinwag.R
import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.dagger.components.FragmentComponent
import com.app.chinwag.databinding.FragmentMessagesBinding
import com.app.chinwag.mvvm.BaseActivity
import com.app.chinwag.mvvm.BaseFragment
import com.app.chinwag.viewModel.MessageViewModel

class MessagesFragment : BaseFragment<MessageViewModel>() {

    private lateinit var binding: FragmentMessagesBinding

    override fun setDataBindingLayout() {}

    override fun provideLayoutId(): Int {
        return R.layout.fragment_messages
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
        binding = DataBindingUtil.bind(view)!!
        binding.lifecycleOwner = this
        (activity as BaseActivity<*>).showAdMob(binding.adView)

        addObservers()
    }

    private fun addObservers() {
        (activity?.application as AppineersApplication).isAdRemoved.observe(this@MessagesFragment, Observer {
            if(it){
                binding.adView.visibility = View.GONE
            }
        })
    }
}