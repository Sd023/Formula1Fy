package com.sdapps.formula1fy.home

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler

class HomeScreenActivity : AppCompatActivity(), HomeScreenInteractor.View {

    private lateinit var db: DbHandler
    private lateinit var progressDialog: ProgressDialog
    private lateinit var presenter: HomeScreenPresenter
    private lateinit var driverText: TextView
    private lateinit var consText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        initAll()
    }


    private fun initAll() {
        driverText = findViewById(R.id.driverList)
        consText = findViewById(R.id.constList)
        presenter = HomeScreenPresenter(this)
        progressDialog = ProgressDialog(this)
        presenter.setupView(this)
        db = DbHandler(applicationContext, DataMembers.DB_NAME)
        db.createDB()
        loadScreen()
    }

    override fun loadScreen() {
        val driverList = presenter.getDriverData(db)
        val constructorList = presenter.getConstructorData(db)
    }


    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }


}