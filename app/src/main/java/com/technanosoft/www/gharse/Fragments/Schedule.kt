package com.technanosoft.www.gharse.Fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.technanosoft.www.gharse.Adapters.TabPagerAdapter
import com.technanosoft.www.gharse.Adapters.TabPagerAdapterFragment
import com.technanosoft.www.gharse.R
import kotlinx.android.synthetic.main.app_bar_vendor__home.*
import kotlinx.android.synthetic.main.content_vendor__home.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import android.support.v4.app.FragmentActivity



class Schedule:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_schedule, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressView_circle.progress = 100f
        start_btn.setOnClickListener {
            if((status.text) == "DEACTIVATED"){
                status.text = "ACTIVATED"
                status.setTextColor(resources.getColor(R.color.green))
                start_btn.text = "STOP"
            }else{
                status.text = "DEACTIVATED"
                status.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                start_btn.text = "START"
            }
        }
    }


    companion object {
        fun newInstance(): Schedule {
            val fragmentHome = Schedule()
            val args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }

    }

}