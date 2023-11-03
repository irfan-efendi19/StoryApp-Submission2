package com.dicoding.storyapp.view.uploadstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(
    private val repository: UserRepository
) : ViewModel() {

    suspend fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        long: Float
    ): LiveData<Result<UploadResponse>> {
        return repository.uploadImage(file, description, lat, long)
    }
}