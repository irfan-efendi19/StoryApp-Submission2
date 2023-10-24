package com.dicoding.storyapp.view.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryItem>(DETAIL_STORY) as ListStoryItem
        setupData(story)
    }

    private fun setupData(storyItem: ListStoryItem) {
        Glide.with(applicationContext)
            .load(storyItem.photoUrl)
            .into(binding.imageView2)
        binding.titleDetail.text = storyItem.name
        binding.descDetail.text = storyItem.description
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}