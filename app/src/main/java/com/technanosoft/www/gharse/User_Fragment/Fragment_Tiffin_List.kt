package com.technanosoft.www.gharse.User_Fragment

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.technanosoft.www.gharse.Adapters.TiffinAdapter
import com.technanosoft.www.gharse.BuildConfig
import com.technanosoft.www.gharse.Models.MyOrders
import com.technanosoft.www.gharse.Models.Tiffin_Data
import com.technanosoft.www.gharse.Networking.Connection
import com.technanosoft.www.gharse.Networking.RequestHandler
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_list_tiffin.*
import kotlinx.android.synthetic.main.fragment_my_orders.*
import kotlinx.android.synthetic.main.loadmore_refresh.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class Fragment_Tiffin_List:Fragment(){

    private var listing_rec : RecyclerView? = null
    var posts: MutableList<Tiffin_Data?> = ArrayList()
    private lateinit var adapter: TiffinAdapter
    private var progress: ProgressBar? = null
    lateinit var manager: LinearLayoutManager
    var itemQuan:Int = 0
    var subTotal:Int = 0


    companion object {
        fun newInstance(): Fragment_Tiffin_List {
            val fragmentMy = Fragment_Tiffin_List()
            val args = Bundle()
            fragmentMy.arguments = args
            return fragmentMy
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_list_tiffin, container, false)
        listing_rec = view.findViewById<View>(R.id.listing_tiffin) as RecyclerView
        progress = view.findViewById<View>(R.id.progessBar) as ProgressBar
        manager = LinearLayoutManager(Fragment_Tiffin_List.newInstance().context)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = TiffinAdapter(posts)
        listing_rec?.layoutManager = manager!!
        listing_rec?.adapter = adapter
        val ob = Connection()
        if (ob.isNetworkAvailable(context!!)){
            Tiffin_data_grab()
        }
        checkout_tiffin.setOnClickListener {
            val frag = Fragment_Tiffin_CheckOut()
            val bn = Bundle()
            bn.putString("SubTotal",subTotal.toString())
            bn.putString("itemquan",itemQuan.toString())
            frag.arguments = bn
            adfrag(frag)
        }
    }

    private fun adfrag(fragment:Fragment) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.user_home_frag, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun Tiffin_data_grab(){

        val sh = SharedPrefManager()
        sh.SharedPrefManager_fun(context!!)
        val user = sh.getUser()

        class SignUp : AsyncTask<Void, Void, String>() {

            override fun onPreExecute() {
                super.onPreExecute()
                itemQuan = 0
                subTotal = 0
            }

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["RegisterID"] = user.RegisterID.toString()

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL + "GetTiffinCartByRegisterID", params)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                progress!!.visibility = View.GONE
                loadMore!!.visibility = View.GONE
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    val obj = JSONObject(s)
                    val array = obj.getJSONArray("Data")
                    for (i in 0 until array.length()){
                        val ob = array.getJSONObject(i)
                        var TiffinCartID = ob.getString("TiffinCartID")
                        var WeekDayID = ob.getString("WeekDayID")
                        var DayName = ob.getString("DayName")
                        var RegisterID = ob.getString("RegisterID")
                        var RecipeID = ob.getString("RecipeID")
                        var PriceID = ob.getString("PriceID")
                        var Price = ob.getString("Price")
                        var ItemQuantity = ob.getString("ItemQuantity")
                        var SubTotal = ob.getString("SubTotal")
                        var PriceType = ob.getString("PriceType")
                        var FoodTime = ob.getString("FoodTime")
                        var RecipeName = ob.getString("RecipeName")
                        var RecipeImage = ob.getString("RecipeImage")
                        var RecipeType = ob.getString("RecipeType")
                        var RecipeDescription = ob.getString("RecipeDescription")
                        var FromTime = ob.getString("FromTime")
                        var ToTime = ob.getString("ToTime")
                        var Status = ob.getString("Status")
                        var CreatedOn = ob.getString("CreatedOn")
                        itemQuan += ItemQuantity.toInt()
                        subTotal += Price.toInt()
                        val dataitem = Tiffin_Data(TiffinCartID,WeekDayID,DayName,RegisterID,RecipeID,PriceID,Price,ItemQuantity,SubTotal,PriceType,FoodTime,RecipeName,RecipeImage,RecipeType,RecipeDescription,FromTime,ToTime,Status,CreatedOn)
                        posts.add(dataitem)
                    }
                    itmqua.text = itemQuan.toString()
                    totalpay.text = subTotal.toString()
                    adapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    Toast.makeText(activity, "Error : $e", Toast.LENGTH_LONG).show()
                }

            }
        }
        //executing the async task
        val ru = SignUp()
        ru.execute()

    }

}