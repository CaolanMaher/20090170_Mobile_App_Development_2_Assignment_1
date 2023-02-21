package ie.wit.a20090170_mobile_app_2_assignment_1.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestModel (
    var id: Long = 0,
    var name : String = "N/A",
    var description : String = "N/A",
    var locationName : String = "N/A",
    var reward : Int = 0,
    var isCompleted : Boolean = false
) : Parcelable