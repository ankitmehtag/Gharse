package com.technanosoft.www.gharse.Adapters.Vendor

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.technanosoft.www.gharse.Fragments.Fragment_My_Recipe
import com.technanosoft.www.gharse.Models.Recipe
import com.technanosoft.www.gharse.Models.Vendor_Models.Recipe_Vendor
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.User_Fragment.Fragment_Recipe_Details
import com.technanosoft.www.gharse.User_Home
import com.technanosoft.www.gharse.ViewHolders.RecipeViewHolder

class RecipeListing(val data:MutableList<Recipe_Vendor?>):RecyclerView.Adapter<RecipeViewHolder>(){

    private var mContext: Context? = null


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecipeViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.user_card,p0,false)
        return RecipeViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(p0: RecipeViewHolder, p1: Int) {
        val Data = data[p1]
        if (Data!!.RecipeType == "Nonveg"){
            p0.RecipeTyp.setImageResource(R.drawable.ic_non_veg)
        }else{
            p0.RecipeTyp.setImageResource(R.drawable.ic_veg_icon)
        }
        Glide.with(p0.view.context).load("http://gharseapp.confertrack.com/uploads/Recipe/"+Data?.RecipeImage).into(p0.Recipeimg)
        p0.RecipeDescription.text = Data.RecipeDescription
        p0.Recipename.text = Data.RecipeName
        p0.from_tim.text = Data.FromTime
        p0.to_tim.text = Data.ToTime

        p0.view.setOnClickListener (object: View.OnClickListener{
            override fun onClick(v: View?) {
                mContext = p0.view.context
                fragmentJump(Data.RecipeID,Data.Flagop)


            }

        })
    }


    private fun fragmentJump(mItemSelected: String,Tiff_Status: String) {
        val mFragment = Fragment_Recipe_Details()
        val mBundle = Bundle()
        mBundle.putString("Data",mItemSelected)
        mBundle.putString("Tiff",Tiff_Status)
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