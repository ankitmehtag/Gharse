package com.technanosoft.www.gharse.Fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.technanosoft.www.gharse.Adapters.TabPagerAdapter
import com.technanosoft.www.gharse.Adapters.TabPagerAdapterFragment
import com.technanosoft.www.gharse.R

class FragmentHome:Fragment(){

    private var tab_layout1 :TabLayout? = null
    private var tab_layout2 :TabLayout? = null
    private var pager1 : ViewPager? = null
    private var pager2 : ViewPager? = null
    companion object {
        fun newInstance(): FragmentHome {
            val fragmentHome = FragmentHome()
            val args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_home, container, false)
        tab_layout1 = view.findViewById<View>(R.id.tab_layout_frag) as TabLayout
        tab_layout2 = view.findViewById<View>(R.id.tab_layout) as TabLayout
        pager1 = view.findViewById<View>(R.id.pager_frag) as ViewPager
        pager2 = view.findViewById<View>(R.id.pager) as ViewPager
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureTabLayout()
        configureTabLayout2()
    }

    private fun configureTabLayout2() {

        tab_layout1!!.addTab(tab_layout1!!.newTab().setText("My Recipe"))
        tab_layout1!!.addTab(tab_layout1!!.newTab().setText("Delivered ORDERS"))

        val adapter = TabPagerAdapterFragment(
            activity!!.supportFragmentManager,
            tab_layout1!!.tabCount)
        pager1!!.adapter = adapter

        pager1!!.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tab_layout1))
        tab_layout1!!.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager1!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }

    private fun configureTabLayout() {

        tab_layout2!!.addTab(tab_layout2!!.newTab().setText("SCHEDULE"))
        tab_layout2!!.addTab(tab_layout2!!.newTab().setText("ALL ORDERS"))

        val adapter = TabPagerAdapter(activity!!.supportFragmentManager,
            tab_layout2!!.tabCount)
        pager2!!.adapter = adapter

        pager2!!.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tab_layout2))
        tab_layout2!!.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager2!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }
}