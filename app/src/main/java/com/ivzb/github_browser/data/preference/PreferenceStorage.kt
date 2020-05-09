package com.ivzb.github_browser.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Storage for app and user preferences.
 */
interface PreferenceStorage {
    var accessToken: String?
}

/**
 * [PreferenceStorage] implementation backed by [androidx.security.crypto.EncryptedSharedPreferences].
 */
@Singleton
class SharedPreferenceStorage @Inject constructor(context: Context) : PreferenceStorage {

    private val prefs: Lazy<SharedPreferences> = lazy { // Lazy to prevent IO access to main thread.
        EncryptedSharedPreferences.create(
            PREFS_FILE_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override var accessToken by StringPreference(prefs, PREF_ACCESS_TOKEN)

    companion object {
        const val PREFS_FILE_NAME = "secure_github_browser_store"
        const val PREF_ACCESS_TOKEN = "pref_access_token"
    }
}

class StringPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String? = null
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return preferences.value.getString(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.value.edit { putString(name, value) }
    }
}
