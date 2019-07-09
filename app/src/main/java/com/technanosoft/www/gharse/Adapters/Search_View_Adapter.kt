package com.technanosoft.www.gharse.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.technanosoft.www.gharse.Models.Recipe
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.ViewHolders.SearchView_ViewHolder
import android.graphics.Typeface
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.technanosoft.www.gharse.User_Fragment.Fragment_User_Home
import com.technanosoft.www.gharse.User_Home
import kotlinx.android.synthetic.main.search_card.view.*


class Search_View_Adapter(var dat:MutableList<Recipe?>): RecyclerView.Adapter<SearchView_ViewHolder>(){

    private var mContext: Context? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SearchView_ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.search_card,p0,false)
        return SearchView_ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dat.count()
    }

    override fun onBindViewHolder(p0: SearchView_ViewHolder, p1: Int) {
        val Data = dat[p1]

        //val am = p0.sview.context.getApplicationContext().getAssets()
        val custom_font = Typeface.createFromAsset(p0.sview.context.assets, "fonts/Lato-Regular.ttf")
        p0.sview.search_text.setTypeface(custom_font,Typeface.NORMAL)
        p0.search_txt.text = Data!!.RecipeName
        p0.sview.setOnClickListener (object: View.OnClickListener{
            override fun onClick(v: View?) {
                mContext = p0.sview.context
                fragmentJump(Data.RecipeName)
            }

        })
    }

    private fun fragmentJump(mItemSelected: String) {
        val mFragment = Fragment_User_Home()
        val mBundle = Bundle()
        mBundle.putString("DataPacket",mItemSelected)
        mFragment.setArguments(mBundle)
        switchContent(R.id.user_home_frag, mFragment)
    }

    fun switchContent(id: Int, fragment: Fragment) {
        if (mContext == null)
            return
        if (mContext is User_Home) {
            val mainActivity = mContext as User_Home
            mainActivity.switchContent(id, fragment)
        }

    }
}