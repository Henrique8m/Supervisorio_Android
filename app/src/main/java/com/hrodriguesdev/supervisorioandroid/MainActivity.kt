package com.hrodriguesdev.supervisorioandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
private lateinit var result_conection: TextView
var url:String = "https://supervisorio-monolitico.herokuapp.com/"

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
//            val intent = Intent(this , PlotActivity().javaClass)
            val intent = Intent(this , FullscreenActivity().javaClass)
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
        result_conection = findViewById(R.id.result_conection)

        Thread {
            try{
                val url = URL(url + "instante")
                val conn = url.openConnection() as HttpsURLConnection
                try {
                    val data = conn.inputStream.bufferedReader().readText()
                    val obj = JSONObject(data)
                    val pcoroa = obj.getDouble("pcoroa")
                    val ptopo = obj.getDouble("ptopo")
                    val tcoroa = obj.getInt("tcoroa")
                    val ttopo = obj.getInt("ttopo")
                    val vazao = obj.getDouble("vazao")
                    val secador = obj.getInt("secador")
                    updatedeTela(pcoroa, ptopo, tcoroa, ttopo, vazao, secador)
                    result_conection.text = "Conected"

                }catch (e: Exception){
                    runOnUiThread {
                        result_conection.text = "Exception 01"
                    }
                    e.printStackTrace()
                }finally {
                    conn.disconnect()
                }
            }catch (e: Exception){
                runOnUiThread {
                    result_conection.text = e.message
                }


            }
        }.start()
    }

    private fun updatedeTela(
        pcoroa: Double,
        ptopo: Double,
        tcoroa: Int,
        ttopo: Int,
        vazao: Double,
        secador: Int
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