package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.data.repo.RepoRepository
import com.ivzb.github_browser.data.star.StarRepository
import com.ivzb.github_browser.domain.repo.FetchRepoUseCase
import com.ivzb.github_browser.domain.repo.ObserveRepoUseCase
import com.ivzb.github_browser.domain.star.IsStarredUseCase
import com.ivzb.github_browser.domain.star.StarUseCase
import com.ivzb.github_browser.domain.star.UnstarUseCase
import com.ivzb.github_browser.model.TestData
import com.ivzb.github_browser.ui.repo_profile.RepoProfileEvent
import com.ivzb.github_browser.ui.repo_profile.RepoProfileViewModel
import com.ivzb.github_browser.ui.repo_profile.RepoProfileViewModel.Companion.COULD_NOT_GET_REPO
import com.ivzb.github_browser.util.LiveDataTestUtil
import com.ivzb.github_browser.util.SyncTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the [RepoProfileViewModel].
 */
class RepoProfileViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    @Test
    fun getRepo_succeeds() {
        // Given a ViewModel
        val repoName = TestData.repo.fullName

        val repoRepository = mock<RepoRepository> {
            on { observeRepo(eq(repoName)) }.thenReturn(TestData.liveDataOf(TestData.repo))
            on { fetchRepo(eq(repoName)) }.thenReturn(true)
        }
        val observeRepoUseCase = ObserveRepoUseCase(repoRepository)
        val fetchRepoUseCase = FetchRepoUseCase(repoRepository)

        val starRepository = mock<StarRepository> {
            on { isStarred(eq(repoName)) }.thenReturn(true)
        }
        val isStarredUseCase = IsStarredUseCase(starRepository)
        val starUseCase = StarUseCase(starRepository)
        val unstarUseCase = UnstarUseCase(starRepository)

        val viewModel = RepoProfileViewModel(
            observeRepoUseCase,
            fetchRepoUseCase,
            isStarredUseCase,
            starUseCase,
            unstarUseCase
        )

        // When repo is requested
        viewModel.getRepo(repoName)

        // Then event should be emitted and repo fetched
        val repo = LiveDataTestUtil.getValue(viewModel.repo)
        assertThat(repo?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(TestData.repo)))

        verify(repoRepository).fetchRepo(eq(repoName))

        val loading = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loading?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))

        val isStarred = LiveDataTestUtil.getValue(viewModel.isStarred)
        assertThat(isStarred?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        verify(starRepository).isStarred(eq(repoName))
    }

    @Test
    fun getRepo_fails() {
        // Given a ViewModel
        val repoName = TestData.repo.fullName

        val repoRepository = mock<RepoRepository> {
            on { observeRepo(eq(repoName)) }.thenReturn(TestData.liveDataOf(null))
            on { fetchRepo(eq(repoName)) }.thenReturn(false)
        }
        val observeRepoUseCase = ObserveRepoUseCase(repoRepository)
        val fetchRepoUseCase = FetchRepoUseCase(repoRepository)

        val starRepository = mock<StarRepository> {
            on { isStarred(eq(repoName)) }.thenReturn(null)
        }
        val isStarredUseCase = IsStarredUseCase(starRepository)
        val starUseCase = StarUseCase(starRepository)
        val unstarUseCase = UnstarUseCase(starRepository)

        val viewModel = RepoProfileViewModel(
            observeRepoUseCase,
            fetchRepoUseCase,
            isStarredUseCase,
            starUseCase,
            unstarUseCase
        )

        // When current repo is requested
        viewModel.getRepo(repoName)

        // Then loading and error events should be emitted
        val repo = LiveDataTestUtil.getValue(viewModel.repo)
        assertTrue(repo?.getContentIfNotHandled() == null)

        verify(repoRepository).fetchRepo(eq(repoName))

        val loading = LiveDataTestUtil.getValue(viewModel.loading)
        assertThat(loading?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))

        val errorEvent = LiveDataTestUtil.getValue(viewModel.error)
        assertThat(
            errorEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(COULD_NOT_GET_REPO))
        )

        val isStarred = LiveDataTestUtil.getValue(viewModel.isStarred)
        assertTrue(isStarred?.getContentIfNotHandled() == null)

        verify(starRepository).isStarred(eq(repoName))
    }

    @Test
    fun star_succeeds() {
        // Given a ViewModel
        val repoName = TestData.repo.fullName

        val repoRepository = mock<RepoRepository>()
        val observeRepoUseCase = ObserveRepoUseCase(repoRepository)
        val fetchRepoUseCase = FetchRepoUseCase(repoRepository)

        val starRepository = mock<StarRepository> {
            on { star(eq(repoName)) }.thenReturn(true)
        }
        val isStarredUseCase = IsStarredUseCase(starRepository)
        val starUseCase = StarUseCase(starRepository)
        val unstarUseCase = UnstarUseCase(starRepository)

        val viewModel = RepoProfileViewModel(
            observeRepoUseCase,
            fetchRepoUseCase,
            isStarredUseCase,
            starUseCase,
            unstarUseCase
        )

        // When star repo
        viewModel.star(repoName)

        // Then event should be emitted and repo fetched
        val isStarred = LiveDataTestUtil.getValue(viewModel.isStarred)
        assertThat(isStarred?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        verify(starRepository).star(eq(repoName))
    }

    @Test
    fun star_fails() {
        // Given a ViewModel
        val repoName = TestData.repo.fullName

        val repoRepository = mock<RepoRepository>()
        val observeRepoUseCase = ObserveRepoUseCase(repoRepository)
        val fetchRepoUseCase = FetchRepoUseCase(repoRepository)

        val starRepository = mock<StarRepository> {
            on { star(eq(repoName)) }.thenReturn(false)
        }
        val isStarredUseCase = IsStarredUseCase(starRepository)
        val starUseCase = StarUseCase(starRepository)
        val unstarUseCase = UnstarUseCase(starRepository)

        val viewModel = RepoProfileViewModel(
            observeRepoUseCase,
            fetchRepoUseCase,
            isStarredUseCase,
            starUseCase,
            unstarUseCase
        )

        // When star repo
        viewModel.star(repoName)

        // Then event should be emitted and repo fetched
        val isStarred = LiveDataTestUtil.getValue(viewModel.isStarred)
        assertThat(isStarred?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))

        verify(starRepository).star(eq(repoName))
    }

    @Test
    fun star_noInternet() {
        // Given a ViewModel
        val repoName = TestData.repo.fullName

        val repoRepository = mock<RepoRepository>()
        val observeRepoUseCase = ObserveRepoUseCase(repoRepository)
        val fetchRepoUseCase = FetchRepoUseCase(repoRepository)

        val starRepository = mock<StarRepository> {
            on { star(eq(repoName)) }.thenReturn(null)
        }
        val isStarredUseCase = IsStarredUseCase(starRepository)
        val starUseCase = StarUseCase(starRepository)
        val unstarUseCase = UnstarUseCase(starRepository)

        val viewModel = RepoProfileViewModel(
            observeRepoUseCase,
            fetchRepoUseCase,
            isStarredUseCase,
            starUseCase,
            unstarUseCase
        )

        // When star repo
        viewModel.star(repoName)

        // Then event should be emitted and repo fetched
        val isStarred = LiveDataTestUtil.getValue(viewModel.isStarred)
        assertTrue(isStarred?.getContentIfNotHandled() == null)

        verify(starRepository).star(eq(repoName))
    }

    @Test
    fun unstar_succeeds() {
        // Given a ViewModel
        val repoName = TestData.repo.fullName

        val repoRepository = mock<RepoRepository>()
        val observeRepoUseCase = ObserveRepoUseCase(repoRepository)
        val fetchRepoUseCase = FetchRepoUseCase(repoRepository)

        val starRepository = mock<StarRepository> {
            on { unstar(eq(repoName)) }.thenReturn(false)
        }
        val isStarredUseCase = IsStarredUseCase(starRepository)
        val starUseCase = StarUseCase(starRepository)
        val unstarUseCase = UnstarUseCase(starRepository)

        val viewModel = RepoProfileViewModel(
            observeRepoUseCase,
            fetchRepoUseCase,
            isStarredUseCase,
            starUseCase,
            unstarUseCase
        )

        // When unstar repo
        viewModel.unstar(repoName)

        // Then event should be emitted and repo fetched
        val isStarred = LiveDataTestUtil.getValue(viewModel.isStarred)
        assertThat(isStarred?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(false)))

        verify(starRepository).unstar(eq(repoName))
    }

    @Test
    fun unstar_fails() {
        // Given a ViewModel
        val repoName = TestData.repo.fullName

        val repoRepository = mock<RepoRepository>()
        val observeRepoUseCase = ObserveRepoUseCase(repoRepository)
        val fetchRepoUseCase = FetchRepoUseCase(repoRepository)

        val starRepository = mock<StarRepository> {
            on { unstar(eq(repoName)) }.thenReturn(true)
        }
        val isStarredUseCase = IsStarredUseCase(starRepository)
        val starUseCase = StarUseCase(starRepository)
        val unstarUseCase = UnstarUseCase(starRepository)

        val viewModel = RepoProfileViewModel(
            observeRepoUseCase,
            fetchRepoUseCase,
            isStarredUseCase,
            starUseCase,
            unstarUseCase
        )

        // When unstar repo
        viewModel.unstar(repoName)

        // Then event should be emitted and repo fetched
        val isStarred = LiveDataTestUtil.getValue(viewModel.isStarred)
        assertThat(isStarred?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(true)))

        verify(starRepository).unstar(eq(repoName))
    }

    @Test
    fun unstar_noInternet() {
        // Given a ViewModel
        val repoName = TestData.repo.fullName

        val repoRepository = mock<RepoRepository>()
        val observeRepoUseCase = ObserveRepoUseCase(repoRepository)
        val fetchRepoUseCase = FetchRepoUseCase(repoRepository)

        val starRepository = mock<StarRepository> {
            on { unstar(eq(repoName)) }.thenReturn(null)
        }
        val isStarredUseCase = IsStarredUseCase(starRepository)
        val starUseCase = StarUseCase(starRepository)
        val unstarUseCase = UnstarUseCase(starRepository)

        val viewModel = RepoProfileViewModel(
            observeRepoUseCase,
            fetchRepoUseCase,
            isStarredUseCase,
            starUseCase,
            unstarUseCase
        )

        // When unstar repo
        viewModel.unstar(repoName)

        // Then event should be emitted and repo fetched
        val isStarred = LiveDataTestUtil.getValue(viewModel.isStarred)
        assertTrue(isStarred?.getContentIfNotHandled() == null)

        verify(starRepository).unstar(eq(repoName))
    }

    @Test
    fun contributorsClick_emitsEvent() {
        // Given a ViewModel
        val repoRepository = mock<RepoRepository>()
        val observeRepoUseCase = ObserveRepoUseCase(repoRepository)
        val fetchRepoUseCase = FetchRepoUseCase(repoRepository)

        val starRepository = mock<StarRepository>()
        val isStarredUseCase = IsStarredUseCase(starRepository)
        val starUseCase = StarUseCase(starRepository)
        val unstarUseCase = UnstarUseCase(starRepository)

        val viewModel = RepoProfileViewModel(
            observeRepoUseCase,
            fetchRepoUseCase,
            isStarredUseCase,
            starUseCase,
            unstarUseCase
        )

        // When contributors is clicked
        viewModel.contributorsClick(TestData.repo.fullName)

        // Then event should be emitted
        val repoProfileEvent = LiveDataTestUtil.getValue(viewModel.repoProfileEvent)
        assertThat(
            repoProfileEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(Pair(RepoProfileEvent.Contributors, TestData.repo.fullName)))
        )
    }

    @Test
    fun ownerClick_emitsEvent() {
        // Given a ViewModel
        val repoRepository = mock<RepoRepository>()
        val observeRepoUseCase = ObserveRepoUseCase(repoRepository)
        val fetchRepoUseCase = FetchRepoUseCase(repoRepository)

        val starRepository = mock<StarRepository>()
        val isStarredUseCase = IsStarredUseCase(starRepository)
        val starUseCase = StarUseCase(starRepository)
        val unstarUseCase = UnstarUseCase(starRepository)

        val viewModel = RepoProfileViewModel(
            observeRepoUseCase,
            fetchRepoUseCase,
            isStarredUseCase,
            starUseCase,
            unstarUseCase
        )

        // When owner is clicked
        viewModel.ownerClick(TestData.repo.owner)

        // Then event should be emitted
        val repoProfileEvent = LiveDataTestUtil.getValue(viewModel.repoProfileEvent)
        assertThat(
            repoProfileEvent?.getContentIfNotHandled(),
            `is`(CoreMatchers.equalTo(Pair(RepoProfileEvent.Owner, TestData.repo.owner)))
        )
    }
}
