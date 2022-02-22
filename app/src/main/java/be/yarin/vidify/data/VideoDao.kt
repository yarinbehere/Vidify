package be.yarin.vidify.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Query("SELECT * FROM vid_table")
    fun getVideos(): Flow<List<Video>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: Video)

    @Update
    suspend fun update(video: Video)

    @Delete
    suspend fun delete(video: Video)

}