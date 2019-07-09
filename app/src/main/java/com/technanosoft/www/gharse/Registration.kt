package com.technanosoft.www.gharse


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.AsyncTask
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.ArrayAdapter
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.technanosoft.www.gharse.Networking.RequestHandler
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import kotlinx.android.synthetic.main.activity_selection.*
import org.json.JSONException
import org.json.JSONObject


class Registration : AppCompatActivity() {

    protected var mLastLocation: Location? = null
    private var lat:String? = null
    private var long:String? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    lateinit var progressDialog : ProgressDialog
    var materialBetterSpinner: MaterialBetterSpinner? = null

    var SPINNER_DATA = arrayOf("Male", "Female")

    val lat2  = 77.3144245
    val long2 = 28.4216698

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        materialBetterSpinner = findViewById<View>(R.id.material_spinner2) as MaterialBetterSpinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA)
        materialBetterSpinner!!.setAdapter(adapter)
        login_user_btn.setOnClickListener {
            val int = Intent(this,Login::class.java)
            startActivity(int)
        }
        btn_signup_user.setOnClickListener {
            progressDialog = ProgressDialog(this)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Please wait..")
            progressDialog.setIndeterminateDrawable(getDrawable(R.drawable.anim1))
            progressDialog.show()
            //getLastLocation()
            //check()
            sendPostRequest()

        }
        skip_reg.setOnClickListener {
            val intent = Intent(this,User_Home::class.java)
            intent.putExtra("Tiff","F")
            startActivity(intent)
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result
                    lat = mLastLocation!!.latitude.toString()
                    long = mLastLocation!!.longitude.toString()
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    showMessage(getString(R.string.no_location_detected))
                }
            }
    }

    private fun showMessage(text: String) {
        AlertDialog.Builder(this)
            .setTitle("Warning")
            .setMessage(text+"\nPlease Open Location")
            .setCancelable(false)
            .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which ->
            }).show()
    }


    private fun showSnackbar(mainTextStringId: Int, actionStringId: Int,
                             listener: View.OnClickListener) {

        Toast.makeText(this@Registration, getString(mainTextStringId), Toast.LENGTH_LONG).show()
    }




    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@Registration,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                View.OnClickListener {
                    // Request permission
                    startLocationPermissionRequest()
                })

        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest()
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation()
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                    View.OnClickListener {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package",
                            BuildConfig.APPLICATION_ID, null)
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    })
            }
        }
    }
    private fun check(){
        val namee = input_name_user!!.getText().toString().trim()
        val emailadd = input_emailaddress_user!!.getText().toString().trim()
        val mobile = input_user_mobile!!.getText().toString().trim()
        val gender  = materialBetterSpinner!!.getText().toString().trim()
        val password = input_user_pass!!.getText().toString().trim()
        var lattitu = lat
        var longitude = long

        if(lat != null && long != null){
            val dis = distance(lat!!.toDouble(),long!!.toDouble(),lat2,long2)
            getLastLocation()
            if(dis<=10){
                Toast.makeText(this,"We Provide services in 10Km area",Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
                return
            }
        }

        if(TextUtils.isEmpty(namee)){
            input_name_user!!.setError("Please Enter Name")
            input_name_user!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(emailadd)){
            input_emailaddress_user!!.setError("Please Enter Email Id")
            input_emailaddress_user!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(mobile)){
            input_user_mobile!!.setError("Please Enter Mobile Number")
            input_user_mobile!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(gender)){
            materialBetterSpinner!!.setError("Please Select Gender")
            materialBetterSpinner!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(password)){
            input_user_pass!!.setError("Please Create Password")
            input_user_pass!!.requestFocus()
            progressDialog.dismiss()
            return
        }
       /* else if (TextUtils.isEmpty(lat)){
            lat = "Lat"
            //progressDialog.dismiss()
            //getLastLocation()
            //return
        }
        else if (TextUtils.isEmpty(long)){
            long = "Long"
            //progressDialog.dismiss()
            //getLastLocation()
            //return
        }*/
        if(lat == null && long == null){
            lattitu = "Lat"
            longitude = "Long"
        }


        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["Name"] = namee
                params["Email"] = emailadd
                params["Mobile"] = mobile
                params["Gender"] = gender
                params["Password"] = password
                params["Status"] ="Active"
                params["RegistrationType"] = "User"
                params["Latitude"] = lattitu!!
                params["Longitude"] = longitude!!

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"Registration", params)
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
                        ob.SharedPrefManager_fun(this@Registration)
                        val use= User(data.getString("RegisterID").toInt(),data.getString("Name"),data.getString("Email"),
                            data.getString("Mobile"),
                            data.getString("Gender"),
                            data.getString("Status"),
                            data.getString("RegistrationType"),
                            data.getString("Latitude")
                            ,data.getString("Longitude"))
                        ob.userLogin(use)
                        ob.otpup("F",password)
                        val intent = Intent(this@Registration,Otp_User::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@Registration,"Registration : $ioh",Toast.LENGTH_LONG).show()
                    }else{
                        SweetAlertDialog(this@Registration,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText(ioh)
                            .show()
                        Toast.makeText(this@Registration,"Error : $ioh",Toast.LENGTH_LONG).show()
                    }

                } catch (e: JSONException) {

                }

            }
        }

        //executing the async task
        val ru = SignUp()
        ru.execute()

    }

    companion object {

        private val TAG = "LocationProvider"

        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }


    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta)))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60.0 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }



    private fun sendPostRequest() {

        val surl :   String = BuildConfig.Base_URL+"Registration"

        progressDialog.dismiss()

        val namee = input_name_user!!.getText().toString().trim()
        val emailadd = input_emailaddress_user!!.getText().toString().trim()
        val mobile = input_user_mobile!!.getText().toString().trim()
        val gender  = materialBetterSpinner!!.getText().toString().trim()
        val password = input_user_pass!!.getText().toString().trim()
        var lattitu = lat
        var longitude = long

        if(lat != null && long != null){
            val dis = distance(lat!!.toDouble(),long!!.toDouble(),lat2,long2)
            getLastLocation()
            if(dis<=10){
                Toast.makeText(this,"We Provide services in 10Km area",Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
                return
            }
        }

        if(TextUtils.isEmpty(namee)){
            input_name_user!!.setError("Please Enter Name")
            input_name_user!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(emailadd)){
            input_emailaddress_user!!.setError("Please Enter Email Id")
            input_emailaddress_user!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(mobile)){
            input_user_mobile!!.setError("Please Enter Mobile Number")
            input_user_mobile!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(gender)){
            materialBetterSpinner!!.setError("Please Select Gender")
            materialBetterSpinner!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(password)){
            input_user_pass!!.setError("Please Create Password")
            input_user_pass!!.requestFocus()
            progressDialog.dismiss()
            return
        }
        /* else if (TextUtils.isEmpty(lat)){
             lat = "Lat"
             //progressDialog.dismiss()
             //getLastLocation()
             //return
         }
         else if (TextUtils.isEmpty(long)){
             long = "Long"
             //progressDialog.dismiss()
             //getLastLocation()
             //return
         }*/
        if(lat == null && long == null){
            lattitu = "Lat"
            longitude = "Long"
        }



        val oRequest = object : StringRequest(Request.Method.POST, surl, Response.Listener { response ->
            try {
                val obj = JSONObject(response)
                val iobj = obj.getJSONObject("ApiStatus")
                val cod = iobj.getString("StatusCode")
                val ioh = iobj.getString("StatusMessage")
                if (cod == "200"){
                    val data = obj.getJSONObject("Data")
                    val ob = SharedPrefManager()
                    ob.SharedPrefManager_fun(this@Registration)
                    val use= User(data.getString("RegisterID").toInt(),data.getString("Name"),data.getString("Email"),
                        data.getString("Mobile"),
                        data.getString("Gender"),
                        data.getString("Status"),
                        data.getString("RegistrationType"),
                        data.getString("Latitude")
                        ,data.getString("Longitude"))
                    ob.userLogin(use)
                    ob.otpup("F",password)
                    val intent = Intent(this@Registration,Otp_User::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@Registration,"Registration : $ioh",Toast.LENGTH_LONG).show()
                }else{
                    SweetAlertDialog(this@Registration,SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText(ioh)
                        .show()
                    Toast.makeText(this@Registration,"Error : $ioh",Toast.LENGTH_LONG).show()
                }

            } catch (e: JSONException) {

            }

        }, Response.ErrorListener { error ->
            Toast.makeText(applicationContext, "Please Check Your Internet Connection ", Toast.LENGTH_SHORT).show();
        }) {
            override fun getParams(): Map<String, String> {

                val params = HashMap<String, String>()
                params["Name"] = namee
                params["Email"] = emailadd
                params["Mobile"] = mobile
                params["Gender"] = gender
                params["Password"] = password
                params["Status"] ="Active"
                params["RegistrationType"] = "User"
                params["Latitude"] = lattitu!!
                params["Longitude"] = longitude!!

                return params

            }
        }

        val requestQ = Volley.newRequestQueue(this)
        requestQ.add(oRequest)



    }



}