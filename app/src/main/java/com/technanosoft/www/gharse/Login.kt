package com.technanosoft.www.gharse

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.Manifest
import android.app.ProgressDialog
import android.net.Uri
import android.os.AsyncTask
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.technanosoft.www.gharse.Networking.RequestHandler
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_selection.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class Login : AppCompatActivity() {


    lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sh = SharedPrefManager()
        sh.SharedPrefManager_fun(this)
        if (sh.isLoggedIn("user")){
            if (sh.otview() == "F"){
                val intent = Intent(this,Otp_User::class.java)
                startActivity(intent)
                finish()
            }else{
            val intent = Intent(this,User_Home::class.java)
            intent.putExtra("Tiff","F")
            startActivity(intent)
            finish()
            }
        }
        btn_login_user.setOnClickListener {
            progressDialog = ProgressDialog(this)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Please wait..")
            progressDialog.setIndeterminateDrawable(getDrawable(R.drawable.anim1))
            progressDialog.show()
            //check()

            sendPostRequest()
        }
        reg_jump.setOnClickListener {
            val intent = Intent(this,Registration::class.java)
            startActivity(intent)
            finish()
        }
        skip_regs.setOnClickListener {
            val intent = Intent(this,User_Home::class.java)
            intent.putExtra("Tiff","F")
            startActivity(intent)
            finish()
        }
    }

    private fun check(){
        val emailadd = input_emailaddres!!.getText().toString().trim()
        val password = input_password!!.getText().toString().trim()

        if(TextUtils.isEmpty(emailadd)){
            input_emailaddres!!.setError("Please Enter Email Id")
            input_emailaddres!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(password)){
            input_password!!.setError("Please Enter Password")
            input_password!!.requestFocus()
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
                params["RegistrationType"] = "User"

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
                        ob.SharedPrefManager_fun(this@Login)
                        val use= User(data.getString("RegisterID").toInt(),data.getString("Name"),data.getString("Email"),
                            data.getString("Mobile"),
                            data.getString("Gender"),
                            data.getString("Status"),
                            data.getString("RegistrationType"),
                            data.getString("Latitude")
                            ,data.getString("Longitude"))
                        ob.userLogin(use)
                        ob.otpup("T","")
                        Toast.makeText(this@Login,"Message : $ioh",Toast.LENGTH_LONG).show()
                        val intent = Intent(this@Login,User_Home::class.java)
                        intent.putExtra("Prev","Login")
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@Login,"Error : $ioh",Toast.LENGTH_LONG).show()
                    }

                } catch (e: JSONException) {

                }

            }
        }

        //executing the async task
        val ru = SignUp()
        ru.execute()

    }


    private fun sendPostRequest() {


        val emailadd = input_emailaddres!!.getText().toString().trim()
        val password = input_password!!.getText().toString().trim()

        if(TextUtils.isEmpty(emailadd)){
            input_emailaddres!!.setError("Please Enter Email Id")
            input_emailaddres!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(password)){
            input_password!!.setError("Please Enter Password")
            input_password!!.requestFocus()
            progressDialog.dismiss()
            return
        }

        //val surl :   String = "http://gharse.theposline.in/api/GetVersion"

        progressDialog.dismiss()

        val surl : String =  BuildConfig.Base_URL+"Login";

        val oRequest = object : StringRequest(Request.Method.POST, surl, Response.Listener { response ->


            try {
                val obj = JSONObject(response)
                val iobj = obj.getJSONObject("ApiStatus")
                val cod = iobj.getString("StatusCode")
                val ioh = iobj.getString("StatusMessage")
                if (cod == "200"){
                    val data = obj.getJSONObject("Data")
                    val ob = SharedPrefManager()
                    ob.SharedPrefManager_fun(this@Login)
                    val use= User(data.getString("RegisterID").toInt(),data.getString("Name"),data.getString("Email"),
                        data.getString("Mobile"),
                        data.getString("Gender"),
                        data.getString("Status"),
                        data.getString("RegistrationType"),
                        data.getString("Latitude")
                        ,data.getString("Longitude"))
                    ob.userLogin(use)
                    ob.otpup("T","")
                    Toast.makeText(this@Login,"Message : $ioh",Toast.LENGTH_LONG).show()
                    val intent = Intent(this@Login,User_Home::class.java)
                    intent.putExtra("Prev","Login")
                    intent.putExtra("Tiff","F")

                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this@Login,"Error : $ioh",Toast.LENGTH_LONG).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(applicationContext, "Error: " + (error.toString()), Toast.LENGTH_SHORT).show();
        }) {
            override fun getParams(): Map<String, String> {

                val params = HashMap<String, String>()
                params["Email"] = emailadd
                params["Password"] = password
                params["RegistrationType"] = "User"

                return params
            }
        }

        val requestQ = Volley.newRequestQueue(this)
        requestQ.add(oRequest)



    }


}
