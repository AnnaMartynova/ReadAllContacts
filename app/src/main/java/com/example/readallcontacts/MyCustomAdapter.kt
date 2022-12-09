package com.example.readallcontacts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.security.AccessController.getContext
import java.text.FieldPosition

class MyCustomAdapter(private var context: MainActivity, private var items: ArrayList<UserDto>): BaseAdapter() {

    private class ViewHolder(row: View?){
        var namet:TextView? = null
        var numbert:TextView? = null
        var profile:ImageView?

        init {
            this.namet = row?.findViewById(R.id.tv_name)
            this.numbert = row?.findViewById(R.id.tv_number)
            this.profile = row?.findViewById(R.id.imgAvatar)
        }
    }

    override fun getCount(): Int  {
        return items.size
    }

    override fun getItem(i: Int): Any {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    @SuppressLint("InflateParams", "ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
                val inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.contact_info,null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        }else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var userDto = items[position]
        viewHolder.namet?.text = userDto.displayName
        viewHolder.numbert?.text = userDto.phoneNumber
        viewHolder.profile?.setImageBitmap(userDto.imageId)

        return view as View
    }
}


