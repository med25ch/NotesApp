package com.resse.notesapp.data.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.resse.notesapp.R
import com.resse.notesapp.data.adapters.ToDoListAdapter
import com.resse.notesapp.data.dependencies.ToDoApplication
import com.resse.notesapp.data.interfaces.ItemClickListener
import com.resse.notesapp.data.models.ToDoData
import com.resse.notesapp.data.viewModels.*
import com.resse.notesapp.databinding.FragmentListBinding
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import timber.log.Timber


class ListFragment : Fragment() , ItemClickListener , SearchView.OnQueryTextListener{

    private var isFabOpen = false

    private val mTodoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory((activity?.application as ToDoApplication).repository)
    }

    private val mSharedViewModel : SharedViewModel by viewModels{
        SharedViewModelFactory(activity?.application)
    }

    private val mSavedStateViewModel : SavedStateViewModel by viewModels()


    private lateinit var viewModel: MyObservable
    private lateinit var recyclerViewAdapter : ToDoListAdapter
    private var searchCanProcess : Boolean = false


    // The type of binding class will change from fragment to fragment
    private var _binding : FragmentListBinding? = null

    private val binding get() = _binding!! // Helper Property

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Data binding
        _binding = FragmentListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mSharedViewModel = mSharedViewModel

        // Set Menu
        setupMenu()

        // Set Expandable Floating Button :

        setupExpandableFAB()

        // Set Recycler View
        val recyclerView = binding.recyclerView
        recyclerViewAdapter = ToDoListAdapter(this)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)


        //Animation for Recycler view
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 200
        }

        // Using SavedState
        var restoredIndex = mSavedStateViewModel.restoreSortingIndex()

        //Using SharedPreferences :
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.list_fragment_key), Context.MODE_PRIVATE)

        //Read From Shared pref :
        val restoredIndexFromSharedPref = readFromSharedPref()

        if (restoredIndex != null){
            matchSelectedChoiceWithSort(mSavedStateViewModel.restoreSortingIndex()!!)
        }else {
            if (sharedPref != null){
                matchSelectedChoiceWithSort(restoredIndexFromSharedPref)
            }else{
                matchSelectedChoiceWithSort(4)
            }
        }

        return binding.root
    }

    private fun setupExpandableFAB() {

        binding.floatingActionButton.setOnClickListener {
            if (!isFabOpen){
                showFABMenu()
            }else {
                closeFABMenu()
            }
        }

    }

    private fun showFABMenu() {
        isFabOpen = true
        binding.fbAddPersonalNote.animate().translationY(-resources.getDimension(R.dimen.standard_55))
        binding.fbAddIdeaNote.animate().translationY(-resources.getDimension(R.dimen.standard_105))

        binding.addPersonalNoteText.animate().translationY(-resources.getDimension(R.dimen.standard_55))
        binding.addIdeaNoteText.animate().translationY(-resources.getDimension(R.dimen.standard_105))

        binding.addPersonalNoteText.visibility = View.VISIBLE
        binding.addIdeaNoteText.visibility = View.VISIBLE
    }

    private fun closeFABMenu() {
        isFabOpen = false
        binding.fbAddPersonalNote.animate().translationY(0F)
        binding.fbAddIdeaNote.animate().translationY(0F)
        binding.addIdeaNoteText.visibility = View.GONE
        binding.addPersonalNoteText.visibility = View.GONE
    }



    private fun readFromSharedPref(): Int {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = 0
        return sharedPref?.getInt(getString(R.string.list_fragment_key), defaultValue)
            ?: defaultValue
    }

    private fun writeToSharedPref(index :Int){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(getString(R.string.list_fragment_key), index)
            apply()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[MyObservable::class.java]
           //ViewModelProviders.of(this)[MyObservable::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)

                //Search menu
                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@ListFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.menu_delete_all -> {
                        //delete all items
                        mTodoViewModel.confirmItemRemoval(requireContext())
                        true
                    }

                    R.id.menu_sort ->{
                        showRadioConfirmationDialog()
                        return true
                    }
                    else -> {
                        true
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onItemClickListener(data: ToDoData) {
        Timber.d("ToDoData Clicked: ID : ${data.id} - ${data.title} - ${data.priority} ")
        viewModel.data.value = data
        findNavController().navigate(R.id.action_listFragment_to_updateFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Timber.d("onQueryTextSubmit || Can Process Search : $searchCanProcess")
        if(query != null && searchCanProcess){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Timber.d("onQueryTextChange || Can Process Search : $searchCanProcess")
        if(newText != null && searchCanProcess){
            searchThroughDatabase(newText)
        }
        searchCanProcess = true
        return true
    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery = query
        searchQuery = "%$searchQuery%"

        mTodoViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner) { toDos ->
            // Update the cached copy of the words in the adapter.
            toDos.let {
                recyclerViewAdapter.submitList(it)
                Timber.d("User searched for : ID : $searchQuery || Found : ${toDos.count()} notes")
            }
        }
    }

    fun showRadioConfirmationDialog() {
        var dialogChoices = mTodoViewModel.dialogChoices
        var selectedChoice : String
        var selectedChoiceIndex = readFromSharedPref()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sort by")
            .setSingleChoiceItems(dialogChoices, selectedChoiceIndex) { dialog_, which ->
                mTodoViewModel.selectedChoiceIndex = which
                selectedChoice = dialogChoices[which]
            }
            .setPositiveButton("Ok") { dialog, which ->
                Toast.makeText(context, "index ${mTodoViewModel.selectedChoiceIndex}", Toast.LENGTH_SHORT)
                    .show()
                matchSelectedChoiceWithSort(mTodoViewModel.selectedChoiceIndex)

                //Save index to SavedStateHandle
                mSavedStateViewModel.saveSortingIndex(mTodoViewModel.selectedChoiceIndex)

                //Write to shared Pref
                writeToSharedPref(mTodoViewModel.selectedChoiceIndex)

            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun matchSelectedChoiceWithSort(selectedChoiceIndex: Int) {
        when(selectedChoiceIndex){
            0 -> {
                // Add an observer on the LiveData returned by allToDoData.
                // The onChanged() method fires when the observed data changes and the activity is
                // in the foreground.
                mTodoViewModel.allToDoData.observe(viewLifecycleOwner) { toDos ->
                    // Update the cached copy of the notes in the adapter.
                    toDos.let {
                        recyclerViewAdapter.submitList(it)
                        mSharedViewModel.isDatabaseEmpty(toDos)

                    }
                }
                Timber.d("Sorting notes : Newest to Oldest")
            }
            1 -> {
                // Add an observer on the LiveData returned by allToDoData.
                // The onChanged() method fires when the observed data changes and the activity is
                // in the foreground.
                mTodoViewModel.sortHighToLow().observe(viewLifecycleOwner){ toDos ->
                    // Update the cached copy of the words in the adapter.
                    toDos.let {
                        recyclerViewAdapter.submitList(it)
                        mSharedViewModel.isDatabaseEmpty(toDos)
                    }
                }

                Timber.d("Sorting notes : High to Low Priority")
            }
            2 -> {
                mTodoViewModel.sortLowToHigh().observe(viewLifecycleOwner){ toDos ->
                    // Update the cached copy of the words in the adapter.
                    toDos.let {
                        recyclerViewAdapter.submitList(it)
                        mSharedViewModel.isDatabaseEmpty(toDos)
                    }
                }

                Timber.d("Sorting notes : Low to High Priority")
            }
            3 -> {
                mTodoViewModel.sortHighToLow().observe(viewLifecycleOwner){ toDos ->
                    // Update the cached copy of the words in the adapter.
                    toDos.let {
                        recyclerViewAdapter.submitList(it)
                        mSharedViewModel.isDatabaseEmpty(toDos)
                    }
                }

                Timber.d("Sorting notes : Oldest to Newest")
            }
            4 -> {
                mTodoViewModel.sortHighToLow().observe(viewLifecycleOwner){ toDos ->
                    // Update the cached copy of the words in the adapter.
                    toDos.let {
                        recyclerViewAdapter.submitList(it)
                        mSharedViewModel.isDatabaseEmpty(toDos)
                    }
                }}
            else -> {
                mTodoViewModel.allToDoData.observe(viewLifecycleOwner) { toDos ->
                    // Update the cached copy of the notes in the adapter.
                    toDos.let {
                        recyclerViewAdapter.submitList(it)
                        mSharedViewModel.isDatabaseEmpty(toDos)
                    }
                }
            }
        }
    }
}