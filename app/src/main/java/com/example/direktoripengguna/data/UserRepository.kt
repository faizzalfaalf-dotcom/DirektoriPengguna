package com.example.direktoripengguna.data

import com.example.direktoripengguna.network.ApiService
import com.example.direktoripengguna.network.RetrofitClient
import retrofit2.Response

class UserRepository(private val api: ApiService = RetrofitClient.api) {

    suspend fun getUsers(): List<User> = api.getUsers()

    suspend fun getPostsByUser(userId: Int): List<Post> = api.getPostsByUser(userId)

    suspend fun buatPost(post: PostBaru): Post = api.buatPost(post)

    suspend fun hapusPost(postId: Int): Response<Unit> = api.hapusPost(postId)
}
