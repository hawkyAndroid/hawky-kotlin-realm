package com.hawky.hawkysdk.realm

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

object HawkyRealm {
    private const val REALM_DATABASE_VERSION: Long = 2
    private const val REALM_DATABASE_NAME = "hawky.realm"

    /**
     * 初始化 Realm 参数配置
     */
    fun init(context: Context) {
        Realm.init(context)

        val config = RealmConfiguration.Builder()
            .name(REALM_DATABASE_NAME) // 设置库名，默认为 "default.realm"
            .schemaVersion(REALM_DATABASE_VERSION) // 设置版本
            // 可指定初始化的数据库文件：
            // Realm会把assets路径下的xxx.realm文件拷贝到Context.getFilesDir()默认目录中，
            // 以替换默认创建的空数据库文件。
            // .assetFile("xxx.realm")
            .migration(HawkyMigration()) // 升级
            .encryptionKey(getRealmKey())// 加密
            .build()

        // 设置config 为realm 默认配置，
        // 之后可通过 Realm.getDefaultInstance() 来创建或从缓存中拿到 Realm 对象。
        Realm.setDefaultConfiguration(config)

        // 当然也可以不进行 setDefaultConfiguration操作，
        // 通过 Realm.getInstance(config) 也可获取 Realm 对象。
    }

    /**
     * NOTE：Realm 密钥必须是64个字节
     */
    private fun getRealmKey() = byteArrayOf(
        0x03, 0x01, 0x02, 0x02, 0x01, 0x01, 0x03, 0x02,
        0x03, 0x01, 0x02, 0x02, 0x01, 0x01, 0x03, 0x02,
        0x03, 0x01, 0x02, 0x02, 0x01, 0x01, 0x03, 0x02,
        0x03, 0x01, 0x02, 0x02, 0x01, 0x01, 0x03, 0x02,
        0x03, 0x01, 0x02, 0x02, 0x01, 0x01, 0x03, 0x02,
        0x03, 0x01, 0x02, 0x02, 0x01, 0x01, 0x03, 0x02,
        0x03, 0x01, 0x02, 0x02, 0x01, 0x01, 0x03, 0x02,
        0x03, 0x01, 0x02, 0x02, 0x01, 0x01, 0x03, 0x02
    )

}