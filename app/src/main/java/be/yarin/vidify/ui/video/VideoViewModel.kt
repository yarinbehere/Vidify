package be.yarin.vidify.ui.video

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import be.yarin.vidify.data.VideoDao

class VideoViewModel @ViewModelInject constructor(
    private val videoDao : VideoDao
): ViewModel() {

    // Use Flow for LiveData to observe without Context
    val videos = videoDao.getVideos().asLiveData()
}