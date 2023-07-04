package com.sdapps.formula1fy.f1.fragments.driver

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.databinding.FragmentMainDriverBinding
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.fragments.home.HomeDriverAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DriverFragment : Fragment(), DriverInteractor.View{
    private lateinit var context : Context
    private lateinit var dpresenter : DriverPresenter
    private lateinit var db: DbHandler

    private var binding: FragmentMainDriverBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = requireContext().applicationContext
        binding = FragmentMainDriverBinding.inflate(inflater, container, false)
        initAll()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun initAll(){
        dpresenter = DriverPresenter(context)
        db= DbHandler(context,DataMembers.DB_NAME)
        dpresenter.attachView(this)
        CoroutineScope(Dispatchers.Main).launch {
            dpresenter.getDriverData(db)
        }
    }

    override fun setUpDriverData(list: ArrayList<DriverBO>) {
        binding!!.mainDriverFragment.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = MainDriverAdapter(list)
        binding!!.mainDriverFragment.isNestedScrollingEnabled = false
        binding!!.mainDriverFragment.adapter = adapter
    }
}