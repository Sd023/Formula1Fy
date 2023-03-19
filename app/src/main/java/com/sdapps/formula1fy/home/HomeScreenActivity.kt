package com.sdapps.formula1fy.home

import android.app.ProgressDialog
import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler
import com.sdapps.formula1fy.home.drivers.DriversActivity
import com.sdapps.formula1fy.login.LoginScreenActivity

class HomeScreenActivity : AppCompatActivity(), HomeScreenInteractor.View, View.OnClickListener {

    private lateinit var db: DbHandler
    private lateinit var progressDialog: ProgressDialog
    private lateinit var presenter: HomeScreenPresenter
    private lateinit var shapeImg : ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        initAll()
    }


    private fun initAll() {
        shapeImg = findViewById(R.id.driversView)
        presenter = HomeScreenPresenter(this)
        progressDialog = ProgressDialog(this)
        presenter.setupView(this)
        db = DbHandler(applicationContext, DataMembers.DB_NAME)
        getMessageFromDead()
        db.createDB()
        loadScreen()
    }

    override fun loadScreen() {
        val rad = resources.getDimension(R.dimen.corner_radius)
        val driverImg = shapeImg.shapeAppearanceModel.toBuilder().setAllCornerSizes(rad).build()
        shapeImg.shapeAppearanceModel = driverImg

        shapeImg.setOnClickListener(this)

    }

    override fun getMessageFromDead() {
        val userId: String = intent?.getStringExtra("USER").toString()
        Log.d("userID", "---<<<$userId>>>")
    }

    override fun moveToNextScreen() {
        val intent = Intent(this@HomeScreenActivity, DriversActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(p0: View?) {
        presenter.moveToNextScreen()
    }

}