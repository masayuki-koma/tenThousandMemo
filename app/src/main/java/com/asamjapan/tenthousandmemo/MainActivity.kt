package com.asamjapan.tenthousandmemo

import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.tabs.TabLayout
import fragments.AddmemoFragment
import fragments.ChartFragment
import fragments.MemoviewFragment
import fragments.ThousandFragment
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private var sectionPagerAdapter: SectionPagerAdapter? = null
    private val addmemofragment = AddmemoFragment()
    private val chartfragment = ChartFragment()
    private val memoviewfragment = MemoviewFragment()
    private var currentFragment: ThousandFragment = addmemofragment
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.v("HomeActivity", "verbose")
        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        container.adapter = sectionPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                var ft: FragmentTransaction? = fragmentManager.beginTransaction()
                when(tab?.position){
                    0 -> {
                        currentFragment = chartfragment



                    }
                    1 -> {
                        currentFragment = addmemofragment


                    }
                    2 -> {
                        currentFragment = memoviewfragment

                    }
                }
            }

        })


    }




    inner class SectionPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
        //各タブでどのフラグメントを扱うかを指定

        override fun getItem(position: Int): Fragment {

            return when(position){
                0 -> chartfragment
                1 -> addmemofragment
                else -> memoviewfragment
            }
        }

        override fun getItemPosition(`object`: Any): Int {

            return PagerAdapter.POSITION_NONE
        }

        override fun getCount(): Int =3


    }


    companion object{
        fun newIntent(context: Context) = Intent(context,
            MainActivity::class.java)
    }
}
