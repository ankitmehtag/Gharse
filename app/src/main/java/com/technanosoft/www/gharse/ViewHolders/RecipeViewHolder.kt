package com.technanosoft.www.gharse.ViewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.technanosoft.www.gharse.Models.Recipe
import com.technanosoft.www.gharse.R

class RecipeViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    var all: Recipe? = null
    var Recipename : TextView = view.findViewById<View>(R.id.Recipename) as TextView
    var Recipeimg : ImageView = view.findViewById<View>(R.id.Recipeimg) as ImageView
    var RecipeTyp : ImageView = view.findViewById<View>(R.id.RecipeTyp) as ImageView
    var from_tim : TextView = view.findViewById<View>(R.id.from_tim) as TextView
    var to_tim : TextView = view.findViewById<View>(R.id.to_tim) as TextView
    var RecipeDescription : TextView = view.findViewById<View>(R.id.RecipeDescription) as TextView
}