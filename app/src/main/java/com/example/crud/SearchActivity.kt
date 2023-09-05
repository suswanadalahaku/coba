package com.example.crud

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.example.crud.databinding.ActivitySearchBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    val db = Firebase.firestore
    private var mList = ArrayList<DataModel>()
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recyView = binding.recyclerView
        adapter = SearchAdapter(mList)
        recyView.adapter = adapter

        val userIdList = listOf("aFehzRa7enr5bcMIcaIT", "tOdfTIpV40KCbkDOwd9H", "2kgooDDP0iUQNTRhzgzH")
        fetchDataFromFirestore(userIdList)

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Tidak perlu melakukan apa-apa saat pengguna menekan tombol "Submit"
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Saat teks pencarian berubah, lakukan pencarian dengan adapter
                filteredList(newText)
                return true
            }
        })
    }

    private fun filteredList(query: String?) {
        if (query != null) {
            val filterList = ArrayList<DataModel>()
            for (i in mList) {
                if (i.nama.lowercase(Locale.ROOT).contains(query)) {
                    filterList.add(i)
                }
            }
            adapter.setFilteredList(filterList)
        }
    }

    private fun fetchDataFromFirestore(userIdList: List<String>) {
        val usersCollectionRef = db.collection("data")

        for (userId in userIdList) {
            usersCollectionRef.document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val namaText = document.getString("Nama") ?: ""
                        val gender = document.getString("JK") ?: ""
                        val kesukaan = document.getString("Hobi") ?: ""
                        val lok = document.getString("Alamat") ?: ""
                        val area = document.getString("Deskripsi") ?: ""
                        val bintang = document.getDouble("Rating") ?: 0.0

                        // Tambahkan data ke dalam list
                        mList.add(DataModel(userId, namaText, lok, gender, kesukaan, area, bintang))

                        // Cek jika semua data sudah diambil
                        if (mList.size == userIdList.size) {
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Terjadi kesalahan saat mengambil data
                    Log.w(ContentValues.TAG, "Gagal mengambil data", e)
                }
        }
    }
}
