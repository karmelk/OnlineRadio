package com.onlinestation.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.NavHostFragment
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Pattern

fun hideKeyboard(activity: Activity, viewToHide: View?) {
    viewToHide?.let {
        val imm = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

inline fun <reified F> getCurrentFragment(navHostFragment: NavHostFragment): F? {
    return if (navHostFragment.isAdded && navHostFragment.childFragmentManager.fragments.size > 0) {
        if (navHostFragment.childFragmentManager.fragments[0] is F) {
            navHostFragment.childFragmentManager.fragments[0] as F
        } else {
            null
        }
    } else null
}


fun parseM3UToString(urlM3u: String?, type: String): String? {
    var ligne: String
    try {
        val urlPage = URL(urlM3u)
        val connection = urlPage.openConnection() as HttpURLConnection
        val inputStream = connection.inputStream
        val bufferedReader =
            BufferedReader(InputStreamReader(inputStream))
        val stringBuffer = StringBuffer()
        when (type) {
            "m3u" -> {
                while (bufferedReader.readLine().also { ligne = it } != null) {
                    if (ligne.contains("http")) {
                        connection.disconnect()
                        bufferedReader.close()
                        inputStream.close()
                        return ligne
                    }
                    stringBuffer.append(ligne)
                }
            }
            "m3u8" -> {
                try {
                    val r = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
                    var line: String
                    var segmentsMap: HashMap<String?, Int?>? = null
                    val digitRegex = "\\d+"
                    val p = Pattern.compile(digitRegex)
                    while (r.readLine().also { line = it } != null) {
                        if (line == "#EXTM3U") { //start of m3u8
                            segmentsMap = HashMap()
                        } else if (line.contains("#EXTINF")) { //once found EXTINFO use runner to get the next line which contains the media file, parse duration of the segment
                            val matcher = p.matcher(line)
                            matcher.find()
                            //find the first matching digit, which represents the duration of the segment, dont call .find() again that will throw digit which may be contained in the description.
                            segmentsMap!![r.readLine()] = matcher.group(0).toInt()
                        }
                    }
                    r.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            else -> {
                while (bufferedReader.readLine().also { ligne = it } != null) {
                    if (ligne.contains("http")) {
                        connection.disconnect()
                        bufferedReader.close()
                        //                        inputStream.close();
                        ligne = ligne.split("http".toRegex()).toTypedArray()[1]
                        ligne = "http$ligne"
                        Log.e("line", ligne)
                        return ligne
                    }
                    stringBuffer.append(ligne)
                }
            }
        }
        connection.disconnect()
        bufferedReader.close()
        inputStream.close()
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}