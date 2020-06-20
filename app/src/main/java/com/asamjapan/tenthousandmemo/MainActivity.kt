package com.asamjapan.tenthousandmemo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var mPie: PieChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var realm = Realm.getDefaultInstance()
        setupPieChartView()
        addBtn.setOnClickListener{
            addBtnfunction()
        }
        viewBtn.setOnClickListener{
            viewBtnfunction()
        }

    }

    fun addBtnfunction(){
        startActivity(Intent(this,AddMemoActivity::class.java))
        finish()

    }
    fun viewBtnfunction(){
        startActivity(Intent(this,MemoViewActivity::class.java))
        finish()
    }

    fun setupPieChartView(){
        val realm = Realm.getDefaultInstance()
        val results: RealmResults<time> = realm.where<time>(time::class.java).findAll()
        var accumulate:Float = 0f
        for (result in results){
            accumulate+= result.time!!
        }

        timeText.text ="${"%,.1f".format(accumulate)}時間達成!"


        mPie = this.findViewById(R.id.pieChartView)
        mPie?.setUsePercentValues(true)
        val desc: Description = Description()
        desc.text = ""
        mPie?.description = desc
        val legend: Legend? = mPie?.legend
        legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

        //Floatにしないとエラー
        //累計時間が1,000時間超えた場合は、あまりの時間を円グラフに反映させる（テキストは累計時間を反映させる）
        var passedTime =accumulate-((accumulate.toInt()/1000)*1000F)
        var remainTime = 1000-passedTime

        val value = Arrays.asList(passedTime,remainTime)
        val label = Arrays.asList("", "")
        val entry = ArrayList<PieEntry>()
        for(i in value.indices) {
            entry.add( PieEntry(value.get(i), label.get(i)) )
        }
        val dataSet = PieDataSet(entry, "")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.setColors(intArrayOf(R.color.piechart1, R.color.piechart2), this@MainActivity)
        dataSet.setDrawValues(true)

        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(20f)
        pieData.setValueTextColor(Color.WHITE)

        mPie?.data = pieData

    }
}
