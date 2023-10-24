package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.preference.UserPreference
import com.dicoding.storyapp.data.preference.dataStore
import com.dicoding.storyapp.data.remote.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun injectionRepository(context: Context): UserRepository = runBlocking  {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = pref.getSession().first()
        val apiService = ApiConfig.getApiService(user.token)
        UserRepository.getInstance(apiService, pref)
    }
}