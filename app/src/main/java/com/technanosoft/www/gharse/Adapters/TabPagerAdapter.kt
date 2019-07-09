package com.technanosoft.www.gharse.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.technanosoft.www.gharse.Fragments.Orders
import com.technanosoft.www.gharse.Fragments.Schedule

class TabPagerAdapter(fm: FragmentManager, private var tabCount: Int) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return Schedule()
            1 -> return Orders()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}