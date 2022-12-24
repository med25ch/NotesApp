package com.resse.notesapp.data.fragments

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.resse.notesapp.R
import com.resse.notesapp.data.dependencies.ToDoApplication
import com.resse.notesapp.data.viewModels.*
import com.resse.notesapp.databinding.FragmentUpdateBinding
import timber.log.Timber


class UpdateFragment : Fragment() {

    private lateinit var viewModel: MyObservable

    private val mTodoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory((activity?.application as ToDoApplication).repository)
    }

    private val mSharedViewModel : SharedViewModel by viewModels{
        SharedViewModelFactory(activity?.application)
    }

    // The type of binding class will change from fragment to fragment
    private var _binding : FragmentUpdateBinding? = null

    private val binding get() = _binding!! // Helper Property

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Data binding
        _binding = FragmentUpdateBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mSharedViewModel = mSharedViewModel

        // Set Menu
        setupMenu()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[MyObservable::class.java]
            //ViewModelProviders.of(this)[MyObservable::class.java]
        } ?: throw Exception("Invalid Activity")

        // Update UI with Data
        putDataToUI()
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
                        updateItem()
                        true
                    }
                    R.id.menu_delete -> {
                        //delete item
                        viewModel.data.observe(viewLifecycleOwner, Observer {
                            val toDoData = viewModel.data.value
                            if (toDoData != null) {
                                mSharedViewModel.confirmItemRemoval(requireContext(),mTodoViewModel,toDoData,this@UpdateFragment)
                            }
                        })
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

    private fun updateItem() {
        val title = binding.currentNoteTitleET.text.toString()
        val description = binding.currentNoteDescriptionET.text.toString()

        // find the radioButton by returned id
        val selectedId =binding.radioUpdate.checkedRadioButtonId
        val radioButton = binding.radioUpdate.findViewById<RadioButton>(selectedId)
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

    private fun putDataToUI() {

        // List of RadioButtons
        val radioButtonList = mutableListOf<RadioButton>()
        radioButtonList.add(binding.radioUpdateHigh)
        radioButtonList.add(binding.radioUpdateMedium)
        radioButtonList.add(binding.radioUpdateLow)

        // Need Code for Priority

        viewModel.data.observe(viewLifecycleOwner, Observer {
            val toDoData = viewModel.data.value
            if (toDoData != null) {
                binding.currentNoteTitleET.setText(toDoData.title)
                binding.currentNoteDescriptionET.setText(toDoData.description)
                viewModel.validatePriority(toDoData.priority,radioButtonList)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}