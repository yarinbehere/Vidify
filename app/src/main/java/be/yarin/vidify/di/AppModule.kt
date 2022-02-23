package be.yarin.vidify.di

import android.app.Application
import androidx.room.Room
import be.yarin.vidify.data.VideoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: VideoDatabase.Callback
    ) = Room.databaseBuilder(app, VideoDatabase::class.java, "video_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideVideoDao(db: VideoDatabase) = db.videoDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun privateApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope