package com.asamjapan.tenthousandmemo

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class MemoViewActivity : AppCompatActivity() {
    private lateinit var memosRV: RecyclerView
    private lateinit var memoList:ArrayList<time>
    private lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_viewactivity)
        realm = Realm.getDefaultInstance()
        memosRV = findViewById(R.id.memosRV)
        memosRV.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        memoList = ArrayList()
        getAllNotes()
        realm.close()

    }
    private fun getAllNotes(){
        memoList.clear()
        memoList = ArrayList()
        val results: RealmResults<time> = realm.where<time>(time::class.java).findAll()
        memosRV.adapter = MemosAdapter(this,results)
        //データセットが変更されたことを、登録されているすべてのobserverに通知する。
        memosRV.adapter!!.notifyDataSetChanged()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item?.itemId){
            R.id.home ->{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //戻るボタンを押したときの処理、メイン画面に戻る
    override fun onBackPressed(){
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }

}
