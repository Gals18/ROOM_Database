/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.galuhsaputri.android.roomwordssample

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * The Room Magic is in this file, where you map a method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */
//TODO:2 Membuat DAO atau data akses objek
//dalam kode ini kita membuat kelas abstak yaitu WordDao
@Dao
interface WordDao {

    // Aliran selalu menyimpan/men-cache versi data terbaru. Memberi tahu pengamatnya saat
    //data telah berubah.
    @Query("SELECT * FROM word_table ORDER BY word DESC")
    fun getAlphabetizedWords(): Flow<List<Word>>
    //kode diatas merupakan query untuk menampilkan data dengan berdasarkan asceding.
    //sehinggga yang data yang tertampilkan dilayar

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)
    //kode diatas digunakan untuk membuat data baru dengan menginsert atau menambahkan data
    //dan data akan ditambahkan kedalam tabel_word.
    //Strategi onConflict yang dipilih akan mengabaikan kata baru jika sama persis
    // dengan kata yang sudah ada dalam daftar.
    // Untuk mengetahui lebih lanjut strategi konflik yang tersedia, lihat dokumentasi.

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
    //kode diatas untuk menghapus baris, tetapi blum saya buat dalam aplikasi ini untuk tombolnya.

    @Delete
    fun delete(word: Word)

}
