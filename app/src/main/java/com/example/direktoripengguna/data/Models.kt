package com.example.direktoripengguna.data

// Bentuk data sesuai JSON dari JSONPlaceholder.
// Field lain pada JSON (address, company, dst.) diabaikan oleh Gson.

data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String
)

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

// Body untuk request POST /posts
data class PostBaru(
    val title: String,
    val body: String,
    val userId: Int
)
