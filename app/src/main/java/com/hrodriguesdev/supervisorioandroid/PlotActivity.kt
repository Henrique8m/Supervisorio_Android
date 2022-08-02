package com.hrodriguesdev.supervisorioandroid

import android.app.Activity
import android.content.Intent
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.androidplot.util.PixelUtils
import com.androidplot.xy.*
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import kotlin.math.roundToInt


class PlotActivity : AppCompatActivity() {
    private var plot: XYPlot? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plot)

        val buttonMain = findViewById<Button>(R.id.btn_mainview)
        buttonMain.setOnClickListener{
            finish()
//            val intent1 = Intent(this , MainActivity().javaClass)
//            startActivity(intent1)

        }

        // initialize our XYPlot reference:

        plot = findViewById<View>(R.id.plot) as XYPlot

        // create a couple arrays of y-values to plot:
        val domainLabels = arrayOf<String>("11:00", "11:20", "11:40", "12:00", "12:20", "12:40", "13:00", "13:20")
        val seriesPressao: List<Number> = arrayListOf<Number>(0, 5.60, 5.50, 5.20, 5.00, 4.8, 5.2, 5.2)
        val seriesVazao: List<Number> = arrayListOf<Number>(20.00, 15.50, 16.00, 16.50, 16.50, 14.00, 16.00, 16.00)

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        val series1: XYSeries = SimpleXYSeries(
            seriesPressao , SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Coroa"
        )
        val series2: XYSeries = SimpleXYSeries(
            seriesVazao, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Vazão"
        )

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        val series1Format = LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels)

        val series2Format = LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels2)

        // add an "dash" effect to the series2 line:
        series2Format.linePaint.pathEffect = DashPathEffect(
            floatArrayOf( // always use DP when specifying pixel sizes, to keep things consistent across devices:
                PixelUtils.dpToPix(20f),
                PixelUtils.dpToPix(15f)
            ), 0F
        )

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.interpolationParams =
            CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)

        series2Format.interpolationParams =
            CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)

        // add a new series' to the xyplot:
        plot!!.addSeries(series1, series1Format)
        plot!!.addSeries(series2, series2Format)

        plot!!.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format() {
            override fun format(obj: Any, toAppendTo: StringBuffer, pos: FieldPosition?): StringBuffer? {
                val i = (obj as Number).toFloat().roundToInt()
                return toAppendTo.append(domainLabels[i])
            }

            override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                return null
            }
        }
    }
}