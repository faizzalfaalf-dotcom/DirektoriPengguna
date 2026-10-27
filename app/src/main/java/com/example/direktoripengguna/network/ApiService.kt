package com.example.direktoripengguna.network

import com.example.direktoripengguna.data.Post
import com.example.direktoripengguna.data.PostBaru
import com.example.direktoripengguna.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // GET https://jsonplaceholder.typicode.com/users
    @GET("users")
    suspend fun getUsers(): List<User>

    // GET https://jsonplaceholder.typicode.com/users/{id}/posts
    @GET("users/{id}/posts")
    suspend fun getPostsByUser(@Path("id") userId: Int): List<Post>

    // POST https://jsonplaceholder.typicode.com/posts  (balasan 201 + id baru)
    @POST("posts")
    suspend fun buatPost(@Body post: PostBaru): Post

    // DELETE https://jsonplaceholder.typicode.com/posts/{id} (balasan 200 OK)
    @DELETE("posts/{id}")
    suspend fun hapusPost(@Path("id") postId: Int): Response<Unit>
}
