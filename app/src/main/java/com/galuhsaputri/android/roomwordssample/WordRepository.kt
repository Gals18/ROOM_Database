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

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

//TODO:4 Membuat repositori
//untuk mengelola kueri dan memungkinkan Anda menggunakan beberapa backend.
//Repositori mengimplementasikan logika guna memutuskan apakah mengambil data
// dari jaringan atau menggunakan hasil yang di-cache di database lokal.
class WordRepository(private val wordDao: WordDao) {

    // Room mengeksekusi semua query pada thread terpisah.
    // Aliran yang Diamati akan memberi tahu pengamat ketika data telah berubah.
    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    // Secara default Room menjalankan kueri penangguhan dari utas utama, oleh karena itu, kita tidak perlu
    // mengimplementasikan hal lain untuk memastikan kita tidak melakukan pekerjaan database yang berjalan lama
    // keluar dari utas utama.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

}
