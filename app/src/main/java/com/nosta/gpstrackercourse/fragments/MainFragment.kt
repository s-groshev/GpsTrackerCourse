package com.nosta.gpstrackercourse.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import  android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.nosta.gpstrackercourse.R
import com.nosta.gpstrackercourse.databinding.FragmentMainBinding
import com.nosta.gpstrackercourse.location.LocationService
import com.nosta.gpstrackercourse.utils.DialogManager
import com.nosta.gpstrackercourse.utils.checkPermission
import com.nosta.gpstrackercourse.utils.showToast
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainFragment : Fragment() {
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsOsm()
        Log.d("MyLog", "onCreateView")
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("MyLog", "onResume")
        checkLocPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initOSM()
        Log.d("MyLog", "onViewCreated")
        registerPermission()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity?.startForegroundService(Intent(activity, LocationService::class.java))
        } else {
            activity?.startService(Intent(activity, LocationService::class.java))
        }
    }

    private fun settingsOsm(){
        Configuration.getInstance().load(
            activity as AppCompatActivity,
            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }

    private fun initOSM() = with(binding) {
        map.controller.setZoom(20.0)
//        map.controller.animateTo(GeoPoint(40.4167,-3.70325))
        val mLogProvider = GpsMyLocationProvider(activity)
        val mLockOverlay = MyLocationNewOverlay(mLogProvider, map)
        mLockOverlay.enableMyLocation()
        mLockOverlay.enableFollowLocation()
        mLockOverlay.runOnFirstFix{
            map.overlays.clear()
            map.overlays.add(mLockOverlay)
        }
    }

    private fun registerPermission() {
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                initOSM()
                checkLocationEnabled()
            } else {
                showToast("Вы не дали разрешения на использользование местоположения!")
            }
        }
    }

    private fun checkLocPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkPermissionAfter10()
        } else {
            checkPermissionBefore10()
        }
    }

    private fun checkPermissionAfter10(){
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            && checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            initOSM()
            checkLocationEnabled()
        } else {
            pLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ))
        }
    }

    private fun checkPermissionBefore10(){
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            initOSM()
            checkLocationEnabled()
        } else {
            pLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun checkLocationEnabled() {
        val lManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isEnabled = lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isEnabled) {
            DialogManager.showLocEnableDialog(
                activity as AppCompatActivity,
                object: DialogManager.Listener {
                    override fun onClick() {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }

                }
            )
        } else {
            showToast("Location enabled")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}