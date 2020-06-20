package com.asamjapan.tenthousandmemo


import android.icu.text.SimpleDateFormat
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

import java.time.LocalDate
import java.util.*

//RealmObjectを拡張してRealmモデルの作成
//open を必ずつける
open class time (
    //idはPrimaryKey
    //PrimaryKeyを使用すると、copyToRealmOrUpdateまたはinsertOrUpdateメソッドを使用できるようになる
    @PrimaryKey
    var id:Int? = null,
    var time :Float? = null,
    var date: Date? = null,
    var description:String? = null
):RealmObject()