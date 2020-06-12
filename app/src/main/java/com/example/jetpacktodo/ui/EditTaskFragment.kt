package com.example.jetpacktodo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.jetpacktodo.EDIT_TASK_OK
import com.example.jetpacktodo.EventObserver
import com.example.jetpacktodo.databinding.FragmentEditTaskBinding
import com.google.android.material.snackbar.Snackbar

class EditTaskFragment : Fragment() {
    private lateinit var binding: FragmentEditTaskBinding
    private val viewModel by viewModels<EditTaskViewModel>()
    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTaskBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        arguments?.let {
            viewModel.loadTask(args.taskId)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.snackBarEvent.observe(viewLifecycleOwner, EventObserver {
            Snackbar.make(view, getString(it), Snackbar.LENGTH_SHORT).show()
        })

        viewModel.taskUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action =
                EditTaskFragmentDirections.actionEditTaskFragmentToTaskListFragment(EDIT_TASK_OK)
            findNavController().navigate(action)
        })
    }

}