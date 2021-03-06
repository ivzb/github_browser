package com.ivzb.github_browser.domain.star

import com.ivzb.github_browser.data.star.StarRepository
import com.ivzb.github_browser.domain.UseCase
import javax.inject.Inject

open class StarUseCase @Inject constructor(
    private val repository: StarRepository
) : UseCase<String, Boolean?>() {

    override fun execute(parameters: String) =
        repository.star(parameters)
}
