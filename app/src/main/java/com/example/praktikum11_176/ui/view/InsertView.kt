package com.example.praktikum11_176.ui.view

import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.view.ScrollingView
import com.example.praktikum11_176.ui.viewmodel.FormErrorState
import com.example.praktikum11_176.ui.viewmodel.FormState
import com.example.praktikum11_176.ui.viewmodel.HomeUiState
import com.example.praktikum11_176.ui.viewmodel.InsertUiState
import com.example.praktikum11_176.ui.viewmodel.InsertViewModel
import com.example.praktikum11_176.ui.viewmodel.MahasiswaEvent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.viewpager.widget.ViewPager.LayoutParams
import com.example.praktikum11_176.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertMhsView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiState //state utama utk loading, success, error
    val uiEvent = viewModel.uiEvent //state untuk form dan validasi
    val snackbarHostState = remember {SnackbarHostState()}
    val coroutineScope = rememberCoroutineScope()
//    val scrollView = ScrollView(this).apply {
//        layoutParams = LinearLayout.LayoutParams(
//            LinearLayout = LayoutParams.MATCH_PARENT,
//            LinearLayout = LayoutParams.MATCH_PARENT
//        )
//    }

    //observasi perubahan state untuk snackbar dan navigasi
    LaunchedEffect(uiState) {
        when (uiState) {
            is FormState.Success -> {
                println(
                    "InsertMhsView: uiState is FormState.Success, navigate to home" + uiState.message
                )
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
                delay(700)
                onNavigate() //navigasi langsung

                viewModel.resetSnackBarMessage() //reset snackbar state
            }
            is FormState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
            else -> Unit
        }
    }

    Scaffold (
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        topBar = {
            TopAppBar(
                title = { Text("Tambah Mahasiswa")},
                navigationIcon = {
                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ){ padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(5.dp)
        ) {
            InsertBodyMhs(
                uiState = uiEvent,
                homeUiState = uiState,
                onValueChange = {updateEvent ->
                  viewModel.updateState(updateEvent)
                },
                onClick = {
                    if (viewModel.validateFields()) {
                        viewModel.insertMahasiswa()
                    }
                }
            )
        }
    }
}

@Composable
fun InsertBodyMhs(
    modifier: Modifier = Modifier,
    onValueChange: (MahasiswaEvent) -> Unit,
    uiState: InsertUiState,
    onClick: () -> Unit,
    homeUiState: FormState
) {
    Column (
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormMahasiswa(
            mahasiswaEvent = uiState.insertUiEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = homeUiState !is FormState.Loading,
        ) {
            if (homeUiState is FormState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .size(10.dp)
                        .padding(end = 8.dp)
                )
                Text("Loading ...")
            } else {
                Text("Add")
            }
        }
    }
}

@Composable
fun FormMahasiswa(
    mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    onValueChange: (MahasiswaEvent) -> Unit,
    errorState: FormErrorState = FormErrorState(),
    modifier: Modifier = Modifier
) {
    val jenis_kelamin = listOf("Laki-laki", "Perempuan")
    val kelas = listOf("A", "B", "C", "D", "E")

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nama,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(nama = it))
            },
            label = { Text("Nama")},
            isError = errorState.nama != null,
            placeholder = { Text("Masukkan nama")},
        )
        Text(
            text = errorState.nama ?: "",
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(1.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nim,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(nim = it))
            },
            label = { Text("NIM")},
            isError = errorState.nim != null,
            placeholder = { Text("Masukkan NIM")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = errorState.nim ?: "",
            color = Color.Red)

        Spacer(modifier = Modifier.height(1.dp))
        Text(text = "Jenis Kelamin")
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            jenis_kelamin.forEach { jk ->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    RadioButton(
                        selected = mahasiswaEvent.jenis_kelamin == jk,
                        onClick = {
                            onValueChange(mahasiswaEvent.copy(jenis_kelamin = jk))
                        },
                    )
                    Text(
                        text = jk,
                    )
                }
            }
        }
        Text(
            text = errorState.jenis_kelamin ?: "",
            color = Color.Red
        )

//        OutlinedTextField(
//            modifier = Modifier.fillMaxWidth(),
//            value = mahasiswaEvent.jenis_kelamin,
//            onValueChange = {
//                onValueChange(mahasiswaEvent.copy(jenis_kelamin = it))
//            },
//            label = { Text("Jenis Kelamin")},
//            isError = errorState.jenis_kelamin != null,
//            placeholder = { Text("Masukkan Jenis Kelamin")},
//        )
//        Text(
//            text = errorState.jenis_kelamin ?: "",
//            color = Color.Red)
        Spacer(modifier = Modifier.height(1.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.alamat,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(alamat = it))
            },
            label = { Text("Alamat")},
            isError = errorState.alamat != null,
            placeholder = { Text("Masukkan alamat")},
        )
        Text(
            text = errorState.alamat ?: "",
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(1.dp))
        Text(text = "Kelas")
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            kelas.forEach { kelas ->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    RadioButton(
                        selected = mahasiswaEvent.kelas == kelas,
                        onClick = {
                            onValueChange(mahasiswaEvent.copy(kelas = kelas))
                        },
                    )
                    Text(
                        text = kelas,
                    )
                }
            }
        }
        Text(
            text = errorState.kelas ?: "",
            color = Color.Red
        )

//        OutlinedTextField(
//            modifier = Modifier.fillMaxWidth(),
//            value = mahasiswaEvent.kelas,
//            onValueChange = {
//                onValueChange(mahasiswaEvent.copy(kelas = it))
//            },
//            label = { Text("Kelas")},
//            isError = errorState.kelas != null,
//            placeholder = { Text("Masukkan Kelas")},
//        )
//        Text(
//            text = errorState.kelas ?: "",
//            color = Color.Red)

        Spacer(modifier = Modifier.height(1.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.angkatan,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(angkatan = it))
            },
            label = { Text("Angkatan")},
            isError = errorState.angkatan != null,
            placeholder = { Text("Masukkan angkatan")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = errorState.angkatan?: "",
            color = Color.Red
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.judulSkripsi,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(judulSkripsi = it))
            },
            label = { Text("Judul Skripsi")},
            isError = errorState.judulSkripsi != null,
            placeholder = { Text("Masukkan Judul Skripsi")},
        )
        Text(
            text = errorState.judulSkripsi?: "",
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(1.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dosen1,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(dosen1 = it))
            },
            label = { Text("Dosen Pembimbing 1")},
            isError = errorState.dosen1 != null,
            placeholder = { Text("Masukkan Dosen Pembimbing 1")},
        )
        Text(
            text = errorState.dosen1?: "",
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(1.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dosen2,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(dosen2 = it))
            },
            label = { Text("Dosen Pembimbing 2")},
            isError = errorState.dosen2 != null,
            placeholder = { Text("Masukkan osen Pembimbing 2")},
        )
        Text(
            text = errorState.dosen2?: "",
            color = Color.Red
        )
    }
}