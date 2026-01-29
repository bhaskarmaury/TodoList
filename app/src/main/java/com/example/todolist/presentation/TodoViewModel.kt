package com.example.todolist.presentation

import android.app.Application
import android.icu.text.CaseMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Room
import com.example.todolist.data.TodoDataRepository
import com.example.todolist.data.TodoDatabase
import com.example.todolist.domain.AddTodoUseCase
import com.example.todolist.domain.Todo
import com.example.todolist.domain.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class TodoViewModel(application: Application): AndroidViewModel(application) {
    private  val db = Room.databaseBuilder(
        context = application,
        klass = TodoDatabase::class.java,
        name = "my_todo_db"
    ).build()
    private val todoRepository= TodoDataRepository(dao = db.todoDao())
    private val addUseCase = AddTodoUseCase(todoRepository)


    open val todos = todoRepository.getTodos().stateIn(
        viewModelScope, started = SharingStarted.Lazily, initialValue = emptyList()
    )
    fun addTodo(title: String){
        viewModelScope.launch {
            addUseCase.execute(title)
        }
    }

    fun toggleTodoDone(todo: Todo){
        viewModelScope.launch {
            todoRepository.updateTodo(todo = todo.copy(isDone = !todo.isDone))
        }

    }

    fun editTodo(todo: Todo, newTitle: String){
        viewModelScope.launch {
            if (newTitle.isNotBlank()){
                todoRepository.updateTodo(todo = todo.copy(title = newTitle))
            }

        }
    }
    fun deleteTodo(todo: Todo){
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }

    }
}