package be.yarin.vidify.data

import android.content.Context
import android.util.Log
import androidx.datastore.createDataStore
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesRepository"

enum class SortOrder {
    BY_NAME, BY_DATE_CREATE, BY_DATE_VISIT
}

data class FilterPreferences(
    val sortOrder: SortOrder,
    val hideCompleted: Boolean,
    val hideUnfavorated: Boolean
)

@Singleton
class PreferencesRepository @Inject constructor(@ApplicationContext context: Context) {

    // Create the datastore for all user pref
    private val dataStore = context.createDataStore("user_pref")

    // flow for settings
    val prefFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, exception.toString())
                emit(emptyPreferences()) // If reading fails, avoid crash and emit empty prefs
            } else {
                throw exception
            }

        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE_CREATE.name
            )
            val hideCompleted = preferences[PreferencesKeys.HIDE_COMPLETED] ?: false
            val hideUnfavorated = preferences[PreferencesKeys.HIDE_UNFAVORATED] ?: false

            FilterPreferences(sortOrder, hideCompleted, hideUnfavorated)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    suspend fun updateHideUnfavorated(hideUnfavorated: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.HIDE_UNFAVORATED] = hideUnfavorated
        }
    }

    private object PreferencesKeys {
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hide_completed")
        val HIDE_UNFAVORATED = preferencesKey<Boolean>("hide_unfavorated")
    }

}