package com.hawky.hawkysdk.realm.realmdao

import com.hawky.hawkysdk.realm.realmmodel.BookModel
import io.realm.Realm
import io.realm.Sort
import io.realm.exceptions.RealmPrimaryKeyConstraintException

object BookDao {

    fun saveBook(book: BookModel) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.insert(book)
        realm.commitTransaction()
    }

    fun queryBook(name: String): BookModel? {
        val realm = Realm.getDefaultInstance()
        return realm.where(BookModel::class.java).equalTo("name", name)
            .sort("id", Sort.DESCENDING).findFirst()
    }

    @Synchronized
    fun getNextId(): Long {
        return try {
            val realm = Realm.getDefaultInstance()
            val currentIdNum: Number? = realm.where(BookModel::class.java).max("id")
            if (currentIdNum != null) currentIdNum.toLong() + 1 else 1L
        } catch (e: Exception) {
            val errorMsg = "Exception during getPrimaryKey(): " + e.message
            throw RealmPrimaryKeyConstraintException(errorMsg)
        }
    }
}