package be.yarin.vidify.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import be.yarin.vidify.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Video::class], version = 1)
abstract class VideoDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    class Callback @Inject constructor(
        private val database: Provider<VideoDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().videoDao()

            applicationScope.launch {
                dao.insert(Video("Creme"))
                dao.insert(Video("Pizza", favorite = true))
                dao.insert(Video("Burger", completed = true))
            }

        }

    }

}