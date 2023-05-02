package com.hawky.realm

import androidx.multidex.MultiDexApplication
import com.hawky.hawkysdk.realm.HawkyRealm

class RealmApplication : MultiDexApplication() {
    companion object {
        lateinit var instance: RealmApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // init RealmDB
        HawkyRealm.init(this)
    }
}