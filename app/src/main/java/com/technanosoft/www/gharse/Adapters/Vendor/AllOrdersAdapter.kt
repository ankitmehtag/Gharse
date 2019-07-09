package com.technanosoft.www.gharse.Adapters.Vendor

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.technanosoft.www.gharse.Fragments.Fragment_Order_Details
import com.technanosoft.www.gharse.Models.Vendor_Models.Vendor_Orders
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.Vendor_Home
import com.technanosoft.www.gharse.ViewHolders.AllOrdersViewHolder


class AllOrdersAdapter(val data:MutableList<Vendor_Orders?>): RecyclerView.Adapter<AllOrdersViewHolder>(){

    private var mContext: Context? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AllOrdersViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.vendor_orders,p0,false)
        return AllOrdersViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(p0: AllOrdersViewHolder, p1: Int) {
        val Data = data[p1]
        p0.addre.text = Data!!.DeliveryAddress
        if (Data.CuponActive == "Active"){
            p0.coupon_sec.visibility = View.VISIBLE
            p0.AftrCuponP.text = Data.AfterCouponTotalPrice
            p0.CuponPri.text = Data.CuponPrice
        }
        p0.contact_num.text = Data.ContactNumber
        p0.deliveryti.text = Data.DeliveryTime
        p0.itemquan.text = Data.ItemsCount
        p0.ordernum.text = Data.OrderNumber
        p0.ordertime.text = Data.OrderTime
        p0.payment_sta.text = Data.PaymentStatus
        p0.paymethod.text = Data.PaymentMethod
        p0.subtota.text = Data.TotalPrice
        p0.vi.setOnClickListener {
            mContext = p0.vi.context
            fragmentJump(Data.OrderID)
        }
    }

    private fun fragmentJump(mItemSelected: String) {
        val mFragment = Fragment_Order_Details()
        val mBundle = Bundle()
        mBundle.putString("ID_DATA",mItemSelected)
        mFragment.setArguments(mBundle)
        switchContent(R.id.contain_frage, mFragment)
    }

    fun switchContent(id: Int, fragment: Fragment) {
        if (mContext == null)
            return
        if (mContext is Vendor_Home) {
            val mainActivity = mContext as Vendor_Home
            mainActivity.switchContent(id, fragment)
        }

    }

}
