package com.nosta.gpstrackercourse.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.nosta.gpstrackercourse.MainApp
import com.nosta.gpstrackercourse.MainViewModel
import com.nosta.gpstrackercourse.R
import com.nosta.gpstrackercourse.databinding.ViewTrackBinding
import com.nosta.gpstrackercourse.utils.showToast
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay

class ViewTrackFragment : Fragment() {
    private var mgr: CacheManager? = null
    private var startPoint: GeoPoint? = null
    private lateinit var binding: ViewTrackBinding
    private val model: MainViewModel by activityViewModels{
        MainViewModel.ViewModeFactory((requireContext().applicationContext as MainApp).database)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsOsm()
        binding = ViewTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTrack()
//        binding.map.setBuiltInZoomControls(false);
        binding.map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.map.setMultiTouchControls(true);
        val mRotationGestureOverlay = RotationGestureOverlay(context, binding.map)
        mRotationGestureOverlay.setEnabled(true)
        binding.map.getOverlays().add(mRotationGestureOverlay)

        binding.map.setZoomRounding(true);//округление зумов
        binding.fCenter.setOnClickListener{
            //пока не надо переносить к старту
//            if (startPoint != null) binding.map.controller.animateTo(startPoint)
            mgr = CacheManager(binding.map);
            var info: String= "Zoom: ${binding.map.zoomLevel} \n Size: ${mgr?.cacheCapacity()}" +
                    "\n User: ${mgr?.currentCacheUsage()}" +
                    "\n tile cache: " + Configuration.getInstance().getOsmdroidTileCache().toString() +
                    "\n Cache maxSize: " + Configuration.getInstance().tileFileSystemCacheMaxBytes +
                    "\n Cache trimSize: " + Configuration.getInstance().tileFileSystemCacheTrimBytes +
                    "\n Cache freeSpace: " + Configuration.getInstance().getOsmdroidTileCache().freeSpace +
                    "\n ThreadsDownload: " + Configuration.getInstance().tileFileSystemThreads +
                    "\n QueueSize " + Configuration.getInstance().tileFileSystemMaxQueueSize;
//            Configuration.getInstance().tileFileSystemCacheMaxBytes = 999999999;
//            Configuration.getInstance().osmdroidTileCache = File("asd");
            showToast(info)
        }
    }

    private fun getTrack() = with(binding){
        model.currentTrack.observe(viewLifecycleOwner) {
            tvData.text = "Data: ${it.data}"
            tvTime.text = it.time
            tvAverageVel.text = "Average speed ${it.speed} km/h"
            tvDistance.text = "Distance ${it.distance} km"
            val polyline = getPolyline(it.geoPoints)
            map.overlays.add(polyline)
            setMarkers(polyline.actualPoints)
            goToStartPosition(polyline.actualPoints[0])
            startPoint = polyline.actualPoints[0]
        }
    }

    private fun goToStartPosition(startPosition: GeoPoint) {
        binding.map.controller.zoomTo(18.0)
        binding.map.controller.animateTo(startPosition)
    }

    private fun setMarkers(list: List<GeoPoint>) = with(binding){
        val startMarker = Marker(map)
        val finishMarker = Marker(map)
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        finishMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarker.icon = getDrawable(requireContext(), R.drawable.ic_start_position)
        finishMarker.icon = getDrawable(requireContext(), R.drawable.ic_finish_position)
        startMarker.position = list[0]
        finishMarker.position = list[list.size - 1]
        // TODO эта херня не работает
//        startMarker.title = "Bauman Monument"
//        startMarker.snippet = "Bauman Monument"
//        startMarker.image = resources.getDrawable(R.drawable.baumanka)
//        startMarker.subDescription = """
//            Bsdkfghskjdfghskjdjfglks
//            asdfkjagksjdfgakjsdf
//            asdfkjaghsjkdgja
//            sdflgjshdkf
//            faskjdfhgakjsdhfgakjsfg
//            kasjdfhlkajsdhlkfjahlkjhsdfl
//            kasjdfhlaksjdfhkjh
//            asdjf;asjdf;lajksdfl
//            osadfkjasodjfpoa
//            aihdfoihasodfuasdfm
//            lksdfgjdfshjghdfiouhoiuh.
//            """.trimIndent()
        map.overlays.add(startMarker)
        map.overlays.add(finishMarker)
    }
    private fun getPolyline(geoPoint: String): Polyline {
        val polyline = Polyline()
        polyline.outlinePaint.color = Color.parseColor(
            PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("color_key", "#FF009EDA"))
        val list = geoPoint.split("/")
        list.forEach {
            if (it.isEmpty()) return@forEach
            val points = it.split(",")
            polyline.addPoint(GeoPoint(points[0].toDouble(),points[1].toDouble()))
        }
        return polyline
    }


    private fun settingsOsm(){
        Configuration.getInstance().load(
            activity as AppCompatActivity,
            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }

    companion object {
        @JvmStatic
        fun newInstance() = ViewTrackFragment()
    }
}