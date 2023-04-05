package com.nosta.gpstrackercourse.fragments

import  android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nosta.gpstrackercourse.R
import com.nosta.gpstrackercourse.databinding.FragmentMainBinding
import com.nosta.gpstrackercourse.databinding.ViewTrackBinding

class ViewTrackFragment : Fragment() {
    private lateinit var binding: ViewTrackBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ViewTrackFragment()
    }
}