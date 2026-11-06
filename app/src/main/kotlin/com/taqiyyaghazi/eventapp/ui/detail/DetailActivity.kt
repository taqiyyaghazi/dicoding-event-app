package com.taqiyyaghazi.eventapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.taqiyyaghazi.eventapp.R
import com.taqiyyaghazi.eventapp.data.local.entity.EventEntity
import com.taqiyyaghazi.eventapp.data.remote.response.ListEventsItem
import com.taqiyyaghazi.eventapp.databinding.ActivityDetailBinding
import com.taqiyyaghazi.eventapp.ui.common.ViewModelFactory
import com.taqiyyaghazi.eventapp.utils.Result

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    private var currentEvent: ListEventsItem? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_detail_event)

        val eventId = intent.getStringExtra(EXTRA_EVENT_ID) ?: return

        setupObservers(eventId)
    }

    private fun setupObservers(eventId: String) {
        detailViewModel.getDetailEvent(eventId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val event = result.data
                    currentEvent = event
                    populateDetails(event)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        detailViewModel.isFavorite(eventId).observe(this) { favoriteStatus ->
            isFavorite = favoriteStatus
            updateFavoriteIcon(favoriteStatus)
        }

        binding.fabFavorite.setOnClickListener {
            val event = currentEvent ?: return@setOnClickListener
            val eventEntity = EventEntity(
                id = event.id.toString(),
                name = event.name,
                mediaCover = event.mediaCover,
                summary = event.summary,
                ownerName = event.ownerName,
                beginTime = event.beginTime
            )
            detailViewModel.setFavorite(eventEntity, !isFavorite)
            val message = if (!isFavorite) {
                getString(R.string.favorite_added)
            } else {
                getString(R.string.favorite_removed)
            }
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun populateDetails(event: ListEventsItem) {
        binding.tvEventName.text = event.name
        binding.tvOwnerName.text = getString(R.string.event_organizer_label, event.ownerName)
        binding.tvBeginTime.text = getString(R.string.event_begin_time_label, event.beginTime)

        val remainingQuota = event.quota - event.registrants
        binding.tvRemainingQuota.text = getString(R.string.event_remaining_quota_label, remainingQuota)

        binding.tvDescription.text = HtmlCompat.fromHtml(
            event.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.ivMediaCover)

        binding.btnOpenLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(browserIntent)
        }
    }

    private fun updateFavoriteIcon(isFav: Boolean) {
        if (isFav) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
