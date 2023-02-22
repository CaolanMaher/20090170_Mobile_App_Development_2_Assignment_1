package ie.wit.a20090170_mobile_app_2_assignment_1.ui.campaign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestManager
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel

class CampaignViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    private val questsList = MutableLiveData<List<QuestModel>>()

    val observableStatus: LiveData<Boolean>
        get() = status

    val observableQuestsList: LiveData<List<QuestModel>>
        get() = questsList

    init {
        load()
    }

    fun load() {
        questsList.value = QuestManager.findAll()
    }

    fun searchByQuestName(name : String) {
        questsList.value = QuestManager.searchByQuestName(name)
    }
}