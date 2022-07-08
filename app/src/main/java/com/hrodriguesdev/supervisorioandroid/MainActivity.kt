package com.hrodriguesdev.supervisorioandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

private lateinit var result: TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonConverter = findViewById<Button>(R.id.btn_converter)
        buttonConverter.setOnClickListener{
            converter()
        }
    }

    private fun converter(){
        result = findViewById(R.id.result_pCoroa)

        Thread {
            try{
                val url = URL("http://192.168.15.2:8080/pyrometry")
                val conn = url.openConnection() as HttpURLConnection
                try {
                    val data = conn.inputStream.bufferedReader().readText()
                    val obj = JSONObject(data)
                    runOnUiThread {
                        val res = obj.getDouble("pcoroa")

                        //result.text = "R$ ${"%.4f".format(value.toDouble() * res)} "
                        result.text = res.toString()
                        result.visibility = View.VISIBLE
                    }
                }catch (e: Exception){
                    runOnUiThread {
                        result.text = "Exception 01"
                        result.visibility = View.VISIBLE
                    }
                    e.printStackTrace()
                }finally {
                    conn.disconnect()
                }
            }catch (e: Exception){
                runOnUiThread {
                    result.text = e.message
                    result.visibility = View.VISIBLE
                }


            }
        }.start()
    }

}