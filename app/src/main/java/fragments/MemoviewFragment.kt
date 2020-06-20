package fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.asamjapan.tenthousandmemo.MemosAdapter

import com.asamjapan.tenthousandmemo.R
import com.asamjapan.tenthousandmemo.time
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_memoview.*
import io.realm.RealmChangeListener
import io.realm.kotlin.where



class MemoviewFragment : ThousandFragment() {

    private lateinit var memoList:ArrayList<time>
    private lateinit var realm: Realm
    lateinit var realmListener: RealmChangeListener<Realm>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.v("MemoviewFragment","verbose")
        val view = inflater.inflate(R.layout.fragment_memoview,container,false)
        //意味不明だけどrealmインスタンス生成して他をviewcreated にすると動いた
        realm = Realm.getDefaultInstance()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //memoRVのview.はここじゃないと動かない
        var memoRV: RecyclerView = view.findViewById(R.id.memosRV)
        memoRV.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        memoList = ArrayList()
        getAllNotes()
    }

    override fun onStart() {
        super.onStart()
        var timechange = realm.where<time>().findFirst()
        timechange?.addChangeListener(RealmChangeListener { time ->
            updateList()
            // React to change
        })
    }

    //なるほどね、onDestroyでrealm.close()すればいいのか
    override fun onDestroy() {
        super.onDestroy()
        // Realmオブジェクトの削除
        realm.close()
    }
    private fun getAllNotes(){
        memoList.clear()
        memoList = ArrayList()
        val results: RealmResults<time> = realm.where<time>(time::class.java).findAll()
        //fragmentではcontext=this ができないからactivityでやる

        memosRV.adapter = getActivity()?.let { MemosAdapter(it,results) }

        //データセットが変更されたことを、登録されているすべてのobserverに通知する。
        memosRV.adapter!!.notifyDataSetChanged()
    }
    override fun updateList() {
        Toast.makeText(activity,"更新中",Toast.LENGTH_SHORT).show()
    }

}
