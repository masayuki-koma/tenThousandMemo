package com.asamjapan.tenthousandmemo

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

//Applicationクラスは、Androidアプリケーションがプロセスとして起動する際に作成され、呼び出されます。
//継承したクラスの作成
//AndroidManifest.xmlへの独自Applicationクラスの登録を行うことで生成
class FirstSetting : Application(){
    override fun onCreate() {
        super.onCreate()
        //init Realm
        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
            .name("Notes.db")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(configuration)
    }
}