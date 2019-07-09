package com.technanosoft.www.gharse.Adapters

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.technanosoft.www.gharse.BuildConfig
import com.technanosoft.www.gharse.Models.MyOrders
import com.technanosoft.www.gharse.Networking.RequestHandler
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.SharedPrefManager
import com.technanosoft.www.gharse.User_Fragment.Fragment_My_Orders
import com.technanosoft.www.gharse.User_Fragment.Fragment_User_Home
import com.technanosoft.www.gharse.User_Home
import com.technanosoft.www.gharse.ViewHolders.MyOrderViewHolder
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class MyOrderAdapter(var data:MutableList<MyOrders?>):RecyclerView.Adapter<MyOrderViewHolder>() {

    private var mContext: Context? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyOrderViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.cart_card,p0,false)
        return MyOrderViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(p0: MyOrderViewHolder, p1: Int) {
        val Data = data[p1]
        if (Data!!.RecipeType == "Nonveg"){
            p0.RecipTyp.setImageResource(R.drawable.ic_non_veg)
        }else{
            p0.RecipTyp.setImageResource(R.drawable.ic_veg_icon)
        }
        p0.delete_btn.setOnClickListener {
            Cart_check(p0.viewe.context,Data.CartID)
            data.removeAt(p1)
            try {
                notifyDataSetChanged()
            }catch (e:Exception){
                Toast.makeText(p0.viewe.context, "Error : $e", Toast.LENGTH_LONG).show()
            }

        }
        Glide.with(p0.viewe.context).load("http://gharse.theposline.in/uploads/Recipe/"+Data?.RecipeImage).into(p0.Recipimg)
        p0.quanti.text = Data.ItemCount
        p0.Recipeame.text = Data.RecipeName
        p0.from_tme.text = Data.FromTime
        p0.to_tme.text = Data.ToTime
        p0.subtota.text = Data.SubTotal
    }

    private fun Cart_check(Con: Context,cartId:String ){
        val sha = SharedPrefManager()
        sha.SharedPrefManager_fun(Con)
        val user = sha.getUser()



        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["RegisterID"] = user.RegisterID.toString()
                params["CartID"] = cartId

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"AddToCart", params)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                //progress!!.visibility = View.GONE
                try {
                    val obj = JSONObject(s)
                    val ol = obj.getJSONObject("ApiStatus")
                    val code = ol.getString("StatusCode")
                    val message = ol.getString("StatusMessage")
                    if (code.toInt() == 200){
                        Toast.makeText(Con, "Deleted : $message", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(Con, "Error : $e", Toast.LENGTH_LONG).show()
                }

            }
        }
        //executing the async task
        val ru = SignUp()
        ru.execute()

    }
}