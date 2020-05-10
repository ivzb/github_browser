package com.ivzb.github_browser.model.network

import com.google.gson.annotations.SerializedName

data class SearchResponse<T>(

    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @SerializedName("items")
    val items: List<T>
)
