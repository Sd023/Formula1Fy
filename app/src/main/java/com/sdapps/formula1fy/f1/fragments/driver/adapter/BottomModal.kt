package com.sdapps.formula1fy.f1.fragments.driver.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sdapps.formula1fy.R
import com.sdapps.formula1fy.databinding.FragmentMainDriverBinding
import com.sdapps.formula1fy.databinding.ModalViewBottomBinding
import com.sdapps.formula1fy.f1.bo.DriverBO

class BottomModal(val driverBo: DriverBO): BottomSheetDialogFragment() {
    private lateinit var context : Context

    private var binding: ModalViewBottomBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = requireContext().applicationContext
        binding = ModalViewBottomBinding.inflate(inflater, container, false)
        initAll()
        return binding!!.root
    }

    fun initAll(){
        binding!!.driverName.text = driverBo.driverName
        binding!!.driverNation.text = driverBo.driverNationality
        binding!!.driverDOB.text = driverBo.driverDOB

    }
    companion object {
        const val TAG = "BottomModalSheet"
    }
}