package com.hawky.hawkysdk.realm.realmdao

import io.realm.RealmModel

interface RealmCallback {
    fun onResult(success: Boolean, error: String?, result: RealmModel?)
}