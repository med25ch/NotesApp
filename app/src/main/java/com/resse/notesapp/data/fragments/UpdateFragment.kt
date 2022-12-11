package com.resse.notesapp.data.fragments

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.resse.notesapp.R
import com.resse.notesapp.data.viewModels.MyObservable
import com.resse.notesapp.data.viewModels.SharedViewModel
import com.resse.notesapp.data.viewModels.SharedViewModelFactory


class UpdateFragment : Fragment() {

    private lateinit var viewModel: MyObservable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        // Set Menu
        setupMenu()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[MyObservable::class.java]
            //ViewModelProviders.of(this)[MyObservable::class.java]
        } ?: throw Exception("Invalid Activity")

        // Update UI with Data
        putDataToUI(view)
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return  when (menuItem.itemId){
                    R.id.menu_save -> {
                        true
                    }
                    else ->  {
                        findNavController().navigate(R.id.listFragment)
                        true
                    }
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun putDataToUI(view: View) {
        val mTitle = view.findViewById<EditText>(R.id.current_note_title_ET)
        val mDescription = view.findViewById<EditText>(R.id.current_note_description_ET)

        // Need Code for Priority

        viewModel.data.observe(viewLifecycleOwner, Observer {
            val toDoData = viewModel.data.value
            if (toDoData != null) {
                mTitle.setText(toDoData.title)
                mDescription.setText(toDoData.description)
            }
        })



    }

}