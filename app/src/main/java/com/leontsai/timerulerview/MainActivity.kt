package com.leontsai.timerulerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leontsai.timerulerlib.TimeRulerView
import com.leontsai.timerulerlib.bean.TimeInfo
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


        rulerView.onSelectTimeListener = object : TimeRulerView.OnSelectTimeListener {
            override fun onSelectTime(time: Long) {
                rulerView_tv.text = sdf.format(time)
            }
        }


        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.set(year,month,day,12,0,0)
        rulerView.timeInMillis = calendar.timeInMillis
        rulerView_tv.text = sdf.format(rulerView.timeInMillis)




        val start0 = Calendar.getInstance()
        start0.set(year, month, day, 9, 30, 0)
        val end0 = Calendar.getInstance()
        end0.set(year, month, day, 10, 40, 0)

        val start1 = Calendar.getInstance()
        start1.set(year, month, day, 11, 40, 0)
        val end1 = Calendar.getInstance()
        end1.set(year, month, day, 12, 10, 0)

        val start2 = Calendar.getInstance()
        start2.set(year, month, day, 13, 50, 0)
        val end2 = Calendar.getInstance()
        end2.set(year, month, day, 14, 20, 0)

        val start3 = Calendar.getInstance()
        start3.set(year, month, day, 16, 30, 0)
        val end3 = Calendar.getInstance()
        end3.set(year, month, day, 19, 40, 0)


        val timeInfo0 = TimeInfo(start0, end0)
        val timeInfo1 = TimeInfo(start1, end1)
        val timeInfo2 = TimeInfo(start2, end2)
        val timeInfo3 = TimeInfo(start3, end3)

        val timeInfos = arrayListOf<TimeInfo>()
        timeInfos.add(timeInfo0)
        timeInfos.add(timeInfo1)
        timeInfos.add(timeInfo2)
        timeInfos.add(timeInfo3)


        rulerView.timeInfos = timeInfos

    }

}
