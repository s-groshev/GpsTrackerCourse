package com.nosta.gpstrackercourse.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import com.nosta.gpstrackercourse.R
import com.nosta.gpstrackercourse.utils.showToast

class SettingFragment: PreferenceFragmentCompat() {
    private lateinit var timePref: Preference
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
        init()
    }

    private fun init() {
        timePref = findPreference("update_time_key")!!
        val chancheListener = onChangeListener()
        timePref.onPreferenceChangeListener = chancheListener
        initPrefs()
    }

    private fun onChangeListener(): OnPreferenceChangeListener{
        return Preference.OnPreferenceChangeListener{
            preference, value ->
            val nameArray = resources.getStringArray(R.array.loc_time_update_name)
            val valueArray = resources.getStringArray(R.array.loc_time_update_value)
            val title = preference.title.toString().substringBefore(":")
            val pos = valueArray.indexOf(value)
            preference.title = "$title: ${nameArray[pos]}"
            true
        }
    }

    private fun initPrefs() {
        val pref = timePref.preferenceManager.sharedPreferences
        val nameArray = resources.getStringArray(R.array.loc_time_update_name)
        val valueArray = resources.getStringArray(R.array.loc_time_update_value)
        val title = timePref.title
        val pos = valueArray.indexOf(pref?.getString("update_time_key", "3000"))
        timePref.title = "$title: ${nameArray[pos]}"
    }
}