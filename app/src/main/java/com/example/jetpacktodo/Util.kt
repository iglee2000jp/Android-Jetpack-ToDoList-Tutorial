package com.example.jetpacktodo

const val ADD_TASK_OK = 1
const val DELETE_TASK_OK = 2
const val EDIT_TASK_OK = 3

enum class FilterType {
    SHOW_ALL,
    SHOW_ACTIVE,
    SHOW_COMPLETED
}