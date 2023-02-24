package ie.wit.a20090170_mobile_app_2_assignment_1.ui.questDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestManager
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestModel

class QuestDetailViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun updateQuest(quest: QuestModel) {
        status.value = try {
            QuestManager.update(quest)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun deleteQuest(id : Long) {
        status.value = try {
            QuestManager.delete(id)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun getQuests(): List<QuestModel> {
        return QuestManager.findAll()
    }
}