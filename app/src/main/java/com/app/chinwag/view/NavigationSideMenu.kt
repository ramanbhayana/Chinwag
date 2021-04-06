package com.app.chinwag.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.app.chinwag.R
import com.app.chinwag.dagger.components.ActivityComponent
import com.app.chinwag.databinding.ActivityNavigationSideMenuBinding
import com.app.chinwag.mvvm.BaseActivity
import com.app.chinwag.view.friends.FriendsFragment
import com.app.chinwag.view.home.HomeFragment
import com.app.chinwag.view.message.MessagesFragment
import com.app.chinwag.view.profile.ProfileFragment
import com.app.chinwag.view.settings.SettingsFragment
import com.app.chinwag.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_navigation_side_menu.*
import kotlinx.android.synthetic.main.app_bar_main.*

class NavigationSideMenu : BaseActivity<HomeViewModel>(){
    private lateinit var binding: ActivityNavigationSideMenuBinding
    var navigationPosition: Int = 0

    override fun setDataBindingLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_navigation_side_menu)
        binding.lifecycleOwner = this
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }
    override fun setupView(savedInstanceState: Bundle?) {
        binding.apply {
            setSupportActionBar(toolbar)
            setUpDrawerLayout()

            navigationPosition = R.id.action_home
            setCurrentFragment(HomeFragment())
            navigationView.setCheckedItem(navigationPosition)
            toolbar.title = getString(R.string.home)


            navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_home -> {
                        toolbar.title = getString(R.string.home)
                        if (supportFragmentManager.findFragmentById(R.id.frameContainer) is HomeFragment) {
                            true
                        } else {
                            setCurrentFragment(HomeFragment())
                            true
                        }
                    }
                    R.id.action_friends -> {
                        toolbar.title = getString(R.string.friends)
                        if (supportFragmentManager.findFragmentById(R.id.frameContainer) is FriendsFragment) {
                            true
                        } else {
                            setCurrentFragment(FriendsFragment())
                            true
                        }
                    }
                    R.id.action_message -> {
                        toolbar.title = getString(R.string.message)
                        if (supportFragmentManager.findFragmentById(R.id.frameContainer) is MessagesFragment) {
                            true
                        } else {
                            setCurrentFragment(MessagesFragment())
                            true
                        }
                    }
                    R.id.action_profile -> {
                        toolbar.title = getString(R.string.profile)
                        if (supportFragmentManager.findFragmentById(R.id.frameContainer) is ProfileFragment) {
                            true
                        } else {
                            setCurrentFragment(ProfileFragment())
                            true
                        }
                    }
                    R.id.action_settings -> {
                        toolbar.title = getString(R.string.setting)
                        if (supportFragmentManager.findFragmentById(R.id.frameContainer) is SettingsFragment) {
                            true
                        } else {
                            setCurrentFragment(SettingsFragment())
                            true
                        }
                    }
                }
                // set item as selected to persist highlight
                menuItem.isChecked = true
                // close drawer when item is tapped
                drawerLayout.closeDrawers()
                true
            }



            drawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener{
                override fun onDrawerStateChanged(p0: Int) {
                   // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDrawerSlide(p0: View, p1: Float) {
                  //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDrawerClosed(p0: View) {
                  //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDrawerOpened(p0: View) {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }}


    private fun setUpDrawerLayout() {
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }



    @SuppressLint("WrongConstant")
    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START)
        }

        if (navigationPosition == R.id.action_home) {
            finish()
        } else {

            navigationPosition = R.id.action_home
            setCurrentFragment(HomeFragment())
            navigationView.setCheckedItem(navigationPosition)
            toolbar.title = getString(R.string.home)
        }
    }


    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameContainer, fragment)
            commit()
        }


}