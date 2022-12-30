package com.resse.notesapp.data.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.resse.notesapp.data.uistate.UiState
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

        //Observe UiState :

        viewModel.uiState().observe(viewLifecycleOwner, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })

        // Generate Button
        binding.generateBtn.setOnClickListener {
            var checkedList = getActivityType()
            viewModel.getBoredActivity(checkedList)
        }

        return binding.root

    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> {
                onLoad()
            }
            is UiState.Success -> {
                onSuccess()
            }
            is UiState.Error -> {
                onError()
            }
        }
    }

    private fun onLoad() = with(binding) {
        progressBar.visibility = View.VISIBLE
    }

    private fun onSuccess()= with(binding) {
        progressBar.visibility = View.INVISIBLE
    }

    private fun onError() = with(binding)  {
        progressBar.visibility = View.INVISIBLE
    }

    private fun getActivityType(): MutableList<String> = with(binding)  {
        var checkboxList = mutableListOf(
            educationCheckBox,
            socialCheckBox,
            diyCheckBox,
            recreationalCheckBox,
            cookingCheckBox,
            relaxationCheckBox
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