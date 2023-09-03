package com.example.crud

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.example.crud.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore

    fun viewData(userId: String, nama: TextView, jk: TextView, hobi: TextView, alamat: TextView, deskripsi: TextView, rate: RatingBar) {
        val usersCollectionRef = db.collection("data")

        usersCollectionRef.document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val namaText = document.getString("Nama") ?: ""
                    val gender = document.getString("JK") ?: ""
                    val kesukaan = document.getString("Hobi") ?: ""
                    val lok = document.getString("Alamat") ?: ""
                    val area = document.getString("Deskripsi") ?: ""
                    val bintang = document.getDouble("Rating") ?: ""

                    // Tampilkan data dalam TextViews
                    nama.text = namaText
                    jk.text = gender
                    hobi.text = kesukaan
                    alamat.text = lok
                    deskripsi.text = area
                    rate.rating = bintang.toString().toFloat()
                } else {
                    // Dokumen tidak ditemukan atau tidak ada data
                    Log.d(TAG, "Dokumen tidak ditemukan")
                    // Mungkin Anda ingin menampilkan pesan kesalahan atau melakukan tindakan lain di sini.
                }
            }
            .addOnFailureListener { e ->
                // Terjadi kesalahan saat mengambil data
                Log.w(TAG, "Gagal mengambil data", e)
            }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val nama = binding.viewNama
        val jk = binding.viewJk
        val alamat = binding.viewAlamat
        val hobi = binding.viewHobi
        val txtArea = binding.viewArea
        val rating = binding.ratingBar
        val documentId = "aFehzRa7enr5bcMIcaIT" // ID dokumen yang ingin dihapus

        viewData(documentId, nama, jk, hobi, alamat, txtArea, rating)

        binding.update.setOnClickListener{
            val intent = Intent(this, UpdateActivity::class.java)
            startActivity(intent)
        }

        binding.textHapus.text = "Hapus Data dengan UserId : $documentId"
        binding.hapus.setOnClickListener{
            // Mengambil referensi dokumen yang akan dihapus
            val docRef = db.collection("data").document(documentId)

            // Menghapus dokumen
            docRef.delete()
                .addOnSuccessListener {
                    // Dokumen berhasil dihapus
                    Log.d(TAG, "Dokumen berhasil dihapus")
                    viewData(documentId,nama, jk, hobi, alamat, txtArea, rating)
                }
                .addOnFailureListener { e ->
                    // Terjadi kesalahan saat menghapus dokumen
                    Log.w(TAG, "Gagal menghapus dokumen", e)
                }
        }

    }
}