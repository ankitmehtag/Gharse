package com.technanosoft.www.gharse.Adapters.Vendor

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.technanosoft.www.gharse.Models.Vendor_Models.Vendor_Orders
import com.technanosoft.www.gharse.R
import com.technanosoft.www.gharse.ViewHolders.AllOrdersViewHolder

class Delevered_Orders(val data:MutableList<Vendor_Orders?>): RecyclerView.Adapter<AllOrdersViewHolder>() {

    private var mContext: Context? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AllOrdersViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.vendor_orders, p0, false)
        return AllOrdersViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(p0: AllOrdersViewHolder, p1: Int) {
        val Data = data[p1]
        p0.addre.text = Data!!.DeliveryAddress
        if (Data.CuponActive == "Active") {
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

    }
}