package com.hawky.hawkysdk.realm.realmmodel

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PersonModel : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var name: String? = null
    var email: String? = null
}