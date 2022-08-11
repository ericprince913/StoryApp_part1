package com.example.bpaai_submission1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bpaai_submission1.EnterApp.LoginActivity
import com.example.bpaai_submission1.Model.StoryModel
import com.example.bpaai_submission1.Response.AllStoryResponse
import com.example.bpaai_submission1.Response.ListStoryItem
import com.example.bpaai_submission1.Retrofit.ApiConfig
import com.example.bpaai_submission1.camera.MainCamera
import com.example.bpaai_submission1.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var storyViewModel: UserViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        getStories()

        binding.button.setOnClickListener {
            startActivity(Intent(this, MainCamera::class.java))
        }

        binding.buttonLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.outButton.setOnClickListener {
            storyViewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun setupViewModel() {
        storyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(Preference.getInstance(dataStore))
        )[UserViewModel::class.java]
    }

    private fun getStories() {
        showLoading(true)

        storyViewModel.getUser().observe(this ) {
            if(it != null) {
                val client = ApiConfig.getApiService().getStory("Bearer " + it.token)
                client.enqueue(object: Callback<AllStoryResponse> {
                    override fun onResponse(
                        call: Call<AllStoryResponse>,
                        response: Response<AllStoryResponse>
                    ) {
                        showLoading(false)
                        val responseBody = response.body()
                        Log.d(TAG, "onResponse: $responseBody")
                        if(response.isSuccessful && responseBody?.message == "Stories fetched successfully") {
                            setStoriesData(responseBody.listStory)
                            //Toast.makeText(this@MainActivity, getString(R.string.success), Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e(TAG, "onFailure1: ${response.message()}")
                            //Toast.makeText(this@MainActivity, getString(R.string.fail), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<AllStoryResponse>, t: Throwable) {
                        showLoading(false)
                        Log.e(TAG, "onFailure2: ${t.message}")
                        //Toast.makeText(this@MainActivity, getString(R.string.fail), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun setStoriesData(items: List<ListStoryItem>) {
        val listStories = ArrayList<StoryModel>()
        for(item in items) {
            val story = StoryModel(
                item.name,
                item.photoUrl,
                item.description,
            )
            listStories.add(story)
        }

        val storyAdapter = StoryAdapter(listStories)
        binding.recyclerView.adapter = storyAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "Story Activity"
    }

}