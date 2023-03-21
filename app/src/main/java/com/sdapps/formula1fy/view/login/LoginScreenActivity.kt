package com.sdapps.formula1fy.view.login

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.core.DataMembers
import com.sdapps.formula1fy.core.DbHandler
import com.sdapps.formula1fy.view.home.HomeScreenActivity

class LoginScreenActivity : AppCompatActivity(), LoginContractor.View, OnClickListener {


    private lateinit var presenter: LoginScreenPresenter
    private lateinit var loginButton: Button
    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var db: DbHandler
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initDB();
        initViews();
    }

    fun initDB() {
        db = DbHandler(applicationContext, DataMembers.DB_NAME)
        db.createDB()
    }


    fun initViews() {
        progressDialog = ProgressDialog(this)
        loginButton = findViewById(R.id.loginBtn)
        emailEdit = findViewById(R.id.loginEmail)
        passwordEdit = findViewById(R.id.loginPassword)
        presenter = LoginScreenPresenter(this)
        presenter.attachView(this)
        loginButton.setOnClickListener(this)
        auth = Firebase.auth
    }


    override fun showAlert() {

    }

    override fun onError() {
        Toast.makeText(applicationContext, "Error Fetching Details!", Toast.LENGTH_LONG).show()

    }

    override fun showLoading() {
        progressDialog.apply {
            setTitle("Formula1Fy")
            setMessage("Fetching Data...")
            setCancelable(false)
            show()
        }
    }

    override fun hideLoading() {
        progressDialog.dismiss()
    }

    override fun moveToNextScreen(userBO: UserBO) {
        val i = Intent(this@LoginScreenActivity, HomeScreenActivity::class.java)
        i.putExtra("USER",userBO.userId)
        startActivity(i)
        finish()
    }

    override fun checkCurrentUser() {
        if(auth.currentUser !=null){
            startActivity(Intent(this@LoginScreenActivity, HomeScreenActivity::class.java))
            finish()
        }else{
            Toast.makeText(applicationContext, "Session Time Out! Please Login Again", Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(p0: View?) {
        try {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            presenter.performLogin(email, password)

        } catch (ex: Exception) {
            onError()
            ex.printStackTrace()
        }

    }

    override fun onStart() {
        super.onStart()
        checkCurrentUser()
    }

    override fun onRestart() {
        super.onRestart()
        checkCurrentUser()
    }
}