package com.angelbustamante.tasklist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angelbustamante.tasklist.R
import com.angelbustamante.tasklist.data.Task

class TaskAdapter(
    private val tasks: MutableList<Task>, // Usar val en lugar de var
    private val onLongClick: (Task, Context) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.taskTitle)
        val dueDate: TextView = itemView.findViewById(R.id.taskDueDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.dueDate.text = "Vence: ${task.dueDate}"

        holder.itemView.setOnLongClickListener {
            onLongClick(task, holder.itemView.context)
            true
        }
    }

    override fun getItemCount() = tasks.size
}