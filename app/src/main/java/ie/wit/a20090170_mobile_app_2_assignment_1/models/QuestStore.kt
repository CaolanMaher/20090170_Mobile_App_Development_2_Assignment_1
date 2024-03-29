package ie.wit.a20090170_mobile_app_2_assignment_1.models

interface QuestStore {
    fun findAll() : List<QuestModel>
    fun findAllForUser(userID: String) : List<QuestModel>
    fun findById(id : Long) : QuestModel?
    fun create(quest : QuestModel)
    fun update(quest : QuestModel)
    fun updateImage(userID: String, imageUri: String)
    fun delete(id : Long)
    fun searchByQuestName(name : String) : List<QuestModel>
    fun getAllFromDatabase()
    //fun getAllForUser(userID: String)
    fun addToDatabase(quest : QuestModel)
    fun deleteFromDatabase(id : Long)
    fun updateInDatabase(quest : QuestModel)
    fun getLatestID()
}