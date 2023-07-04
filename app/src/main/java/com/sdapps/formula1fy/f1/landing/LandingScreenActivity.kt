package com.sdapps.formula1fy.f1.landing

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.core.utils.NetworkTools
import com.sdapps.formula1fy.f1.base.view.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LandingScreenActivity : AppCompatActivity(), LandingContractor.View,
    View.OnClickListener {

    private lateinit var appNameTitle: TextView
    private lateinit var startBtn: Button
    private var landPresenter: LandingPresenter? = null
    private lateinit var dbHandler: DbHandler

    private lateinit var pf: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        pf = ProgressDialog(this)
        setContentView(R.layout.activity_landing_screen)
        landPresenter = LandingPresenter(applicationContext)
        dbHandler = DbHandler(applicationContext, DataMembers.DB_NAME)
        dbHandler.createDB()
        landPresenter!!.attachView(this, applicationContext)
        initAll()

    }


    override fun showLoading() {
        pf.setTitle("Formula1Fy")
        pf.setMessage("Loading data...")
        pf.show()
    }

    override fun hideLoading() {
        try {
            pf.dismiss()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun moveToNextScreen() {
        startActivity(Intent(this@LandingScreenActivity, BaseActivity::class.java))
        finish()
    }

    override fun initAll() {
        startBtn = findViewById(R.id.start)
        startBtn.setOnClickListener(this)
        appNameTitle = findViewById(R.id.landingAppName)
        appNameTitle.setText(landPresenter?.getAppString(), TextView.BufferType.SPANNABLE)
    }

    override fun onClick(v: View?) {
        CoroutineScope(Dispatchers.Main).launch {
            showLoading()
            if (NetworkTools().isNetworkAndInternetAvailable(applicationContext)) {
                val fetchDriverData = async(Dispatchers.IO) {
                    landPresenter!!.fetchDriverData()
                }
                fetchDriverData.await()
                val fetchConstructorData = async(Dispatchers.IO) {
                    landPresenter!!.fetchConstructorData()
                }
                fetchConstructorData.await()

                val latestRaceJson = async(Dispatchers.IO){
                    landPresenter!!.fetchLatestResults()
                }
                latestRaceJson.await()

                val fetchRaceData = async(Dispatchers.IO) {
                    landPresenter!!.fetchRaceData()
                }
                fetchRaceData.await()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        landPresenter!!.detachView()
    }

    override fun onResume() {
        super.onResume()
    }

     fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}