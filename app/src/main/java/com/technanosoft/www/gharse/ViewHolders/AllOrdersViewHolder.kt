package com.technanosoft.www.gharse.ViewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.technanosoft.www.gharse.Models.Vendor_Models.Vendor_Orders
import com.technanosoft.www.gharse.R

class AllOrdersViewHolder(val vi:View):RecyclerView.ViewHolder(vi){
    var allor : Vendor_Orders? = null
    var ordernum : TextView = vi.findViewById<View>(R.id.ordernum) as TextView
    var itemquan : TextView = vi.findViewById<View>(R.id.itemco) as TextView
    var subtota : TextView = vi.findViewById<View>(R.id.subtotal) as TextView
    var ordertime : TextView = vi.findViewById<View>(R.id.ordertime) as TextView
    var deliveryti : TextView = vi.findViewById<View>(R.id.deliveryti) as TextView
    var coupon_sec : LinearLayout = vi.findViewById<View>(R.id.coupon_sec) as LinearLayout
    var CuponPri : TextView = vi.findViewById<View>(R.id.CuponPr) as TextView
    var AftrCuponP : TextView = vi.findViewById<View>(R.id.AftrCuponPr) as TextView
    var addre : TextView = vi.findViewById<View>(R.id.Address) as TextView
    var contact_num : TextView = vi.findViewById<View>(R.id.contact_num) as TextView
    var paymethod : TextView = vi.findViewById<View>(R.id.paymethod) as TextView
    var payment_sta : TextView = vi.findViewById<View>(R.id.payment_sta) as TextView
}