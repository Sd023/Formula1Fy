package com.sdapps.formula1fy.f1.fragments.driver

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.databinding.FragmentDriverBinding


class DriverFragment : Fragment(), DriverInteractor.View {
    private lateinit var db: DbHandler
    private lateinit var presenter: DriverPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var context: Context

    private var binding: FragmentDriverBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = requireContext().applicationContext
        binding = FragmentDriverBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAll()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        activity?.window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN


    }


    override fun onDestroyView() {
        super.onDestroyView()
        //activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun initAll() {
        presenter = DriverPresenter(context)
        presenter.attachView(this)
        db = DbHandler(context, DataMembers.DB_NAME)
        val list = presenter.getDriverData(db)
        loadDriverView(list)
    }

    private fun loadDriverView(list: ArrayList<DriverBO>) {
        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = DriverAdapter(list)
        binding!!.recyclerView.adapter = adapter

    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }


}