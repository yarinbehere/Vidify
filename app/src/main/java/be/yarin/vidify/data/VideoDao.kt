package be.yarin.vidify.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    fun getVideos(
        searchQuery: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean,
        hideUnfavorited: Boolean
    ): Flow<List<Video>> =
        when (sortOrder) {
            SortOrder.BY_NAME -> getVideosSortByName(searchQuery, hideCompleted, hideUnfavorited)
            SortOrder.BY_DATE_CREATE -> getVideosSortByDateCreate(
                searchQuery,
                hideCompleted,
                hideUnfavorited
            )
            SortOrder.BY_DATE_VISIT -> getVideosSortByDateVisit(
                searchQuery,
                hideCompleted,
                hideUnfavorited
            )
            else -> getVideosSortByName(searchQuery, hideCompleted, hideUnfavorited)
        }

    // Search for query in any subString of name, sort for favorites
    @Query("SELECT * FROM vid_table WHERE (completed != :hideCompleted OR completed = 0) AND (favorite == :hideUnfavorited OR favorite = 1) AND name LIKE '%' || :searchQuery || '%' ORDER BY name ASC, name")
    fun getVideosSortByName(
        searchQuery: String,
        hideCompleted: Boolean,
        hideUnfavorited: Boolean
    ): Flow<List<Video>>

    // Search for query in any subString of name, sort for favorites
    @Query("SELECT * FROM vid_table WHERE (completed != :hideCompleted OR completed = 0) AND (favorite == :hideUnfavorited OR favorite = 1) AND name LIKE '%' || :searchQuery || '%' ORDER BY created ASC, created")
    fun getVideosSortByDateCreate(
        searchQuery: String,
        hideCompleted: Boolean,
        hideUnfavorited: Boolean
    ): Flow<List<Video>>

    // Search for query in any subString of name, sort for favorites
    @Query("SELECT * FROM vid_table WHERE (completed != :hideCompleted OR completed = 0) AND (favorite == :hideUnfavorited OR favorite = 1) AND name LIKE '%' || :searchQuery || '%' ORDER BY visited ASC, visited")
    fun getVideosSortByDateVisit(
        searchQuery: String,
        hideCompleted: Boolean,
        hideUnfavorited: Boolean
    ): Flow<List<Video>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: Video)

    @Update
    suspend fun update(video: Video)

    @Delete
    suspend fun delete(video: Video)

}