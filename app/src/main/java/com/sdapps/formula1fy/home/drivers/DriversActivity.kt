package com.sdapps.formula1fy.home.drivers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.ModelBO.DriverBO
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.DBHelper
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler
import java.sql.Driver

class DriversActivity : AppCompatActivity(), DriverInteractor.View {
    private lateinit var db: DbHandler
    private lateinit var presenter: DriverPresenter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drivers)
        initAll()
    }

    override fun initAll() {
        presenter = DriverPresenter(this)
        presenter.attachView(this)
        recyclerView = findViewById(R.id.recyclerView)
        db = DbHandler(this, DataMembers.DB_NAME)
        val list = presenter.getDriverData(db)
        loadDriverView(list)
    }

    private fun loadDriverView(list: ArrayList<DriverBO>) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = DriverAdapter(list)
        recyclerView.adapter = adapter

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }


}