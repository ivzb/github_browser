package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.data.repo.RepoRepository
import com.ivzb.github_browser.domain.repo.*
import com.ivzb.github_browser.model.TestData
import com.ivzb.github_browser.model.repo.RepoType
import com.ivzb.github_browser.ui.repos.ReposViewModel
import com.ivzb.github_browser.util.LiveDataTestUtil
import com.ivzb.github_browser.util.SyncTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the [ReposViewModel].
 */
class ReposViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    @Test
    fun getRepos_succeeds() {
        // Given a ViewModel
        val user = TestData.user.name
        val type = RepoType.Own

        val repoRepository = mock<RepoRepository> {
            on { observeRepos(eq(user), eq(type)) }.thenReturn(TestData.liveDataOf(TestData.repos))
        }
        val observeReposUseCase = ObserveReposUseCase(repoRepository)
        val fetchReposUseCase = FetchReposUseCase(repoRepository)

        val viewModel = ReposViewModel(observeReposUseCase, fetchReposUseCase)

        // When repos are requested
        viewModel.getRepos(user, type)

        // Then event should be emitted and repos fetched
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val repos = LiveDataTestUtil.getValue(viewModel.repos)
        assertThat(repos?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.repos as List<Any>)))

        verify(repoRepository).fetchRepos(eq(user), eq(type))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getRepos_empty() {
        // Given a ViewModel
        val user = TestData.user.name
        val type = RepoType.Own

        val repoRepository = mock<RepoRepository> {
            on { observeRepos(eq(user), eq(type)) }.thenReturn(TestData.liveDataOf(listOf()))
        }
        val observeReposUseCase = ObserveReposUseCase(repoRepository)
        val fetchReposUseCase = FetchReposUseCase(repoRepository)

        val viewModel = ReposViewModel(observeReposUseCase, fetchReposUseCase)

        // When repos are requested
        viewModel.getRepos(user, type)

        // Then event should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val repos = LiveDataTestUtil.getValue(viewModel.repos)
        assertThat(repos?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.empty as List<Any>)))

        verify(repoRepository).fetchRepos(eq(user), eq(type))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getOwnRepos_fails() {
        // Given a ViewModel
        val user = TestData.user.name
        val type = RepoType.Own

        val repoRepository = mock<RepoRepository> {
            on { observeRepos(eq(user), eq(type)) }.thenReturn(TestData.liveDataOf(null))
        }
        val observeReposUseCase = ObserveReposUseCase(repoRepository)
        val fetchReposUseCase = FetchReposUseCase(repoRepository)

        val viewModel = ReposViewModel(observeReposUseCase, fetchReposUseCase)

        // When repos are requested
        viewModel.getRepos(user, type)

        // Then loading and error events should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val repos = LiveDataTestUtil.getValue(viewModel.repos)
        assertThat(repos?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.noConnection as List<Any>)))

        verify(repoRepository).fetchRepos(eq(user), eq(type))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun clickRepo_emitsEvent() {
        // Given a ViewModel
        val repoRepository = mock<RepoRepository>()
        val observeReposUseCase = ObserveReposUseCase(repoRepository)
        val fetchReposUseCase = FetchReposUseCase(repoRepository)

        val viewModel = ReposViewModel(observeReposUseCase, fetchReposUseCase)

        // When repo is clicked
        viewModel.click(TestData.repo)

        // Then event should be emitted
        val clickEvent = LiveDataTestUtil.getValue(viewModel.click)
        assertThat(clickEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.repo)))
    }

    @Test
    fun searchRepo_emitsEvent() {
        // Given a ViewModel
        val repoRepository = mock<RepoRepository>()
        val observeReposUseCase = ObserveReposUseCase(repoRepository)
        val fetchReposUseCase = FetchReposUseCase(repoRepository)

        val viewModel = ReposViewModel(observeReposUseCase, fetchReposUseCase)

        // When repo is searched
        viewModel.search(TestData.search)

        // Then event should be emitted
        val searchEvent = LiveDataTestUtil.getValue(viewModel.searchQuery)
        assertThat(
            searchEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(TestData.search))
        )
    }

    @Test
    fun searchNullRepo_emitsEvent() {
        // Given a ViewModel
        val repoRepository = mock<RepoRepository>()
        val observeReposUseCase = ObserveReposUseCase(repoRepository)
        val fetchReposUseCase = FetchReposUseCase(repoRepository)

        val viewModel = ReposViewModel(observeReposUseCase, fetchReposUseCase)

        // When repo is searched
        viewModel.search(null)

        // Then event should be emitted
        val searchEvent = LiveDataTestUtil.getValue(viewModel.searchQuery)
        assertThat(searchEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo("")))
    }
}
