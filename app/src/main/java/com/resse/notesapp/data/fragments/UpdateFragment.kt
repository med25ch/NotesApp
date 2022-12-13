package com.resse.notesapp.data.fragments

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.resse.notesapp.R
import com.resse.notesapp.data.dependencies.ToDoApplication
import com.resse.notesapp.data.viewModels.*
import timber.log.Timber


class UpdateFragment : Fragment() {

    private lateinit var viewModel: MyObservable

    private lateinit var mTitle:EditText
    private lateinit var mDescription:EditText
    private lateinit var radioUpdate:RadioGroup
    private lateinit var radioButtonHigh:RadioButton
    private lateinit var radioButtonMedium:RadioButton
    private lateinit var radioButtonLow:RadioButton

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
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        // Set Menu
        setupMenu(view)

        //init component
        initComponent(view)

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

    private fun initComponent(view: View) {
        mTitle = view.findViewById<EditText>(R.id.current_note_title_ET)
        mDescription = view.findViewById<EditText>(R.id.current_note_description_ET)

        // get selected radio button from radioGroup
        radioUpdate = view.findViewById<RadioGroup>(R.id.radioUpdate)

        // initiate the radioButton
        radioButtonHigh = view.findViewById<RadioButton>(R.id.radioUpdateHigh)
        radioButtonMedium = view.findViewById<RadioButton>(R.id.radioUpdateMedium)
        radioButtonLow = view.findViewById<RadioButton>(R.id.radioUpdateLow)
    }

    private fun setupMenu(view: View) {
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
                        updateItem(view)
                        true
                    }
                    else ->  {
                        findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                        true
                    }
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateItem(view: View) {
        val title = mTitle.text.toString()
        val description = mDescription.text.toString()

        // find the radioButton by returned id
        val selectedId =radioUpdate.checkedRadioButtonId
        val radioButton = view.findViewById<RadioButton>(selectedId)
        val mPriority = radioButton.text.toString()


        val validation = mSharedViewModel.verifyDataFromUser(title,description)

        if(validation){
            viewModel.data.observe(viewLifecycleOwner, Observer {
                val toDoData = viewModel.data.value
                if (toDoData != null) {
                    val updatedItem = viewModel.updateToDoData(toDoData.id,title,description,mPriority)
                    mTodoViewModel.updateData(updatedItem)
                    Timber.d("Update item id : ${updatedItem.id}")
                    findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                }
            })
        }else{
            // Put some message to inform user to fill all fields
            Timber.d("Validation failed during update")
        }
    }

    private fun putDataToUI(view: View) {

        // List of RadioButtons
        val radioButtonList = mutableListOf<RadioButton>()
        radioButtonList.add(radioButtonHigh)
        radioButtonList.add(radioButtonMedium)
        radioButtonList.add(radioButtonLow)

        // Need Code for Priority

        viewModel.data.observe(viewLifecycleOwner, Observer {
            val toDoData = viewModel.data.value
            if (toDoData != null) {
                mTitle.setText(toDoData.title)
                mDescription.setText(toDoData.description)
                viewModel.validatePriority(toDoData.priority,radioButtonList)
            }
        })

    }


}