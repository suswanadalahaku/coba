package com.example.crud

import android.R
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatDelegate
import com.example.crud.databinding.ActivityCreateBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Locale
import java.util.Locale.setDefault

class CreateActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateBinding
    val db = Firebase.firestore

    lateinit var prefHelper: PrefHelper

    //halo bang

    fun View.startAnimation(context: Context, animationId: Int) {
        val animation = AnimationUtils.loadAnimation(context, animationId)

        // Atur listener untuk menghapus animasi saat selesai
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // Animasi dimulai
            }

            override fun onAnimationEnd(animation: Animation) {
                // Animasi selesai, Anda dapat menambahkan tindakan di sini
            }

            override fun onAnimationRepeat(animation: Animation) {
                // Tidak digunakan dalam kasus ini
            }
        })

        // Terapkan animasi ke View ini
        startAnimation(animation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCreateBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefHelper = PrefHelper(this)

        val locale = Locale("id") // Ganti dengan kode bahasa sesuai keinginan Anda
        setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        val resources: Resources = resources
        resources.updateConfiguration(config, resources.displayMetrics)

        when(prefHelper.getBoolean("pref_is_dark_mode")){
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        val switch = binding.switchToggle
        val bg = binding.bg
        val spinner = binding.dropdown
        val txtInput = binding.textInput
        val rg = binding.radioGrup
        val rad1 = binding.radio1
        val rad2 = binding.radio2
        val check1 = binding.checkbox1
        val check2 = binding.checkbox2
        val btn = binding.btnSubmit
        val txt = binding.text
        val area = binding.txtArea
        val rating = binding.ratingBar
        val searchView = binding.searchView

        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        animation.duration = 1000

        switch.startAnimation(animation)
        spinner.startAnimation(animation)
        rg.startAnimation(animation)
        check1.startAnimation(animation)
        check2.startAnimation(animation)
        btn.startAnimation(animation)
        txt.startAnimation(animation)
        area.startAnimation(animation)
        rating.startAnimation(animation)
        searchView.startAnimation(animation)
        txtInput.startAnimation(animation)



        //inisialisasi dropdown select/spinner
        val options = listOf("Cibiru", "Lembang", "Kopo", "Soeta")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //inisialisasi SwitchToggle
        switch.isChecked = prefHelper.getBoolean("pref_is_dark_mode")
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                prefHelper.put("pref_is_dark_mode", true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                prefHelper.put("pref_is_dark_mode",false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        //search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Jalankan logika pencarian di sini berdasarkan query.
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Reaksi saat teks dalam SearchView berubah (opsional).
                return true
            }
        })

        //SeekBar/Slider
        val seekBar = binding.slider
        val value = binding.sliderValue
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                value.text = "Value: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Handler yang dipanggil ketika pengguna mulai menyeret SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Handler yang dipanggil ketika pengguna menghentikan menyeret SeekBar
            }
        })

        //DatePicker
        val chooseDateButton = binding.chooseDateButton

        // Inisialisasi Calendar dengan tanggal default (contoh: 1 Januari 2000)
        val calendar = Calendar.getInstance()
//        calendar.set(2000, 0, 1) // 0 adalah indeks untuk Januari

        chooseDateButton.setOnClickListener {
            // Memuat animasi dari file XML
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)

            // Atur listener untuk menghapus animasi saat selesai
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    // Animasi dimulai
                }

                override fun onAnimationEnd(animation: Animation) {
                    // Animasi selesai, Anda dapat menambahkan tindakan di sini
                }

                override fun onAnimationRepeat(animation: Animation) {
                    // Tidak digunakan dalam kasus ini
                }
            })

            // Terapkan animasi ke ImageButton
            chooseDateButton.startAnimation(animation)


            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Tangani pemilihan tanggal di sini (misalnya, tampilkan di TextView)
                    // selectedYear, selectedMonth, dan selectedDay adalah tanggal yang dipilih
                },
                year,
                month,
                dayOfMonth
            )

            datePickerDialog.show()
        }



        btn.setOnClickListener{
            var isiNama:String
            var isiJK:String = ""
            var isiAlamat:String
            var isiCheck = ""
            var teksArea:String
            var isiRating:Double

            //Mengambil Value Nama
            isiNama = txtInput.text.toString()

            //Mengambil Value JK
            if (rad1.isChecked){
                isiJK = rad1.getTag().toString()
            }else if (rad2.isChecked){
                isiJK = rad2.getTag().toString()
            }

            //Mengambil Value Alamat
            isiAlamat = spinner.selectedItem.toString()

            //Mengambil Value CheckBox
            if(check1.isChecked){
                isiCheck += check1.getTag().toString()+", "
            }
            if (check2.isChecked){
                isiCheck += check2.getTag().toString()+", "
            }

            //Mengambil Value TextArea
            teksArea = area.text.toString()

            //Mengambil Value Rating
            isiRating = rating.rating.toDouble()


            // Create a new user with a first, middle, and last name
            val user = hashMapOf(
                "Hobi" to isiCheck,
                "Alamat" to isiAlamat,
                "JK" to isiJK,
                "Nama" to isiNama,
                "Deskripsi" to teksArea,
                "Rating" to isiRating
            )

            // Add a new document with a generated ID
            db.collection("data")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }

            txtInput.text = null
            spinner.setSelection(0)
            val grup = binding.radioGrup
            grup.isSelected = false
            check1.isChecked = false
            check2.isChecked = false
            area.text = null
            rating.rating = 0.0f
        }

        txt.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}