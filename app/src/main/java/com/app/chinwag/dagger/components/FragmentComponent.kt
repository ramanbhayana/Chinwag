package com.app.chinwag.dagger.components

import com.app.chinwag.dagger.FragmentScope
import com.app.chinwag.dagger.modules.FragmentModule
import com.app.chinwag.view.friends.FriendsFragment
import com.app.chinwag.view.home.HomeFragment
import com.app.chinwag.view.message.MessagesFragment
import com.app.chinwag.view.profile.ProfileFragment
import com.app.chinwag.view.settings.SettingsFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent{
    fun inject(settingsFragment: SettingsFragment)
    fun inject(profileFragment:ProfileFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(friendsFragment: FriendsFragment)
    fun inject(messagesFragment: MessagesFragment)
}