package com.hrodriguesdev.supervisorioandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.*
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hrodriguesdev.supervisorioandroid.databinding.ActivityFullscreenBinding
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private lateinit var var_pcoroa: TextView
private lateinit var var_ptopo: TextView
private lateinit var var_tcoroa: TextView
private lateinit var var_ttopo: TextView

private lateinit var result_vazao: TextView
private lateinit var result_secador: TextView
private lateinit var result_conection: TextView
var url:String = "https://supervisorio-monolitico.herokuapp.com/"

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity(), Runnable{

    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var fullscreenContent: ImageView
    private lateinit var fullscreenContentControls: LinearLayout
    private val hideHandler = Handler(Looper.myLooper()!!)

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
//        supportActionBar?.show()
        fullscreenContentControls.visibility = View.VISIBLE
    }
    private var isFullscreen: Boolean = false

    private val hideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {

            MotionEvent.ACTION_DOWN -> if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS)

                val intent = Intent(this , MainActivityNavBar().javaClass)
                startActivity(intent)

            }
            MotionEvent.ACTION_UP -> view.performClick()
            else -> {
            }
        }
        false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Atualização dos objetos em tela
        var handler: Handler = Handler()
        handler.postDelayed(this, 10000)


        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.fullscreenContent
        fullscreenContent.setOnClickListener { toggle() }

        fullscreenContentControls = binding.fullscreenContentControls

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        binding.dummyButton.setOnTouchListener(delayHideTouchListener)

        //Variaveis da view
        var_pcoroa = binding.txtPreCoroa
        var_ptopo = binding.txtPreTopo
        var_tcoroa = binding.txtTmpCoroa
        var_ttopo = binding.txtTmpTopo
    }

        private fun updatede(){


//            result_vazao = findViewById(R.id.result_vazao)
//            result_secador = findViewById(R.id.result_secador)
//            result_conection = findViewById(R.id.result_conection)
//
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
//                        result_conection.text = "Conected"

                    }catch (e: Exception){
                        runOnUiThread {
//                            result_conection.text = "Exception 01"
                        }
                        e.printStackTrace()
                    }finally {
                        conn.disconnect()
                    }
                }catch (e: Exception){
                    runOnUiThread {
//                        result_conection.text = e.message
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
                var_pcoroa.text = pcoroa.toString()
                var_ptopo.text = ptopo.toString()
                var_tcoroa.text = tcoroa.toString()
                var_ttopo.text = ttopo.toString()
//                result_vazao.text = vazao.toString()
//                result_secador.text = secador.toString()
            }
        }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (isFullscreen) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        fullscreenContentControls.visibility = View.GONE
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        isFullscreen = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }

    override fun run() {
        updatede()
    }
}