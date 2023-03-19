package com.sdapps.formula1fy.home.drivers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.DBHelper
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler
import java.sql.Driver

class DriversActivity : AppCompatActivity(), DriverInteractor.View {
    private lateinit var db : DbHandler
    private lateinit var presenter: DriverPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drivers)
        initAll()
    }

    override fun initAll() {
        presenter = DriverPresenter(this)
        db = DbHandler(this, DataMembers.DB_NAME)
        val list = presenter.getDriverData(db)
        Log.d(
            "ListSize",
            "The list size is - > ${list.size} "
        )


    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }


}