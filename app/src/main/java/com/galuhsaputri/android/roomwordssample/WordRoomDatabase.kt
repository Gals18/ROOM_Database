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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
//TODO:3 Membuat Database Room
//Database mengekspos akses data objek melalui metode "getter" abstrak untuk setiap @Dao.
@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            //getDatabase akan menghasilkan singleton.
            //pola singleton adalah pola desain perangkat lunak yang
            // membatasi instantiasi kelas ke satu instance "tunggal".
            context: Context,
            scope: CoroutineScope
        ): WordRoomDatabase {
            // jika INSTANCE bukan null, kembalikan,
            // jika ya, buat databasenya
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                    // Menghapus dan membangun kembali alih-alih bermigrasi
                    // jika tidak ada objek Migrasi.
                    // Migrasi bukan bagian dari codelab ini.
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Ganti metode onCreate untuk mengisi database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Jika Anda ingin menyimpan data melalui restart aplikasi,
                // beri komentar pada baris berikut.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.wordDao())
                    }
                }
            }
        }

        /**
         * Isi database dalam coroutine baru.
         * Jika Anda ingin memulai dengan lebih banyak kata, tambahkan saja.
         */
        suspend fun populateDatabase(wordDao: WordDao) {
            // Mulai aplikasi dengan database bersih setiap saat.
            // Tidak diperlukan jika Anda hanya mengisi pada pembuatan.
            wordDao.deleteAll()

            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)
        }
    }
}
