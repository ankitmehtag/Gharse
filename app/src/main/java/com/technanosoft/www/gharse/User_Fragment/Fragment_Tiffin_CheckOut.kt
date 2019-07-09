package com.technanosoft.www.gharse.User_Fragment

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.technanosoft.www.gharse.Networking.RequestHandler
import kotlinx.android.synthetic.main.fragment_tiffin_checkout.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.EditText
import android.util.Log
import android.widget.Spinner
import com.payu.india.CallBackHandler.OnetapCallback
import com.payu.india.Extras.PayUChecksum
import com.payu.india.Interfaces.OneClickPaymentListener
import com.payu.india.Model.PaymentParams
import com.payu.india.Model.PayuConfig
import com.payu.india.Payu.Payu
import com.payu.india.Payu.PayuConstants
import com.technanosoft.www.gharse.*
import kotlinx.android.synthetic.main.fragment_week_plan.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class Fragment_Tiffin_CheckOut: Fragment(){




    var adapte: ArrayAdapter<String>? = null
    var paymode = arrayOf("Cash","Online")
    var sub:String? = null
    var totalp:String? = null
    lateinit var progressDialog : ProgressDialog
    lateinit var billDateEditText: EditText

    companion object {
        fun newInstance(): Fragment_Tiffin_CheckOut {
            val fragmentMy = Fragment_Tiffin_CheckOut()
            val args = Bundle()
            fragmentMy.arguments = args
            return fragmentMy
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_tiffin_checkout, container, false)
        billDateEditText = view.findViewById<View>(R.id.tiffin_start) as EditText
        return view
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapte = ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, paymode)
        payment_mode.setAdapter(adapte)
        sub = arguments!!.getString("SubTotal")
        totalp = arguments!!.getString("itemquan")
        val sh = SharedPrefManager()
        sh.SharedPrefManager_fun(context!!)
        val us = sh.getUser()
        contact_number!!.setText(us.Mobile)
        btn_tfn_checkout.setOnClickListener {
            progressDialog = ProgressDialog(context)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Please wait..")
            progressDialog.setIndeterminateDrawable(activity!!.getDrawable(R.drawable.anim1))
            progressDialog.show()
            tiffin_end.text = extent(tiffin_start.text.toString())
            HitApi()
        }
        tiffin_start.setOnClickListener {
            showDatePicker()
        }
        showDatePicker()
    }

    fun extent(pat:String):String{
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val c = Calendar.getInstance()
            try {
                c.setTime(sdf.parse(pat));
            } catch (e: ParseException) {
                e.printStackTrace()
            }
                c.add(Calendar.DATE, 7)
        val resultdate = Date(c.timeInMillis)
        return sdf.format(resultdate)
    }

    fun showDatePicker() {

        billDateEditText.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            billDateEditText.setText(sdf.format(cal.time))
        }

        billDateEditText.setOnClickListener {

            Log.d("Clicked", "Interview Date Clicked")

            val dialog = DatePickerDialog(context!!, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
            dialog.datePicker.maxDate = (CalendarHelper.getCurrentDateInMills())+(1000*60*60*24*67)
            dialog.show()
        }
    }

    fun HitApi(){

        val address = input_user_add!!.text.toString().trim()
        val contact = contact_number!!.text.toString().trim()
        val inidate = tiffin_start!!.text.toString().trim()
        val enddate = tiffin_end!!.text.toString().trim()
        val paymode = payment_mode!!.text.toString().trim()

        if(TextUtils.isEmpty(address)){
            input_user_add!!.setError("Please Enter Address")
            input_user_add!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(contact)&& contact.length < 10){
            contact_number!!.setError("Please Enter Contact Number of Length 10")
            contact_number!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if (TextUtils.isEmpty(paymode)){
            payment_mode!!.setError("Please Pick Payment Mode")
            payment_mode!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if (TextUtils.isEmpty(inidate)){
            tiffin_start!!.setError("Please Pick Start Date")
            tiffin_start!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if (TextUtils.isEmpty(enddate)){
            tiffin_end!!.setError("Please Pick End Date")
            tiffin_end!!.requestFocus()
            progressDialog.dismiss()
            return
        }


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
                params["ItemsCount"] = totalp.toString()
                params["AfterCouponTotalPrice"] = "0"
                params["CuponActive"] = "InActive"
                params["CuponPrice"] = "0"
                params["CuponCode"] = ""
                params["TiffinStartDate"] = inidate
                params["TiffinEndDate"] = enddate
                params["TiffinOrderStatus"] = "Accepted"
                params["TiffinDeliveryAddress"] = address
                params["ContactNumber"] = contact
                params["PaymentMethod"] = paymode
                params["PaymentStatus"] = "Pending"

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"TiffinCheckout", params)
            }


            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    val obj = JSONObject(s)
                    val Dat = obj.getJSONObject("Data")
                    val iobj = obj.getJSONObject("ApiStatus")
                    val cod = iobj.getString("StatusCode")
                    val ioh = iobj.getString("StatusMessage")
                    if (cod == "200"){
                        if (paymode == "Online"){
                            var orderid = Dat.getString("OrderID")
                            var TotalPrice = Dat.getString("TotalPrice")
                            val intent  = Intent(context, Payment_Gateway::class.java)
                            intent.putExtra("tx",orderid)
                            intent.putExtra("am",TotalPrice)
                            intent.putExtra("pin","Tiffin")
                            startActivity(intent)

                        Toast.makeText(context,"Message : $ioh", Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                        }
                    }else{
                        Toast.makeText(context,"Error : $ioh", Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                    }

                } catch (e: JSONException) {

                }

            }
        }

        //executing the async task
        val ru = SignUp()
        ru.execute()
    }


}