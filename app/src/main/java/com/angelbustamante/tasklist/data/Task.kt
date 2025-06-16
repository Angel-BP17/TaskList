package com.angelbustamante.tasklist.data

data class Task(
    val id: Int,
    var title: String,
    var dueDate: String
) {
    // Esto permite identificar tareas correctamente al editar/eliminar
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Task
        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}