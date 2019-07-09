package com.technanosoft.www.gharse.Fragments

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import com.technanosoft.www.gharse.Adapters.Vendor.Order_View_List
import com.technanosoft.www.gharse.BuildConfig
import com.technanosoft.www.gharse.Models.Vendor_Models.Recipe_Vendor
import com.technanosoft.www.gharse.Networking.Connection
import com.technanosoft.www.gharse.Networking.RequestHandler
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_order_view.*
import kotlinx.android.synthetic.main.fragment_tiffin_checkout.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class Fragment_Order_Details:Fragment(){



    var paymode = arrayOf("Baked","On The Way")

    private var listing_rec : RecyclerView? = null
    var posts: MutableList<Recipe_Vendor?> = ArrayList()
    private lateinit var adapter: Order_View_List
    private var progress: ProgressBar? = null
    lateinit var manager: LinearLayoutManager
    var adapte: ArrayAdapter<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_order_view, container, false)
        listing_rec = view.findViewById<View>(R.id.order_listing_view) as RecyclerView
        progress = view.findViewById<View>(R.id.progessBar) as ProgressBar
        manager = LinearLayoutManager(context)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = Order_View_List(posts)
        listing_rec?.layoutManager = manager!!
        listing_rec?.adapter = adapter
        val orderID = arguments!!.getString("ID_DATA")
        /*adapte = ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, paymode)
        oredr_status.setAdapter(adapte)*/
        val ob = Connection()
        if (ob.isNetworkAvailable(context!!)){
            check(orderID!!)
        }
        else{
            Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show()
        }
        /*status_or.setOnClickListener {
           // HitData()
        }*/
    }

    fun HitData(){

        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params[""]

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"GetVersion", params)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    val obj = JSONObject(s)
                    val iobj = obj.getJSONObject("ApiStatus")
                    val cod = iobj.getString("StatusCode")
                    val ioh = iobj.getString("StatusMessage")
                    if (cod == "200"){
                        val dat = obj.getJSONObject("Data")
                        val Version = dat.getString("VersionValue")
                            Toast.makeText(context,"Please Update Your Application", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(context,"Error : $ioh", Toast.LENGTH_LONG).show()
                    }

                } catch (e: JSONException) {

                }

            }
        }

        //executing the async task
        val ru = SignUp()
        ru.execute()
    }

    private fun check(ID:String){


        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["OrderID"] = ID

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"GetOrderDetailsByOrderID", params)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                progress!!.visibility = View.GONE
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    val obj = JSONObject(s)
                    val Order_Data = obj.getJSONObject("OrderData")
                    order_ID.text = Order_Data.getString("OrderID")
                    price_total.text = Order_Data.getString("TotalPrice")
                    quantity_order.text = Order_Data.getString("ItemsCount")
                    order_time.text = Order_Data.getString("OrderTime")
                    deliveryAdd.text = Order_Data.getString("DeliveryAddress")
                    contact_numb.text = Order_Data.getString("ContactNumber")
                    payMode.text = Order_Data.getString("PaymentMethod")
                    pay_status.text = Order_Data.getString("PaymentStatus")
                    val array = obj.getJSONArray("Data")
                    for (i in 0 until array.length()){
                        val ob = array.getJSONObject(i)
                        var RecipeID = ob.getString("RecipeID")
                        var RegisterID = ob.getString("RegisterID")
                        var PageIndex = ob.getString("PageIndex")
                        var PageSize = ob.getString("PageSize")
                        var Price =ob.getString("Price")
                        var PriceType = ob.getString("PriceType")
                        var RecipeName = ob.getString("RecipeName")
                        var RecipeImage = ob.getString("RecipeImage")
                        var RecipeType = ob.getString("RecipeType")
                        var RecipeDescriptio = ob.getString("RecipeDescription")
                        var FromTime = (ob.getString("FromTime")).toString()
                        var ToTime = (ob.getString("ToTime")).toString()
                        var Status = ob.getString("Status")
                        val dataitem = Recipe_Vendor(RecipeID,RegisterID,PageIndex,PageSize,Price,PriceType,RecipeName,RecipeImage,RecipeType,RecipeDescriptio,FromTime,ToTime,Status,"2")
                        posts.add(dataitem)
                    }
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