package com.resse.notesapp.data.fragments

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.resse.notesapp.R
import com.resse.notesapp.data.dependencies.ToDoApplication
import com.resse.notesapp.data.models.ToDoData
import com.resse.notesapp.data.viewModels.SharedViewModel
import com.resse.notesapp.data.viewModels.SharedViewModelFactory
import com.resse.notesapp.data.viewModels.ToDoViewModel
import com.resse.notesapp.data.viewModels.ToDoViewModelFactory
import timber.log.Timber


class AddFragment : Fragment() {

    private val mTodoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory((activity?.application as ToDoApplication).repository)
    }

    private val mSharedViewModel : SharedViewModel by viewModels{
        SharedViewModelFactory(activity?.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)



        // Set Menu
        setupMenu(view)


        return view
    }

    private fun setupMenu(view: View) {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return when (menuItem.itemId) {
                    R.id.menu_add -> {
                        insertDataToDb(view)
                        true
                    }
                    else -> {
                        findNavController().navigate(R.id.action_addFragment_to_listFragment)
                        true
                    }
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun insertDataToDb(view: View) {
        val mTitle = view.findViewById<EditText>(R.id.note_title_ET).text.toString()
        // get selected radio button from radioGroup
        val selectedId = view.findViewById<RadioGroup>(R.id.radio).checkedRadioButtonId
        // find the radioButton by returned id
        val radioButton = view.findViewById<RadioButton>(selectedId)
        val mPriority = radioButton.text.toString()
        val mDescription = view.findViewById<EditText>(R.id.note_description_ET).text.toString()

        val validation = mSharedViewModel.verifyDataFromUser(mTitle, mDescription)

        if(validation){
            // create data
            val newData = ToDoData(
                id =0,
                mTitle,
                mSharedViewModel.parsePriority(mPriority),
                mDescription
            )
            //insert data to db
            mTodoViewModel.insert(newData)
            Timber.d("New Note inserted to Database ")
            Toast.makeText(requireContext(),"${newData.title} added : ${newData.priority}",Toast.LENGTH_LONG).show()

        }else{
            // Put some message to inform user to fill all fields
            Timber.d("Fields validation failed . NoteData not inserted to database")
        }
    }

}