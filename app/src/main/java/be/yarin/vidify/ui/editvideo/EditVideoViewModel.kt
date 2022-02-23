package be.yarin.vidify.ui.editvideo

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.yarin.vidify.data.Video
import be.yarin.vidify.data.VideoDao
import kotlinx.coroutines.launch

class EditVideoViewModel @ViewModelInject constructor(
    private val videoDao: VideoDao,
    @Assisted private val state: SavedStateHandle // survive system process kill
) : ViewModel() {

    val video = state.get<Video>("video")

    var videoName = state.get<String>("videoName") ?: video?.name ?: ""
        set(value) {
            field = value
            state.set("videoName", value)
        }

    var videoFavorite = state.get<Boolean>("videoFavorite") ?: video?.favorite ?: false
        set(value) {
            field = value
            state.set("videoFavorite", value)
        }

    var videoComplete = state.get<Boolean>("videoComplete") ?: video?.completed ?: false
        set(value) {
            field = value
            state.set("videoComplete", value)
        }

    fun onVideoCheckedChanged(video: Video, isChecked: Boolean) = viewModelScope.launch {
        videoDao.update(video.copy(completed = isChecked))
    }

    fun onVideoFavorited(video: Video, isChecked: Boolean) = viewModelScope.launch {
        videoDao.update(video.copy(favorite = isChecked))
    }

    fun onVideoRecentVisit(video: Video, date: Long) = viewModelScope.launch {
        videoDao.update(video.copy(visited = date))
    }
}