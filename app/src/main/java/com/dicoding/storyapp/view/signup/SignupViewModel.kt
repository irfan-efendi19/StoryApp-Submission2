package com.dicoding.storyapp.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.response.RegisterResponse

class SignupViewModel(private var repository: UserRepository) : ViewModel() {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return repository.register(name, email, password)
    }
}