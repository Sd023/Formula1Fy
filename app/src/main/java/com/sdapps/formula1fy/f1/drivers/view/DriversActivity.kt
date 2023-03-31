package com.sdapps.formula1fy.f1.drivers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.drivers.DriverInteractor
import com.sdapps.formula1fy.f1.drivers.adapter.DriverAdapter
import com.sdapps.formula1fy.f1.drivers.presenter.DriverPresenter

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