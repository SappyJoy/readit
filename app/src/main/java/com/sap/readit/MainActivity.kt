package com.sap.readit

import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.sap.readit.client.Client

class MainActivity : AppCompatActivity() {

    private lateinit var mBtnNext: Button
    private lateinit var mText: TextView
    private lateinit var client: Client
    private lateinit var clientThread: Thread

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        client = Client()


        mText = findViewById(R.id.text)
        mBtnNext = findViewById(R.id.button)

        Thread {
            client.startConnection("178.46.198.15", 6666)
            val text = client.sendMessage("next")
            mText.setText(text)
        }.start()

        mText.setMovementMethod(ScrollingMovementMethod())


        mBtnNext.setOnClickListener {
            Thread {
                val text = client.sendMessage("next")
                mText.setText(text)
                mText.scrollTo(0, 0)
            }.start()
        }
    }
}