package ie.wit.a20090170_mobile_app_2_assignment_1.models

interface QuestStore {
    fun findAll() : List<QuestModel>
    fun findById(id : Long) : QuestModel?
    fun create(quest : QuestModel)
}