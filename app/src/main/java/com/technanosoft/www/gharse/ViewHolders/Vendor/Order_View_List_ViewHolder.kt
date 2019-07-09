package com.technanosoft.www.gharse.ViewHolders.Vendor

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.technanosoft.www.gharse.Models.Vendor_Models.Recipe_Vendor
import com.technanosoft.www.gharse.R

class Order_View_List_ViewHolder(val Vi:View):RecyclerView.ViewHolder(Vi){
    var all: Recipe_Vendor? = null
    var Recipename : TextView = Vi.findViewById<View>(R.id.Recip) as TextView
    var Recipeimg : ImageView = Vi.findViewById<View>(R.id.Recipeimge) as ImageView
    var RecipeTyp : ImageView = Vi.findViewById<View>(R.id.RecipeTy) as ImageView
    var from_tim : TextView = Vi.findViewById<View>(R.id.from_time) as TextView
    var to_tim : TextView = Vi.findViewById<View>(R.id.to_time) as TextView
    var PriceTyp : TextView = Vi.findViewById<View>(R.id.price_typ) as TextView

}