package ie.wit.a20090170_mobile_app_2_assignment_1.ui.campaign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestManager
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel
import timber.log.Timber

class CampaignViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    private val questsList = MutableLiveData<List<QuestModel>>()

    val observableStatus: LiveData<Boolean>
        get() = status

    val observableQuestsList: LiveData<List<QuestModel>>
        get() = questsList

    fun load() {
        Timber.v("FETCHING DATA")
        questsList.value = QuestManager.findAll()
        //questsList.value = QuestManager.getAllFromDatabase()
    }

    fun searchByQuestName(name : String) {
        questsList.value = QuestManager.searchByQuestName(name)
    }
}