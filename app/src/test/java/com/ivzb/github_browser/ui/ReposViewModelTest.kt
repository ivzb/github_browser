package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.data.repo.RepoRepository
import com.ivzb.github_browser.domain.repo.GetReposUseCase
import com.ivzb.github_browser.model.TestData
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

        val repoRepository = mock<RepoRepository> {
            on { getRepos(eq(user)) }.thenReturn(TestData.repos)
        }
        val getReposUseCase = GetReposUseCase(repoRepository)

        val viewModel = ReposViewModel(getReposUseCase)

        // When repos are requested
        viewModel.getRepos(user)

        // Then event should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val repos = LiveDataTestUtil.getValue(viewModel.repos)
        assertThat(repos, `is`(CoreMatchers.equalTo(TestData.repos as List<Any>)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun getRepos_fails() {
        // Given a ViewModel
        val user = TestData.user.name

        val repoRepository = mock<RepoRepository> {
            on { getRepos(eq(user)) }.thenReturn(null)
        }
        val getReposUseCase = GetReposUseCase(repoRepository)

        val viewModel = ReposViewModel(getReposUseCase)

        // When repos are requested
        viewModel.getRepos(user)

        // Then loading and error events should be emitted
        val loadingEventAtStart = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtStart?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        val repos = LiveDataTestUtil.getValue(viewModel.repos)
        assertThat(repos, `is`(CoreMatchers.equalTo(TestData.noConnection as List<Any>)))

        val loadingEventAtEnd = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loadingEventAtEnd?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))
    }

    @Test
    fun clickRepo_emitsEvent() {
        // Given a ViewModel
        val repoRepository = mock<RepoRepository>()
        val getRepoUseCase = GetReposUseCase(repoRepository)

        val viewModel = ReposViewModel(getRepoUseCase)

        // When repo is clicked
        viewModel.click(TestData.repo)

        // Then event should be emitted
        val clickEvent = LiveDataTestUtil.getValue(viewModel.click)
        assertThat(clickEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.repo)))
    }
}
