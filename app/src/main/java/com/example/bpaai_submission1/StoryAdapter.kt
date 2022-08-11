package com.example.bpaai_submission1

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bpaai_submission1.Model.StoryModel

class StoryAdapter (private val listStory: ArrayList<StoryModel>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.avatar)
        val name: TextView = view.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.recyclerview_item, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = listStory[position].name
        Glide.with(viewHolder.itemView.context)
            .load(listStory[position].avatar)
            .into(viewHolder.img)

        viewHolder.itemView.setOnClickListener {
            val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                viewHolder.itemView.context as Activity,
                Pair(viewHolder.img, "image"),
                Pair(viewHolder.name, "name"),
            )

            val intent = Intent(viewHolder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.STORY_DETAIL, listStory[position])
            viewHolder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return listStory.size
    }
}