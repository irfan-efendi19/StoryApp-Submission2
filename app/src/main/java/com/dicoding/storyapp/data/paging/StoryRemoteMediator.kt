package com.dicoding.storyapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.dicoding.storyapp.data.database.DatabaseStory
import com.dicoding.storyapp.data.remote.ApiService
import com.dicoding.storyapp.data.response.ListStoryItem

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: DatabaseStory,
    private val apiService: ApiService
) : RemoteMediator<Int, ListStoryItem>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
        TODO("Not yet implemented")
    }

}