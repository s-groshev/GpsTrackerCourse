package com.nosta.gpstrackercourse.fragments

import  android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosta.gpstrackercourse.MainApp
import com.nosta.gpstrackercourse.MainViewModel
import com.nosta.gpstrackercourse.R
import com.nosta.gpstrackercourse.databinding.FragmentMainBinding
import com.nosta.gpstrackercourse.databinding.TracksBinding
import com.nosta.gpstrackercourse.databinding.ViewTrackBinding
import com.nosta.gpstrackercourse.db.TrackAdapter
import com.nosta.gpstrackercourse.db.TrackItem
import com.nosta.gpstrackercourse.utils.openFragment

class TracksFragment : Fragment(), TrackAdapter.Listener {
    private lateinit var binding: TracksBinding
    private lateinit var adapter: TrackAdapter
    private val model: MainViewModel by activityViewModels{
        MainViewModel.ViewModeFactory((requireContext().applicationContext as MainApp).database)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRCView()
        getTracks()
    }

    private fun getTracks() {
        model.tracks.observe(viewLifecycleOwner){
            adapter.submitList(it)
            binding.tvEmpty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun initRCView() = with(binding) {
        adapter = TrackAdapter(this@TracksFragment)
        rcView.layoutManager = LinearLayoutManager(requireContext())
        rcView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = TracksFragment()
    }

    override fun onClick(track: TrackItem, type: TrackAdapter.ClickType) {
//        Log.d("MyLog", "Delete track ${track.id}")
//        Log.d("MyLog", "track ${track.id} type $type")
//        model.deleteTrack(track)
        when(type){
            TrackAdapter.ClickType.DELETE -> model.deleteTrack(track)
            TrackAdapter.ClickType.OPEN -> {
                model.currentTrack.value = track
                openFragment(ViewTrackFragment.newInstance())
            }
        }
    }
}