package com.example.marvel.utils

import java.io.UnsupportedEncodingException
import java.security.MessageDigest

object NetworkUtils {

    const val privateKey = "5e094664d5dd9ce6fafcad7090284c4815b21840"
    const val publicKey = "be4e2710bbc5c6aa16626a627fc62287"
    const val timeStamp = 1
    const val baseUrl = "https://gateway.marvel.com:443/v1/public/"

    fun getHash(): String {
        val string = timeStamp.toString() + privateKey + publicKey
        val md: MessageDigest = MessageDigest.getInstance("MD5")
        try {
            val array = md.digest(string.toByteArray())
            val sb = StringBuffer()
            for (i in array.indices) {
                sb.append(Integer.toHexString(array[i].toInt() and 0xFF or 0x100).substring(1, 3))
            }
            return sb.toString()
        } catch (e: java.security.NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (ex: UnsupportedEncodingException) {
            ex.printStackTrace()
        }
        return ""
    }
}