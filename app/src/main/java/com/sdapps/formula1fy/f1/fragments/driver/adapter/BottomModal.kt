package com.sdapps.formula1fy.f1.fragments.driver.adapter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.models.F1Contants
import com.sdapps.formula1fy.databinding.ModalViewBottomBinding
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.fragments.driver.DriverInteractor
import com.sdapps.formula1fy.f1.fragments.home.HomeContractor
import java.lang.StringBuilder


class BottomModal(val driverBo: DriverBO): BottomSheetDialogFragment(), BottomModalInteractor.View{
    private lateinit var context : Context

    private var binding: ModalViewBottomBinding? = null
    private lateinit var db : DbHandler
    private lateinit var presenter: BottomPresenter
    data class Race(val startPos: Int, val endPos: Int)
    companion object {
        const val TAG = "BottomModalSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = requireContext().applicationContext
        binding = ModalViewBottomBinding.inflate(inflater, container, false)
        initAll()
        return binding!!.root
    }

    fun initAll(){
        presenter = BottomPresenter()
        presenter.attachView(this, context)
        db = DbHandler(context, DataMembers.DB_NAME)
        presenter.getTheData(driverBo)

        binding!!.driverDOB.text = driverBo.driverDOB
        binding!!.driverName.text = driverBo.driverName
        binding!!.driverNation.text = driverBo.driverNationality
    }


    override fun sendHashMapData(map: HashMap<String, Int>, list: ArrayList<Int>) {
        try{
            val dMap : HashMap<String, Int> = hashMapOf()
            val dList : ArrayList<PieEntry> = arrayListOf()

            dMap["Wins"] = map["won"]!!.toInt()
            dMap["Total"] =map["total"]!!.toInt()
            for(type in dMap.keys){
                dList.add(PieEntry(dMap[type]!!.toFloat(), type))
            }
            val teamColorId = driverBo.constructorId
            val teamColor = F1Contants.teamColorMap.getOrDefault(teamColorId, ContextCompat.getColor(context, R.color.card_color))

            val pieDataSet = PieDataSet(dList,"")
            setupPiechart(pieDataSet, teamColor)
            setupLineChart(map,list)
        }catch (ex: Exception){
            ex.printStackTrace()
            view
        }
    }


    fun setupPiechart(pieDataSet : PieDataSet, teamColor: Int){
        pieDataSet.valueTextSize = 12f //pie-chart data size
        pieDataSet.colors =
            arrayListOf(ContextCompat.getColor(context, R.color.card_color), ContextCompat.getColor(context,teamColor))
        binding!!.chart.description.isEnabled = false
        binding!!.chart.legend.textSize = 14f
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)

        if(pieData != null){
            binding!!.chart.data = pieData
            binding!!.chart.invalidate()
        }else{
            binding!!.chart.visibility = View.GONE
        }

    }

    fun setupLineChart(map: HashMap<String, Int> ,list: ArrayList<Int>){

        val dataPoints = ArrayList<Entry>()

        val totalRace = map["total"]?.toFloat()?: 0F
        val bestPos = map["best"]?.toFloat()?: 0F
        val worstPos = map["worst"]?.toFloat()?: 0F


        for((index, value) in list.withIndex()){
            val xVal = index.toFloat()
            val yVal = value.toFloat()
            dataPoints.add(Entry(xVal, yVal))
        }

        val teamColorId = driverBo.constructorId
        val teamColor = F1Contants.teamColorMap.getOrDefault(teamColorId,ContextCompat.getColor(context, R.color.card_color))

        val dataSet = LineDataSet(dataPoints, "Driver")
        if(teamColor== null){
            dataSet.color = Color.RED
        }else{
            dataSet.color = ContextCompat.getColor(context,teamColor)
        }
        dataSet.setDrawCircles(true)
        dataSet.setCircleColor(Color.RED)
        dataSet.lineWidth = 3f

        val data : ArrayList<ILineDataSet>  = ArrayList()
        data.add(dataSet)

        val lineChartData = LineData(data)
        prepareLineChartData(lineChartData)


    }


    fun prepareLineChartData(lineData: LineData){
        val sb = StringBuilder()
        sb.append("Points scored by ").append(" ${driverBo.driverCode} ").append("this season")
        binding!!.lineChartTitle.text  = sb.toString()
        binding!!.lineChart.data = lineData
        binding!!.lineChart.description.isEnabled = false
        binding!!.lineChart.xAxis.textSize = 12f
        binding!!.lineChart.axisLeft.textSize = 12f
        binding!!.lineChart.xAxis.setDrawGridLines(false)
        binding!!.lineChart.axisRight.setDrawGridLines(false)
        binding!!.lineChart.legend.isEnabled = false
        binding!!.lineChart.invalidate()
    }
}