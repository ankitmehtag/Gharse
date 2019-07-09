package com.technanosoft.www.gharse.User_Fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.technanosoft.www.gharse.Models.Tiffin_Sys
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.SharedPrefManager
import com.technanosoft.www.gharse.User_Home
import kotlinx.android.synthetic.main.fragment_tiffin.*

class Fragment_Tiffin: Fragment(){


    var adapte: ArrayAdapter<String>? = null

    var prictyp = arrayOf("Lunch","Dinner")

    companion object {
        fun newInstance(): Fragment_Tiffin {
            val fragmentTiffin = Fragment_Tiffin()
            val args = Bundle()
            fragmentTiffin.arguments = args
            return fragmentTiffin
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_tiffin, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapte = ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, prictyp)
        sun_typ!!.setAdapter(adapte)
        mon_typ!!.setAdapter(adapte)
        tue_typ!!.setAdapter(adapte)
        wed_typ!!.setAdapter(adapte)
        thu_typ!!.setAdapter(adapte)
        fri_typ!!.setAdapter(adapte)
        sat_typ!!.setAdapter(adapte)
        btn_snday.setOnClickListener {
            val sh = SharedPrefManager()
            sh.SharedPrefManager_fun(context!!)
            sh.putweekId(Tiffin_Sys("7",sun_typ.text.trim().toString()))
            val inte = Intent(context,User_Home::class.java)
            inte.putExtra("Tiff","T")
            startActivity(inte)
        }
        btn_mnday.setOnClickListener {
            val sh = SharedPrefManager()
            sh.SharedPrefManager_fun(context!!)
            sh.putweekId(Tiffin_Sys("1",mon_typ.text.trim().toString()))
            val inte = Intent(context,User_Home::class.java)
            inte.putExtra("Tiff","T")
            startActivity(inte)
        }
        btn_tuday.setOnClickListener {
            val sh = SharedPrefManager()
            sh.SharedPrefManager_fun(context!!)
            sh.putweekId(Tiffin_Sys("2",tue_typ.text.trim().toString()))
            val inte = Intent(context,User_Home::class.java)
            inte.putExtra("Tiff","T")
            startActivity(inte)
        }
        btn_weday.setOnClickListener {
            val sh = SharedPrefManager()
            sh.SharedPrefManager_fun(context!!)
            sh.putweekId(Tiffin_Sys("3",wed_typ.text.trim().toString()))
            val inte = Intent(context,User_Home::class.java)
            inte.putExtra("Tiff","T")
            startActivity(inte)
        }
        btn_thday.setOnClickListener {
            val sh = SharedPrefManager()
            sh.SharedPrefManager_fun(context!!)
            sh.putweekId(Tiffin_Sys("4",thu_typ.text.trim().toString()))
            val inte = Intent(context,User_Home::class.java)
            inte.putExtra("Tiff","T")
            startActivity(inte)
        }
        btn_frday.setOnClickListener {
            val sh = SharedPrefManager()
            sh.SharedPrefManager_fun(context!!)
            sh.putweekId(Tiffin_Sys("5",fri_typ.text.trim().toString()))
            val inte = Intent(context,User_Home::class.java)
            inte.putExtra("Tiff","T")
            startActivity(inte)
        }
        btn_saday.setOnClickListener {
            val sh = SharedPrefManager()
            sh.SharedPrefManager_fun(context!!)
            sh.putweekId(Tiffin_Sys("6",sat_typ.text.trim().toString()))
            val inte = Intent(context,User_Home::class.java)
            inte.putExtra("Tiff","T")
            startActivity(inte)
        }
    }

    private fun addfrag(fragment:Fragment) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.user_home_frag,Fragment.instantiate(context,fragment.javaClass.name))
        transaction.addToBackStack(null)
        transaction.commit()
    }

}