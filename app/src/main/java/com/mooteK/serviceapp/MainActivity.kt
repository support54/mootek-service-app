package com.mootek.serviceapp

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.ValueCallback
import android.net.Uri
import android.print.PrintAttributes
import android.print.PrintManager
import android.content.Context

class MainActivity : Activity() {
    private lateinit var web: WebView
    private lateinit var store: LocalStore
    private var fileCallback: ValueCallback<Array<Uri>>? = null
    private val fileRequest = 7102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = LocalStore(this)
        web = WebView(this)
        setContentView(web)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.allowFileAccess = true
        web.settings.allowContentAccess = true
        web.settings.javaScriptCanOpenWindowsAutomatically = true
        web.addJavascriptInterface(AndroidBridge(), "MootekNative")
        web.webViewClient = WebViewClient()
        web.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(v: WebView?, callback: ValueCallback<Array<Uri>>?, params: FileChooserParams?): Boolean {
                fileCallback?.onReceiveValue(null)
                fileCallback = callback
                return try {
                    val chooser = params?.createIntent() ?: return false
                    startActivityForResult(chooser, fileRequest)
                    true
                } catch (_: Exception) {
                    fileCallback = null
                    false
                }
            }
        }
        web.loadUrl("file:///android_asset/index.html")
    }

    inner class AndroidBridge {
        @JavascriptInterface fun getItem(key: String): String? = store.get(key)
        @JavascriptInterface fun setItem(key: String, value: String) { store.put(key, value) }
        @JavascriptInterface fun removeItem(key: String) { store.remove(key) }
        @JavascriptInterface fun clear() { store.clear() }
        @JavascriptInterface fun share(text: String) {
            runOnUiThread {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, text)
                }
                startActivity(Intent.createChooser(intent, "Share MOOTEK report"))
            }
        }
        @JavascriptInterface fun printPage() {
            runOnUiThread {
                val manager = getSystemService(Context.PRINT_SERVICE) as PrintManager
                val adapter = web.createPrintDocumentAdapter("MOOTEK_Report")
                manager.print("MOOTEK_Report", adapter, PrintAttributes.Builder().build())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileRequest) {
            val result = if (resultCode == RESULT_OK && data != null) WebChromeClient.FileChooserParams.parseResult(resultCode, data) else null
            fileCallback?.onReceiveValue(result)
            fileCallback = null
        }
    }

    override fun onBackPressed() {
        if (::web.isInitialized) web.evaluateJavascript("if(typeof goHome==='function'){goHome()}else{history.back()}", null)
        else super.onBackPressed()
    }
}

private class LocalStore(context: Context) : SQLiteOpenHelper(context, "mootek_local.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE app_store (k TEXT PRIMARY KEY NOT NULL, v TEXT NOT NULL)")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun get(key: String): String? = readableDatabase.rawQuery("SELECT v FROM app_store WHERE k=?", arrayOf(key)).use { c -> if (c.moveToFirst()) c.getString(0) else null }
    fun put(key: String, value: String) { writableDatabase.execSQL("INSERT OR REPLACE INTO app_store(k,v) VALUES(?,?)", arrayOf(key, value)) }
    fun remove(key: String) { writableDatabase.delete("app_store", "k=?", arrayOf(key)) }
    fun clear() { writableDatabase.delete("app_store", null, null) }
}
