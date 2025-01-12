package com.example.praktikum11_176.ui.viewmodel

import com.example.praktikum11_176.model.Mahasiswa

//data class variabel yang menyampaikan data input form
data class MahasiswaEvent(
    val nim: String = "",
    val nama: String = "",
    val jenis_kelamin: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = ""
)