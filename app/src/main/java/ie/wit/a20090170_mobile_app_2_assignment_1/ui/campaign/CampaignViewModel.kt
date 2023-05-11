package ie.wit.a20090170_mobile_app_2_assignment_1.ui.campaign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestManager
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel
import timber.log.Timber

class CampaignViewModel : ViewModel() {

    var auth: FirebaseAuth = Firebase.auth

    var readOnly = MutableLiveData(false)

    private val status = MutableLiveData<Boolean>()

    private val questsList = MutableLiveData<List<QuestModel>>()

    val observableStatus: LiveData<Boolean>
        get() = status

    val observableQuestsList: LiveData<List<QuestModel>>
        get() = questsList

    fun load() {
        Timber.v("FETCHING DATA")

        readOnly.value = false

        val user = auth.currentUser

        if(user != null) {

            //questsList.value = QuestManager.getAllForUser(user.uid)

            //questsList.value = QuestManager.find
            //questsList.value = QuestManager.findAll()
            //questsList.value = QuestManager.getAllFromDatabase()

            questsList.value = QuestManager.findAllForUser(user.uid)
        }
    }

    fun loadAll() {
        Timber.v("FETCHING ALL DATA")

        readOnly.value = true

        val user = auth.currentUser

        if(user != null) {

            //questsList.value = QuestManager.getAllForUser(user.uid)

            //questsList.value = QuestManager.find
            //questsList.value = QuestManager.findAll()
            //QuestManager.getAllFromDatabase()

            questsList.value = QuestManager.findAll()
        }
    }

    fun searchByQuestName(name : String) {
        questsList.value = QuestManager.searchByQuestName(name)
    }

    fun delete(id : Long) {
        try {
            QuestManager.delete(id)
            Timber.i("Successfully Deleted : ${questsList.value}")
        }
        catch (e: Exception) {
            Timber.i("Error Deleting : ${e.message}")
        }
    }
}