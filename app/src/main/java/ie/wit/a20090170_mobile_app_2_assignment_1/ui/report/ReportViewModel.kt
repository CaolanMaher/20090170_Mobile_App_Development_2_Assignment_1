package ie.wit.a20090170_mobile_app_2_assignment_1.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.a20090170_mobile_app_2_assignment_1.models.DonationManager
import ie.wit.a20090170_mobile_app_2_assignment_1.models.DonationModel

class ReportViewModel : ViewModel() {

    private val donationsList = MutableLiveData<List<DonationModel>>()

    val observableDonationsList: LiveData<List<DonationModel>>
        get() = donationsList

    init {
        load()
    }

    fun load() {
        donationsList.value = DonationManager.findAll()
    }
}


/*
class ReportViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}
*/