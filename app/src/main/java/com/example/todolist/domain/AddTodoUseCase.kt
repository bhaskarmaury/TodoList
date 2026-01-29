package com.example.todolist.domain

import android.icu.text.CaseMap

class AddTodoUseCase(private val todoRepository: TodoRepository) {
    suspend fun execute(title: String){
        todoRepository.addTodo(Todo(title = title))

    }

}