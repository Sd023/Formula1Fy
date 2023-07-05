package com.sdapps.formula1fy.f1.base.view

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.utils.NetworkTools
import com.sdapps.formula1fy.f1.base.BaseContractor

class BaseActivity : AppCompatActivity(), BaseContractor.View {

    private lateinit var db: DbHandler
    private lateinit var progressDialog: ProgressDialog
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
        progressDialog = ProgressDialog(this)

        db = DbHandler(applicationContext, DataMembers.DB_NAME)
        getMessageFromDead()
        db.createDB()
        loadScreen()
        bottomBar.selectedItemId = R.id.home_menu



        bottomBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_menu -> callHomeFragment()
                R.id.driver_menu -> callDriverFragment()
                R.id.constructor_menu -> callConstructorFragment()
                R.id.schedule_menu -> callScheduleFragment()
                else -> showToast()
            }
        }


    }

    fun callConstructorFragment(): Boolean{
        val nav = findNavController(R.id.navHostFrag)
        nav.navigate(R.id.cons_frag)
        return true
    }

    fun callScheduleFragment(): Boolean{
        val nav = findNavController(R.id.navHostFrag)
        nav.navigate(R.id.calendar_frag)
        return true
    }

    fun callHomeFragment(): Boolean{
       val navController = findNavController(R.id.navHostFrag)
        navController.navigate(R.id.home_frag)
        return true
    }
    fun callDriverFragment(): Boolean{
        val navController = findNavController(R.id.navHostFrag)
        navController.navigate(R.id.driver_frag)
        return true
    }
    fun showToast(): Boolean{return true}

    override fun loadScreen() {


    }

    override fun getMessageFromDead() {
    }


    override fun moveToNextScreen(isDriver: Boolean) {

    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onError() {
        hideLoading()
        Toast.makeText(this, "Error Fetching Data! ", Toast.LENGTH_LONG).show()
    }

}