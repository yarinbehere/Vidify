package be.yarin.vidify.ui.video

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import be.yarin.vidify.R
import be.yarin.vidify.data.SortOrder
import be.yarin.vidify.data.Video
import be.yarin.vidify.databinding.FragmentVideoBinding
import be.yarin.vidify.util.OnQueryTextListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideosFragment : Fragment(R.layout.fragment_video), VideoAdapter.onItemClickListener {

    private val viewModel: VideoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentVideoBinding.bind(view)

        val videoAdapter = VideoAdapter(this)

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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.videosEvent.collect { event ->
                when (event) {
                    is VideoViewModel.VideosEvent.NavigateToEditVideoScreen -> {
                        val action =
                            VideosFragmentDirections.actionVideosFragmentToEditVideoFragment(event.video)
                        findNavController().navigate(action)
                    }
                    else -> {

                    }
                }
            }
        }

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_fragment_videos, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.OnQueryTextListener {
            viewModel.searchQuery.value = it
            // crossinline for no return
        }

        // Update menu for current view lifecycle scope
        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_complete).isChecked =
                viewModel.prefFlow.first().hideCompleted // Observe only once
        }
        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_unfavorite).isChecked =
                viewModel.prefFlow.first().hideUnfavorated // Observe only once
        }


    }

    override fun onItemClick(video: Video) {
        viewModel.onVideoSelected(video)
    }

    override fun onCheckBoxClick(video: Video, isChecked: Boolean) {
        viewModel.onVideoCheckedChanged(video, isChecked)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_date -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE_CREATE)
                true
            }
            R.id.action_sort_recent -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE_VISIT)
                true
            }
            R.id.action_hide_complete -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedVideo(item.isChecked)
                true
            }
            R.id.action_hide_unfavorite -> {
                item.isChecked = !item.isChecked
                viewModel.onHideUnfavoratedVideo(item.isChecked)
                true
            }
            else -> return super.onOptionsItemSelected(item)

        }
        //        return super.onOptionsItemSelected(item)
    }

}