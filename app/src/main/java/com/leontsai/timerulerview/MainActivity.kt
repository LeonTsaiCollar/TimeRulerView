package com.leontsai.timerulerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leontsai.timerulerlib.TimeRulerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rulerView.onSelectTimeListener= object : TimeRulerView.OnSelectTimeListener {
            override fun onSelectTime(time: Calendar) {

            }
        }
    }

}
