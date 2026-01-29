package com.example.todolist.data

import android.R.attr.title
import com.example.todolist.domain.Todo
import com.example.todolist.domain.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoDataRepository (private val dao: TodoDao): TodoRepository{
    override fun getTodos(): Flow<List<Todo>> {
        return dao.getAllTodos().map{list ->
            list.map {
                Todo(it.id, it.title, it.isDone)
            }


        }
    }

    override suspend fun addTodo(todo: Todo) {
        dao.insert(TodoEntity(title = todo.title, isDone= todo.isDone))

    }

    override suspend fun updateTodo(todo: Todo) {
         dao.update(TodoEntity(title = todo.title, isDone= todo.isDone))
    }
    override suspend fun deleteTodo(todo: Todo){
        dao.delete(TodoEntity(title = todo.title, isDone= todo.isDone))
    
    }
}