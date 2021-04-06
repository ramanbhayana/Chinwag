package com.app.chinwag.dagger.modules

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import com.app.chinwag.api.network.NetworkHelper
import com.app.chinwag.commonUtils.rx.SchedulerProvider
import com.app.chinwag.mvvm.BaseFragment
import com.app.chinwag.mvvm.ViewModelProviderFactory
import com.app.chinwag.repository.*
import com.app.chinwag.repository.inappbilling.BillingRepository
import com.app.chinwag.viewModel.*
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class FragmentModule(private val fragment: BaseFragment<*>) {

    @Provides
    fun provideSettingsViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        billingRepository: BillingRepository,
        settingsRepository: SettingsRepository
    ): SettingsViewModel = ViewModelProviders.of(
        fragment.requireActivity(), ViewModelProviderFactory(
            SettingsViewModel::
            class
        ) {
            SettingsViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                billingRepository,
                settingsRepository
            )
        }).get(SettingsViewModel::class.java)

    @Provides
    fun provideUserProfileViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        application: Application,
        userProfileRepository: UserProfileRepository
    ): UserProfileViewModel = ViewModelProviders.of(
        fragment.requireActivity(), ViewModelProviderFactory(
            UserProfileViewModel::
            class
        ) {
            UserProfileViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                application,
                userProfileRepository
            )
        }).get(UserProfileViewModel::class.java)

    @Provides
    fun provideHomeViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        homeRepository: HomeRepository
    ): HomeViewModel = ViewModelProviders.of(
        fragment.requireActivity(), ViewModelProviderFactory(
            HomeViewModel::
            class
        ) {
            HomeViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                homeRepository
            )
        }).get(HomeViewModel::class.java)

    @Provides
    fun provideFriendViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        friendRepository: FriendRepository
    ): FriendViewModel = ViewModelProviders.of(
        fragment.requireActivity(), ViewModelProviderFactory(
            FriendViewModel::
            class
        ) {
            FriendViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                friendRepository
            )
        }).get(FriendViewModel::class.java)

    @Provides
    fun provideMessageViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        messageRepository: MessageRepository
    ): MessageViewModel = ViewModelProviders.of(
        fragment.requireActivity(), ViewModelProviderFactory(
            MessageViewModel::
            class
        ) {
            MessageViewModel(
                schedulerProvider,
                compositeDisposable,
                networkHelper,
                messageRepository
            )
        }).get(MessageViewModel::class.java)
}