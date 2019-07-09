package com.technanosoft.www.gharse.Fragments

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import com.technanosoft.www.gharse.Adapters.Vendor.RecipeListing
import com.technanosoft.www.gharse.BuildConfig
import com.technanosoft.www.gharse.Models.Recipe
import com.technanosoft.www.gharse.Models.Vendor_Models.Recipe_Vendor
import com.technanosoft.www.gharse.Networking.Connection
import com.technanosoft.www.gharse.Networking.RequestHandler
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.SharedPrefManager
import kotlinx.android.synthetic.main.loadmore_refresh.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class Fragment_My_Recipe:Fragment(){

    private var listing_rec : RecyclerView? = null
    var posts: MutableList<Recipe_Vendor?> = ArrayList()
    private lateinit var adapter: RecipeListing
    private var progress: ProgressBar? = null
    lateinit var manager: LinearLayoutManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_my_recipe, container, false)
        listing_rec = view.findViewById<View>(R.id.listing_my_recipe) as RecyclerView
        progress = view.findViewById<View>(R.id.progessBar) as ProgressBar
        manager = LinearLayoutManager(context)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = RecipeListing(posts)
        listing_rec?.layoutManager = manager!!
        listing_rec?.adapter = adapter
        val ob = Connection()
        if (ob.isNetworkAvailable(context!!)){
            check()
        }
        else{
            Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show()
        }

    }
    private fun check(){

        val sh = SharedPrefManager()
        sh.SharedPrefManager_fun(context!!)
        val user = sh.getUser()

        class SignUp : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                var params: MutableMap<String, String>? = null
                params = HashMap()
                //params["RegisterID"] = user.RegisterID.toString()

                params["RegisterID"] = "4"
                    //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"GetAllVendorRecipe", params)
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
                        var RecipeID = ob.getString("RecipeID")
                        var RegisterID = ob.getString("RegisterID")
                        var PageIndex = ob.getString("PageIndex")
                        var PageSize = ob.getString("PageSize")
                        var Price =ob.getString("Price")
                        var PriceType = ob.getString("PriceType")
                        var RecipeName = ob.getString("RecipeName")

                       //   var RecipeName = ob.getString("RecipeName")


                        var RecipeImage = ob.getString("RecipeImage")
                        var RecipeType = ob.getString("RecipeType")
                        var RecipeDescriptio = ob.getString("RecipeDescription")
                        var FromTime = (ob.getString("FromTime")).toString()
                        var ToTime = (ob.getString("ToTime")).toString()
                        var Status = ob.getString("Status")
                        val dataitem = Recipe_Vendor(RecipeID,RegisterID,PageIndex,PageSize,Price,PriceType,RecipeName,RecipeImage,RecipeType,RecipeDescriptio,FromTime,ToTime,Status,"2")
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