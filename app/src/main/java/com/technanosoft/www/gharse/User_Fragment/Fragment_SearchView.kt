package com.technanosoft.www.gharse.User_Fragment

import android.app.SearchManager
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import com.technanosoft.www.gharse.Adapters.ListingAdapter_user
import com.technanosoft.www.gharse.Adapters.Search_View_Adapter
import com.technanosoft.www.gharse.BuildConfig
import com.technanosoft.www.gharse.Models.Recipe
import com.technanosoft.www.gharse.Networking.Connection
import com.technanosoft.www.gharse.Networking.RequestHandler
import com.technanosoft.www.gharse.R
import kotlinx.android.synthetic.main.loadmore_refresh.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap
import android.view.View.OnFocusChangeListener
import android.support.v4.view.MenuItemCompat.setOnActionExpandListener





class Fragment_SearchView:Fragment(), SearchView.OnQueryTextListener{


    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null
    private var listing_re : RecyclerView? = null
    var posts: MutableList<Recipe?> = ArrayList()
    private lateinit var adapter: Search_View_Adapter
    private var progress: ProgressBar? = null
    lateinit var manager: LinearLayoutManager
    var pageNumber: Int = 1

    companion object {
        fun newInstance(): Fragment_SearchView {
            val fragmentSearch = Fragment_SearchView()
            val args = Bundle()
            fragmentSearch.arguments = args
            return fragmentSearch
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.toolbar_menu,menu)

        menu!!.performIdentifierAction(R.id.action_search, 0)

        val searchItem = menu!!.findItem(R.id.action_search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }

        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.length > 1){
                        check(newText)
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.length > 1){
                        check(query)
                    }
                    return true
                }
            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.getItemId()) {
            R.id.action_search ->{

                return false
            }
            else -> {
            }
        }
        searchView!!.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_search_view, container, false)
        listing_re = view.findViewById<View>(R.id.listing_sea) as RecyclerView
        progress = view.findViewById<View>(R.id.progessBar) as ProgressBar
        manager = LinearLayoutManager(Fragment_SearchView.newInstance().context)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progress!!.visibility = View.GONE
        loadMore!!.visibility = View.GONE
        adapter = Search_View_Adapter(posts)
        listing_re?.layoutManager = manager!!
        listing_re?.adapter = adapter

    }

    private fun check(str:String){

        val st = str

        class SignUp : AsyncTask<Void, Void, String>() {
            override fun onPreExecute() {
                super.onPreExecute()
                progress!!.visibility = View.VISIBLE
                posts.clear()
            }

            override fun doInBackground(vararg voids: Void): String {
                //creating request handler object
                val requestHandler = RequestHandler()

                //creating request parameters
                var params: MutableMap<String, String>? = null
                params = HashMap()
                params["RecipeName"] = st

                //returning the response
                return requestHandler.sendPostRequest(BuildConfig.Base_URL+"GetAllRecipeNames", params)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                progress!!.visibility = View.GONE
                loadMore!!.visibility = View.GONE
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    val obj = JSONObject(s)
                    val array = obj.getJSONArray("Data")
                    for (i in 0 until array.length()){
                        val ob = array.getJSONObject(i)
                        var RecipeName = ob.getString("RecipeName")

                        val dataitem = Recipe("","","",RecipeName,"","","","","","")
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
    fun addfrage(fragment:Fragment) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.user_home_frag, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
//menu!!.performIdentifierAction(R.id.action_search, 0)