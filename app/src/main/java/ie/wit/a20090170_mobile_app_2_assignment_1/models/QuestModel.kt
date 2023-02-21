package ie.wit.a20090170_mobile_app_2_assignment_1.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestModel (
    var id: Long = 0,
    val description : String = "N/A",
    val locationName : String = "N/A",
    val reward : Int = 0,
    val isCompleted : Boolean = false
) : Parcelable