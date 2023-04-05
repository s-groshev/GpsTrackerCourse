package com.nosta.gpstrackercourse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nosta.gpstrackercourse.databinding.ActivityMainBinding
import com.nosta.gpstrackercourse.fragments.MainFragment
import com.nosta.gpstrackercourse.fragments.SettingFragment
import com.nosta.gpstrackercourse.fragments.TracksFragment
import com.nosta.gpstrackercourse.utils.openFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding /*Зачем нужен байндинг*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) /*подключаем биндинг настраиваем один активити*/
        onBottomNavClicks()
        openFragment(MainFragment.newInstance())
    }

    private fun onBottomNavClicks(){
        binding.bNan.setOnItemSelectedListener {
            when(it.itemId){
                R.id.id_home -> openFragment(MainFragment.newInstance())
                R.id.id_tracks -> openFragment(TracksFragment.newInstance())
                R.id.id_settings -> openFragment(SettingFragment())
            }
            true
        }
    }
}