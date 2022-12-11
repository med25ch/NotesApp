package com.resse.notesapp.data.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.resse.notesapp.R
import com.resse.notesapp.data.adapters.ToDoListAdapter
import com.resse.notesapp.data.dependencies.ToDoApplication
import com.resse.notesapp.data.viewModels.ToDoViewModel
import com.resse.notesapp.data.viewModels.ToDoViewModelFactory


class ListFragment : Fragment(){

    private val mTodoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory((activity?.application as ToDoApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        // Set Menu
        setupMenu()

        // Set Recycler View
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = ToDoListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mTodoViewModel.allToDoData.observe(viewLifecycleOwner) { toDos ->
            // Update the cached copy of the words in the adapter.
            toDos.let { adapter.submitList(it) }
        }

        return view
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                findNavController().navigate(R.id.addFragment)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}