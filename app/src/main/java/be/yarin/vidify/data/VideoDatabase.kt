package be.yarin.vidify.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import be.yarin.vidify.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Video::class], version = 5)
abstract class VideoDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    class Callback @Inject constructor(
        private val database: Provider<VideoDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().videoDao()

            applicationScope.launch {
                dao.insert(Video("Avocado on Toast with a Twist", "z15ipHV4Now"))
                dao.insert(Video("Teriyaki Duck", "ie6GyJbtDP8"))
                dao.insert(Video("Perfect Spicy Green Shakshuka", "s73qMhFGo14", favorite = true))
                dao.insert(Video("Sunday Beef Dinners", "Es3B8Swni14"))
                dao.insert(Video("Snacking Recipes", "jSb-o8a6u5I"))
                dao.insert(
                    Video(
                        "Homemade Chocolate Donuts",
                        "Yw-FSUEc8Pc",
                        completed = true,
                        favorite = true
                    )
                )
                dao.insert(
                    Video(
                        "Classic Scrambled Eggs and Smoked Salmon",
                        "yv64abAJvEA",
                        favorite = true
                    )
                )
                dao.insert(Video("Christmas Beef Wellington", "Cyskqnp1j64"))
                dao.insert(Video("French Inspired Recipes", "ZS9n3ehTD1c"))
                dao.insert(Video("Chorizo Omelette Recipe", "c7pAzpS0aSs"))
                dao.insert(Video("Thai-Style Meatballs", "tpikKCfm-kU", completed = true))
                dao.insert(Video("Egg-cellent Recipes", "USy5lPI__N4"))
                dao.insert(Video("Sandwich Recipes", "QClxL_mEoCA"))
                dao.insert(
                    Video(
                        "Steak, Fried rice and Fried Eggs",
                        "99ND_B5kMd4",
                        completed = false
                    )
                )
                dao.insert(Video("Homemade Ramen Made Quick", "dWpHngp_ug"))
                dao.insert(Video("Breakfast Recipes", "jikqguPDskA"))
                dao.insert(Video("Bacon Cheesy Toast", "2ZxMNXBwHfQ", favorite = true))
                dao.insert(Video("Winter Warmers", "8xr_OQtlhoI"))
                dao.insert(Video("Winter Beef Recipes", "eTqGvxI-QFY"))
                dao.insert(Video("Classic Recipes With A Twist", "Ja_8F2cQyDE"))
            }

        }

    }

}