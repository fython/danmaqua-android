package moe.feng.danmaqua.ui.settings

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moe.feng.common.eventshelper.EventsHelper
import moe.feng.danmaqua.event.SettingsChangedListener
import androidx.content.eventsHelper

abstract class BasePreferenceFragment : PreferenceFragmentCompat() {

    private var eventsHelper: EventsHelper? = null
    private val settingsChangedListener = object : SettingsChangedListener {
        override fun onSettingsChanged() {
            this@BasePreferenceFragment.onSettingsChanged()
        }
    }

    val defaultPreferences: SharedPreferences get() = MMKV.defaultMMKV()

    override fun onDestroy() {
        super.onDestroy()
        eventsHelper?.unregisterListener(settingsChangedListener)
        eventsHelper = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        eventsHelper = context.eventsHelper
        eventsHelper?.registerListener(settingsChangedListener)
        val title = getActivityTitle(context)
        if (title != null) {
            val activity = if (context is Activity) context else this.activity
            activity?.title = title
        }
    }

    fun setPreferenceClickListener(key: String,
                                   onClick: suspend CoroutineScope.() -> Unit) {
        findPreference<Preference>(key)!!.setOnPreferenceClickListener {
            lifecycleScope.launch { onClick() }
            true
        }
    }

    inline fun <reified T : Any> setPreferenceChangeListener(
        key: String,
        crossinline onChange: (T) -> Boolean) {
        findPreference<Preference>(key)!!.setOnPreferenceChangeListener { _, newValue ->
            onChange(newValue as T)
        }
    }

    fun launchPreference(action: String) {
        activity?.let { PreferenceActivity
            .launch(it, action) }
    }

    open fun getActivityTitle(context: Context): CharSequence? {
        return null
    }

    open fun onSettingsChanged() {

    }

    inline fun <reified T : Preference> preference(key: String): Lazy<T> {
        return lazy { findPreference<T>(key)!! }
    }

    fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenCreated(block)

    fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenStarted(block)

    fun launchWhenResumed(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenResumed(block)

}