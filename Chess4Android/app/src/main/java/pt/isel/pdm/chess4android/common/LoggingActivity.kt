package pt.isel.pdm.chess4android.common

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

abstract class LoggingActivity : AppCompatActivity() {
    init {
        Log.v(TAG, "init()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate()")
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart()")
    }


    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy()")
    }
}