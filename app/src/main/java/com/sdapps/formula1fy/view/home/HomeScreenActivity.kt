package com.sdapps.formula1fy.view.home

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler
import com.sdapps.formula1fy.view.constructors.ConstructorActivity
import com.sdapps.formula1fy.view.drivers.DriversActivity

class HomeScreenActivity : AppCompatActivity(), HomeScreenInteractor.View {

    private lateinit var db: DbHandler
    private lateinit var progressDialog: ProgressDialog
    private lateinit var presenter: HomeScreenPresenter
    private lateinit var driverImageView: ShapeableImageView
    private lateinit var constructorImageView: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        initAll()
    }


    private fun initAll() {
        driverImageView = findViewById(R.id.driversView)
        constructorImageView = findViewById(R.id.constructorView)
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
        val driverImg =
            driverImageView.shapeAppearanceModel.toBuilder().setAllCornerSizes(rad).build()
        driverImageView.shapeAppearanceModel = driverImg
        constructorImageView.shapeAppearanceModel = driverImg

        driverImageView.setOnClickListener {
            presenter.moveToNextScreen(true)
        }
        constructorImageView.setOnClickListener {
            presenter.moveToNextScreen(false)
        }

    }

    override fun getMessageFromDead() {
        val userId: String = intent?.getStringExtra("USER").toString()
    }

    override fun moveToNextScreen(isDriver: Boolean) {
        if (isDriver) {
            val intent = Intent(this@HomeScreenActivity, DriversActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this@HomeScreenActivity, ConstructorActivity::class.java)
            startActivity(intent)
        }

    }

}