package com.technanosoft.www.gharse.ViewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.technanosoft.www.gharse.Models.Recipe
import com.technanosoft.www.gharse.R

class SearchView_ViewHolder(val sview: View):RecyclerView.ViewHolder(sview){
    var alr:Recipe? = null
    var search_txt:TextView = sview.findViewById<View>(R.id.search_text) as TextView
}