package com.hrodriguesdev.supervisorioandroid

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private lateinit var result_pcoroa: TextView
private lateinit var result_ptopo: TextView
private lateinit var result_tcoroa: TextView
private lateinit var result_ttopo: TextView
private lateinit var result_vazao: TextView
private lateinit var result_secador: TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updatede()
        //Forçar Modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val buttonAtualizar = findViewById<Button>(R.id.btn_atualizar)
        buttonAtualizar.setOnClickListener{
            updatede()
        }

        val buttonGrafico = findViewById<Button>(R.id.btn_grafico)
        buttonGrafico.setOnClickListener{
            val intent = Intent(this , MainActivity2().javaClass)
            startActivity(intent)
        }
    }

    private fun updatede(){

        result_pcoroa = findViewById(R.id.result_pCoroa)
        result_ptopo = findViewById(R.id.result_ptopo)
        result_tcoroa = findViewById(R.id.result_tcoroa)
        result_ttopo = findViewById(R.id.result_ttopo)
        result_vazao = findViewById(R.id.result_vazao)
        result_secador = findViewById(R.id.result_secador)

        Thread {
            try{
                val url = URL("https://supervisorio-gateway.herokuapp.com/pyrometry")
                val conn = url.openConnection() as HttpsURLConnection
                try {
                    val data = conn.inputStream.bufferedReader().readText()
                    val obj = JSONObject(data)
                    val pcoroa = obj.getDouble("pcoroa")
                    val ptopo = obj.getDouble("ptopo")
                    val tcoroa = obj.getDouble("tcoroa")
                    val ttopo = obj.getDouble("ttopo")
                    val vazao = obj.getDouble("vazao")
                    val secador = obj.getDouble("secador")
                    updatedeTela(pcoroa, ptopo, tcoroa, ttopo, vazao, secador)

                }catch (e: Exception){
                    runOnUiThread {
                        result_pcoroa.text = "Exception 01"
                    }
                    e.printStackTrace()
                }finally {
                    conn.disconnect()
                }
            }catch (e: Exception){
                runOnUiThread {
                    result_pcoroa.text = e.message
                }


            }
        }.start()
    }

    private fun updatedeTela(
        pcoroa: Double,
        ptopo: Double,
        tcoroa: Double,
        ttopo: Double,
        vazao: Double,
        secador: Double
    ) {
        runOnUiThread {

            //result.text = "R$ ${"%.4f".format(value.toDouble() * res)} "
            result_pcoroa.text = pcoroa.toString()
            result_ptopo.text = ptopo.toString()
            result_tcoroa.text = tcoroa.toString()
            result_ttopo.text = ttopo.toString()
            result_vazao.text = vazao.toString()
            result_secador.text = secador.toString()
        }
    }

}