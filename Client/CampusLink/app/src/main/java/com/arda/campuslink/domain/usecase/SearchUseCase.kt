package com.arda.campuslink.domain.usecase

import com.arda.campuslink.domain.model.SearchResult
import com.arda.campuslink.domain.repository.SearchRepository
import com.arda.campuslink.domain.repository.UserRepository
import com.arda.mainapp.auth.Resource
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    suspend fun getSearchResults(parameter : String): Resource<SearchResult> {
        return searchRepository.runSearch(parameter = parameter)
    }
}