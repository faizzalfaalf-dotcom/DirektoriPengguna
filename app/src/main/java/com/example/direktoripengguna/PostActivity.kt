package com.example.direktoripengguna

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.direktoripengguna.data.Post
import com.example.direktoripengguna.data.PostBaru
import com.example.direktoripengguna.data.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PostActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_ID = "user_id"
        const val EXTRA_USER_NAME = "user_name"
    }

    private var userId = 0
    private val repository = UserRepository()
    private var daftarPost: MutableList<Post> = mutableListOf()

    private lateinit var etJudul: EditText
    private lateinit var btnTambah: Button
    private lateinit var tvStatus: TextView
    private lateinit var listPost: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        userId = intent.getIntExtra(EXTRA_USER_ID, 0)
        title = intent.getStringExtra(EXTRA_USER_NAME) ?: "Postingan"

        etJudul = findViewById(R.id.etJudul)
        btnTambah = findViewById(R.id.btnTambah)
        tvStatus = findViewById(R.id.tvStatus)
        listPost = findViewById(R.id.listPost)

        btnTambah.setOnClickListener { kirimPost() }

        listPost.setOnItemLongClickListener { _, _, position, _ ->
            val post = daftarPost[position]
            AlertDialog.Builder(this)
                .setTitle("Hapus Postingan")
                .setMessage("Apakah Anda yakin ingin menghapus postingan \"${post.title}\"?")
                .setPositiveButton("Hapus") { _, _ ->
                    hapusPost(post, position)
                }
                .setNegativeButton("Batal", null)
                .show()
            true
        }

        muatPosts()
    }

    private fun muatPosts() {
        tvStatus.text = "Memuat postingan..."

        lifecycleScope.launch {
            try {
                val posts = repository.getPostsByUser(userId)
                daftarPost = posts.toMutableList()
                tampilkanDaftarPost()
                tvStatus.text = "${daftarPost.size} postingan"
            } catch (e: HttpException) {
                tvStatus.text = "Server error: HTTP ${e.code()}"
            } catch (e: IOException) {
                tvStatus.text = "Tidak ada koneksi internet"
            }
        }
    }

    private fun tampilkanDaftarPost() {
        listPost.adapter = ArrayAdapter(
            this@PostActivity,
            android.R.layout.simple_list_item_1,
            daftarPost.map { it.title }
        )
    }

    private fun kirimPost() {
        val judul = etJudul.text.toString().trim()
        if (judul.isEmpty()) {
            etJudul.error = "Judul tidak boleh kosong"
            return
        }

        btnTambah.isEnabled = false
        lifecycleScope.launch {
            try {
                val baru = PostBaru(
                    title = judul,
                    body = "Dibuat dari aplikasi Direktori Pengguna",
                    userId = userId
                )
                val hasil = repository.buatPost(baru)
                tvStatus.text = "Tersimpan dengan id ${hasil.id}"
                etJudul.text.clear()
            } catch (e: HttpException) {
                tvStatus.text = "Gagal menyimpan: HTTP ${e.code()}"
            } catch (e: IOException) {
                tvStatus.text = "Tidak ada koneksi internet"
            } finally {
                btnTambah.isEnabled = true
            }
        }
    }

    private fun hapusPost(post: Post, position: Int) {
        tvStatus.text = "Menghapus postingan..."

        lifecycleScope.launch {
            try {
                val response = repository.hapusPost(post.id)
                if (response.isSuccessful) {
                    daftarPost.removeAt(position)
                    tampilkanDaftarPost()
                    tvStatus.text = "Postingan id ${post.id} berhasil dihapus"
                } else {
                    tvStatus.text = "Gagal menghapus: HTTP ${response.code()}"
                }
            } catch (e: HttpException) {
                tvStatus.text = "Gagal menghapus: HTTP ${e.code()}"
            } catch (e: IOException) {
                tvStatus.text = "Tidak ada koneksi internet"
            }
        }
    }
}
