package com.technanosoft.www.gharse.Fragments

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
import com.technanosoft.www.gharse.Adapters.ListingAdapter_user
import com.technanosoft.www.gharse.Adapters.Vendor.AllOrdersAdapter
import com.technanosoft.www.gharse.BuildConfig
import com.technanosoft.www.gharse.Models.Vendor_Models.Vendor_Orders
import com.technanosoft.www.gharse.Networking.Connection
import com.technanosoft.www.gharse.Networking.RequestHandler
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.SharedPrefManager
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class Orders : Fragment(){

    companion object {
        fun newInstance(): Orders {
            val fragmentHome = Orders()
            val args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }
    }

    private var listing_rec : RecyclerView? = null
    var posts: MutableList<Vendor_Orders?> = ArrayList()
    private lateinit var adapter: AllOrdersAdapter
    private var progress: ProgressBar? = null
    lateinit var manager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_orders, container, false)
        listing_rec = view.findViewById<View>(R.id.listing_Order_vend) as RecyclerView
        progress = view.findViewById<View>(R.id.progessBar) as ProgressBar
        manager = LinearLayoutManager(Orders.newInstance().context)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = AllOrdersAdapter(posts)
        listing_rec?.layoutManager = manager!!
        listing_rec?.adapter = adapter
        val ob = Connection()
        if (ob.isNetworkAvailable(context!!)){
            GetOrder()
        }
        else{
            Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show()
        }
    }
    private fun GetOrder(){

        val sh = SharedPrefManager()
        sh.SharedPrefManager_fun(context!!)
        val user = sh.getUser()

        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                    var params: MutableMap<String, String>? = null
                    params = HashMap()
                    params["RegisterID"] = user.RegisterID.toString()

                    //returning the response
                    return requestHandler.sendPostRequest(BuildConfig.Base_URL + "GetVendorOrdersByRegisterID", params)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                progress!!.visibility = View.GONE
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    val obj = JSONObject(s)
                    val array = obj.getJSONArray("Data")
                    for (i in 0 until array.length()){
                        val ob = array.getJSONObject(i)
                        var OrderID = ob.getString("OrderID")
                        var RegisterID = ob.getString("RegisterID")
                        var Name = ob.getString("Name")
                        var OrderNumber = ob.getString("OrderNumber")
                        var ItemsCount = ob.getString("ItemsCount")
                        var TotalPrice = ob.getString("TotalPrice")
                        var AfterCouponTotalPrice = ob.getString("AfterCouponTotalPrice")
                        var CuponActive = ob.getString("CuponActive")
                        var CuponPrice = ob.getString("CuponPrice")
                        var CuponCode = ob.getString("CuponCode")
                        var OrderTime = ob.getString("OrderTime")
                        var DeliveryTime = ob.getString("DeliveryTime")
                        var OrderStatus = ob.getString("OrderStatus")
                        var DeliveryAddress = ob.getString("DeliveryAddress")
                        var ContactNumber = ob.getString("ContactNumber")
                        var PaymentMethod = ob.getString("PaymentMethod")
                        var PaymentStatus = ob.getString("PaymentStatus")
                        val dataitem = Vendor_Orders(OrderID,RegisterID,Name,OrderNumber,ItemsCount,TotalPrice,AfterCouponTotalPrice,CuponActive,CuponPrice,CuponCode,OrderTime,DeliveryTime,OrderStatus,DeliveryAddress,ContactNumber,PaymentMethod,PaymentStatus)
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