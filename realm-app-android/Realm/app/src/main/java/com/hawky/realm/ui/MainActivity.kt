package com.hawky.realm.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hawky.hawkybase.DebugLog
import com.hawky.hawkysdk.realm.realmdao.BookDao
import com.hawky.hawkysdk.realm.realmdao.PersonDao
import com.hawky.hawkysdk.realm.realmdao.RealmCallback
import com.hawky.hawkysdk.realm.realmmodel.BookModel
import com.hawky.hawkysdk.realm.realmmodel.PersonModel
import com.hawky.realm.R
import com.hawky.realm.RealmApplication
import com.hawky.realm.databinding.ActivityMainBinding
import com.hawky.realm.model.User
import io.realm.RealmModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun initData() {
        // realm = Realm.getDefaultInstance()
        binding.event = HandleClickEvent()
    }

    // 处理点击事件
    inner class HandleClickEvent {
        fun addUser(view: View) {
            val person = PersonModel()
            person.id = PersonDao.getNextId()
            person.name = "张三"
            person.email = "123@qq.com"
            PersonDao.savePerson(person, object : RealmCallback {
                override fun onResult(success: Boolean, error: String?, result: RealmModel?) {
                    if (success) {
                        Toast.makeText(RealmApplication.instance, "ADD OK", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(RealmApplication.instance, error, Toast.LENGTH_SHORT).show()
                    }
                }
            })

////            val book = BookModel()
////            book.id = BookDao.getNextId()
////            book.name = "XXX活着"
////            book.type = 3
//            book.price = 34.8
//            book.publishTime = 20000101
//            BookDao.saveBook(book)
        }

        fun deleteUser(view: View) {
            PersonDao.deletePerson(0, object : RealmCallback {
                override fun onResult(success: Boolean, error: String?, result: RealmModel?) {
                    if (success) {
                        Toast.makeText(RealmApplication.instance, "DEL OK", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(RealmApplication.instance, error, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        fun updateUser(view: View) {
            val person = PersonModel()
            person.id = 1
            person.name = "张三"
            person.email = "123456@qq.com"
            PersonDao.updatePerson(person, object : RealmCallback {
                override fun onResult(success: Boolean, error: String?, result: RealmModel?) {
                    if (success) {
                        Toast.makeText(RealmApplication.instance, "UPDATE OK", Toast.LENGTH_SHORT)
                            .show()
                        person.run {
                            val user = User()
                            user.id = id
                            user.name = name
                            user.email = email
                            binding.user = user
                        }
                    } else {
                        Toast.makeText(RealmApplication.instance, error, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        fun queryUser(view: View) {
            val person = PersonDao.queryPerson("123@qq.com")
            DebugLog.d("person:${person}")
            person?.run {
                val user = User()
                user.id = id
                user.name = name
                user.email = email
                binding.user = user
            }

//            val book = BookDao.queryBook("活着")
//            DebugLog.d("book:${book}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 同一个Realm实例操作完成后，记得close()
        // realm.close()
        binding.unbind()
    }

}