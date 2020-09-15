package com.kikulabs.movieappsloadmore

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.kikulabs.movieappsloadmore.adapter.MoviesAdapter
import com.kikulabs.movieappsloadmore.model.DataMovies
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by rizki kurniawan.
 **/

class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private val dataMovies = ArrayList<DataMovies>()
    private lateinit var moviesAdapter: MoviesAdapter
    private var isLoading: Boolean = false
    private var page: Int = 1
    private var totalPage: Int = 0
    var url = "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.API_KEY + "&language=en-US&page="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        getDataMovies()
        initListener()
    }

    private fun initView() {
        recycler_view_movie_activity_main.setHasFixedSize(true)
        recycler_view_movie_activity_main.layoutManager = LinearLayoutManager(this)

        moviesAdapter = MoviesAdapter(this, dataMovies)
        recycler_view_movie_activity_main.adapter = moviesAdapter
    }

    private fun getDataMovies() {
        Log.d(TAG, "page: $page")
        showLoading(true)

        AndroidNetworking.get(url + page)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.d("Success", "onResponse: $response")
                    hideLoading()
                    run {
                        try {
                            val datalist = response.getJSONArray("results")
                            for (i in 0 until datalist.length()) {
                                val data = datalist.getJSONObject(i)
                                val movieItems = DataMovies()
                                movieItems.title = data.getString("original_title")
                                movieItems.poster = data.getString("poster_path")
                                movieItems.backdrop = data.getString("backdrop_path")
                                movieItems.releaseDate = data.getString("release_date")
                                movieItems.voteAverage = data.getString("vote_average")
                                movieItems.language = data.getString("original_language")
                                movieItems.overview = data.getString("overview")
                                dataMovies.add(movieItems)
                            }

                            totalPage = response.getInt("total_pages")
                            moviesAdapter.notifyDataSetChanged()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("Error", "onError: $error")
                }
            })
    }

    private fun initListener() {
        recycler_view_movie_activity_main.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView?.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager?.itemCount
                val lastVisiblePosition =
                    linearLayoutManager?.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                Log.d(TAG, "countItem: $countItem")
                Log.d(TAG, "lastVisiblePosition: $lastVisiblePosition")
                Log.d(TAG, "isLastPosition: $isLastPosition")
                if (!isLoading && isLastPosition && page < totalPage) {
                    showLoading(true)
                    page = page.let { it.plus(1) }
                    getDataMovies()
                }
            }
        })
    }

    private fun showLoading(isRefresh: Boolean) {
        isLoading = true
        progress_bar_horizontal_activity_main.visibility = View.VISIBLE
        recycler_view_movie_activity_main.visibility.let {
            if (isRefresh) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun hideLoading() {
        isLoading = false
        progress_bar_horizontal_activity_main.visibility = View.GONE
        recycler_view_movie_activity_main.visibility = View.VISIBLE
    }
}