package com.nosta.gpstrackercourse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nosta.gpstrackercourse.location.LocationModel

class MainViewModel : ViewModel() {
    val locationUpdates = MutableLiveData<LocationModel>()
    val timeData = MutableLiveData<String>()
}