package com.example.jetpacktodo.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.jetpacktodo.DELETE_TASK_OK
import com.example.jetpacktodo.EventObserver
import com.example.jetpacktodo.R
import com.example.jetpacktodo.databinding.FragmentTaskDetailBinding
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


class TaskDetailFragment : Fragment() {
    private lateinit var binding: FragmentTaskDetailBinding
    private val viewModel by viewModels<TaskDetailViewModel>()
    private val args: TaskDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("taskId: ${args.taskId}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskDetailBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        arguments?.let {
            viewModel.loadTask(args.taskId)
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.deleteTaskEvent.observe(viewLifecycleOwner, EventObserver {
            val action =
                TaskDetailFragmentDirections.actionTaskDetailFragmentToTaskListFragment(
                    DELETE_TASK_OK
                )
            findNavController().navigate(action)
        })

//        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
//            viewModel.updateCheckbox(isChecked)
//        }

        viewModel.snackBarEvent.observe(viewLifecycleOwner, EventObserver {
            Snackbar.make(view, getString(it), Snackbar.LENGTH_SHORT).show()
        })

        viewModel.navigateToEdit.observe(viewLifecycleOwner, EventObserver {
            val action =
                TaskDetailFragmentDirections.actionTaskDetailFragmentToEditTaskFragment(it)
            findNavController().navigate(action)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_detail_delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {
                viewModel.deleteTask()
                true
            }
            else -> false
        }
    }
}
