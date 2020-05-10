package com.ivzb.github_browser.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import javax.inject.Inject

class SearchViewModel @Inject constructor(): ViewModel() {

    val searchQuery = MutableLiveData<String>()
    val searchEvent: MutableLiveData<Event<Pair<SearchEvent, String>>> = MutableLiveData()

    fun search(query: String?) =
        searchQuery.postValue(query ?: "")

    fun searchReposClick(query: String) =
        emitEvent(SearchEvent.Repos, query)

    fun searchUsersClick(query: String) =
        emitEvent(SearchEvent.Users, query)

    private fun emitEvent(event: SearchEvent, query: String) =
        searchEvent.postValue(Event(Pair(event, query)))
}

enum class SearchEvent {
    Repos,
    Users
}
