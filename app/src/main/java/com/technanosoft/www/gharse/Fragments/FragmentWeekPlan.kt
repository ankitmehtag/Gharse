package com.technanosoft.www.gharse.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.technanosoft.www.gharse.R
import android.widget.TimePicker
import android.content.DialogInterface
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.AsyncTask
import android.text.TextUtils
import android.widget.PopupWindow
import cn.pedant.SweetAlert.SweetAlertDialog
import com.technanosoft.www.gharse.BuildConfig
import com.technanosoft.www.gharse.Networking.RequestHandler
import kotlinx.android.synthetic.main.fragment_week_plan.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class FragmentWeekPlan:Fragment(){


    lateinit var cont:Context
    private var mon: String? = null
    private var tue: String? = null
    private var wed: String? = null
    private var thu: String? = null
    private var fri: String? = null
    private var sat: String? = null
    private var sun: String? = null
    private var tomon: String? = null
    private var totue: String? = null
    private var towed: String? = null
    private var tothu: String? = null
    private var tofri: String? = null
    private var tosat: String? = null
    private var tosun: String? = null
    private var monbtnf:Button? = null
    private var tuebtnf:Button? = null
    private var wedbtnf:Button? = null
    private var thubtnf:Button? = null
    private var fribtnf:Button? = null
    private var satbtnf:Button? = null
    private var sunbtnf:Button? = null
    private var monbtnt:Button? = null
    private var tuebtnt:Button? = null
    private var wedbtnt:Button? = null
    private var thubtnt:Button? = null
    private var fribtnt:Button? = null
    private var satbtnt:Button? = null
    private var sunbtnt:Button? = null
    private var update:Button? = null
    lateinit var progressDialog : ProgressDialog
    var it = ""

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        cont = context!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_week_plan, container, false)
        monbtnf = view.findViewById<View>(R.id.mon_from) as Button
        tuebtnf = view.findViewById<View>(R.id.tue_from) as Button
        wedbtnf = view.findViewById<View>(R.id.wed_from) as Button
        thubtnf = view.findViewById<View>(R.id.thu_from) as Button
        fribtnf = view.findViewById<View>(R.id.fri_from) as Button
        satbtnf = view.findViewById<View>(R.id.sat_from) as Button
        sunbtnf = view.findViewById<View>(R.id.sun_from) as Button
        monbtnt = view.findViewById<View>(R.id.mon_to) as Button
        tuebtnt = view.findViewById<View>(R.id.tue_to) as Button
        wedbtnt = view.findViewById<View>(R.id.wed_to) as Button
        thubtnt = view.findViewById<View>(R.id.thu_to) as Button
        fribtnt = view.findViewById<View>(R.id.fri_to) as Button
        satbtnt = view.findViewById<View>(R.id.sat_to) as Button
        sunbtnt = view.findViewById<View>(R.id.sun_to) as Button
        update = view.findViewById<View>(R.id.update_week) as Button
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        monbtnf!!.setOnClickListener {View->
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                mon = t+min

            }),hour,minute,false)
            tpd.show()
        }
        monbtnt!!.setOnClickListener {
            mon_from_txt.text = mon
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                tomon = t+min

            }),hour,minute,false)
            tpd.show()
        }
        tuebtnf!!.setOnClickListener{View->
            mon_to_txt.text = tomon
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                tue = t+min

            }),hour,minute,false)
            tpd.show()
        }
        tuebtnt!!.setOnClickListener { View->
            tue_from_txt.text = tue
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                totue = t+min

            }),hour,minute,false)
            tpd.show()
        }
        wedbtnf!!.setOnClickListener {View->
            tue_to_txt.text = totue
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                wed = t+min

            }),hour,minute,false)
            tpd.show()
        }
        wedbtnt!!.setOnClickListener { View->
            wed_from_txt.text = wed
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                towed = t+min

            }),hour,minute,false)
            tpd.show()
        }
        thubtnf!!.setOnClickListener {View->
            wed_to_txt.text = towed
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                thu = t+min

            }),hour,minute,false)
            tpd.show()

        }
        thubtnt!!.setOnClickListener {View->
            thu_from_txt.text = thu
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                tothu = t+min

            }),hour,minute,false)
            tpd.show()
        }
        fribtnf!!.setOnClickListener {View->
            thu_to_txt.text = tothu
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                fri = t+min

            }),hour,minute,false)
            tpd.show()
        }
        fribtnt!!.setOnClickListener { View->
            fri_from_txt.text = fri
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                tofri = t+min

            }),hour,minute,false)
            tpd.show()
        }
        satbtnf!!.setOnClickListener {View->
            fri_to_txt.text = tofri
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                sat = t+min

            }),hour,minute,false)
            tpd.show()
        }
        satbtnt!!.setOnClickListener { View->
            sat_from_txt.text = sat
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                tosat = t+min

            }),hour,minute,false)
            tpd.show()
        }
        sunbtnf!!.setOnClickListener { View->
            sat_to_txt.text = tosat
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                sun = t+min

            }),hour,minute,false)
            tpd.show() }
        sunbtnt!!.setOnClickListener {View->
            sun_from_txt.text = sun
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, min ->
                val t = h.toString()+":"
                tosun = t+min

            }),hour,minute,false)
            tpd.show()

        }
        update!!.setOnClickListener {
            sun_to_txt.text = tosun
            progressDialog = ProgressDialog(activity)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Please wait..")
            progressDialog.setIndeterminateDrawable(resources.getDrawable(R.drawable.anim1))
            progressDialog.show()
            check()
        }

    }

    private fun check(){
        var monday_from = ""
        var monday_to = ""
        var tuesday_from = ""
        var tuesday_to = ""
        var wednesday_from = ""
        var wednesday_to = ""
        var thursday_from = ""
        var thursday_to = ""
        var friday_from = ""
        var friday_to = ""
        var saturday_from = ""
        var saturday_to = ""
        var sunday_from = ""
        var sunday_to = ""

        if(mon != null){
            monday_from = mon!!
        }
        if(tomon != null){
            monday_to = tomon!!
        }
        if(tue != null){
            tuesday_from = tue!!
        }
        if(totue != null){
            tuesday_to = totue!!
        }
        if(wed != null){
            wednesday_from = wed!!
        }
        if(towed != null){
            wednesday_to = towed!!
        }
        if(thu != null){
            thursday_from = thu!!
        }
        if(tothu != null){
            thursday_to = tothu!!
        }
        if(fri != null){
            friday_from = fri!!
        }
        if(tofri != null){
            friday_to = tofri!!
        }
        if(sat != null){
            saturday_from = sat!!
        }
        if(tosat != null){
            saturday_to = tosat!!
        }
        if(sun != null){
            sunday_from = sun!!
        }
        if(tosun != null){
            sunday_to = tosun!!
        }


        /*if(TextUtils.isEmpty(monday_from)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(monday_to)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(tuesday_from)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(tuesday_to)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(wednesday_from)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(wednesday_to)){
            progressDialog.dismiss()
            return
        }else if(TextUtils.isEmpty(thursday_from)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(thursday_to)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(friday_from)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(friday_to)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(saturday_from)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(saturday_to)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(sunday_from)){
            progressDialog.dismiss()
            return
        }
        else if(TextUtils.isEmpty(sunday_to)){
            progressDialog.dismiss()
            return
        }*/



        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["RegisterID"] = "1"
                params["MonFrom"] = monday_from
                        params["MonTo"] = monday_to
                        params["TueFrom"] = tuesday_from
                        params["TueTo"] = tuesday_to
                        params["WedFrom"] = wednesday_from
                        params["WedTo"] = wednesday_to
                        params["ThuFrom"] = thursday_from
                        params["ThuTo"] = thursday_to
                        params["FriFrom"] = friday_from
                        params["FriTo"] = friday_to
                        params["SatFrom"] = saturday_from
                        params["SatTo"] = saturday_to
                        params["SunFrom"] = sunday_from
                        params["SunTo"] = sunday_to

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"InsertUpdateWeekPlan", params)
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
                        SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Week Plan")
                            .setContentText("Updated")
                            .show()
                        Toast.makeText(activity,"Message : $ioh",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(activity,"Error : $ioh",Toast.LENGTH_LONG).show()
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