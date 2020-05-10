package com.ivzb.github_browser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivzb.github_browser.model.TestData
import com.ivzb.github_browser.ui.search.SearchEvent
import com.ivzb.github_browser.ui.search.SearchViewModel
import com.ivzb.github_browser.util.LiveDataTestUtil
import com.ivzb.github_browser.util.SyncTaskExecutorRule
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the [SearchViewModel].
 */
class SearchViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    @Test
    fun search_emitsEvent() {
        // Given a ViewModel
        val viewModel = SearchViewModel()

        // When search
        viewModel.search(TestData.search)

        // Then event should be emitted
        val searchQuery = LiveDataTestUtil.getValue(viewModel.searchQuery)
        assertThat(searchQuery, `is`(CoreMatchers.equalTo(TestData.search)))
    }

    @Test
    fun emptySearch_emitsEvent() {
        // Given a ViewModel
        val viewModel = SearchViewModel()

        // When search
        viewModel.search(null)

        // Then event should be emitted
        val searchQuery = LiveDataTestUtil.getValue(viewModel.searchQuery)
        assertThat(searchQuery, `is`(CoreMatchers.equalTo("")))
    }

    @Test
    fun searchReposClick_emitsEvent() {
        // Given a ViewModel
        val viewModel = SearchViewModel()

        // When login is clicked
        viewModel.searchReposClick(TestData.search)

        // Then event should be emitted
        val searchEvent = LiveDataTestUtil.getValue(viewModel.searchEvent)
        assertThat(searchEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(Pair(SearchEvent.Repos, TestData.search))))
    }

    @Test
    fun searchUsersClick_emitsEvent() {
        // Given a ViewModel
        val viewModel = SearchViewModel()

        // When login is clicked
        viewModel.searchUsersClick(TestData.search)

        // Then event should be emitted
        val searchEvent = LiveDataTestUtil.getValue(viewModel.searchEvent)
        assertThat(searchEvent?.getContentIfNotHandled(), `is`(CoreMatchers.equalTo(Pair(SearchEvent.Users, TestData.search))))
    }
}
