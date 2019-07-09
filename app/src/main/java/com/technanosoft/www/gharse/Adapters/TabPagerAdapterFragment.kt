package com.technanosoft.www.gharse.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.technanosoft.www.gharse.Fragments.*

class TabPagerAdapterFragment(fm: FragmentManager, private var tabCount: Int) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return Fragment_My_Recipe()
            1 -> return QueueOrders()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

}