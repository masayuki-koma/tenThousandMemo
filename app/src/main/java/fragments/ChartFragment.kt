package fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.asamjapan.tenthousandmemo.R
import com.asamjapan.tenthousandmemo.R.id
import com.asamjapan.tenthousandmemo.time
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
import kotlinx.android.synthetic.main.fragment_chart.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class ChartFragment : ThousandFragment() {
    private var mPie: PieChart? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun updateList() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPieChartView()
    }
    fun setupPieChartView(){

        val realm = Realm.getDefaultInstance()
        val results: RealmResults<time> = realm.where<time>(time::class.java).findAll()
        var accumulate:Float = 0f
        for (result in results){
            accumulate+= result.time!!
        }
        timeText.text ="${"%,.1f".format(accumulate)}時間達成!"

        mPie = pieChartView
        mPie?.setUsePercentValues(true)
        val desc: Description = Description()
        desc.text = ""
        mPie?.description = desc
        val legend: Legend? = mPie?.legend
        legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
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
        dataSet.setColors(intArrayOf(R.color.piechart1, R.color.piechart2), getActivity())
        dataSet.setDrawValues(true)

        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(20f)
        pieData.setValueTextColor(Color.WHITE)

        mPie?.data = pieData

    }



}

