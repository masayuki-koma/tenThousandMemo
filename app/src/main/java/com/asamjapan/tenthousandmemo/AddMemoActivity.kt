package com.asamjapan.tenthousandmemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_add_memo.*
import java.lang.Exception
import java.lang.String.format
import io.realm.Realm
import java.util.Date
import java.util.GregorianCalendar
import java.text.SimpleDateFormat
import kotlin.math.round

import java.util.*

class AddMemoActivity : AppCompatActivity() {
    private lateinit var realm:Realm
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_memo)
        realm = Realm.getDefaultInstance()
        descriptionEd.setScroller(Scroller(this));
        descriptionEd.setMaxLines(3);
        descriptionEd.setVerticalScrollBarEnabled(true);
        descriptionEd.setMovementMethod(ScrollingMovementMethod());


        numPickerFunc()
        saveBtn.setOnClickListener(){
            addNotesToDB()
        }

    }
    fun numPickerFunc(){
        numPicker0.setMaxValue(2)
        numPicker0.setMinValue(0)
        numPicker1.setMaxValue(9)
        numPicker1.setMinValue(0)
        numPicker2.setMaxValue(5)
        numPicker2.setMinValue(0)
        numPicker3.setMaxValue(9)
        numPicker3.setMinValue(0)
    }
    fun numPickerEvent():Float{

        val hourTen = numPicker0.value.toFloat()
        val hourOne = numPicker1.value.toFloat()
        val minuteTen = numPicker2.value.toFloat()
        val minuteOne = numPicker3.value.toFloat()
        val timepassed = (hourTen*10+hourOne)+((minuteTen*10+minuteOne)/60)
        val timeCut = (round(timepassed*10))/10f
        return timeCut

    }
    @SuppressLint("SimpleDateFormat")
    fun getToday(): Date {
        val d = Date() // 現在時刻
        return d
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addNotesToDB(){

        try{
            val timeAdd = numPickerEvent()
            numPickerEvent()
            realm.beginTransaction()
            val currentIdNumber:Number? = realm.where(time::class.java).max("id")
            val nextID:Int
            nextID = if(currentIdNumber==null){
                1
            }else{
                currentIdNumber.toInt()+1
            }
            val memo = time()
            memo.id = nextID
            memo.date = getToday()
            //データベースにはデータ型を入れて表示するときだけSimpleDateFormatに入れたら解決
            val df = SimpleDateFormat("yyyy年MM月dd日")
            val dayMessage = df.format(memo.date)
            //現在時刻はいったん後回し
            memo.description = descriptionEd.text.toString()
            memo.time = timeAdd
            realm.copyToRealmOrUpdate(memo)
            //Realmの編集を終了する
            realm.commitTransaction()
            startActivity(Intent(this,MainActivity::class.java))
            finish()


        }catch (e:Exception){
            Toast.makeText(this,"保存に失敗しました",Toast.LENGTH_LONG).show()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        //MainActivityに遷移してAddNotesActivityを終了する


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

    override fun onBackPressed(){
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }



}
