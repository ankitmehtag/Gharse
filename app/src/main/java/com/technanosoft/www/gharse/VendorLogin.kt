package com.technanosoft.www.gharse

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.technanosoft.www.gharse.Networking.RequestHandler
import kotlinx.android.synthetic.main.activity_vendor_login.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class VendorLogin : AppCompatActivity() {

    lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_login)
        val sh = SharedPrefManager()
        sh.SharedPrefManager_fun(this)
        if (sh.isLoggedIn("vendor")){
            if (sh.otview() == "F"){
                val intent = Intent(this,Otp_Vendor::class.java)
                startActivity(intent)
                finish()
            }else{
            val intent = Intent(this,Vendor_Home::class.java)
            startActivity(intent)
            finish()
            }
        }
        btn_login_vendor.setOnClickListener {
            progressDialog = ProgressDialog(this)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Please wait..")
            progressDialog.setIndeterminateDrawable(getDrawable(R.drawable.anim1))
            progressDialog.show()
            check()
        }
        vendor_reg.setOnClickListener {
            val intent = Intent(this,Registration_Vendor::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun check(){
        val emailadd = input_emailaddress_vendor!!.getText().toString().trim()
        val password = input_password_vendor!!.getText().toString().trim()

        if(TextUtils.isEmpty(emailadd)){
            input_emailaddress_vendor!!.setError("Please Enter Email Id")
            input_emailaddress_vendor!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(password)){
            input_password_vendor!!.setError("Please Enter Password")
            input_password_vendor!!.requestFocus()
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
                params["Email"] = emailadd
                params["Password"] = password
                params["RegistrationType"] = "Vendor"

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"Login", params)
            }


            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                progressDialog.dismiss()
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    val obj = JSONObject(s)
                    val iobj = obj.getJSONObject("ApiStatus")
                    val cod = iobj.getString("StatusCode")
                    val ioh = iobj.getString("StatusMessage")
                    if (cod == "200"){
                        val data = obj.getJSONObject("Data")
                        val ob = SharedPrefManager()
                        ob.SharedPrefManager_fun(this@VendorLogin)
                        val use= User(data.getString("RegisterID").toInt(),data.getString("Name"),data.getString("Email"),
                            data.getString("Mobile"),
                            data.getString("Gender"),
                            data.getString("Status"),
                            data.getString("RegistrationType"),
                            data.getString("Latitude")
                            ,data.getString("Longitude"))
                        ob.userLogin(use)
                        ob.otpup("T","")
                        Toast.makeText(this@VendorLogin,"Message : $ioh",Toast.LENGTH_LONG).show()
                        val intent = Intent(this@VendorLogin,Vendor_Home::class.java)
                        intent.putExtra("Prev","Vendor_Registration")
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@VendorLogin,"Error : $ioh",Toast.LENGTH_LONG).show()
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
