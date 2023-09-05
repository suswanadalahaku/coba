package com.example.crud

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class SearchAdapter(
    private var listDatabase: List<DataModel>
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        return  ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_search, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val listDB = listDatabase[position]
        holder.nama.text = listDB.nama
        holder.alamat.text = listDB.alamat
        if(listDB.jk.equals("Laki-Laki")){
            holder.jk.check(R.id.jk_p)
        }else if (listDB.jk.equals("Perempuan")){
            holder.jk.check(R.id.jk_w)
        }
        holder.hobi.text = listDB.hobi
        holder.deskripsi.text = listDB.deskripsi
        holder.rating.rating = listDB.rating.toFloat()
    }

    override fun getItemCount(): Int {
        return listDatabase.size
    }

    fun setFilteredList(listDatabase: List<DataModel>){
        this.listDatabase = listDatabase
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val nama = view.findViewById<TextView>(R.id.view_nama)
        val alamat = view.findViewById<TextView>(R.id.view_alamat)
        val jk = view.findViewById<RadioGroup>(R.id.jk_group)
        val hobi = view.findViewById<TextView>(R.id.view_hobi)
        val deskripsi = view.findViewById<TextView>(R.id.view_deskripsi)
        val rating = view.findViewById<RatingBar>(R.id.view_rating)
    }

}