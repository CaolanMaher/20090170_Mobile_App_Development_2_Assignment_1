package ie.wit.a20090170_mobile_app_2_assignment_1.models

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class QuestModel (
    var userId: String = "",
    var id: Long = 0,
    var name : String = "N/A",
    var description : String = "N/A",
    var locationName : String = "N/A",
    var reward : Int = 0,
    var isCompleted : Boolean = false,
    var latitude : Double = 0.0,
    var longitude : Double = 0.0,
    var profilepic: String = "",
    var isFavourite: Boolean = false
) : Parcelable

{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "UserID" to userId,
            "ID" to id,
            "Name" to name,
            "Description" to description,
            "LocationName" to locationName,
            "Reward" to reward,
            "isCompleted" to isCompleted,
            "Latitude" to latitude,
            "Longitude" to longitude,
            "ProfilePic" to profilepic,
            "isFavourite" to isFavourite
        )
    }
}