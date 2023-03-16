package com.sdapps.formula1fy.home

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.ModelBO.UserBO
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler
import com.sdapps.formula1fy.home.homeadapter.DriverAdapter

class HomeScreenActivity : AppCompatActivity(), HomeScreenInteractor.View {

    private lateinit var db: DbHandler
    private lateinit var progressDialog: ProgressDialog
    private lateinit var presenter: HomeScreenPresenter
    private lateinit var driverText: TextView
    private lateinit var consText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var userBO: UserBO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        initAll()
    }


    private fun initAll() {
        recyclerView = findViewById(R.id.driver_recyclerView)
        presenter = HomeScreenPresenter(this)
        progressDialog = ProgressDialog(this)
        presenter.setupView(this)
        db = DbHandler(applicationContext, DataMembers.DB_NAME)
        getMessageFromDead()
        db.createDB()
        loadScreen()
    }

    override fun loadScreen() {
        val driverList = presenter.getDriverData(db)
        val constructorList = presenter.getConstructorData(db)

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val cardAdapter = DriverAdapter(driverList)
        recyclerView.adapter = cardAdapter
        progressDialog.dismiss()
    }

    override fun getMessageFromDead() {
        val userId : String = intent?.getStringExtra("USER").toString()
        Log.d("userID", "---<<<$userId>>>")
    }

}