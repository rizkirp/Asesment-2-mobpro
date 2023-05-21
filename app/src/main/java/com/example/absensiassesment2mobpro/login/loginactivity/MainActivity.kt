package com.example.absensiassesment2mobpro.login.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.absensiassesment2mobpro.BottomNavigationActivity
import com.example.absensiassesment2mobpro.databinding.ActivityMainBinding
import com.example.absensiassesment2mobpro.datastore.DataStoreUtil
import com.example.absensiassesment2mobpro.login.AuthViewModel
import com.example.absensiassesment2mobpro.login.registeractivity.RegisterActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Masuk Ke Aplikasi"

        val dataStoreUtil = DataStoreUtil(this)


        lifecycleScope.launch {
            dataStoreUtil.username.collectLatest {
                if (!it.isNullOrBlank()) {
                    val intent = Intent(this@MainActivity, BottomNavigationActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        binding.loginBtn.setOnClickListener {
            val username = binding.usernameEdt.text.toString()
            val password = binding.passwordEdt.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Data Tidak Lengkap", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    viewModel.isExist(username).collectLatest {
                        if (it) {
                            viewModel.login(username).collectLatest { pass->
                                if (pass == password) {
                                    dataStoreUtil.saveSession(username)
                                    finish()
                                    val intent = Intent(this@MainActivity, BottomNavigationActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this@MainActivity, "Kata sandi salah", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Username Belum Terdaftar", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.registerTxt.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

}