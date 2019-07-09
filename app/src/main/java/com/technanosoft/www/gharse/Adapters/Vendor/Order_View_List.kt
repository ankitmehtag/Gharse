package com.technanosoft.www.gharse.Adapters.Vendor

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.technanosoft.www.gharse.Fragments.Fragment_Order_Details
import com.technanosoft.www.gharse.Models.Vendor_Models.Recipe_Vendor
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.Vendor_Home
import com.technanosoft.www.gharse.ViewHolders.Vendor.Order_View_List_ViewHolder

class Order_View_List(var data:MutableList<Recipe_Vendor?>):RecyclerView.Adapter<Order_View_List_ViewHolder>(){


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Order_View_List_ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.order_cards,p0,false)
        return Order_View_List_ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(p0: Order_View_List_ViewHolder, p1: Int) {
        val Data = data[p1]
        if (Data!!.RecipeType == "Nonveg"){
            p0.RecipeTyp.setImageResource(R.drawable.ic_non_veg)
        }else{
            p0.RecipeTyp.setImageResource(R.drawable.ic_veg_icon)
        }
        Glide.with(p0.Vi.context).load("http://gharse.theposline.in/uploads/Recipe/"+Data?.RecipeImage).into(p0.Recipeimg)
        p0.PriceTyp.text = Data.PriceType
        p0.Recipename.text = Data.RecipeName
        p0.from_tim.text = Data.FromTime
        p0.to_tim.text = Data.ToTime
    }



}