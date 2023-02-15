package ie.wit.a20090170_mobile_app_2_assignment_1.models

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object QuestManager : QuestStore {

    val quests = ArrayList<QuestModel>()

    override fun findAll(): List<QuestModel> {
        return quests
    }

    override fun findById(id: Long): QuestModel? {
        val foundQuest : QuestModel? = quests.find { it.id == id }
        return foundQuest
    }

    override fun create(quest: QuestModel) {
        quest.id = getId()
        quests.add(quest)
        logAll()
    }

    private fun logAll() {
        Timber.v("** Quest List")
        quests.forEach { Timber.v("Quest $it")}
    }

}