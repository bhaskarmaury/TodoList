package com.example.todolist.uiscreens

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.TableInfo
import com.example.todolist.presentation.TodoViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(todoViewModel: TodoViewModel){
     var task by remember { mutableStateOf(value = "") }
    val todoList by todoViewModel.todos.collectAsStateWithLifecycle()


    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Todo List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {
                OutlinedTextField(
                    value = task,
                    onValueChange = { task = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(text = "Enter a task") },
                    label = { Text(text = "Add a task") },
                    shape = RoundedCornerShape(size = 22.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,

                        )

                )
                Button(
                    onClick = {
                        if (task.isNotBlank()) {
                            todoViewModel.addTodo(title = task)
                            task = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)

                ) {
                    Text(text = "Add")
                }
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
            HorizontalDivider(color = Color.Red)
            Spacer(modifier = Modifier.height(height = 12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(items = todoList, key = { it.id }) { todo ->
                    var isEditing by remember(key1 = todo.id) { mutableStateOf(value = false) }
                    var newTitle by remember(key1 = todo.id) { mutableStateOf(value = todo.title) }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = todo.isDone,
                                onCheckedChange = {
                                    todoViewModel.toggleTodoDone(todo = todo)

                                }, colors = CheckboxDefaults.colors(checkedColor = Color.Red)
                            )
                            if (isEditing) {
                                OutlinedTextField(
                                    value = newTitle,
                                    onValueChange = { newTitle = it },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(22.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent


                                    )
                                )
                                Button(
                                    onClick = {
                                        todoViewModel.editTodo(todo, newTitle)
                                        isEditing = false

                                    }, modifier = Modifier.padding(start = 4.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)

                                )
                                {
                                    Text(text = "Save")
                                }
                            } else {
                                Text(
                                    text = todo.title,
                                    modifier = Modifier.padding(8.dp),
                                    style = if (todo.isDone)
                                        LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
                                    else
                                        LocalTextStyle.current.copy(textDecoration = TextDecoration.None)

                                )

                            }
                        }
                        Row {
                            IconButton(onClick = {
                                if (!isEditing) newTitle = todo.title
                                isEditing = !isEditing

                            })
                            {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.Red

                                )
                            }
                            IconButton(onClick = {
                                todoViewModel.deleteTodo(todo)
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}