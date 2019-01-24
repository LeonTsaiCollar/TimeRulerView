package com.leontsai.timerulerlib.callback


internal interface OnActionListener {
    /**
     * 手指移动
     */
    fun onMove(distanceX: Float)
}