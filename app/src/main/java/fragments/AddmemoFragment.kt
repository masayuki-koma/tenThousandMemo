package fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.asamjapan.tenthousandmemo.MainActivity
import com.asamjapan.tenthousandmemo.R
import com.asamjapan.tenthousandmemo.time
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_addmemo.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round


/**
 * A simple [Fragment] subclass.
 */
class AddmemoFragment : ThousandFragment() {

    var times: RealmResults<time>? = null
    //realmの更新を受けて発動する関数
    fun runQuery() {
        times = realm.where<time>()
            .findAllAsync()
        (times as RealmResults<time>?)?.addChangeListener { times ->
            // Persons was updated
            Toast.makeText(activity,"どうなってる",Toast.LENGTH_SHORT).show()
            var handler = Handler()
            handler.postDelayed({

                // Delay and Start Activity
                var intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            } , 100) // here we're delaying to startActivity after 3seconds



        }
    }

    private lateinit var realm: Realm
    private val memoviewfragment = MemoviewFragment()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_addmemo, container, false)
    }

    override fun updateList() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("addmemofagment","verbose")
        realm = Realm.getDefaultInstance()
        descriptionEdFunc()
        numPickerFunc()
        saveBtn.setOnClickListener() {
            addNotesToDB()

        }
    }
    fun descriptionEdFunc(){

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
    private fun addNotesToDB(){
        try {
            val timeAdd = numPickerEvent()
            numPickerEvent()
            realm.beginTransaction()
            val currentIdNumber: Number? = realm.where(time::class.java).max("id")
            val nextID: Int
            nextID = if (currentIdNumber == null) {
                1
            } else {
                currentIdNumber.toInt() + 1
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
            runQuery()

        }catch (e: Exception){
            Toast.makeText(activity,"保存に失敗しました", Toast.LENGTH_LONG).show()

        }






    }

}
