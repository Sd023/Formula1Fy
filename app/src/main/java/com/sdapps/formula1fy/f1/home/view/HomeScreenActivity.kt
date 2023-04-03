package com.sdapps.formula1fy.f1.home.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.imageview.ShapeableImageView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.utils.NetworkTools
import com.sdapps.formula1fy.f1.constructors.view.ConstructorActivity
import com.sdapps.formula1fy.f1.drivers.view.DriversActivity
import com.sdapps.formula1fy.f1.home.HomeScreenInteractor
import com.sdapps.formula1fy.f1.home.presenter.HomeScreenPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenActivity : AppCompatActivity(), HomeScreenInteractor.View {

    private lateinit var db: DbHandler
    private lateinit var progressDialog: ProgressDialog
    private lateinit var presenter: HomeScreenPresenter
    private lateinit var driverCard: CardView
    private lateinit var constructorCard: CardView
    private lateinit var network:NetworkTools
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        initAll()
    }


    private fun initAll() {
        network = NetworkTools()
        driverCard = findViewById(R.id.driverCardView)
        constructorCard = findViewById(R.id.constructorCardView)
        presenter = HomeScreenPresenter(this)
        progressDialog = ProgressDialog(this)
        presenter.setupView(this)
        db = DbHandler(applicationContext, DataMembers.DB_NAME)
        getMessageFromDead()
        db.createDB()
        loadScreen()
    }

    override fun loadScreen() {

        driverCard.setOnClickListener {
            lifecycleScope.launch {
                if(presenter.isCheckDataAvailable(true,db)){
                    moveToNextScreen(true)
                }else{
                    if(network.isNetworkAndInternetAvailable(applicationContext)){
                        presenter.fetchDriverData()
                    }
                    else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(applicationContext,"Please Connect To internet!", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }

        }
        constructorCard.setOnClickListener {
            lifecycleScope.launch {
                if(presenter.isCheckDataAvailable(false,db)){
                    moveToNextScreen(false)
                }else{
                    if(network.isNetworkAndInternetAvailable(applicationContext)){
                        presenter.fetchConstructorData()
                    }
                    else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(applicationContext,"Please Connect To internet!", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }

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

    override fun showLoading() {
        progressDialog.setTitle("Formula1Fy")
        progressDialog.setMessage("Loading Data..")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    override fun hideLoading() {
        progressDialog.dismiss()
    }

    override fun onError() {
        hideLoading()
        Toast.makeText(this, "Error Fetching Data! ", Toast.LENGTH_LONG).show()
    }

}