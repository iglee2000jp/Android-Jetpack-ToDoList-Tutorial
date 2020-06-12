package com.example.jetpacktodo.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpacktodo.EventObserver
import com.example.jetpacktodo.FilterType
import com.example.jetpacktodo.R

import com.example.jetpacktodo.databinding.FragmentTaskListBinding
import com.example.jetpacktodo.databinding.ListItemBinding
import com.example.jetpacktodo.source.model.Task
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


class TaskListFragment : Fragment() {
    private val viewModel by viewModels<TaskListViewModel>()
    private lateinit var viewDataBinding: FragmentTaskListBinding
    private lateinit var adapter: TaskAdapter
    private val args: TaskListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentTaskListBinding.inflate(inflater, container, false)
        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            Timber.d("args: ${args.result}")
            viewModel.showResult(args.result)
        }
        setupListAdapter()
        viewModel.navigateToDetails.observe(viewLifecycleOwner,
            EventObserver {
                val action =
                    TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(it)
                findNavController().navigate(action)
            })
        viewModel.navigateToAddTask.observe(viewLifecycleOwner,
            EventObserver {
                val action =
                    TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment()
                findNavController().navigate(action)
            })
        viewModel.snackBarEvent.observe(viewLifecycleOwner, EventObserver {
            Snackbar.make(view, getString(it), Snackbar.LENGTH_SHORT).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_list_filter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.active_tasks -> {
                viewModel.selectFilterType(FilterType.SHOW_ACTIVE)
                Toast.makeText(
                    context,
                    getString(R.string.current_active_tasks),
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
            R.id.completed_tasks -> {
                viewModel.selectFilterType(FilterType.SHOW_COMPLETED)
                Toast.makeText(
                    context,
                    getString(R.string.current_completed_tasks),
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
            R.id.all_tasks -> {
                viewModel.selectFilterType(FilterType.SHOW_ALL)
                Toast.makeText(context, getString(R.string.current_all_tasks), Toast.LENGTH_SHORT)
                    .show()
                true
            }
            else -> false
        }
    }

    private fun setupListAdapter() {
        adapter = TaskAdapter(viewModel)
        viewDataBinding.listRecycler.adapter = adapter
        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            tasks?.let {
                adapter.submitList(tasks)
            }
        })
    }

    private inner class TaskAdapter(private val viewModel: TaskListViewModel) :
        ListAdapter<Task, TaskViewHolder>(
            TaskDiffCallback()
        ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            return TaskViewHolder.from(
                parent
            )
        }

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            val task = getItem(position)
            holder.bind(task, viewModel)
        }

    }

    private class TaskViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): TaskViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBinding.inflate(layoutInflater, parent, false)
                return TaskViewHolder(
                    binding
                )
            }
        }

        fun bind(task: Task, viewModel: TaskListViewModel) {
            binding.task = task
            binding.viewModel = viewModel
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.taskId == newItem.taskId
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

}

