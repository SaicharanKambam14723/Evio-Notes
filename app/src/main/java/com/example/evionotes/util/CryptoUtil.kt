package com.example.evionotes.util

import java.security.MessageDigest
import android.util.Base64 as AndroidBase64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoUtil @Inject constructor() {
    private val algorithm = "AES/ECB/PKCS1Padding"
    private val keyAlgorithm = "AES"

    fun generateSalt(): String {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return AndroidBase64.encodeToString(salt, AndroidBase64.DEFAULT)
    }

    fun hashPassword(password: String, salt: String): String {
        val saltBytes = AndroidBase64.decode(salt, AndroidBase64.DEFAULT)
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(saltBytes)
        val hash = digest.digest(password.toByteArray())
        return AndroidBase64.encodeToString(hash, AndroidBase64.DEFAULT)
    }

    fun verifyPassword(password: String, hash: String, salt: String): Boolean {
        val computedHash = hashPassword(password, salt)
        return computedHash == hash
    }

    private fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(keyAlgorithm)
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey {
        val keyBytes = "MySecretKey123454321yeKterceSyM".toByteArray()
        val key = ByteArray(32)
        System.arraycopy(keyBytes, 0, key, 0, minOf(keyBytes.size, 32))
        return SecretKeySpec(key, keyAlgorithm)
    }

    fun encrypt(data: String): String {
        return try {
            val cipher = Cipher.getInstance(algorithm)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val encryptedBytes = cipher.doFinal(data.toByteArray())
            AndroidBase64.encodeToString(encryptedBytes, AndroidBase64.DEFAULT)
        } catch(e: Exception) {
            data
        }
    }

    fun decrypt(encryptedData: String): String {
        return try {
            val cipher = Cipher.getInstance(algorithm)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey())
            val decryptedBytes = cipher.doFinal(AndroidBase64.decode(encryptedData, AndroidBase64.DEFAULT))
            String(decryptedBytes)
        } catch (e: Exception) {
            encryptedData
        }
    }
}
