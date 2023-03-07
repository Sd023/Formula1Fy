package com.sdapps.formula1fy.login

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler

class LoginScreenActivity : AppCompatActivity(), LoginContractor.View, OnClickListener {


    private lateinit var presenter: LoginScreenPresenter
    private lateinit var loginButton: Button
    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var db: DbHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDB();
        initViews();
    }

    fun initDB() {
        db = DbHandler(applicationContext, DataMembers.DB_NAME)
        db.createDB()
    }


    fun initViews() {
        loginButton = findViewById(R.id.loginBtn)
        emailEdit = findViewById(R.id.loginEmail)
        passwordEdit = findViewById(R.id.loginPassword)
        presenter = LoginScreenPresenter(this)
        loginButton.setOnClickListener(this)
    }


    override fun showAlert() {

    }

    override fun onError() {
        Toast.makeText(applicationContext, "Error Fetching Details!", Toast.LENGTH_LONG).show()

    }

    override fun onClick(p0: View?) {
        try {
            presenter.fetchDriverData()
        } catch (ex: Exception) {
            onError()
            ex.printStackTrace()
        }

    }
}