package com.example.absensiassesment2mobpro.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.absensiassesment2mobpro.R
import com.example.absensiassesment2mobpro.databinding.SignItemBinding
import com.example.absensiassesment2mobpro.room.absensi.Absensi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SignAdapter(
    private val list: List<Absensi>,
    private val viewModel: SignViewModel,
    val username: String,
    private val deleteClick: DeleteClick
) : RecyclerView.Adapter<SignAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignAdapter.ViewHolder {
        val binding = SignItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SignAdapter.ViewHolder, position: Int) {
        val absensi = list[position]
        with(holder) {
            binding.nameTxt.text = absensi.fullName
            binding.timeTxt.text = absensi.time
            binding.dateTxt.text = absensi.date
            binding.descTxt.text = absensi.desc

            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getUser(username).collectLatest {
                    if (it == 0) {
                        binding.genderImg.setImageResource(R.drawable.male)
                    } else {
                        binding.genderImg.setImageResource(R.drawable.female)
                    }
                }
            }

            binding.deleteImg.setOnClickListener {
                deleteClick.onClick(absensi)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: SignItemBinding) : RecyclerView.ViewHolder(binding.root)

}