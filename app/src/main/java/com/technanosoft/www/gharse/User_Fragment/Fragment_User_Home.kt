package com.technanosoft.www.gharse.User_Fragment

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import com.technanosoft.www.gharse.Adapters.ListingAdapter_user
import com.technanosoft.www.gharse.Models.Recipe
import com.technanosoft.www.gharse.Networking.Connection
import com.technanosoft.www.gharse.Networking.RequestHandler
import org.json.JSONException
import org.json.JSONObject
import android.app.SearchManager
import android.content.Context
import android.util.Log
import android.view.View.VISIBLE
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.technanosoft.www.gharse.*
import kotlinx.android.synthetic.main.loadmore_refresh.*
import java.util.*


class  Fragment_User_Home:Fragment(), SearchView.OnQueryTextListener{
    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }


    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }

    companion object {
        fun newInstance(): Fragment_User_Home {
            val fragmentHome = Fragment_User_Home()
            val args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }
    }

    private var listing_rec : RecyclerView? = null
    var posts: MutableList<Recipe?> = ArrayList()
    private lateinit var adapter: ListingAdapter_user
    private var progress: ProgressBar? = null
    lateinit var manager: LinearLayoutManager
    var isScrolling: Boolean? = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var pageNumber: Int = 1
    var tiff:String? = "F"

    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.toolbar_menu,menu)

        val searchItem = menu!!.findItem(R.id.action_search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    val userIn = newText.toLowerCase()
                    var post: MutableList<Recipe?> = ArrayList()
                    println(posts.size)


                    Log.i("onQueryTextChange", newText)
                    println(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.i("onQueryTextSubmit", query)
                    println(query)
                    return true
                }
            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.getItemId()) {
            R.id.action_search ->{
                val frag = Fragment_SearchView()
                addfrag(frag)
                return false
            }
            else -> {
            }
        }
        searchView!!.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view
                = inflater!!.inflate(R.layout.fragment_user_home, container, false)
        listing_rec = view.findViewById<View>(R.id.listing_rec) as RecyclerView
        progress = view.findViewById<View>(R.id.progessBar) as ProgressBar
        manager = LinearLayoutManager(Fragment_User_Home.newInstance().context)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ListingAdapter_user(posts)
        listing_rec?.layoutManager = manager!!
        listing_rec?.adapter = adapter
        tiff = arguments!!.getString("Tiff")
        listing_rec?.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = manager.childCount
                totalItems = manager.itemCount


                scrollOutItems = manager.findFirstVisibleItemPosition()

                if (isScrolling!! && (currentItems + scrollOutItems === totalItems)) {
                    isScrolling = false
                  //  check()
                    progress!!.visibility = View.GONE
                    sendPostRequest()
                    loadMore.visibility = VISIBLE
                }
            }
        })
        val ob = Connection()
        if (ob.isNetworkAvailable(context!!)){
            Timer().schedule(object : TimerTask(){
                override fun run() {
                  //  check()

                    sendPostRequest()

                }
            },2500L)
        }
        else{
            Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show()
        }

    }

    private fun check(){

        var str = arguments!!.getString("DataPacket")

        class DataGet : AsyncTask<Void, Void, String>() {
            override fun onPreExecute() {
                super.onPreExecute()
                if (str!=null){
                    posts.clear()
                }
            }

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                if (str!=null){
                    var params: MutableMap<String, String>? = null
                    params = HashMap()
                    params["RecipeName"] = str!!

                    //returning the response
                    return requestHandler.sendPostRequest(BuildConfig.Base_URL+"SearchRecipeByName", params)
                }else {
                    var params: MutableMap<String, String>? = null
                    params = HashMap()
                    params["PageIndex"] = pageNumber.toString()
                    params["PageSize"] = "10"

                    //returning the response
                    return requestHandler.sendPostRequest(BuildConfig.Base_URL + "GetAllRecipeByPaging", params)
                }
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)

               progress!!.visibility = View.GONE
//                loadMore!!.visibility = View.GONE
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                        val obj = JSONObject(s)
                        val array = obj.getJSONArray("Data")
                        for (i in 0 until array.length()){
                            val ob = array.getJSONObject(i)
                            var RecipeID = ob.getString("RecipeID")
                            var RegisterID = ob.getString("RegisterID")
                            var PageIndex = ob.getString("PageIndex")
                            var RecipeName = ob.getString("HotelName")
                            var RecipeImage = ob.getString("RecipeImage")
                            var RecipeType = ob.getString("RecipeType")
                            var RecipeDescriptio = ob.getString("RecipeDescription")
                            var FromTime = (ob.getString("FromTime")).toString()
                            var ToTime = (ob.getString("ToTime")).toString()
                            try {
                                val dataitem = Recipe(RecipeID,RegisterID,PageIndex,RecipeName,RecipeImage,RecipeType,RecipeDescriptio,FromTime,ToTime,tiff!!)
                                posts.add(dataitem)
                            }catch (e:Exception){

                            }
                        }
                        if (str!=null)
                        {

                        }else
                        {
                            pageNumber += 1
                        }
                    str = ""
                    adapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    Toast.makeText(activity, "Error : $e", Toast.LENGTH_LONG).show()
                }

            }
        }
        //executing the async task
        val ru = DataGet()
        ru.execute()

    }

    private fun addfrag(fragment:Fragment) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.user_home_frag, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun sendPostRequest() {


       // progress!!.visibility = View.GONE



        var str = arguments!!.getString("DataPacket")


        if (str!=null){
            posts.clear()
        }


        val homeUrl : String = BuildConfig.Base_URL+"GetAllRecipeByPaging";



        if (str!=null){
            val searchByName: String = BuildConfig.Base_URL+"SearchRecipeByName";



              var params: MutableMap<String, String>? = null
              params = HashMap()
              params["RecipeName"] = str!!
            //returning the response
           // return requestHandler.sendPostRequest(BuildConfig.Base_URL+"SearchRecipeByName", params)
        }else {

          val oRequest = object : StringRequest(Request.Method.POST, homeUrl, Response.Listener { response ->


            try {
                val obj = JSONObject(response)
                val array = obj.getJSONArray("Data")
                for (i in 0 until array.length()){
                    val ob = array.getJSONObject(i)
                    var RecipeID = ob.getString("RecipeID")
                    var RegisterID = ob.getString("RegisterID")
                    var PageIndex = ob.getString("PageIndex")
                    var RecipeName = ob.getString("HotelName")
                    var RecipeImage = ob.getString("RecipeImage")
                    var RecipeType = ob.getString("RecipeType")
                    var RecipeDescriptio = ob.getString("RecipeDescription")
                    var FromTime = (ob.getString("FromTime")).toString()
                    var ToTime = (ob.getString("ToTime")).toString()
                    try {
                        val dataitem = Recipe(RecipeID,RegisterID,PageIndex,RecipeName,RecipeImage,RecipeType,RecipeDescriptio,FromTime,ToTime,tiff!!)
                        posts.add(dataitem)
                    }catch (e:Exception){

                    }
                }
                if (str!=null){

                }else{
                    pageNumber += 1
                }
                str = ""
                adapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                Toast.makeText(activity, "Error : $e", Toast.LENGTH_LONG).show()
            }

        }, Response.ErrorListener { error ->
            Toast.makeText(activity, "Error: " + (error.toString()), Toast.LENGTH_SHORT).show();
        }) {
            override fun getParams(): Map<String, String> {

                val params = HashMap<String, String>()
                params["PageIndex"] = pageNumber.toString()
                params["PageSize"] = "10"

                return params

            }
        }

        val requestQ = Volley.newRequestQueue(activity)
        requestQ.add(oRequest)

    }


}}