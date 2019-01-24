package com.leontsai.timerulerlib.callback

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent


internal class OnTouchListener(val listener: OnActionListener) : GestureDetector.SimpleOnGestureListener() {

    @Volatile
    private var filterFirstMove = true

    override fun onContextClick(e: MotionEvent?): Boolean {
        Log.i("cyl", "onContextClick")

        return super.onContextClick(e)
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.i("cyl", "onDoubleTap")

        return super.onDoubleTap(e)
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        Log.i("cyl", "onDoubleTapEvent")

        return super.onDoubleTapEvent(e)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        Log.i("cyl", "onDown")
        filterFirstMove = true
        return super.onDown(e)
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        Log.i("cyl", "onFling")

        return super.onFling(e1, e2, velocityX, velocityY)
    }

    override fun onLongPress(e: MotionEvent?) {
        super.onLongPress(e)
        Log.i("cyl", "onLongPress")

    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        Log.i("cyl", "onScroll---->distanceX:$distanceX   distanceY: $distanceY")
        if (Math.abs(distanceX) <= 0.2f) return false

        //第一个distanceX有bug,不能用，过滤掉
        if (filterFirstMove) {
            filterFirstMove = false
            return false
        }
        listener.onMove(-distanceX)
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
        super.onShowPress(e)
        Log.i("cyl", "onShowPress")

    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Log.i("cyl", "onSingleTapConfirmed")

        return super.onSingleTapConfirmed(e)
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.i("cyl", "onSingleTapUp")

        return super.onSingleTapUp(e)
    }
}