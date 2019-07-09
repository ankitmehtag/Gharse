package com.technanosoft.www.gharse.Adapters

import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.technanosoft.www.gharse.BuildConfig
import com.technanosoft.www.gharse.Models.Tiffin_Data
import com.technanosoft.www.gharse.Networking.RequestHandler
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.SharedPrefManager
import com.technanosoft.www.gharse.ViewHolders.TiffinViewHolder
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class TiffinAdapter(var data:MutableList<Tiffin_Data?>):RecyclerView.Adapter<TiffinViewHolder>(){
    private var mContext: Context? = null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TiffinViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.tiffin_card,p0,false)
        return TiffinViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(p0: TiffinViewHolder, p1: Int) {
        val Data = data[p1]
        Glide.with(p0.io.context).load("http://gharse.theposline.in/uploads/Recipe/"+Data!!.RecipeImage).into(p0.Recipeim)
        p0.Recipenme.text = Data!!.RecipeName
        p0.foodtime.text = Data!!.FoodTime
        p0.itmqua.text = Data!!.ItemQuantity
        p0.totalpay.text = Data!!.SubTotal
        p0.weekday.text = Data!!.DayName
        p0.delbtn.setOnClickListener {
            Cart_check(p0.io.context,Data.TiffinCartID)
            data.removeAt(p1)
            try {
                notifyDataSetChanged()
            }catch (e:Exception){
                Toast.makeText(p0.io.context, "Error : $e", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun Cart_check(Con: Context, cartId:String ){
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
                params["TiffinCartID"] = cartId
                params["RegisterID"] = user.RegisterID.toString()

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"DeleteTiffinCart", params)
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