package com.technanosoft.www.gharse.User_Fragment

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.technanosoft.www.gharse.Networking.Connection
import com.technanosoft.www.gharse.Networking.RequestHandler
import com.travijuu.numberpicker.library.NumberPicker
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.bumptech.glide.request.RequestOptions
import com.technanosoft.www.gharse.Models.PricTyp
import com.technanosoft.www.gharse.Models.PriceList
import java.util.ArrayList
import android.widget.AdapterView.OnItemSelectedListener
import cn.pedant.SweetAlert.SweetAlertDialog
import com.technanosoft.www.gharse.*
import com.technanosoft.www.gharse.Fragments.Fragment_My_Recipe
import kotlinx.android.synthetic.main.fragment_recipe_details.*


class Fragment_Recipe_Details:Fragment(){

    private var mContext: Context? = null
    var image_recipe : ImageView? = null
    var recipe_nam : TextView? = null
    var description_txt : TextView? = null
    var linear_layout_view_cart : LinearLayout? = null
    var Sho : ImageButton? = null
    var Hid : ImageButton? = null
    var price_Typ : MaterialBetterSpinner? = null
    var price_type_c : TextView? = null
    var quan : NumberPicker? = null
    var total : TextView? = null
    var cart : CardView? = null
    private var RecipeID: String = ""
    private var RegisterID: String = ""
    private var PageIndex: String = ""
    private var RecipeName: String = ""
    private var RecipeImage: String = ""
    private var RecipeType: String = ""
    private var RecipeDescriptio: String = ""
    private var FromTime: String = ""
    private var ToTime: String = ""
    var prictyp = arrayOf("","","")
    var posts: MutableList<PriceList> = ArrayList()
    var adapte: ArrayAdapter<String>? = null
    var i = 0
    var to = 1
    var sub = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_details, container, false)
        image_recipe = view.findViewById<View>(R.id.img_recipe) as ImageView
        recipe_nam = view.findViewById<View>(R.id.recipe_name) as TextView
        description_txt = view.findViewById<View>(R.id.description_text) as TextView
        Sho = view.findViewById<View>(R.id.show) as ImageButton
        Hid = view.findViewById<View>(R.id.hide) as ImageButton
        price_Typ = view.findViewById<View>(R.id.price_Type) as MaterialBetterSpinner
        price_type_c = view.findViewById<View>(R.id.price_type_cas) as TextView
        quan = view.findViewById<View>(R.id.quantity) as NumberPicker
        total = view.findViewById<View>(R.id.total_price) as TextView
        cart = view.findViewById<View>(R.id.add_to_cart) as CardView

        linear_layout_view_cart = view.findViewById<View>(R.id.viewcart_layout) as LinearLayout


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /*val sh = SharedPrefManager()
        sh.SharedPrefManager_fun(context!!)
        val hio = sh.getWeekID().weekid
        if (hio != null){
            btn_add.visibility = View.VISIBLE
        }else{
            cart!!.visibility = View.VISIBLE
        }*/

        val b = arguments
        val s = (b!!.getString("Data")).toString()
        val br = b!!.getString("Tiff")
        if (br == "T"){
            btn_add.visibility = View.VISIBLE
        }else{
            cart!!.visibility = View.VISIBLE
        }
        val con = Connection()
        if (con.isNetworkAvailable(context!!)){
            check(s!!)
        }
        Sho!!.setOnClickListener {
            description_txt!!.maxLines = 100
            Sho!!.visibility = View.GONE
            Hid!!.visibility = View.VISIBLE
        }
        Hid!!.setOnClickListener {
            description_txt!!.maxLines = 5
            Sho!!.visibility = View.VISIBLE
            Hid!!.visibility = View.GONE
        }
        linear_layout_view_cart!!.setOnClickListener{

            mContext = view?.context
           val  fragMyorder = Fragment_My_Orders()

            switchContent(R.id.user_home_frag, fragMyorder)



            /*



            val mFragment = Fragment_My_Recipe()
            val mBundle = Bundle()
            mBundle.putString("Data",mItemSelected)
            mBundle.putString("Tiff",Tiff_Status)
            mFragment.setArguments(mBundle)
            switchContent(R.id.user_home_frag, mFragment)
*/


        }




        adapte = ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, prictyp)
        price_Typ!!.setAdapter(adapte)
        price_Typ!!.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "Full"){
                    i = 0
                    while (prictyp.get(i) !="Full"){
                        i++
                    }
                    val data = posts[i]
                    price_type_c!!.text = data.Price
                    val price = (data.Price).toInt()
                    sub = to*price
                    total!!.text = sub.toString()
                }else if (s.toString() =="Small"){
                    i = 0
                    while (prictyp.get(i) !="Small"){
                        i++
                    }
                    val data = posts[i]
                    price_type_c!!.text = data.Price
                    val price = (data.Price).toInt()
                    sub = to*price
                    total!!.text = sub.toString()

                }else{
                    i = 0
                    while (prictyp.get(i) !="Half"){
                        i++
                    }
                    val data = posts[i]
                    price_type_c!!.text = data.Price
                    val price = (data.Price).toInt()
                    sub = to*price
                    total!!.text = sub.toString()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        quan!!.setValueChangedListener { value, action ->
            to = value
            val data = posts[i]
            val price = (data.Price).toInt()
            sub = to*price
            total!!.text = sub.toString()
        }
        cart!!.setOnClickListener{
            if(sub == 0){
                Toast.makeText(context,"Please Select Price Type",Toast.LENGTH_SHORT).show()
                linear_layout_view_cart?.visibility = View.GONE

            }else{
            Cart_check()
            }
        }
        btn_add.setOnClickListener {
            Tiffin_Add()
        }
    }

    fun switchContent(id: Int, fragment: Fragment)
    {
        if (mContext == null)
            return
        if (mContext is User_Home) {
            val mainActivity = mContext as User_Home
            mainActivity.switchContent(id,fragment )
        }

    }


    private fun Cart_check(){

        val sha = SharedPrefManager()
        sha.SharedPrefManager_fun(context!!)
        if(!sha.isLoggedIn("user")){
            SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Not Registered")
                .setContentText("Please Register First")
                .setConfirmClickListener {
                    val intent = Intent(context,Registration::class.java)
                    startActivity(intent)
                }
                .show()
            return
        }

        val user = sha.getUser()
        val data = posts[i]
        if(to == 0) {
            Toast.makeText(context,"Plese Add Quantity",Toast.LENGTH_LONG).show()
            return
        }

        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["CartID"] = "0"
                params["RegisterID"] = (user.RegisterID).toString()
                        params["RecipeID"] = data.RecipeID
                        params["PriceID"] = data.PriceID
                        params["Price"] = data.Price
                        params["SubTotal"] = sub.toString()
                        params["ItemCount"] = to.toString()

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"AddToCart", params)
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
                        Toast.makeText(activity, "Added : $message", Toast.LENGTH_LONG).show()

                        linear_layout_view_cart?.visibility = View.VISIBLE


                    }
                } catch (e: JSONException) {
                    Toast.makeText(activity, "Error : $e", Toast.LENGTH_LONG).show()
                }

            }
        }
        //executing the async task
        val ru = SignUp()
        ru.execute()

    }

    private fun Tiffin_Add(){
        val sha = SharedPrefManager()
        sha.SharedPrefManager_fun(context!!)
        val week = sha.getWeekID()
        val user = sha.getUser()
        val data = posts[i]
        if(to == 0){
            Toast.makeText(context,"Plese Add Quantity",Toast.LENGTH_LONG).show()
            return
        }

        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["WeekDayID"] = week.weekid
                params["RegisterID"] = user.RegisterID.toString()
                params["RecipeID"] = data.RecipeID
                params["PriceID"] = data.PriceID
                params["Price"] = data.Price
                params["PriceType"] = data.PriceType
                params["ItemQuantity"] = to.toString()
                params["SubTotal"] = sub.toString()
                params["FoodTime"] = week.foodtime

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"AddToTiffinCart", params)
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
                        sha.delWeek()
                        Toast.makeText(activity, "Added : $message", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(activity, "Error : $e", Toast.LENGTH_LONG).show()
                }

            }
        }
        //executing the async task
        val ru = SignUp()
        ru.execute()

    }


    private fun check(s :String){


        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["RecipeID"] = s

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"GetRecipeDetailsByID", params)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                //progress!!.visibility = View.GONE
                try {
                    val obj = JSONObject(s)
                    val array = obj.getJSONArray("Data")
                        val ob = array.getJSONObject(0)
                         RecipeID = ob.getString("RecipeID")
                         RegisterID = ob.getString("RegisterID")
                         PageIndex = ob.getString("PageIndex")
                         RecipeName = ob.getString("RecipeName")
                         RecipeImage = ob.getString("RecipeImage")
                         RecipeType = ob.getString("RecipeType")
                         RecipeDescriptio = ob.getString("RecipeDescription")
                         FromTime = (ob.getString("FromTime")).toString()
                         ToTime = (ob.getString("ToTime")).toString()
                    val options = RequestOptions()
                    options.centerCrop()
                    Glide.with(context).load("http://gharseapp.confertrack.com/uploads/Recipe/"+RecipeImage).apply(options).into(image_recipe)
                    recipe_nam!!.text = RecipeName
                    description_txt!!.text = RecipeDescriptio
                    val arr = obj.getJSONArray("PriceList")

                    for (i in 0 until arr.length()){
                        val ob_ = arr.getJSONObject(i)
                        val priid = ob_.getString("PriceID")
                        val recid = ob_.getString("RecipeID")
                        val prity = ob_.getString("PriceType")
                        val Pri = ob_.getString("Price")
                        val data = PriceList(priid,recid,prity,Pri)
                        posts.add(data)
                        if (prity != null){
                            prictyp[i]= prity
                        }else{
                            prictyp[i]="-"
                        }
                    }
                    adapte!!.notifyDataSetChanged()
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

