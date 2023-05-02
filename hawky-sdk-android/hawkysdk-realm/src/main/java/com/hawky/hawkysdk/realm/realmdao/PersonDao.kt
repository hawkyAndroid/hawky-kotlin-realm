package com.hawky.hawkysdk.realm.realmdao

import com.hawky.hawkysdk.realm.realmmodel.PersonModel
import io.realm.Realm
import io.realm.Sort
import io.realm.exceptions.RealmPrimaryKeyConstraintException

object PersonDao {

    /**
     * 保存数据
     * 注意：1）无论 copyToRealm 还是 insert 都必须设置主键，否则再次执行会报 “existing primary key value”
     *     2）Realm对象只能在它创建的线程上访问（“Realm access from incorrect thread”）
     */
    fun savePerson(person: PersonModel, callback: RealmCallback?) {
        val realm = Realm.getDefaultInstance()
        //========= 方式1：手动使用事务 ====================//
        /*realm.beginTransaction()
        // insert 速度快而且没有返回值，一般用在数据修改少、查询多的场景。
        realm.insert(person)
        realm.commitTransaction()*/

        //========== 方式2：executeTransaction 必须在子线程中使用================//
        /*realm.executeTransaction {
            it.insert(person)
        }*/

        //========== 方式3：异步使用事务 ===================//
        realm.executeTransactionAsync({
            // 子线程中执行任务（copyToRealm 带返回值）
            // 注意这里不能用 realm 只能用 it，否则报错
            it.copyToRealm(person)// 必须设置主键id 且不能相同，否则报错
            //  it.copyToRealmOrUpdate(person)// id相同则更新，否则插入
        }, {
            // 当前线程
            callback?.onResult(true, null, person)
        }) {
            // 当前线程
            callback?.onResult(false, it.message, null)
        }
    }

    /**
     * 删除数据
     */
    fun deletePerson(id: Int, callback: RealmCallback?) {
        val realm = Realm.getDefaultInstance()
        //  realm.delete(PersonModel::class.java)// 全部删除
        realm.executeTransactionAsync({
            it.where(PersonModel::class.java).equalTo("id", id).findFirst()
                ?.deleteFromRealm() ?: throw Throwable(
                "User($id) doesn't exists",
                null
            )// 异常往外抛，统一在onResult线程中处理
        }, {
            callback?.onResult(true, null, null)
        }) {
            callback?.onResult(false, it.message, null)
        }
    }

    /**
     * 更新数据
     */
    fun updatePerson(person: PersonModel, callback: RealmCallback?) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({
            it.copyToRealmOrUpdate(person)
        }, {
            callback?.onResult(true, null, null)
        }) {
            callback?.onResult(false, it.message, null)
        }
    }

    /**
     * 查询数据：无需开启事务
     * realm 查询速度贼快
     */
    fun queryPerson(email: String): PersonModel? {
        val realm = Realm.getDefaultInstance()
        //  realm.where(PersonModel::class.java).findAll().forEach { DebugLog.d("$it") }
        // 查询指定邮箱且按id倒序的第一个PersonModel
        return realm.where(PersonModel::class.java).equalTo("email", email)
            .sort("id", Sort.DESCENDING).findFirst()
    }

    @Synchronized
    fun getNextId(): Long {
        return try {
            val realm = Realm.getDefaultInstance()
            val currentIdNum: Number? = realm.where(PersonModel::class.java).max("id")
            if (currentIdNum != null) currentIdNum.toLong() + 1 else 1L
        } catch (e: Exception) {
            val errorMsg = "Exception during getPrimaryKey(): " + e.message
            throw RealmPrimaryKeyConstraintException(errorMsg)
        }
    }

}