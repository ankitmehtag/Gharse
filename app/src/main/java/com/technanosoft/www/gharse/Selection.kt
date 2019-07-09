package com.technanosoft.www.gharse

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.technanosoft.www.gharse.Networking.RequestHandler
import kotlinx.android.synthetic.main.activity_selection.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap
import com.technanosoft.www.gharse.Networking.AppController
import com.android.volley.VolleyLog
import com.android.volley.VolleyError
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class Selection : AppCompatActivity() {

    private var down: LinearLayout? = null
    private var up: LinearLayout? = null
    private var uptoa: Animation? = null
    private var downtoa: Animation? = null
    lateinit var progressDialog: ProgressDialog
    private val ver = "1.1"
    private var pDialog: ProgressDialog? = null

    val urlVersion: String = "http://gharse.theposline.in/api/GetVersion";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_selection)
        uptoa = AnimationUtils.loadAnimation(this, R.anim.uptodown)
        downtoa = AnimationUtils.loadAnimation(this, R.anim.downtoup)
        down = findViewById<View>(R.id.downui) as LinearLayout
        up = findViewById<View>(R.id.upui) as LinearLayout
        user_btn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            //val intent = Intent(this,Otp_User::class.java)
            startActivity(intent)
        }
        vendor_btn.setOnClickListener {
            val intent = Intent(this, VendorLogin::class.java)
            startActivity(intent)
        }
        up!!.animation = uptoa
        down!!.animation = downtoa

        user_btn.isEnabled = false
        vendor_btn.isEnabled = false

        progressDialog = ProgressDialog(this)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Please wait..")
        progressDialog.setIndeterminateDrawable(getDrawable(R.drawable.anim1))
        progressDialog.show()
        //  check()
        sendPostRequest()
      }

/*

    fun check() {
        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL + "GetVersion", params)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                progressDialog.dismiss()
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    val obj = JSONObject(s)
                    val iobj = obj.getJSONObject("ApiStatus")
                    val cod = iobj.getString("StatusCode")
                    Log.d("app_data", cod.toString())
                    val ioh = iobj.getString("StatusMessage")
                    if (cod == "200") {
                        val dat = obj.getJSONObject("Data")
                        val Version = dat.getString("VersionValue")
                        if (Version == ver) {
                            user_btn.isEnabled = true
                            vendor_btn.isEnabled = true
                        } else {
                            Toast.makeText(this@Selection, "Please Update your App", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@Selection, "Error : $ioh", Toast.LENGTH_LONG).show()
                    }

                } catch (e: JSONException) {

                }

            }
        }

        //executing the async task
        val ru = SignUp()
        ru.execute()
    }
*/



    private fun sendPostRequest() {

        val surl :   String = "http://gharse.theposline.in/api/GetVersion"

        progressDialog.dismiss()


        val oRequest = object : StringRequest(Request.Method.POST, surl, Response.Listener { response ->
            try {


                Log.d("datais",response.toString())

                val obj = JSONObject(response)
                val iobj = obj.getJSONObject("ApiStatus")
                val cod = iobj.getString("StatusCode")
                Log.d("app_data", cod.toString())
                val ioh = iobj.getString("StatusMessage")
                if (cod == "200") {
                    val dat = obj.getJSONObject("Data")
                    val Version = dat.getString("VersionValue")
                    if (Version == ver) {
                        user_btn.isEnabled = true
                        vendor_btn.isEnabled = true
                    } else {
                        Toast.makeText(this@Selection, "Please Update your App", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@Selection, "Error : $ioh", Toast.LENGTH_LONG).show()
                }







            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(applicationContext, "Please Check Your Internet Connection ", Toast.LENGTH_SHORT).show();
        }) {
            override fun getParams(): Map<String, String> {

                val params = HashMap<String, String>()
                params["PageIndex"] = "2"
                params["PageSize"] = "10"
                return params
            }
        }

        val requestQ = Volley.newRequestQueue(this)
        requestQ.add(oRequest)



    }

}


