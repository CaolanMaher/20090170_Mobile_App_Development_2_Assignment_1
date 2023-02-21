package ie.wit.a20090170_mobile_app_2_assignment_1.ui.campaign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestManager
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel

class CampaignViewModel : ViewModel() {

    private val questsList = MutableLiveData<List<QuestModel>>()

    val observableQuestsList: LiveData<List<QuestModel>>
        get() = questsList

    init {
        load()
    }

    fun load() {
        questsList.value = QuestManager.findAll()
    }
}