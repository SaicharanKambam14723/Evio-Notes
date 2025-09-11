package com.example.evionotes.data.security

import kotlinx.coroutines.CoroutineStart
import javax.crypto.Cipher
import javax.crypto.SecretKey
import android.util.Base64
import javax.crypto.spec.GCMParameterSpec

class CryptoManager(
    private val secretKey: SecretKey
) {
    private val cipherTransform = "AES/GCM/NoPadding"
    private val ivSize = 12
    private val taglength = 128

    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(cipherTransform)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        val combined = iv + encryptedBytes
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    fun decrypt(encryptedText: String): String {
        val combined = Base64.decode(encryptedText, Base64.DEFAULT)

        val iv = combined.copyOfRange(0, ivSize)
        val encryptedBytes = combined.copyOfRange(ivSize, combined.size)

        val cipher = Cipher.getInstance(cipherTransform)
        val spec = GCMParameterSpec(taglength, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val decreptedBytes = cipher.doFinal(encryptedBytes)

        return String(decreptedBytes, Charsets.UTF_8)
    }
}