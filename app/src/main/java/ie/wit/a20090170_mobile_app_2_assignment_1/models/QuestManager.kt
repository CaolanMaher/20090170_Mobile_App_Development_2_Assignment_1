package ie.wit.a20090170_mobile_app_2_assignment_1.models

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

var questLastId = 0L;

internal fun getQuestId(): Long {
    return questLastId++
}

object QuestManager : QuestStore {

    val quests = ArrayList<QuestModel>()

    val db = Firebase.firestore

    init {
        getAllFromDatabase()
        getLatestID()
    }

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

        addToDatabase(quest)
        logAll()
    }

    override fun update(quest: QuestModel) {
        val oldQuest : QuestModel? = quests.find { it.id == quest.id }
        quests.remove(oldQuest)
        quests.add(quest)

        updateInDatabase(quest)
    }

    override fun delete(id: Long) {
        val foundQuest : QuestModel? = quests.find { it.id == id }
        quests.remove(foundQuest)

        deleteFromDatabase(id)
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

    override fun getAllFromDatabase() {
        quests.clear()

        db.collection("Quests")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Timber.v("Found DATA")
                    val questID = document.data.get("ID").toString().toLong()
                    val questName = document.data.get("Name").toString()
                    val questDescription = document.data.get("Description").toString()
                    val questLocationName = document.data.get("LocationName").toString()
                    val questReward = document.data.get("Reward").toString().toInt()
                    val questIsComplete = document.data.get("isCompleted").toString().toBoolean()
                    val quest = QuestModel(questID, questName, questDescription, questLocationName, questReward, questIsComplete)

                    Timber.v("QUEST NAME $questName $questIsComplete")

                    quests.add(quest)
                }
            }
            .addOnFailureListener { exception ->
                Timber.v("** Could Not Get From Database **")
                Timber.v("FAILED$exception")
            }

        //return quests
    }

    override fun addToDatabase(quest: QuestModel) {

        val questToAdd = hashMapOf(
            "ID" to quest.id,
            "Name" to quest.name,
            "Description" to quest.description,
            "LocationName" to quest.locationName,
            "Reward" to quest.reward,
            "isCompleted" to quest.isCompleted
        )

        db.collection("Quests")
            .add(questToAdd)
            .addOnFailureListener { exception ->
                Timber.v("** Could Not Add To Database **")
                Timber.v("FAILED$exception")
            }
    }

    override fun deleteFromDatabase(id: Long) {

        db.collection("Quests")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data.get("ID").toString().toLong() == id) {
                        db.collection("Quests").document(document.id).delete()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Timber.v("** Could Not Remove From Database **")
                Timber.v("FAILED$exception")
            }
    }

    override fun updateInDatabase(quest: QuestModel) {
        db.collection("Quests")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data.get("ID").toString().toLong() == quest.id) {
                        val questDocument = db.collection("Quests").document(document.id)
                        questDocument.update("Name", quest.name)
                        questDocument.update("Description", quest.description)
                        questDocument.update("LocationName", quest.locationName)
                        questDocument.update("Reward", quest.reward)
                        questDocument.update("isCompleted", quest.isCompleted)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Timber.v("** Could Not Update From Database **")
                Timber.v("FAILED$exception")
            }
    }

    override fun getLatestID() {

        var largestID = 0L

        db.collection("Quests")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data.get("ID").toString().toLong() > largestID) {
                        largestID = document.data.get("ID").toString().toLong()
                        questLastId = largestID + 1
                    }
                }
            }
            .addOnFailureListener { exception ->
                Timber.v("** Could Not Get Latest ID From Database **")
                Timber.v("FAILED$exception")
            }

        if(questLastId == 0L) {
            questLastId = 1
        }
    }


    private fun logAll() {
        Timber.v("** Quests List **")
        quests.forEach { Timber.v("Quest $it") }
    }

}