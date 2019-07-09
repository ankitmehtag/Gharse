package com.technanosoft.www.gharse.User_Fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.technanosoft.www.gharse.*
import com.technanosoft.www.gharse.Adapters.ListingAdapter_user
import com.technanosoft.www.gharse.Adapters.MyOrderAdapter
import com.technanosoft.www.gharse.Models.MyOrders
import com.technanosoft.www.gharse.Models.Recipe
import com.technanosoft.www.gharse.Networking.Connection
import com.technanosoft.www.gharse.Networking.RequestHandler
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_my_orders.*
import kotlinx.android.synthetic.main.loadmore_refresh.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class Fragment_My_Orders:Fragment(){

    private var listing_rec : RecyclerView? = null
    var posts: MutableList<MyOrders?> = ArrayList()
    private lateinit var adapter: MyOrderAdapter
    private var progress: ProgressBar? = null
    private var subto:Int = 0
    private var itemto:Int = 0
    lateinit var manager: LinearLayoutManager
    lateinit var progressDialog : ProgressDialog

    companion object {
        fun newInstance(): Fragment_My_Orders {
            val fragmentMy = Fragment_My_Orders()
            val args = Bundle()
            fragmentMy.arguments = args
            return fragmentMy
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_my_orders, container, false)
        listing_rec = view.findViewById<View>(R.id.listing_cart) as RecyclerView
        progress = view.findViewById<View>(R.id.progessBar) as ProgressBar
        manager = LinearLayoutManager(Fragment_User_Home.newInstance().context)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = MyOrderAdapter(posts)
        listing_rec?.layoutManager = manager!!
        listing_rec?.adapter = adapter
        val ob = Connection()
        if (ob.isNetworkAvailable(context!!)){
            checke()
        }
        else{
            Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show()
        }
        checkout.setOnClickListener {
            val check = checkout_radio.checkedRadioButtonId
            val radiobtn = view!!.findViewById<RadioButton>(check)
            var ui = radiobtn.text
            if (ui == "Cash On Delivery"){
                ui = "Cash"
            }else if (ui =="Online Payment") {
                ui = "Online"
            }
            progressDialog = ProgressDialog(context)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Please wait..")
            progressDialog.setIndeterminateDrawable(resources.getDrawable(R.drawable.anim1))
            progressDialog.show()
            checkout(ui.toString())

        }
        sub_total.text = subto.toString()
    }

    fun checkout(payty:String){
        val sh = SharedPrefManager()
        sh.SharedPrefManager_fun(context!!)
        val user = sh.getUser()
        val add = input_address.text.toString().trim()

        class SignU : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["RegisterID"] = user.RegisterID.toString()
                params["ItemsCount"] = itemto.toString()
                params["TotalPrice"] = subto.toString()
                params["AfterCouponTotalPrice"] = "0"
                params["CuponActive"] = "InActive"
                params["CuponPrice"] = "0"
                params["CuponCode"] = "N/A"
                params["OrderStatus"] = "Accepted"
                params["DeliveryAddress"] = add
                params["ContactNumber"] = user.Mobile
                params["PaymentMethod"] = payty
                params["PaymentStatus"] = "Pending"

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL + "Checkout", params)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                progressDialog.dismiss()
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    val obj = JSONObject(s)
                    val Dat = obj.getJSONObject("Data")
                    val ol = obj.getJSONObject("ApiStatus")
                    val code = ol.getString("StatusCode")
                    val message = ol.getString("StatusMessage")
                    if (code.toInt() == 200){
                        if (payty == "Online") {
                            var orderid = Dat.getString("OrderID")
                            var TotalPrice = Dat.getString("TotalPrice")
                            val intent = Intent(context, Payment_Gateway::class.java)
                            intent.putExtra("tx", orderid)
                            intent.putExtra("am", TotalPrice)
                            intent.putExtra("pin", "Food")
                            startActivity(intent)
                            Toast.makeText(context, "Order Queued : $message+ly", Toast.LENGTH_LONG).show()
                        }else{
                            var i = 0
                            val dialog = SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Order Message")
                                .setContentText("Order Placed Successfully")
                                .setConfirmClickListener{
                                    val intent = Intent(context, Login::class.java)
                                    startActivity(intent)
                                }
                            dialog.show()

                        }
                        /*SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Order Message")
                            .setContentText("Order Placed Successfully")
                            .setConfirmClickListener{
                                val fragment = Fragment_User_Home()
                                val bun = Bundle()
                                bun.putString("Tiff","F")
                                fragment.arguments = bun
                                addfrag(fragment)
                            }.show()*/
                    }
                } catch (e: JSONException) {
                    Toast.makeText(context, "Error : $e", Toast.LENGTH_LONG).show()
                }

            }
        }
        //executing the async task
        val ru = SignU()
        ru.execute()
    }

    private fun addfrag(fragment:Fragment) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.user_home_frag, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun checke(){

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
                    return requestHandler.sendPostRequest(BuildConfig.Base_URL + "GetCartByRegisterID", params)
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
                        var CartID = ob.getString("CartID")
                        var RecipeID = ob.getString("RecipeID")
                        var RegisterID = ob.getString("RegisterID")
                        var PriceID = ob.getString("PriceID")
                        var Price = ob.getString("Price")
                        var ItemCount = ob.getString("ItemCount")
                        var SubTotal = ob.getString("SubTotal")
                        subto += SubTotal.toInt()
                        itemto += ItemCount.toInt()
                        var RecipeName = ob.getString("RecipeName")
                        var RecipeImage = ob.getString("RecipeImage")
                        var RecipeType = ob.getString("RecipeType")
                        var RecipeDescriptio = ob.getString("RecipeDescription")
                        var FromTime = (ob.getString("FromTime")).toString()
                        var ToTime = (ob.getString("ToTime")).toString()
                        val dataitem = MyOrders(CartID,RegisterID,RecipeID,PriceID,Price,ItemCount,SubTotal,RecipeName,RecipeImage,RecipeType,RecipeDescriptio,FromTime,ToTime)
                        posts.add(dataitem)
                    }
                    adapter.notifyDataSetChanged()
                    sub_total.text = subto.toString()
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