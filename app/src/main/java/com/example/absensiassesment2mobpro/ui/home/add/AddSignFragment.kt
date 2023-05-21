package com.example.absensiassesment2mobpro.ui.home.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.absensiassesment2mobpro.databinding.FragmentAddSignBinding
import com.example.absensiassesment2mobpro.datastore.DataStoreUtil
import com.example.absensiassesment2mobpro.room.absensi.Absensi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddSignFragment : Fragment() {

    private var _binding: FragmentAddSignBinding? = null
    private val binding: FragmentAddSignBinding get() = _binding!!

    private lateinit var viewModel: AddSignViewModel

    var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSignBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AddSignViewModel::class.java]

        val dataStore = context?.let { DataStoreUtil(it) }

        lifecycleScope.launch {
            dataStore?.username?.collectLatest {
                username = it ?: ""
            }
        }

        val calendar = Calendar.getInstance()
        val date_ = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calendar.time)
        val time_ = SimpleDateFormat("HH : mm", Locale.getDefault()).format(calendar.time)

        binding.timeEdt.setText(time_)
        binding.dateEdt.setText(date_)

        binding.timeEdt.setOnClickListener {
            TimePickerDialog(context, { view, hourOfDay, minute ->
                run {
                    calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                    calendar[Calendar.MINUTE] = minute

                    binding.timeEdt.setText(SimpleDateFormat("HH : mm", Locale.getDefault()).format(calendar.time))
                }
            },calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], true).show()
        }

        binding.dateEdt.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(it1, { view, year, month, dayOfMonth ->

                    calendar[Calendar.YEAR] = year
                    calendar[Calendar.MONTH] = month
                    calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val simple = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    binding.dateEdt.setText(simple.format(calendar.time))

                },calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]).show()
            }
        }

        viewModel.getUserDetail(username)?.observe(viewLifecycleOwner) {
            binding.nameEdt.setText(it.fullName)
        }

        binding.absenBtn.setOnClickListener {
            val fullname = binding.nameEdt.text.toString()
            val time = binding.timeEdt.text.toString()
            val date = binding.dateEdt.text.toString()
            val desc = binding.descEdt.text.toString()

            if (fullname.isBlank() || time.isBlank() || date.isBlank() ||desc.isBlank()) {
                Toast.makeText(context, "Data Belum Lengkap", Toast.LENGTH_SHORT).show()
            } else {
                val absensi = Absensi(0, username, fullname, desc, time, date)
                viewModel.absensi(absensi)
                Toast.makeText(context, "Absen Berhasil", Toast.LENGTH_SHORT).show()
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }
}