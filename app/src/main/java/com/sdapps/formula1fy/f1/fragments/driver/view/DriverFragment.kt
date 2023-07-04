package com.sdapps.formula1fy.f1.fragments.driver.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.formula1fy.core.dbUtil.DbHandler
import com.sdapps.formula1fy.core.models.DataMembers
import com.sdapps.formula1fy.databinding.FragmentMainDriverBinding
import com.sdapps.formula1fy.f1.bo.DriverBO
import com.sdapps.formula1fy.f1.fragments.driver.DriverInteractor
import com.sdapps.formula1fy.f1.fragments.driver.DriverPresenter
import com.sdapps.formula1fy.f1.fragments.driver.adapter.BottomModal
import com.sdapps.formula1fy.f1.fragments.driver.adapter.MainDriverAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DriverFragment : Fragment(), DriverInteractor.View {
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
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAll()
    }

    fun initAll(){
        dpresenter = DriverPresenter(context)
        db= DbHandler(context,DataMembers.DB_NAME)
        dpresenter.attachView(this)
        lifecycleScope.launch {
            dpresenter.getDriverData(db)
        }
    }

    override fun setUpDriverData(list: ArrayList<DriverBO>) {
        binding!!.mainDriverFragment.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = MainDriverAdapter(list, this)
        binding!!.mainDriverFragment.isNestedScrollingEnabled = false
        binding!!.mainDriverFragment.adapter = adapter
    }

    override fun onCardClick(driverBO: DriverBO) {
        CoroutineScope(Dispatchers.Main).launch(){
            dpresenter.handleCardClick(driverBO)
        }

    }

    override fun showToast(driverBo: DriverBO) {
       openBio(driverBo)
    }

    fun openBio(driverBO : DriverBO){
        val bModal = BottomModal(driverBO)
        val fmgr : FragmentManager? = fragmentManager
        if (fmgr != null) {
            bModal.show(fmgr, BottomModal.TAG)
        }
    }
}