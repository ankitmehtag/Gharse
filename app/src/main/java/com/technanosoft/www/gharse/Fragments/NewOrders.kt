package com.technanosoft.www.gharse.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.technanosoft.www.gharse.R
import kotlinx.android.synthetic.main.fragment_schedule.*
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import android.widget.ArrayAdapter
import android.widget.Toast
import com.travijuu.numberpicker.library.NumberPicker


class NewOrders : Fragment(){
    var materialBetterSpinner: MaterialBetterSpinner? = null
    var numberPicker : NumberPicker? = null

    var SPINNER_DATA = arrayOf("ANDROID", "PHP", "BLOGGER", "WORDPRESS")
    lateinit var Con: Context
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Con = context!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_new_orders, container, false)
        numberPicker = view.findViewById<View>(R.id.number_picker) as NumberPicker
        materialBetterSpinner = view.findViewById<View>(R.id.material_spinner1) as MaterialBetterSpinner
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ArrayAdapter(Con, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA)
        materialBetterSpinner!!.setAdapter(adapter)
        Toast.makeText(Con, materialBetterSpinner!!.text,Toast.LENGTH_SHORT).show()
        var i = materialBetterSpinner!!.text
        numberPicker!!.setMax(15);
        numberPicker!!.setMin(5);
        numberPicker!!.setUnit(2);
        numberPicker!!.setValue(10);
    }


}