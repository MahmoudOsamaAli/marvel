package com.example.marvel

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.model.ResultsItem
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.alterac.blurkit.BlurLayout


class CharactersViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    private val name: TextView = view.findViewById(R.id.character_name)
    private val blurBackground: BlurLayout = view.findViewById(R.id.blur_background)
    private val image: ImageView = view.findViewById(R.id.character_image)
    private val animator: ViewAnimator = view.findViewById(R.id.animator)
    private val context = view.context

    private var item: ResultsItem? = null

    init {
        view.setOnClickListener {

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
        applyBlur()
        val url = item.thumbnail.path + "." + item.thumbnail.extension
        val uri = Uri.parse(url)
        Picasso.get()
            .load(uri)
            .into(image, object : Callback {
                override fun onSuccess() {
                    animator.displayedChild = 1
                }

                override fun onError(e: java.lang.Exception?) {
                    animator.displayedChild = 1
                    image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.image_placeholder))
                    e?.printStackTrace()
                    Log.i(TAG, "onError: ${e?.message}")
                }
            })
    }

    private fun applyBlur() {
        try {
            blurBackground.startBlur()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "CharactersViewHolder"
        fun create(parent: ViewGroup): CharactersViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.character_item, parent, false)
            return CharactersViewHolder(view)
        }
    }
}
