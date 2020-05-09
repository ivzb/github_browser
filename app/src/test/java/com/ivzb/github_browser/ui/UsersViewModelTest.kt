package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.data.user.UserRepository
import com.ivzb.github_browser.domain.user.GetFollowersUseCase
import com.ivzb.github_browser.domain.user.GetFollowingUseCase
import com.ivzb.github_browser.model.TestData
import com.ivzb.github_browser.ui.users.UsersType
import com.ivzb.github_browser.ui.users.UsersViewModel
import com.ivzb.github_browser.util.LiveDataTestUtil
import com.ivzb.github_browser.util.SyncTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the [UsersViewModel].
 */
class UsersViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    @Test
    fun getFollowers_succeeds() {
        // Given a ViewModel
        val user = TestData.user.login

        val userRepository = mock<UserRepository> {
            on { getFollowers(user) }.thenReturn(TestData.users)
        }
        val getFollowingUseCase = GetFollowingUseCase(userRepository)
        val getFollowersUseCase = GetFollowersUseCase(userRepository)

        val viewModel = UsersViewModel(getFollowingUseCase, getFollowersUseCase)

        // When users are requested
        viewModel.getUsers(user, UsersType.Followers)

        // Then event should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val users = LiveDataTestUtil.getValue(viewModel.users)
        assertThat(users, `is`(CoreMatchers.equalTo(TestData.users as List<Any>)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getFollowers_empty() {
        // Given a ViewModel
        val user = TestData.user.login

        val userRepository = mock<UserRepository> {
            on { getFollowers(user) }.thenReturn(listOf())
        }
        val getFollowingUseCase = GetFollowingUseCase(userRepository)
        val getFollowersUseCase = GetFollowersUseCase(userRepository)

        val viewModel = UsersViewModel(getFollowingUseCase, getFollowersUseCase)

        // When users are requested
        viewModel.getUsers(user, UsersType.Followers)

        // Then loading and error events should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val users = LiveDataTestUtil.getValue(viewModel.users)
        assertThat(users, `is`(CoreMatchers.equalTo(TestData.empty as List<Any>)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getFollowers_fails() {
        // Given a ViewModel
        val user = TestData.user.login

        val userRepository = mock<UserRepository> {
            on { getFollowers(user) }.thenReturn(null)
        }
        val getFollowingUseCase = GetFollowingUseCase(userRepository)
        val getFollowersUseCase = GetFollowersUseCase(userRepository)

        val viewModel = UsersViewModel(getFollowingUseCase, getFollowersUseCase)

        // When users are requested
        viewModel.getUsers(user, UsersType.Followers)

        // Then loading and error events should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val users = LiveDataTestUtil.getValue(viewModel.users)
        assertThat(users, `is`(CoreMatchers.equalTo(TestData.noConnection as List<Any>)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getFollowing_succeeds() {
        // Given a ViewModel
        val user = TestData.user.login

        val userRepository = mock<UserRepository> {
            on { getFollowing(user) }.thenReturn(TestData.users)
        }
        val getFollowingUseCase = GetFollowingUseCase(userRepository)
        val getFollowersUseCase = GetFollowersUseCase(userRepository)

        val viewModel = UsersViewModel(getFollowingUseCase, getFollowersUseCase)

        // When users are requested
        viewModel.getUsers(user, UsersType.Following)

        // Then event should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val users = LiveDataTestUtil.getValue(viewModel.users)
        assertThat(users, `is`(CoreMatchers.equalTo(TestData.users as List<Any>)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getFollowing_empty() {
        // Given a ViewModel
        val user = TestData.user.login

        val userRepository = mock<UserRepository> {
            on { getFollowing(user) }.thenReturn(listOf())
        }
        val getFollowingUseCase = GetFollowingUseCase(userRepository)
        val getFollowersUseCase = GetFollowersUseCase(userRepository)

        val viewModel = UsersViewModel(getFollowingUseCase, getFollowersUseCase)

        // When users are requested
        viewModel.getUsers(user, UsersType.Following)

        // Then loading and error events should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val users = LiveDataTestUtil.getValue(viewModel.users)
        assertThat(users, `is`(CoreMatchers.equalTo(TestData.empty as List<Any>)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getFollowing_fails() {
        // Given a ViewModel
        val user = TestData.user.login

        val userRepository = mock<UserRepository> {
            on { getFollowing(user) }.thenReturn(null)
        }
        val getFollowingUseCase = GetFollowingUseCase(userRepository)
        val getFollowersUseCase = GetFollowersUseCase(userRepository)

        val viewModel = UsersViewModel(getFollowingUseCase, getFollowersUseCase)

        // When users are requested
        viewModel.getUsers(user, UsersType.Following)

        // Then loading and error events should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val users = LiveDataTestUtil.getValue(viewModel.users)
        assertThat(users, `is`(CoreMatchers.equalTo(TestData.noConnection as List<Any>)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun clickUser_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val getFollowingUseCase = GetFollowingUseCase(userRepository)
        val getFollowersUseCase = GetFollowersUseCase(userRepository)

        val viewModel = UsersViewModel(getFollowingUseCase, getFollowersUseCase)

        // When user is clicked
        viewModel.click(TestData.user)

        // Then event should be emitted
        val clickEvent = LiveDataTestUtil.getValue(viewModel.click)
        assertThat(clickEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.user)))
    }
}
