package com.leontsai.timerulerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leontsai.timerulerlib.TimeRulerView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rulerView.timeInMillis=Calendar.getInstance().timeInMillis
        rulerView_tv.text = sdf.format(rulerView.timeInMillis)
        rulerView.onSelectTimeListener = object : TimeRulerView.OnSelectTimeListener {
            override fun onSelectTime(time: Long) {
                rulerView_tv.text = sdf.format(time)
            }
        }


    }

}
