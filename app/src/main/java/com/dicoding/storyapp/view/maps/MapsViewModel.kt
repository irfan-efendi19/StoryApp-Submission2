package com.dicoding.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.response.StoryResponse

class MapsViewModel(
    private val repository: UserRepository
) : ViewModel() {

    suspend fun getStoriesWithLocation(): LiveData<Result<StoryResponse>> {
        return repository.getStoriesWithLocation()
    }

}