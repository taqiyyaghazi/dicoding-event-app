package com.taqiyyaghazi.eventapp.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.taqiyyaghazi.eventapp.data.remote.response.ListEventsItem
import com.taqiyyaghazi.eventapp.databinding.FragmentFavoriteBinding
import com.taqiyyaghazi.eventapp.ui.adapter.EventAdapter
import com.taqiyyaghazi.eventapp.ui.common.ViewModelFactory
import com.taqiyyaghazi.eventapp.ui.detail.DetailActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
            }
            startActivity(intent)
        }
        binding.rvFavoriteEvents.adapter = adapter

        viewModel.favoriteEvents.observe(viewLifecycleOwner) { favoriteList ->
            if (favoriteList.isNullOrEmpty()) {
                binding.tvEmptyFavorite.visibility = View.VISIBLE
                adapter.submitList(emptyList())
            } else {
                binding.tvEmptyFavorite.visibility = View.GONE
                val events = favoriteList.map { entity ->
                    ListEventsItem(
                        id = entity.id.toIntOrNull() ?: 0,
                        name = entity.name,
                        summary = entity.summary ?: "",
                        description = "",
                        imageLogo = entity.mediaCover ?: "",
                        mediaCover = entity.mediaCover ?: "",
                        category = "",
                        ownerName = entity.ownerName ?: "",
                        cityName = "",
                        quota = 0,
                        registrants = 0,
                        beginTime = entity.beginTime ?: "",
                        endTime = "",
                        link = ""
                    )
                }
                adapter.submitList(events)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
