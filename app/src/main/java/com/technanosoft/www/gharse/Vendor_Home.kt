package com.technanosoft.www.gharse

import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import com.technanosoft.www.gharse.Adapters.TabPagerAdapter
import com.technanosoft.www.gharse.Adapters.TabPagerAdapterFragment
import com.technanosoft.www.gharse.Fragments.FragmentHome
import com.technanosoft.www.gharse.Fragments.FragmentWeekPlan
import kotlinx.android.synthetic.main.activity_vendor__home.*
import kotlinx.android.synthetic.main.app_bar_vendor__home.*
import kotlinx.android.synthetic.main.content_vendor__home.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import android.text.format.DateFormat.is24HourFormat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.technanosoft.www.gharse.Fragments.Fragment_My_Recipe
import java.text.SimpleDateFormat
import java.util.*


class Vendor_Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var content: FrameLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor__home)
        setSupportActionBar(toolbar)



        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val sh = SharedPrefManager()
        sh.SharedPrefManager_fun(this)
        val io = sh.getUser()

        val navigationView = findViewById<View>(R.id.nav_view_vendor) as NavigationView
        val hView = navigationView.getHeaderView(0)
        val nav_user = hView.findViewById(R.id.name_vendor) as TextView
        val nav_email = hView.findViewById(R.id.emailvendor) as TextView
        nav_user.setText(io.Name)
        nav_email.setText(io.Email)

        nav_view_vendor.setNavigationItemSelectedListener(this)
        content = findViewById(R.id.contain_frage)
        val fragment = FragmentHome.Companion.newInstance()
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.contain_frage, fragment, "MY_FRAGMENT")
            .commit()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.contain_frage, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.week_plan -> {
                addFragment(FragmentWeekPlan())
            }
            R.id.home_bt -> {
                startActivity(Intent(this@Vendor_Home,Vendor_Home::class.java))
                finish()
            }
            R.id.logout_vendor ->{
                val sh = SharedPrefManager()
                sh.SharedPrefManager_fun(this)
                sh.logout()
                finish()
                startActivity(Intent(this,Selection::class.java))
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun switchContent(id: Int, fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(id, fragment, fragment.toString())
        ft.addToBackStack(null)
        ft.commit()
    }

}
