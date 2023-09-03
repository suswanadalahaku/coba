package com.example.crud

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.example.crud.databinding.ActivityUpdateBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateBinding
    val db = Firebase.firestore

    fun updateUserData(userId: String, nama: String, jk: String, alamat: String, hobi: String) {
        // Mengambil referensi dokumen yang akan diperbarui
        val docRef = db.collection("data").document(userId)

        // Data yang ingin diubah
        val updatedData = hashMapOf(
            "Hobi" to hobi,
            "Alamat" to alamat,
            "JK" to jk,
            "Nama" to nama
        )

        // Melakukan update data pada dokumen
        docRef.update(updatedData as Map<String, Any>)
            .addOnSuccessListener {
                // Update berhasil
                Log.d(TAG, "Data berhasil diperbarui")
            }
            .addOnFailureListener { e ->
                // Terjadi kesalahan saat update
                Log.w(TAG, "Gagal melakukan update data", e)
            }
    }

    fun viewData(userId: String, nama: EditText, jk: EditText, hobi: EditText, alamat: EditText) {
        val usersCollectionRef = db.collection("data")

        usersCollectionRef.document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val namaText = document.getString("Nama") ?: ""
                    val gender = document.getString("JK") ?: ""
                    val kesukaan = document.getString("Hobi") ?: ""
                    val lok = document.getString("Alamat") ?: ""

                    // Menampilkan data dalam EditText
                    nama.setText(namaText)
                    jk.setText(gender)
                    hobi.setText(kesukaan)
                    alamat.setText(lok)
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
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userId = "aFehzRa7enr5bcMIcaIT" // ID pengguna yang ingin diperbarui

        val nama = binding.viewNama
        val jk = binding.viewJk
        val alamat = binding.viewAlamat
        val hobi = binding.viewHobi

        viewData(userId, nama, jk, alamat, hobi)

        binding.update.setOnClickListener{
            val nama = binding.viewNama.text.toString()
            val jk = binding.viewJk.text.toString()
            val alamat = binding.viewAlamat.text.toString()
            val hobi = binding.viewHobi.text.toString()
            updateUserData(userId, nama, jk, alamat, hobi)
        }
    }
}