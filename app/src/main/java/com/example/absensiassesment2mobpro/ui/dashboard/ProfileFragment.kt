package com.example.absensiassesment2mobpro.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.absensiassesment2mobpro.R
import com.example.absensiassesment2mobpro.databinding.FragmentProfileBinding
import com.example.absensiassesment2mobpro.datastore.DataStoreUtil
import com.example.absensiassesment2mobpro.login.loginactivity.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        val dataStore = context?.let { DataStoreUtil(it) }
        var username = ""
        lifecycleScope.launch {
            dataStore?.username?.collectLatest {
                username = it ?: ""
            }
        }

        viewModel.getUserDetail(username)?.observe(viewLifecycleOwner) {
            binding.fullnameTxt.text = it.fullName

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.birthdate
            binding.birthdateTxt.text = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calendar.time)
            binding.genderTxt.text = if (it.gender == 0) "Laki Laki" else "Perempuan"
            binding.imageView.setImageResource(if (it.gender == 0) R.drawable.male else R.drawable.female)
            binding.usernameTxt.text = it.username
        }

        binding.logoutTxt.setOnClickListener {
            lifecycleScope.launch {
                dataStore?.deleteSession()
            }
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}