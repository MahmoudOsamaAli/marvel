package com.example.marvel.ui.charactersActivity.adapters

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.R
import com.example.marvel.model.characters.ResultsItem
import com.example.marvel.ui.characterDetails.CharacterDetailsActivity
import com.example.marvel.ui.charactersActivity.CharactersActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CharactersViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    private val name: TextView = view.findViewById(R.id.character_name)
    private val image: ImageView = view.findViewById(R.id.character_image)
    private val animator: ViewAnimator = view.findViewById(R.id.animator)
    private val context = view.context

    private var item: ResultsItem? = null

    init {
        view.setOnClickListener {
            try {
                val intent = Intent(context, CharacterDetailsActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as CharactersActivity,
                    Pair.create(image, ViewCompat.getTransitionName(image)!!),
                    Pair.create(name, ViewCompat.getTransitionName(name)!!)
                )
                val url = item?.thumbnail?.path + "." + item?.thumbnail?.extension
                Log.i("init", "image header: $url")
                intent.putExtra("uri", url)
                intent.putExtra("name", item?.name)
                intent.putExtra("id", item?.id)
                context.startActivity(intent, options.toBundle())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun bind(item: ResultsItem?) {
        if (item != null) {
            showRepoData(item)
        }
    }

    private fun showRepoData(item: ResultsItem) {
        this.item = item
        name.text = item.name.trim()
        if (!item.thumbnail.path.contains("image_not_available")) {
            val url = item.thumbnail.path + "." + item.thumbnail.extension
            val uri = Uri.parse(url)
            Picasso.get().load(uri).into(image, object : Callback {
                override fun onSuccess() {
                    animator.displayedChild = 1
                }

                override fun onError(e: java.lang.Exception?) {
                    animator.displayedChild = 1
                    Picasso.get().load(R.drawable.image_placeholder).into(image)
                    e?.printStackTrace()
                }
            })
        } else {
            animator.displayedChild = 1
            Picasso.get().load(R.drawable.image_placeholder).into(image)
        }
    }

    companion object {
        fun create(parent: ViewGroup): CharactersViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.character_item, parent, false)
            return CharactersViewHolder(view)
        }
    }
}
