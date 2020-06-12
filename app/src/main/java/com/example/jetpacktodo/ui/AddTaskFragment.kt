package com.example.jetpacktodo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.jetpacktodo.ADD_TASK_OK
import com.example.jetpacktodo.EventObserver
import com.example.jetpacktodo.databinding.FragmentAddTaskBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 */
class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding

    //    private val viewModel: AddTaskViewModel by lazy {
//        ViewModelProvider(this).get(AddTaskViewModel::class.java)
//    }
    private val viewModel by viewModels<AddTaskViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //example
//        binding = DataBindingUtil.inflate(
//            inflater,
//            R.layout.fragment_add_task,
//            container,
//            false
//        )
        binding = FragmentAddTaskBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.snackBarEvent.observe(viewLifecycleOwner, EventObserver {
            Snackbar.make(view, getString(it), Snackbar.LENGTH_SHORT).show()
        })
        viewModel.taskSavedEvent.observe(viewLifecycleOwner, EventObserver {
            val action =
                AddTaskFragmentDirections.actionAddTaskFragmentToTaskListFragment(ADD_TASK_OK)
            findNavController().navigate(action)
        })
    }

}
