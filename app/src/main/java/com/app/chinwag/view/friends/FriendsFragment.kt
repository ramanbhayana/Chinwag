package com.app.chinwag.view.friends

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.chinwag.R
import com.app.chinwag.application.AppineersApplication
import com.app.chinwag.dagger.components.FragmentComponent
import com.app.chinwag.databinding.FragmentFriendsBinding
import com.app.chinwag.mvvm.BaseActivity
import com.app.chinwag.mvvm.BaseFragment
import com.app.chinwag.viewModel.FriendViewModel

class FriendsFragment : BaseFragment<FriendViewModel>() {

    private lateinit var binding: FragmentFriendsBinding

    override fun setDataBindingLayout() {}

    override fun provideLayoutId(): Int {
        return R.layout.fragment_friends
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
        binding = DataBindingUtil.bind(view)!!
        binding.lifecycleOwner = this
        (activity as BaseActivity<*>).showAdMob(binding.adView)

        addListeners()
        addObservers()
    }

    private fun addListeners() {
        binding.btnAdd.setOnClickListener {
            (activity as BaseActivity<*>).loadInterstitialAds()
        }
    }

    private fun addObservers() {
        (activity?.application as AppineersApplication).isAdRemoved.observe(
            this@FriendsFragment,
            Observer {
                if (it) {
                    binding.adView.visibility = View.GONE
                }
            })
    }
}