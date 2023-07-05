package com.sdapps.formula1fy.f1.fragments.raceschedule

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.databinding.FragmentRaceScheduleBinding
import com.sdapps.formula1fy.f1.bo.RaceScheduleBO

class RaceScheduleFragment : Fragment(), RaceScheduleInteractor.View{

    private lateinit var binding: FragmentRaceScheduleBinding
    private lateinit var context: Context
    private lateinit var presenter : RaceSchedulePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = requireContext().applicationContext
        binding = FragmentRaceScheduleBinding.inflate(inflater,container,false)
        initAll()
        return binding.root
    }

    fun initAll(){
        presenter = RaceSchedulePresenter(context)
        presenter.attachView(this)
        presenter.getRaceCalendar()

    }

    override fun setAdapter(list: ArrayList<RaceScheduleBO>) {
        val db= DbHandler(context, DataMembers.DB_NAME)
        val roundNumber = presenter.getCurrentRound(db)
        binding.raceScheduleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.raceScheduleView.isNestedScrollingEnabled = false
        val adapter = RaceScheduleAdapter(list, roundNumber)
        binding.raceScheduleView.adapter = adapter
    }

}