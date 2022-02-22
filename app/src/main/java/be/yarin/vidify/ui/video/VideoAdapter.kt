package be.yarin.vidify.ui.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.yarin.vidify.data.Video
import be.yarin.vidify.databinding.ItemVidBinding

class VideoAdapter : ListAdapter<Video, VideoAdapter.VideoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVidBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class VideoViewHolder(private val binding : ItemVidBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            binding.apply {
                cbComplete.isChecked = video.completed
                tvName.text = video.name
                tvName.paint.isStrikeThruText = video.completed
                ivHeart.isVisible = video.favorite
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean = oldItem == newItem
    }

}