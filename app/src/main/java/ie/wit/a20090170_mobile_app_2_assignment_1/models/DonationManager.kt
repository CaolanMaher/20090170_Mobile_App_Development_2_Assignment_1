package ie.wit.a20090170_mobile_app_2_assignment_1.models

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object DonationManager : DonationStore {

    val donations = ArrayList<DonationModel>()

    override fun findAll(): List<DonationModel> {
        return donations
    }

    override fun findById(id:Long) : DonationModel? {
        val foundDonation: DonationModel? = donations.find { it.id == id }
        return foundDonation
    }

    override fun create(donation: DonationModel) {
        donation.id = getId()
        donations.add(donation)
        logAll()
    }

    fun logAll() {
        Timber.v("** Donations List **")
        donations.forEach { Timber.v("Donate ${it}") }
    }
}