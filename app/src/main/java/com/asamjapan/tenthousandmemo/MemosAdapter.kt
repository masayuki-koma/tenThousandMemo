package com.asamjapan.tenthousandmemo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.notes_rv_layout.view.*
import java.text.SimpleDateFormat

class MemosAdapter(private val context: Context, private val memoList: RealmResults<time>)
    : RecyclerView.Adapter<ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val position=viewType
        val v = LayoutInflater.from(parent.context).inflate(R.layout.notes_rv_layout,parent,false)
        // set the view's size, margins, paddings and layout parameters
        //メモを長押ししたらこのメモを削除するかどうかを決める


        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val df = SimpleDateFormat("yyyy-MM-dd")
        val dayMessage = df.format(memoList[position]!!.date)

        holder.itemView.dateTV.text = dayMessage
        holder.itemView.descTV.text = memoList[position]!!.description
        holder.itemView.timeTV.text = memoList[position]!!.time.toString()+"h"
        holder.itemView.setOnLongClickListener{
            //メモのsetOnClickListenerはonBindViewHoloderに書くのが正解！ここでViewとデータ結びつけてるからか？

            AlertDialog.Builder(holder.itemView.getContext()) // FragmentではActivityを取得して生成

                .setTitle("このメモを削除しますか？")
                .setMessage("時間も一緒に削除されるのでお気をつけください")
                .setPositiveButton("OK", { dialog, which ->
                    deleteMemo(position)
                    //メモを削除した瞬間にRecyclerViewを更新する
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, memoList.size);
                    notifyDataSetChanged()
                })
                .setNegativeButton("No", { dialog, which ->
                })
                .show()

            return@setOnLongClickListener true

        }

    }
    class ViewHolder(v: View?):RecyclerView.ViewHolder(v!!)
    private fun deleteMemo(position: Int) {
        val realm = Realm.getDefaultInstance()
        val results = realm.where<time>().findAll()
        realm.beginTransaction()
        val memo = results[position]
        memo?.deleteFromRealm()
        realm.commitTransaction()
    }


}