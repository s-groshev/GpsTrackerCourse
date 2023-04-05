package com.nosta.gpstrackercourse.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import com.nosta.gpstrackercourse.R
import com.nosta.gpstrackercourse.utils.showToast

class SettingFragment: PreferenceFragmentCompat() {
    private lateinit var timePref: Preference
    private lateinit var colorPref: Preference
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
        init()
    }

    private fun init() {
        timePref = findPreference("update_time_key")!!
        colorPref = findPreference("color_key")!!
        val chancheListener = onChangeListener()
        timePref.onPreferenceChangeListener = chancheListener
        colorPref.onPreferenceChangeListener = chancheListener
        initPrefs()
    }

    private fun onChangeListener(): OnPreferenceChangeListener{
        return Preference.OnPreferenceChangeListener{
            preference, value ->
                when(preference.key) {
                    "update_time_key" -> onTimeChange(value.toString())
                    "color_key" -> preference.icon?.setTint(Color.parseColor(value.toString()))
                }
            true
        }
    }

    private fun onTimeChange(value: String) {
        val nameArray = resources.getStringArray(R.array.loc_time_update_name)
        val valueArray = resources.getStringArray(R.array.loc_time_update_value)
        val title = timePref.title.toString().substringBefore(":")
        val pos = valueArray.indexOf(value)
        timePref.title = "$title: ${nameArray[pos]}"
    }



    private fun initPrefs() {
        val pref = timePref.preferenceManager.sharedPreferences
        val nameArray = resources.getStringArray(R.array.loc_time_update_name)
        val valueArray = resources.getStringArray(R.array.loc_time_update_value)
        val title = timePref.title
        val pos = valueArray.indexOf(pref?.getString("update_time_key", "3000"))
        timePref.title = "$title: ${nameArray[pos]}"

        val trackColor = pref?.getString("color_key","#FF009EDA")
        colorPref.icon?.setTint(Color.parseColor(trackColor))

    }
}