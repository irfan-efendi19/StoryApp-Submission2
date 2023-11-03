package com.dicoding.storyapp.storyapp

import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.data.response.LoginResponse
import com.dicoding.storyapp.data.response.LoginResult
import com.dicoding.storyapp.data.response.RegisterResponse
import com.dicoding.storyapp.data.response.StoryResponse
import com.dicoding.storyapp.data.response.UploadResponse

object DataDummy {
    fun generateDummyLoginSuccess(): LoginResponse {
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = LoginResult(
                userId = "userId",
                name = "name",
                token = "token"
            )
        )
    }

    fun generateDummyLoginError(): LoginResponse {
        return LoginResponse(
            error = true,
            message = "invalid password"
        )
    }

    fun generateDummyRegisterSuccess(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyRegisterError(): RegisterResponse {
        return RegisterResponse(
            error = true,
            message = "bad request"
        )
    }

    fun generateDummyCreateStorySuccess(): UploadResponse {
        return UploadResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyCreateStoryError(): UploadResponse {
        return UploadResponse(
            error = true,
            message = "error"
        )
    }

    fun generateDummyStory(): StoryResponse {
        return StoryResponse(
            error = false,
            message = "success",
            listStory = arrayListOf(
                ListStoryItem(
                    id = "id",
                    name = "name",
                    description = "description",
                    photoUrl = "photoUrl",
                    createdAt = "createdAt",
                    lat = 0.01,
                    lon = 0.01
                )
            )
        )
    }

    fun generateErrorDummyStory(): StoryResponse {
        return StoryResponse(
            error = true,
            message = "error",
            listStory = arrayListOf(
                ListStoryItem(
                    id = "id",
                    name = "name",
                    description = "description",
                    photoUrl = "photoUrl",
                    createdAt = "createdAt",
                    lat = 0.01,
                    lon = 0.01
                )
            )
        )
    }
}