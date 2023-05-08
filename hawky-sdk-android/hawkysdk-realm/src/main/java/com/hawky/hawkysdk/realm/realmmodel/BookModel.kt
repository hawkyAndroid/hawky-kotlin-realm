package com.hawky.hawkysdk.realm.realmmodel

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * 新增 BOOK 数据表
 * 注意：一定要对应 RealmMigration schema 中的类型和参数，否则报错
 */
open class BookModel : RealmObject() {
    @PrimaryKey
    var id: Long = 0

    @Required
    var name: String? = null
    var type: Int = 0

    @Required
    var price: Double = 0.0
    var publishTime: Long = 0

}