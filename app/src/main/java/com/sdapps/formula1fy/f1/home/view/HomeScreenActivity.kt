package com.sdapps.formula1fy.f1.home.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.utils.NetworkTools
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
    private lateinit var bottomBar : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        initAll()
    }


    private fun initAll() {
        network = NetworkTools()
        bottomBar = findViewById(R.id.bottomView)
//        driverCard = findViewById(R.id.driverCardView)
//        constructorCard = findViewById(R.id.constructorCardView)
        presenter = HomeScreenPresenter(this)
        progressDialog = ProgressDialog(this)

        db = DbHandler(applicationContext, DataMembers.DB_NAME)
        getMessageFromDead()
        db.createDB()
        loadScreen()
        bottomBar.selectedItemId = R.id.driver_menu
        bottomBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.driver_menu -> callDriverFragment()
                R.id.constructor_menu -> callConsFragment()
                else -> showToast()
            }
        }


    }

    fun callDriverFragment(): Boolean{
       val navController = findNavController(R.id.navHostFrag)
        navController.navigate(R.id.driver_frag)
        return true
    }
    fun callConsFragment(): Boolean{
        val navController = findNavController(R.id.navHostFrag)
        navController.navigate(R.id.cons_frag)
        return true

    }
    fun showToast(): Boolean{return true}

    override fun loadScreen() {

//        driverCard.setOnClickListener {
//            lifecycleScope.launch {
//                moveToNextScreen(true)
//
//            }
//
//        }
//        constructorCard.setOnClickListener {
//            lifecycleScope.launch {
//                moveToNextScreen(false)
//
//            }
//
//        }

    }

    override fun getMessageFromDead() {
    }


    override fun moveToNextScreen(isDriver: Boolean) {

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