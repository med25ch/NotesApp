package com.resse.notesapp.data.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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

    private val mTodoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory((activity?.application as ToDoApplication).repository)
    }

    private val mSharedViewModel : SharedViewModel by viewModels{
        SharedViewModelFactory(activity?.application)
    }

    private lateinit var viewModel: MyObservable
    private lateinit var recyclerViewAdapter : ToDoListAdapter

    // The type of binding class will change from fragment to fragment
    private var _binding : FragmentListBinding? = null

    private val binding get() = _binding!! // Helper Property

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Data binding
        _binding = FragmentListBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        // Set Menu
        setupMenu()


        // Set Recycler View
        val recyclerView = binding.recyclerView
        recyclerViewAdapter = ToDoListAdapter(this)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        //Animation for Recycler view
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 200
        }

        // Add an observer on the LiveData returned by allToDoData.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mTodoViewModel.allToDoData.observe(viewLifecycleOwner) { toDos ->
            // Update the cached copy of the words in the adapter.
            toDos.let {
                recyclerViewAdapter.submitList(it)
                mSharedViewModel.isDatabaseEmpty(toDos)
            }
        }

        return binding.root
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
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            searchThroughDatabase(newText)
        }
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
}