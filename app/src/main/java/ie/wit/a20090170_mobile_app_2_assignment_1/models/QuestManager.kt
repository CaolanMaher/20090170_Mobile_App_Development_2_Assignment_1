package ie.wit.a20090170_mobile_app_2_assignment_1.models

import timber.log.Timber

var questLastId = 0L;

internal fun getQuestId(): Long {
    return questLastId++
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
        quest.id = getQuestId()
        quests.add(quest)
        logAll()
    }

    override fun update(quest: QuestModel) {
        val oldQuest : QuestModel? = quests.find { it.id == quest.id }
        quests.remove(oldQuest)
        quests.add(quest)
    }

    override fun delete(id: Long) {
        val foundQuest : QuestModel? = quests.find { it.id == id }
        quests.remove(foundQuest)
    }

    override fun searchByQuestName(name: String): List<QuestModel> {
        val foundQuests = ArrayList<QuestModel>()

        for(quest in quests) {
            if(quest.name.contains(name)) {
                foundQuests.add(quest)
            }
        }

        return foundQuests
    }

    private fun logAll() {
        Timber.v("** Quests List **")
        quests.forEach { Timber.v("Quest $it") }
    }

}