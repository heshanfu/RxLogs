package com.blackbox.plog.utils

import android.annotation.SuppressLint
import java.io.*
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec


fun checkIfKeyValid(encKey: String): String {

    if (encKey.isEmpty())
        throw(Throwable("Invalid key provided. Can not encrypt with empty key."))

    if (encKey.length < 32)
        throw(Throwable("Invalid key size. Size should be 32 at-least."))

    return encKey.substring(0, 32)
}

@Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
fun generateKey(encKey: String): SecretKey {
    val salt = checkIfKeyValid(encKey)
    val key = salt.toBytes()
    return SecretKeySpec(key, "AES")
}

@SuppressLint("GetInstance")
fun readFileDecrypted(key: SecretKey, filePath: String): String {
    var data = ""

    try {

        val aes2 = Cipher.getInstance("AES/ECB/PKCS5Padding")
        aes2.init(Cipher.DECRYPT_MODE, key)

        val fis = FileInputStream(filePath)
        val cipherInputStream = CipherInputStream(fis, aes2)
        val baos = ByteArrayOutputStream()

        baos.write(cipherInputStream.readBytes())

        //Convert decrypted bytes to String
        data = String(baos.toByteArray())

    } catch (ex: NoSuchAlgorithmException) {
        ex.printStackTrace()
    } catch (ex: NoSuchPaddingException) {
        ex.printStackTrace()
    } catch (ex: InvalidKeyException) {
        ex.printStackTrace()
    } catch (ex: IOException) {
        ex.printStackTrace()
    }

    return data
}

@SuppressLint("GetInstance")
fun appendToFileEncrypted(data: String, key: SecretKey, filePath: String) {
    try {
        val aes = Cipher.getInstance("AES/ECB/PKCS5Padding")
        aes.init(Cipher.ENCRYPT_MODE, key)
        val fs = FileOutputStream(File(filePath), true)
        val out = CipherOutputStream(fs, aes)
        out.write(data.toBytes())
        out.flush()
        out.close()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: NoSuchPaddingException) {
        e.printStackTrace()
    } catch (e: InvalidKeyException) {
        e.printStackTrace()
    } catch (e: IllegalBlockSizeException) {
        e.printStackTrace()
    } catch (e: BadPaddingException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@SuppressLint("GetInstance")
fun writeToFileEncrypted(data: String, key: SecretKey, filePath: String) {
    try {
        val aes = Cipher.getInstance("AES/ECB/PKCS5Padding")
        aes.init(Cipher.ENCRYPT_MODE, key)
        val fs = FileOutputStream(filePath)
        val out = CipherOutputStream(fs, aes)
        out.write(data.toBytes())
        out.flush()
        out.close()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: NoSuchPaddingException) {
        e.printStackTrace()
    } catch (e: InvalidKeyException) {
        e.printStackTrace()
    } catch (e: IllegalBlockSizeException) {
        e.printStackTrace()
    } catch (e: BadPaddingException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun String.toBytes(): ByteArray {
    return this.toByteArray(Charsets.UTF_8)
}