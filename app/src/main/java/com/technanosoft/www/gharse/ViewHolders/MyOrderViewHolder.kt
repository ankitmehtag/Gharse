package com.technanosoft.www.gharse.ViewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.technanosoft.www.gharse.Models.MyOrders
import com.technanosoft.www.gharse.R

class MyOrderViewHolder(val viewe : View):RecyclerView.ViewHolder(viewe){
    var all: MyOrders? = null
    var Recipeame : TextView = viewe.findViewById<View>(R.id.Recipename) as TextView
    var Recipimg : ImageView = viewe.findViewById<View>(R.id.Recipeimg) as ImageView
    var RecipTyp : ImageView = viewe.findViewById<View>(R.id.RecipeTyp) as ImageView
    var from_tme : TextView = viewe.findViewById<View>(R.id.from_tim) as TextView
    var to_tme : TextView = viewe.findViewById<View>(R.id.to_tim) as TextView
    var quanti : TextView = viewe.findViewById<View>(R.id.quantit) as TextView
    var subtota: TextView = viewe.findViewById<View>(R.id.sub_total_cart) as TextView
    var delete_btn : Button = viewe.findViewById<View>(R.id.delete_btn) as Button
}