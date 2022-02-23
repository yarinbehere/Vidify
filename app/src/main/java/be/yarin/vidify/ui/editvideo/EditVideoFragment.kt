package be.yarin.vidify.ui.editvideo

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import be.yarin.vidapp.VidActivity
import be.yarin.vidify.R
import be.yarin.vidify.databinding.FragmentEditVidBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EditVideoFragment : Fragment(R.layout.fragment_edit_vid) {

    private val viewModel: EditVideoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEditVidBinding.bind(view)

        binding.apply {
            tvVidName.text = viewModel.videoName
            cbFav.isChecked = viewModel.videoFavorite
            cbFav.jumpDrawablesToCurrentState() // will be drawn immediately
            tvDateCreated.isVisible = viewModel.video != null
            tvDateCreated.text = "Created: ${viewModel.video?.createdDateFormatted}"
            val url = "https://img.youtube.com/vi/${viewModel.video?.vidid}/maxresdefault.jpg"

            Glide.with(requireContext()).load(url).into(ivThumb)

            ivPlay.setOnClickListener {
                startActivity(
                    Intent(requireContext(), VidActivity::class.java).putExtra(
                        "vidid",
                        viewModel.video?.vidid
                    )
                )
            }

            tvDateVisited.text = "Last Visited: ${viewModel.video?.visitedDateFormatted}"

            cbFav.setOnCheckedChangeListener { _, isChecked ->
                viewModel.video?.let { it1 -> viewModel.onVideoFavorited(it1, isChecked) }
            }

            fbSaveVid.setOnClickListener {
                viewModel.video?.let { it1 -> viewModel.onVideoCheckedChanged(it1, true) }
                Snackbar.make(requireView(), "Marked as complete!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.video?.let { it1 ->
            viewModel.onVideoRecentVisit(
                it1,
                System.currentTimeMillis()
            )
        }
    }

}