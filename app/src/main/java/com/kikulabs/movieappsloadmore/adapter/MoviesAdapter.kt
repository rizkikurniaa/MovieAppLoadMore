package com.kikulabs.movieappsloadmore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iss.method.DateChange
import com.kikulabs.movieappsloadmore.BuildConfig
import com.kikulabs.movieappsloadmore.R
import com.kikulabs.movieappsloadmore.model.DataMovies
import kotlinx.android.synthetic.main.item_movie.view.*

/**
 * Created by rizki kurniawan.
 **/

class MoviesAdapter(
    private val context: Context,
    private var resultTheMovieDb: ArrayList<DataMovies>
) : RecyclerView.Adapter<MoviesAdapter.ViewHolderTheMovieDb>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTheMovieDb =
        ViewHolderTheMovieDb(
            LayoutInflater.from(parent?.context).inflate(R.layout.item_movie, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolderTheMovieDb, position: Int) {
        val dateChange = DateChange()
        val resultItem = resultTheMovieDb[position]
        var rate: Float = resultItem.voteAverage!!.toFloat() / 2

        Glide.with(context)
            .load(BuildConfig.BASE_URL_IMAGE + resultItem.poster)
            .placeholder(R.drawable.movieplaceholder)
            .into(holder.itemView.iv_poster)

        holder.itemView.tv_title.text = resultItem.title
        holder.itemView.ratingBar.rating = rate
        holder.itemView.tv_release_value.text = dateChange.changeFormatDate(resultItem.releaseDate)
        holder.itemView.tv_overview_value.text = resultItem.overview
    }

    override fun getItemCount(): Int = resultTheMovieDb.size

    inner class ViewHolderTheMovieDb(itemView: View) : RecyclerView.ViewHolder(itemView)

}