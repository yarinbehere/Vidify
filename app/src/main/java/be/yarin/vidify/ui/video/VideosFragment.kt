package be.yarin.vidify.ui.video

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import be.yarin.vidify.R
import be.yarin.vidify.databinding.FragmentVideoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideosFragment: Fragment(R.layout.fragment_video) {

    private val viewModel : VideoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentVideoBinding.bind(view)

        val videoAdapter = VideoAdapter()

        binding.apply {
            rvVids.apply {
                adapter = videoAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.videos.observe(viewLifecycleOwner) {
            videoAdapter.submitList(it) // Changes will be applied
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_fragment_videos, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView




    }
}