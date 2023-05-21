package com.example.absensiassesment2mobpro.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.absensiassesment2mobpro.R
import com.example.absensiassesment2mobpro.databinding.FragmentSignBinding
import com.example.absensiassesment2mobpro.datastore.DataStoreUtil
import com.example.absensiassesment2mobpro.room.absensi.Absensi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SignFragment : Fragment(), DeleteClick {

    private var _binding: FragmentSignBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignViewModel

    var username = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SignViewModel::class.java]

        binding.absensiRv.layoutManager = LinearLayoutManager(context)

        val dataStore = context?.let { DataStoreUtil(it) }

        lifecycleScope.launch {
            dataStore?.username?.collectLatest {
                if (it != null) {
                    username = it
                }
            }
        }

        viewModel.allAbsensi()?.observe(viewLifecycleOwner) {
            binding.absensiRv.adapter = SignAdapter(it, viewModel, username, this)
        }

        binding.absensiBtn.setOnClickListener {
            findNavController().navigate(R.id.toAddSign)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(absensi: Absensi) {
        val alert = AlertDialog.Builder(context)
        alert.setMessage("Apakah anda yakin ingin menghapus absensi ini?")
        alert.setPositiveButton("Ya") { dialog, _ ->
            viewModel.deleteAbsensi(absensi)
            dialog.dismiss()
        }

        alert.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        alert.show()
    }
}