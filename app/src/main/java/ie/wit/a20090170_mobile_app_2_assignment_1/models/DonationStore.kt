package ie.wit.a20090170_mobile_app_2_assignment_1.models

interface DonationStore {
    fun findAll() : List<DonationModel>
    fun findById(id: Long) : DonationModel?
    fun create(donation: DonationModel)
}