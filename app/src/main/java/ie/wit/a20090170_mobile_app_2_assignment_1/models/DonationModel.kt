package ie.wit.a20090170_mobile_app_2_assignment_1.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class DonationModel(var id: Long = 0,
                         val paymentmethod: String = "N/A",
                         val amount: Int = 0) : Parcelable