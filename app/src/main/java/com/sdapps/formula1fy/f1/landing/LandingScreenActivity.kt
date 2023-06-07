package com.sdapps.formula1fy.f1.landing

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings.Global
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.f1.base.BaseInterface
import com.sdapps.formula1fy.f1.home.presenter.HomeScreenPresenter
import com.sdapps.formula1fy.f1.home.view.HomeScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LandingScreenActivity : AppCompatActivity(), LandingContractor.View,BaseInterface.BaseView, View.OnClickListener{

   private lateinit var appNameTitle : TextView
   private lateinit var startBtn : Button
   private var landPresenter : LandingPresenter? = null
   private lateinit var dbHandler: DbHandler

    private lateinit var pf : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        pf = ProgressDialog(this)
        setContentView(R.layout.activity_landing_screen)
        landPresenter = LandingPresenter(applicationContext)
        dbHandler = DbHandler(applicationContext,DataMembers.DB_NAME)
        dbHandler.createDB()
        landPresenter!!.attachView(this, applicationContext)
        initAll()

    }

    override fun showLoading() {
        pf.setTitle("loading..")
        pf.show()
    }

    override fun hideLoading() {
       try{
           pf.dismiss()
       }catch (ex: Exception){
           ex.printStackTrace()
       }
    }

    override fun moveToNextScreen() {
       startActivity(Intent(this@LandingScreenActivity, HomeScreenActivity::class.java))
       finish()
    }

    override fun initAll() {
        startBtn = findViewById(R.id.start)
        startBtn.setOnClickListener(this)
        appNameTitle = findViewById(R.id.landingAppName)
        appNameTitle.setText(landPresenter?.getAppString(), TextView.BufferType.SPANNABLE)
    }

    override fun onClick(v: View?) {
        CoroutineScope(Dispatchers.Main).launch{
            landPresenter!!.fetchAllData()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        landPresenter!!.detachView()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun hideStatusBar() {
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