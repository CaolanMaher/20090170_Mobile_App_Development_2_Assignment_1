package ie.wit.a20090170_mobile_app_2_assignment_1.main

import android.app.Application
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestManager
import ie.wit.a20090170_mobile_app_2_assignment_1.models.QuestStore
import timber.log.Timber

class DNDCampaignManagerApp : Application() {

    lateinit var questsStore : QuestStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        questsStore = QuestManager
        Timber.i("DND App Started")
    }
}