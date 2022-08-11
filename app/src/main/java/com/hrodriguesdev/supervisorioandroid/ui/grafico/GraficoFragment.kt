package com.hrodriguesdev.supervisorioandroid.ui.grafico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hrodriguesdev.supervisorioandroid.databinding.FragmentDashboardBinding
import com.hrodriguesdev.supervisorioandroid.databinding.FragmentGraficoBinding

class GraficoFragment : Fragment() {

    private var _binding: FragmentGraficoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val graficoViewModel =
            ViewModelProvider(this).get(GraficoViewModel::class.java)

        _binding = FragmentGraficoBinding.inflate(inflater, container, false)
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