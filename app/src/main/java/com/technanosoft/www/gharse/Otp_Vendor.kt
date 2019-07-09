package com.technanosoft.www.gharse

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.technanosoft.www.gharse.Networking.RequestHandler
import kotlinx.android.synthetic.main.activity_otp__vendor.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class Otp_Vendor : AppCompatActivity() {


    lateinit var progressDialog : ProgressDialog
    var regID:String? = null
    val sh = SharedPrefManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp__vendor)
        sh.SharedPrefManager_fun(this)
        val ui = sh.getUser()
        val lasttwo = ui.Mobile.takeLast(2)
        regID = ui.RegisterID.toString()
        last_num_ven.text = lasttwo
        validate_ven.setOnClickListener {
            progressDialog = ProgressDialog(this)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Please wait..")
            progressDialog.setIndeterminateDrawable(getDrawable(R.drawable.anim1))
            progressDialog.show()
            checkOtp()
        }
        resendot.setOnClickListener {
            progressDialog = ProgressDialog(this)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Please wait..")
            progressDialog.setIndeterminateDrawable(getDrawable(R.drawable.anim1))
            progressDialog.show()
            resendOtp()

        }
    }
    fun resendOtp(){

        val sh = SharedPrefManager()
        sh.SharedPrefManager_fun(this@Otp_Vendor)
        val useer = sh.getUser()
        val pass = sh.password()

        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["Email"] = useer.Email
                params["Mobile"] = useer.Mobile
                params["Password"] = pass

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"ReSendOTP", params)
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
                        val sh = SharedPrefManager()
                        sh.SharedPrefManager_fun(this@Otp_Vendor)
                        sh.otpup("F",pass)
                        progressDialog.dismiss()
                        Toast.makeText(this@Otp_Vendor, "Validate : $message", Toast.LENGTH_LONG).show()

                    }
                } catch (e: JSONException) {
                    Toast.makeText(this@Otp_Vendor, "Error : $e", Toast.LENGTH_LONG).show()
                }

            }
        }
        //executing the async task
        val ru = SignUp()
        ru.execute()

    }



    fun checkOtp(){
        val otp = otp_view_ven!!.text.toString().trim()

        if(TextUtils.isEmpty(otp)){
            otp_view_ven!!.setError("Please Enter OTP")
            otp_view_ven!!.requestFocus()
            progressDialog.dismiss()
            return
        }


        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["RegisterID"] = regID.toString()
                params["OTP"] = otp

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"VerifyOTP", params)
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
                        val sh = SharedPrefManager()
                        sh.SharedPrefManager_fun(this@Otp_Vendor)
                        sh.otpup("T","")
                        Toast.makeText(this@Otp_Vendor, "Validate : $message", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@Otp_Vendor,Vendor_Home::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this@Otp_Vendor, "Error : $e", Toast.LENGTH_LONG).show()
                }

            }
        }
        //executing the async task
        val ru = SignUp()
        ru.execute()
    }
}
