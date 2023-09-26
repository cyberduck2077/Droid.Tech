package ru.data.common.ds.di

import java.io.File

interface ProvideFile {
    fun getFileMain(): File
    fun getFileCrypto(): File
}
