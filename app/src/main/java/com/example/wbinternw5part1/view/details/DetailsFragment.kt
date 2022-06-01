package com.example.wbinternw5part1.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import com.example.wbinternw5part1.databinding.FragmentDetailsBinding
import com.example.wbinternw5part1.model.DataModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderData()
    }

    private fun renderData() {
        val data = arguments?.getParcelable<DataModel>(KEY)
        with(binding) {
            data?.let {
                image.load("https://api.opendota.com${data.img}")
                localizedName.text = data.localized_name
                attackType.text = data.attack_type
                baseHealth.text = data.base_health.toString()
                baseMana.text = data.base_mana.toString()
                baseMr.text = data.base_mr.toString()
                baseAttackMin.text = data.base_attack_min.toString()
                baseAttackMax.text = data.base_attack_max.toString()
            }
        }
    }

    companion object {
        private const val KEY = "details"

        fun newInstance(dataModel: DataModel) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY, dataModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}