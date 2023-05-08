package com.hawky.hawkysdk.realm

import com.hawky.hawkybase.DebugLog
import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration


/**
 * Realm升级使用
 */
class HawkyMigration : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        DebugLog.d("oldVersion:$oldVersion,newVersion:$newVersion")
        val schema = realm.schema
        var oldV = oldVersion

        if (oldV == 0L) {
            oldV++
        }

        if (oldV == 1L) {
            // Migrate from v1 to v2：新增 BookModel 表
            val bookSchema = schema.create("BookModel")
            bookSchema?.addField("id", Long::class.java, FieldAttribute.PRIMARY_KEY)
                ?.addField("name", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("type", Int::class.java)

            oldV++
        }

        if (oldV == 2L) {
            // Migrate from v2 to v3：为 BookModel 表添加字段
            val bookModel = schema.get("BookModel")
            bookModel?.addField("price", Double::class.java, FieldAttribute.REQUIRED)
                ?.addField("publishTime", Long::class.java)

            oldV++
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        return other is HawkyMigration
    }

    override fun hashCode(): Int {
        return HawkyMigration::class.java.hashCode()
    }
}