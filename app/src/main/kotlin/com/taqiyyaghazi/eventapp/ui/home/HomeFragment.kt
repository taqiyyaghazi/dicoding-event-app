package com.taqiyyaghazi.eventapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.taqiyyaghazi.eventapp.data.remote.response.ListEventsItem
import com.taqiyyaghazi.eventapp.databinding.FragmentHomeBinding
import com.taqiyyaghazi.eventapp.ui.adapter.BannerAdapter
import com.taqiyyaghazi.eventapp.ui.adapter.EventAdapter
import com.taqiyyaghazi.eventapp.ui.common.ViewModelFactory
import com.taqiyyaghazi.eventapp.ui.detail.DetailActivity
import com.taqiyyaghazi.eventapp.utils.Result

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var upcomingAdapter: BannerAdapter
    private lateinit var finishedAdapter: EventAdapter
    private lateinit var searchAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupSearchView()
        observeData()
    }

    private fun setupRecyclerViews() {
        upcomingAdapter = BannerAdapter { event -> openDetail(event) }
        binding.rvUpcomingEvents.adapter = upcomingAdapter

        finishedAdapter = EventAdapter { event -> openDetail(event) }
        binding.rvFinishedEvents.adapter = finishedAdapter

        searchAdapter = EventAdapter { event -> openDetail(event) }
        binding.rvSearchResults.adapter = searchAdapter
    }

    private fun setupSearchView() {
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchView.text.toString().trim()
            if (query.isNotEmpty()) {
                homeViewModel.search(query)
            }
            false
        }
    }

    private fun observeData() {
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> binding.progressBarUpcoming.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBarUpcoming.visibility = View.GONE
                    upcomingAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressBarUpcoming.visibility = View.GONE
                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> binding.progressBarFinished.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBarFinished.visibility = View.GONE
                    finishedAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressBarFinished.visibility = View.GONE
                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        homeViewModel.searchResults.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBarSearch.visibility = View.VISIBLE
                    binding.tvSearchEmpty.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBarSearch.visibility = View.GONE
                    if (result.data.isEmpty()) {
                        binding.tvSearchEmpty.visibility = View.VISIBLE
                    } else {
                        binding.tvSearchEmpty.visibility = View.GONE
                    }
                    searchAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressBarSearch.visibility = View.GONE
                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun openDetail(event: ListEventsItem) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
