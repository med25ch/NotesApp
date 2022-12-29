package com.resse.notesapp.data.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.resse.notesapp.data.viewModels.IdeaFragmentViewModel
import com.resse.notesapp.databinding.FragmentIdeaBinding
import java.util.function.Predicate


class IdeaFragment : Fragment() {

    private val viewModel: IdeaFragmentViewModel by viewModels()


    // The type of binding class will change from fragment to fragment
    private var _binding : FragmentIdeaBinding? = null

    private val binding get() = _binding!! // Helper Property

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIdeaBinding.inflate(inflater)


        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = viewLifecycleOwner

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel


        // Generate Button
        binding.generateBtn.setOnClickListener {
            var checkedList = getActivityType()
            viewModel.getBoredActivity(checkedList)
        }

        return binding.root

    }

    private fun getActivityType(): MutableList<String> {
        var checkboxList = mutableListOf(
            binding.educationCheckBox,
            binding.socialCheckBox,
            binding.diyCheckBox,
            binding.recreationalCheckBox,
            binding.cookingCheckBox,
            binding.relaxationCheckBox
        )
        checkboxList.removeIf { x ->
            Predicate { checkbox: CheckBox -> !checkbox.isChecked }.test(x)
        }

        var checkedList = mutableListOf<String>()

        for (checkbox in checkboxList) {
            checkedList.add(checkbox.text.toString().lowercase())
        }
        return checkedList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}