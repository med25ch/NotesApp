package com.resse.notesapp.data.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.resse.notesapp.R
import com.resse.notesapp.data.viewModels.IdeaFragmentViewModel
import com.resse.notesapp.databinding.FragmentIdeaBinding
import com.resse.notesapp.databinding.FragmentListBinding


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



        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}