package com.hrodriguesdev.supervisorioandroid.ui.carregamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hrodriguesdev.supervisorioandroid.databinding.FragmentCarregamentoBinding

class CarregamentoFragment : Fragment() {

    private var _binding: FragmentCarregamentoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val graficoViewModel =
            ViewModelProvider(this).get(CarregamentoViewModel::class.java)

        _binding = FragmentCarregamentoBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
