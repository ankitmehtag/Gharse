package com.technanosoft.www.gharse.ViewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.technanosoft.www.gharse.Models.Tiffin_Data
import com.technanosoft.www.gharse.R

class TiffinViewHolder(val io:View): RecyclerView.ViewHolder(io){
    var alDa: Tiffin_Data? = null
    var Recipenme : TextView = io.findViewById<View>(R.id.Recipenme) as TextView
    var Recipeim : ImageView = io.findViewById<View>(R.id.tiffin_recipe_img) as ImageView
    var itmqua : TextView = io.findViewById<View>(R.id.itmquan) as TextView
    var totalpay : TextView = io.findViewById<View>(R.id.total_pay) as TextView
    var foodtime : TextView = io.findViewById<View>(R.id.foodtime) as TextView
    var weekday : TextView = io.findViewById<View>(R.id.weekday) as TextView
    var delbtn : Button = io.findViewById<View>(R.id.delete_btn_tif) as Button
}