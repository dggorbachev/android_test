package com.sirius.test_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.sirius.test_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val dataModel = DataModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Main image
        Glide.with(this)
            .load(dataModel.image)
            .apply( RequestOptions()
                .fitCenter()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL))
            .into(binding.ivBackdrop)

        // Logo
        Glide.with(this)
            .load(dataModel.logo)
            .centerCrop()
            .into(binding.ivLogo)

        binding.tvAppName.text = dataModel.name
        binding.rbAppRatingAbove.rating = dataModel.rating
        binding.tvAppRatingCountAbove.text = dataModel.gradeCnt


        // Genres/tags
        var layoutManager =
            LinearLayoutManager(binding.rvGenres.context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.initialPrefetchItemCount = dataModel.tags.size

        val genresAdapter = GenresAdapter(genres = dataModel.tags)
        binding.rvGenres.layoutManager = layoutManager
        binding.rvGenres.adapter = genresAdapter

        binding.tvAppDesc.text = dataModel.description
        binding.tvRatingScore.text = dataModel.rating.toString()
        binding.rbAppRatingBelow.rating = dataModel.rating

        val reviews = dataModel.gradeCnt + " Reviews"
        binding.tvAppRatingCountBelow.text = reviews

        // Reviews
        layoutManager =
            LinearLayoutManager(binding.rvAuthors.context, LinearLayoutManager.VERTICAL, false)
        layoutManager.initialPrefetchItemCount = dataModel.reviews.size

        val reviewsAdapter = ReviewsAdapter(reviews = dataModel.reviews)
        binding.rvAuthors.layoutManager = layoutManager
        binding.rvAuthors.adapter = reviewsAdapter
        binding.rvAuthors.isNestedScrollingEnabled = false
    }

    private inner class GenresAdapter(private val genres: List<String>) :
        RecyclerView.Adapter<GenresAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView

            init {
                this.title = view.findViewById<TextView>(R.id.tvTitleGenre)
            }
        }

        lateinit var view: View
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.text = genres[position]
        }

        override fun getItemCount(): Int {
            return genres.size
        }
    }

    private inner class ReviewsAdapter(private val reviews: List<ReviewModel>) :
        RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val authorImage: ImageView
            val authorName: TextView
            val authorDateOfBirth: TextView
            val authorDesc: TextView

            init {
                this.authorImage = view.findViewById<ImageView>(R.id.ivUserImage)
                this.authorName = view.findViewById<TextView>(R.id.tvUsername)
                this.authorDateOfBirth = view.findViewById<TextView>(R.id.tvDate)
                this.authorDesc = view.findViewById<TextView>(R.id.tvUserMessage)
            }
        }

        lateinit var view: View
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val curReview = reviews[position]

            with(holder) {
                Glide.with(view)
                    .load(curReview.userImage)
                    .centerCrop()
                    .into(authorImage)

                authorName.text = curReview.userName
                authorDateOfBirth.text = curReview.date
                authorDesc.text = curReview.message
            }
        }

        override fun getItemCount(): Int {
            return reviews.size
        }
    }
}