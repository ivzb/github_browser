package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.data.user.UserRepository
import com.ivzb.github_browser.domain.user.*
import com.ivzb.github_browser.model.TestData
import com.ivzb.github_browser.model.user.UserType
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
    fun getUsers_succeeds() {
        // Given a ViewModel
        val user = TestData.user.login
        val type = UserType.Following

        val userRepository = mock<UserRepository> {
            on { observeUsers(eq(user), eq(type)) }.thenReturn(TestData.liveDataOf(TestData.users))
        }
        val observeUsersUseCase = ObserveUsersUseCase(userRepository)
        val fetchUsersUseCase = FetchUsersUseCase(userRepository)

        val viewModel = UsersViewModel(observeUsersUseCase, fetchUsersUseCase)

        // When users are requested
        viewModel.getUsers(user, type)

        // Then event should be emitted and users fetched
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val users = LiveDataTestUtil.getValue(viewModel.users)
        assertThat(users?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.users as List<Any>)))

        verify(userRepository).fetchUsers(eq(user), eq(type))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getUsers_empty() {
        // Given a ViewModel
        val user = TestData.user.login
        val type = UserType.Following

        val userRepository = mock<UserRepository> {
            on { observeUsers(eq(user), eq(type)) }.thenReturn(TestData.liveDataOf(listOf()))
        }
        val observeUsersUseCase = ObserveUsersUseCase(userRepository)
        val fetchUsersUseCase = FetchUsersUseCase(userRepository)

        val viewModel = UsersViewModel(observeUsersUseCase, fetchUsersUseCase)

        // When users are requested
        viewModel.getUsers(user, type)

        // Then loading and error events should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val users = LiveDataTestUtil.getValue(viewModel.users)
        assertThat(users?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.empty as List<Any>)))

        verify(userRepository).fetchUsers(eq(user), eq(type))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getUsers_fails() {
        // Given a ViewModel
        val user = TestData.user.login
        val type = UserType.Following

        val userRepository = mock<UserRepository> {
            on { observeUsers(eq(user), eq(type)) }.thenReturn(TestData.liveDataOf(null))
        }
        val observeUsersUseCase = ObserveUsersUseCase(userRepository)
        val fetchUsersUseCase = FetchUsersUseCase(userRepository)

        val viewModel = UsersViewModel(observeUsersUseCase, fetchUsersUseCase)

        // When users are requested
        viewModel.getUsers(user, type)

        // Then loading and error events should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val users = LiveDataTestUtil.getValue(viewModel.users)
        assertThat(users?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.noConnection as List<Any>)))

        verify(userRepository).fetchUsers(eq(user), eq(type))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun clickUser_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val observeUsersUseCase = ObserveUsersUseCase(userRepository)
        val fetchUsersUseCase = FetchUsersUseCase(userRepository)

        val viewModel = UsersViewModel(observeUsersUseCase, fetchUsersUseCase)

        // When user is clicked
        viewModel.click(TestData.user)

        // Then event should be emitted
        val clickEvent = LiveDataTestUtil.getValue(viewModel.click)
        assertThat(clickEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.user)))
    }

    @Test
    fun searchUser_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val observeUsersUseCase = ObserveUsersUseCase(userRepository)
        val fetchUsersUseCase = FetchUsersUseCase(userRepository)

        val viewModel = UsersViewModel(observeUsersUseCase, fetchUsersUseCase)

        // When user is searched
        viewModel.search(TestData.search)

        // Then event should be emitted
        val searchEvent = LiveDataTestUtil.getValue(viewModel.searchQuery)
        assertThat(
            searchEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(TestData.search))
        )
    }

    @Test
    fun searchNullUser_emitsEvent() {
        // Given a ViewModel
        val userRepository = mock<UserRepository>()
        val observeUsersUseCase = ObserveUsersUseCase(userRepository)
        val fetchUsersUseCase = FetchUsersUseCase(userRepository)

        val viewModel = UsersViewModel(observeUsersUseCase, fetchUsersUseCase)

        // When user is searched
        viewModel.search(null)

        // Then event should be emitted
        val searchEvent = LiveDataTestUtil.getValue(viewModel.searchQuery)
        assertThat(
            searchEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(""))
        )
    }
}
