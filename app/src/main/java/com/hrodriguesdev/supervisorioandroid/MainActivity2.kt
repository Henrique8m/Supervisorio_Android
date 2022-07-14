package com.hrodriguesdev.supervisorioandroid

import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.androidplot.util.PixelUtils
import com.androidplot.xy.*
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.util.*


class MainActivity2 : AppCompatActivity() {
    private var plot: XYPlot? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        //Forçar Modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // initialize our XYPlot reference:
        plot = findViewById<View>(R.id.plot) as XYPlot

        // create a couple arrays of y-values to plot:
        val domainLabels = arrayOf<Number>(1, 2, 3, 6, 7, 8, 9, 10, 13, 14)
        val series1Numbers: List<Number> = arrayListOf<Number>(1, 4, 2, 8, 4, 16, 8, 32, 16, 64)
        val series2Numbers: List<Number> = arrayListOf<Number>(5, 2, 10, 5, 20, 10, 40, 20, 80, 40)

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        val series1: XYSeries = SimpleXYSeries(
            series1Numbers , SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Title"
        )
        val series2: XYSeries = SimpleXYSeries(
            series2Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2"
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

        plot!!.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(object : Format() {
            override fun format(obj: Any, toAppendTo: StringBuffer, pos: FieldPosition?): StringBuffer? {
                val i = Math.round((obj as Number).toFloat())
                return toAppendTo.append(domainLabels[i])
            }

            override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                return null
            }
        })
    }
}