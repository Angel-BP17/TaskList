package com.angelbustamante.tasklist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angelbustamante.tasklist.adapters.TaskAdapter
import com.angelbustamante.tasklist.data.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private val taskList = mutableListOf<Task>()
    private var nextId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar Toolbar como ActionBar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "TaskList"

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // CORRECCIÓN: Usar la misma instancia de lista para el adaptador
        adapter = TaskAdapter(taskList) { task, context ->
            showOptionsDialog(task, context)
        }
        recyclerView.adapter = adapter

        // Botón para añadir tareas
        findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener {
            showAddEditTaskDialog(null)
        }

        // Ejemplo de tarea inicial para pruebas (opcional)
        taskList.add(Task(nextId++, "Tarea de ejemplo", "01/01/2023"))
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings) {
            // Abrir actividad de ajustes
            Toast.makeText(this, "Ajustes de la aplicación", Toast.LENGTH_SHORT).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showOptionsDialog(task: Task, context: Context) {
        val options = arrayOf("Editar", "Eliminar")
        AlertDialog.Builder(context)
            .setTitle("Opciones de tarea")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showAddEditTaskDialog(task)
                    1 -> deleteTask(task)
                }
            }
            .show()
    }

    private fun showAddEditTaskDialog(task: Task?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_task, null)
        val editTitle = dialogView.findViewById<EditText>(R.id.editTitle)
        val editDate = dialogView.findViewById<EditText>(R.id.editDate)

        val dialogTitle = if (task == null) R.string.titulo_nueva_tarea else R.string.titulo_editar_tarea
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(dialogTitle)

        dialogBuilder.setPositiveButton(R.string.guardar) { _, _ ->
            val title = editTitle.text.toString()
            val date = editDate.text.toString()

            if (title.isNotEmpty() && date.isNotEmpty()) {
                if (task == null) {
                    // Nueva tarea
                    taskList.add(Task(nextId++, title, date))
                    // CORRECCIÓN: Notificar inserción específica
                    adapter.notifyItemInserted(taskList.size - 1)
                } else {
                    // Editar tarea existente
                    val position = taskList.indexOfFirst { it.id == task.id }
                    if (position != -1) {
                        taskList[position].title = title
                        taskList[position].dueDate = date
                        // CORRECCIÓN: Notificar cambio específico
                        adapter.notifyItemChanged(position)
                    }
                }
            }
        }

        dialogBuilder.setNegativeButton(R.string.cancelar, null)

        // Prellenar campos si estamos editando
        task?.let {
            editTitle.setText(it.title)
            editDate.setText(it.dueDate)
        }

        dialogBuilder.show()
    }

    private fun deleteTask(task: Task) {
        val position = taskList.indexOfFirst { it.id == task.id }
        if (position != -1) {
            taskList.removeAt(position)
            // CORRECCIÓN: Notificar eliminación específica
            adapter.notifyItemRemoved(position)
        }
        Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
    }
}