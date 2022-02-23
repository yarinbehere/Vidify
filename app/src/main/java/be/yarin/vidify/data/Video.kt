package be.yarin.vidify.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "vid_table")
@Parcelize
data class Video(
    val name: String,
    val vidid: String?,
    val favorite: Boolean = false,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val visited: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable {

    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

    val visitedDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(visited)

}