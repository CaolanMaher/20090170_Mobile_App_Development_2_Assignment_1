package ie.wit.a20090170_mobile_app_2_assignment_1.ui.quest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestManager
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel

class QuestViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    //private var quests = ArrayList<QuestModel>()

    fun addQuest(quest: QuestModel) {
        status.value = try {
            QuestManager.create(quest)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun getQuests(): List<QuestModel> {
        return QuestManager.findAll()
    }
}
