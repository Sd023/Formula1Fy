package com.sdapps.formula1fy.f1.fragments.home.view

import android.content.Context
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.databinding.FragmentDriverBinding
import com.sdapps.formula1fy.f1.bo.ConstructorBO
import com.sdapps.formula1fy.f1.bo.ConstructorNewBO
import com.sdapps.formula1fy.f1.bo.LatestResult
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO
import com.sdapps.formula1fy.f1.fragments.home.HomeContractor
import com.sdapps.formula1fy.f1.fragments.home.HomePresenter
import com.sdapps.formula1fy.f1.fragments.home.adapter.HomeConstructorAdapter
import com.sdapps.formula1fy.f1.fragments.home.adapter.HomeDriverAdapter
import com.sdapps.formula1fy.f1.fragments.home.adapter.HomeLatestResultAdapter
import com.sdapps.formula1fy.f1.fragments.home.adapter.HomeNextRaceListAdapter
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), HomeContractor.View {
    private lateinit var db: DbHandler
    private lateinit var presenter: HomePresenter
    private lateinit var context: Context

    private var binding: FragmentDriverBinding? = null
    private lateinit var driversNameList : ArrayList<String>
    private lateinit var listValues : ArrayList<ConstructorNewBO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = requireContext().applicationContext
        binding = FragmentDriverBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAll()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        //activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun initAll() {
        presenter = HomePresenter(context)
        presenter.attachView(this)
        db = DbHandler(context, DataMembers.DB_NAME)
        lifecycleScope.launch {
            presenter.getNextRound(db)
            presenter.getLatestRound(db)
            presenter.getDriverData(db)
            presenter.getConstructorData(db)
       }

    }
    override fun setConstructorAdapter(list: ArrayList<ConstructorBO>, map : HashMap<String, ArrayList<String>>) {
        listValues = arrayListOf()
        for(entry in map.entries){
            val teamBO = ConstructorNewBO(entry.key,entry.value)
            listValues.add(teamBO)
        }
        binding!!.constructorRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val homeConstructorAdapter = HomeConstructorAdapter(list, listValues)
        binding!!.constructorRecyclerView.isNestedScrollingEnabled = false
        binding!!.constructorRecyclerView.adapter = homeConstructorAdapter

    }

    override fun setDriverAdapter(list: ArrayList<DriverBO>) {
        binding!!.driverRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = HomeDriverAdapter(list)
        binding!!.driverRecyclerView.isNestedScrollingEnabled = false
        binding!!.driverRecyclerView.adapter = adapter

    }

    override fun setNextRaceAdapter(list: ArrayList<RaceScheduleBO>) {
        if(list!=null){
            binding!!.nextRaceCard.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = HomeNextRaceListAdapter(list)
            binding!!.nextRaceCard.adapter = adapter
        }else{
          Toast.makeText(context,"No Data", Toast.LENGTH_LONG).show()
        }
    }

    override fun setLatestResults(list: MutableList<LatestResult>) {
        binding!!.latestResultView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = HomeLatestResultAdapter(list)
        binding!!.latestResultView.adapter = adapter
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }


}