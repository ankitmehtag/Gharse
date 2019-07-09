package com.technanosoft.www.gharse

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.technanosoft.www.gharse.User_Fragment.Fragment_My_Orders
import com.technanosoft.www.gharse.User_Fragment.Fragment_Tiffin
import com.technanosoft.www.gharse.User_Fragment.Fragment_Tiffin_List
import com.technanosoft.www.gharse.User_Fragment.Fragment_User_Home
import kotlinx.android.synthetic.main.activity_user__home.*
import kotlinx.android.synthetic.main.app_bar_user__home.*
import android.widget.TextView
import cn.pedant.SweetAlert.SweetAlertDialog


class User_Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    val sh = SharedPrefManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user__home)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        var name = ""
        var email = ""
        sh.SharedPrefManager_fun(this)
        if(sh.isLoggedIn("user")){
            val io = sh.getUser()
            name = io.Name
            email = io.Email
        }

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val hView = navigationView.getHeaderView(0)
        val nav_user = hView.findViewById(R.id.name_usr) as TextView
        val nav_email = hView.findViewById(R.id.email_user) as TextView
        nav_user.setText(name)
        nav_email.setText(email)


        /*
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.findViewById<View>(R.layout.nav_header_home_user_)
        val navUsername = headerView.findViewById(R.id.name_usr) as TextView
        navUsername.text = io.Name*/

       // name_usr.setText(io.Name)
       // email_user.text = io.Email

        nav_view.setNavigationItemSelectedListener(this)


        val Tiif = intent.getStringExtra("Tiff")


        val fragment = Fragment_User_Home.Companion.newInstance()
        val bun = Bundle()
        bun.putString("Tiff",Tiif)
        fragment.arguments = bun
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.user_home_frag, fragment, "MY_FRAGMENT")
            .commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.user_ho ->{
                try{
                    val bun = Bundle()
                    bun.putString("Tiff","F")
                    val frag = Fragment_User_Home()
                    frag.arguments = bun
                    addfragement(frag)
                }catch (e:Exception){

                }

            }

            /*R.id.user_profile -> {

            }*/
            R.id.my_cart ->{
                if (sh.isLoggedIn("user")){
                    addfragement(Fragment_My_Orders())
                }else{
                    showerror()
                }
            }
            R.id.tiffin ->{
                if (sh.isLoggedIn("user")){
                    addfragement(Fragment_Tiffin())
                }else{
                    showerror()
                }

            }
            R.id.tiffin_cart ->{
                if (sh.isLoggedIn("user")){
                    addfragement(Fragment_Tiffin_List.Companion.newInstance())
                }else{
                    showerror()
                }

            }
            R.id.logout ->{
                val sh = SharedPrefManager()
                sh.SharedPrefManager_fun(this)
                sh.logout()
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
    private fun addfragement(fragment:Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.user_home_frag, fragment , "MY")
            .addToBackStack(null)
            .commit()
    }
    private fun showerror(){
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Not Registered")
            .setContentText("Please Register First")
            .setConfirmClickListener {
                val intent = Intent(this,Registration::class.java)
                startActivity(intent)
            }
            .show()
    }

}
