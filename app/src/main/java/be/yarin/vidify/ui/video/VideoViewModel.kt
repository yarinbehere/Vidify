package be.yarin.vidify.ui.video

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import be.yarin.vidify.data.PreferencesRepository
import be.yarin.vidify.data.SortOrder
import be.yarin.vidify.data.Video
import be.yarin.vidify.data.VideoDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class VideoViewModel @ViewModelInject constructor(
    private val videoDao: VideoDao,
    private val prefRepository: PreferencesRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    // Holds current search query, use it as flow
    val searchQuery = state.getLiveData("searchQuery", "")

    // Menu flow
    val prefFlow = prefRepository.prefFlow

    private val videosEventChannel = Channel<VideosEvent>()
    val videosEvent = videosEventChannel.receiveAsFlow()

    // multiple flow operator - when filter/sort changes, run this
    private val videoFlow = combine(
        searchQuery.asFlow(),
        prefFlow
    ) { query, filterPref ->
        // everything will be replaced
        Pair(query, filterPref)
    }
        .flatMapLatest { (query, filterPref) ->
            videoDao.getVideos(
                query,
                filterPref.sortOrder,
                filterPref.hideCompleted,
                filterPref.hideUnfavorated
            )
        }

    // Use Flow for LiveData to observe without Context
    val videos = videoFlow.asLiveData()

    // Update pref - the scope lives for viewmodel
    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        prefRepository.updateSortOrder(sortOrder)
    }

    fun onHideCompletedVideo(hideCompleted: Boolean) = viewModelScope.launch {
        prefRepository.updateHideCompleted(hideCompleted)
    }

    fun onHideUnfavoratedVideo(hideUnfavorated: Boolean) = viewModelScope.launch {
        prefRepository.updateHideUnfavorated(hideUnfavorated)
    }

    fun onVideoSelected(video: Video) = viewModelScope.launch {
        videosEventChannel.send(VideosEvent.NavigateToEditVideoScreen(video))
    }

    fun onVideoCheckedChanged(video: Video, isChecked: Boolean) = viewModelScope.launch {
        videoDao.update(video.copy(completed = isChecked))
    }


    sealed class VideosEvent {
        data class NavigateToEditVideoScreen(val video: Video) : VideosEvent()
    }

}

