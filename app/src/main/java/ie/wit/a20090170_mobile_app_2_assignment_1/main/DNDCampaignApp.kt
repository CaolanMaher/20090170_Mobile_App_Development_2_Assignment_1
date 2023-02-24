package ie.wit.a20090170_mobile_app_2_assignment_1.main

import android.app.Application
import timber.log.Timber

class DNDCampaignApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("DND Campaign Application Started")
    }
}