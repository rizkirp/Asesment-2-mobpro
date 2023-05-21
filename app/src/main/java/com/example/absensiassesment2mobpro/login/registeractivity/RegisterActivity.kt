package com.example.absensiassesment2mobpro.login.registeractivity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import com.example.absensiassesment2mobpro.databinding.ActivityRegisterBinding
import com.example.absensiassesment2mobpro.login.AuthViewModel
import com.example.absensiassesment2mobpro.login.loginactivity.MainActivity
import com.example.absensiassesment2mobpro.room.auth.Auth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar = Calendar.getInstance()

        supportActionBar?.title = "Silahkan Daftar"

        binding.birthDateEdt.setOnClickListener {
            DatePickerDialog(this, { view, year, month, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val simple = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                binding.birthDateEdt.setText(simple.format(calendar.time))
            },calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]).show()
        }

        binding.registerBtn.setOnClickListener {
            val fullname = binding.fullNameEdt.text.toString()
            val username = binding.usernameEdt.text.toString()
            val password = binding.passwordEdt.text.toString()
            val birthDate = calendar.timeInMillis
            val radioButton = findViewById<RadioButton>(binding.radioGr.checkedRadioButtonId)
            val gender = if (radioButton.text.toString() == "Laki Laki") {
                0
            } else {
                1
            }

            if (fullname.isBlank() || username.isBlank() ||password.isBlank() || binding.birthDateEdt.text.toString().isBlank() || binding.radioGr.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Data belum lengkap", Toast.LENGTH_SHORT).show()
            } else {
                val auth = Auth(username, password, fullname, birthDate, gender)
                viewModel.register(auth)
                Toast.makeText(this, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.loginTxt.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}