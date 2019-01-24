package com.leontsai.timerulerlib

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.text.TextPaint
import androidx.annotation.NonNull
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.Utils
import com.leontsai.timerulerlib.bean.ScaleInfo
import com.leontsai.timerulerlib.bean.TimeInfo
import com.leontsai.timerulerlib.callback.OnActionListener
import com.leontsai.timerulerlib.callback.OnTouchListener
import java.util.*


class TimeRulerView(private val mContext: Context, attrs: AttributeSet?) : View(mContext, attrs), OnActionListener {

    private var mGestureDetector = GestureDetector(context, OnTouchListener(this))

    /**
     * 24小时所分的总格数
     */
    private var mTotalCellNum = 48
    /**
     * 文字的字体大小
     */
    private var mTextFontSize = 0f
    /**
     * 文字的颜色
     */
    private var mTextColor = 0
    /**
     * 刻度的颜色
     */
    private var mScaleColor = 0
    /**
     * 顶部的线的颜色
     */
    private var mTopLineColor = 0
    /**
     * 底部的线的颜色
     */
    private var mBottomLineColor = 0
    /**
     * 选择时间段背景颜色
     */
    private var mSelectBackgroundColor = 0
    /**
     * 中间线的颜色
     */
    private var mMiddleLineColor = 0
    /**
     * 顶部线画笔
     */
    private val mTopLinePaint by lazy {
        Paint()
    }
    /**
     * 底部线画笔
     */
    private val mBottomLinePaint by lazy {
        Paint()
    }
    /**
     * 刻度线画笔
     */
    private val mScaleLinePaint by lazy {
        Paint()
    }
    /**
     * 中间线画笔
     */
    private val mMiddleLinePaint by lazy {
        Paint()
    }
    /**
     * 选择时间段的画笔
     */
    private val mSelectPaint by lazy {
        Paint()
    }
    /**
     * 文字画笔
     */
    private val mTextPaint by lazy {
        TextPaint()
    }
    /**
     * 顶部线的粗度
     */
    private var mTopLineStrokeWidth = 0f
    /**
     * 底部线的粗度
     */
    private var mBottomLineStrokeWidth = 0f
    /**
     * 刻度线的粗度
     */
    private var mScaleLineStrokeWidth = 0f
    /**
     * 中间线的粗度
     */
    private var mMiddleLineStrokeWidth = 0f
    /**
     * 每一格在屏幕上显示的长度
     */
    private var mWidthPerCell = 0f
    /**
     * 每一格代表的毫秒数
     */
    private var mMillisecondPerCell = 24 * 3600 * 1000 / mTotalCellNum

    /**
     * 手指移动的距离
     */
    private var mMoveDistance = 0f
    /**
     * 手指最后停留的坐标
     */
    private val mLastX = 0f
    /**
     * 每一次event事件的偏移距离
     */
    private val mPointXoff = 0f
    /**
     * 一天的时间所占的总的像素
     */
    private var totalPixelPerDay = mTotalCellNum * mWidthPerCell
    /**
     * 每一像素所占的毫秒数
     */
    private var mMillisecondPerPixel = 0f
    /**
     * 中间条绑定的日历对象
     */
    private var mCalendar = Calendar.getInstance()
    /**
     * 中间条绑定的日历对象对应的毫秒数
     */
    var timeInMillis = 0L
        get() {
            return mCalendar.timeInMillis
        }
        set(value) {
            field = value
            mCalendar.timeInMillis = field
            initMillisecond = mCalendar.timeInMillis
            invalidate()
        }
    /**
     * 中间条绑定的日历对象初始毫秒值
     */
    private var initMillisecond = 0L
    /**
     * 中间条的X坐标
     */
    private val mMiddleLineX = 0F
    /**
     * 手指滑动的速度
     */
//    private VelocityTracker mTracker;
    /**
     * 选择时间回调
     */
//    private val onChooseTimeListener: OnActionListener? = null
    /**
     * 需要播放的时间段列表
     */
    private val timeInfos: ArrayList<TimeInfo>? = null
    /**
     * 手指滑动划过的天数
     */
    private val dayBySlide: Int = 0

    /**
     * 回放时间段最小时间点
     */
    private val mMinTime: Long = 0
    /**
     * 回放时间段最大时间点
     */
    private val mMaxTime: Long = 0

    private var scaleList = arrayListOf<ScaleInfo>()

    private val shrink_or_magnify: Boolean = false
    private val lastFingerDis: Double = 0.toDouble()
    private val scaleRatio = 1f

    constructor(mContext: Context) : this(mContext, null)

    init {
        Utils.init(mContext)
        isFocusable = true
        isClickable = true
        setOnTouchListener { _: View, ev: MotionEvent ->
            mGestureDetector.onTouchEvent(ev)
        }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimeRulerView)
        initDefaultValue(typedArray)
        typedArray.recycle()
        initPaint()
        initData()
    }

    private fun initData() {
        val year = mCalendar.get(Calendar.YEAR)
        val month = mCalendar.get(Calendar.MONTH)
        val day = mCalendar.get(Calendar.DAY_OF_MONTH)
        mCalendar.set(year, month, day, 12, 0, 0)
        initMillisecond = mCalendar.timeInMillis

        val setTime = Calendar.getInstance()
        for (i in 0..mTotalCellNum) {
            setTime.set(
                year, month, day, if (mTotalCellNum == 48) {
                    i / 2
                } else i, if (mTotalCellNum == 48) {
                    if (i % 2 == 0) 0 else 30
                } else 0, 0
            )
            val scaleInfo = ScaleInfo()
            scaleInfo.time = setTime.timeInMillis
            scaleInfo.text = i
            scaleList.add(scaleInfo)
        }
    }

    private fun initDefaultValue(@NonNull typedArray: TypedArray) {
        mTotalCellNum = typedArray.getInt(R.styleable.TimeRulerView_totalTimePerCell, 48)
        mTextFontSize =
                typedArray.getDimension(R.styleable.TimeRulerView_textFontSize, ConvertUtils.sp2px(13f).toFloat())
        mTextColor = typedArray.getColor(R.styleable.TimeRulerView_textColor, Color.rgb(0, 0, 0))
        mScaleColor = typedArray.getColor(R.styleable.TimeRulerView_scaleColor, Color.rgb(0, 0, 0))
        mTopLineColor = typedArray.getColor(R.styleable.TimeRulerView_topLineColor, Color.rgb(0, 0, 0))
        mBottomLineColor = typedArray.getColor(R.styleable.TimeRulerView_bottomLineColor, Color.rgb(0, 0, 0))
        mMiddleLineColor = typedArray.getColor(R.styleable.TimeRulerView_middleLineColor, Color.rgb(0, 0, 0))
        mSelectBackgroundColor =
                typedArray.getColor(R.styleable.TimeRulerView_selectBackgroundColor, Color.rgb(255, 0, 0))
        mTopLineStrokeWidth =
                typedArray.getDimension(
                    R.styleable.TimeRulerView_topLineStrokeWidth
                    , ConvertUtils.dp2px(3f).toFloat()
                )
        mBottomLineStrokeWidth =
                typedArray.getDimension(
                    R.styleable.TimeRulerView_bottomLineStrokeWidth,
                    ConvertUtils.dp2px(3f).toFloat()
                )
        mScaleLineStrokeWidth = typedArray.getDimension(
            R.styleable.TimeRulerView_scaleLineStrokeWidth,
            ConvertUtils.dp2px(2f).toFloat()
        )
        mMiddleLineStrokeWidth = typedArray.getDimension(
            R.styleable.TimeRulerView_middleLineStrokeWidth,
            ConvertUtils.dp2px(3f).toFloat()
        )
        mWidthPerCell =
                typedArray.getDimension(R.styleable.TimeRulerView_widthPerCell, ConvertUtils.dp2px(50f).toFloat())

        mMillisecondPerPixel = mMillisecondPerCell / mWidthPerCell

    }

    private fun initPaint() {
        mTopLinePaint.isAntiAlias = true
        mTopLinePaint.color = mTopLineColor
        mTopLinePaint.style = Paint.Style.STROKE
        mTopLinePaint.strokeWidth = mTopLineStrokeWidth

        mBottomLinePaint.isAntiAlias = true
        mBottomLinePaint.color = mBottomLineColor
        mBottomLinePaint.style = Paint.Style.STROKE
        mBottomLinePaint.strokeWidth = mBottomLineStrokeWidth

        mScaleLinePaint.isAntiAlias = true
        mScaleLinePaint.color = mScaleColor
        mScaleLinePaint.style = Paint.Style.STROKE
        mScaleLinePaint.strokeWidth = mScaleLineStrokeWidth

        mMiddleLinePaint.isAntiAlias = true
        mMiddleLinePaint.color = mMiddleLineColor
        mMiddleLinePaint.style = Paint.Style.STROKE
        mMiddleLinePaint.strokeWidth = mMiddleLineStrokeWidth

        mSelectPaint.isAntiAlias = true
        mSelectPaint.color = mSelectBackgroundColor
        mSelectPaint.style = Paint.Style.STROKE

        mTextPaint.isAntiAlias = true
        mTextPaint.color = mTextColor
        mTextPaint.style = Paint.Style.STROKE
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = mTextFontSize
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTopLine(canvas!!)
        drawBottomLine(canvas)
        drawMiddleLine(canvas)
        drawScaleLine(canvas)
    }


    private fun drawTopLine(canvas: Canvas) {
        canvas.drawLine(
            0f, 0f, measuredWidth.toFloat(), 0f, mTopLinePaint
        )
    }

    private fun drawBottomLine(canvas: Canvas) {
        canvas.drawLine(
            0f,
            measuredHeight.toFloat(),
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            mBottomLinePaint
        )
    }

    private fun drawMiddleLine(canvas: Canvas) {
        canvas.drawLine(
            measuredWidth / 2f,
            0f,
            measuredWidth / 2f,
            measuredHeight.toFloat(),
            mMiddleLinePaint
        )
    }

    private fun drawScaleLine(canvas: Canvas) {
        for (i in 0..mTotalCellNum) {
            val time = scaleList[i].time
            val distance = Math.abs((time - mCalendar.timeInMillis) / mMillisecondPerPixel)
            if (distance < measuredWidth / 2) {
                val XFromMiddlePoint =
                    if (time < mCalendar.timeInMillis) measuredWidth / 2 - distance else measuredWidth / 2 + distance
                canvas.drawLine(
                    XFromMiddlePoint.toFloat()
                    , (measuredHeight - ConvertUtils.dp2px(5f)).toFloat()
                    , XFromMiddlePoint.toFloat()
                    , measuredHeight.toFloat()
                    , mScaleLinePaint
                )

                drawText(i, XFromMiddlePoint.toFloat(), canvas)
            }
        }
    }

    private fun drawText(i: Int, moveX: Float, canvas: Canvas) {
        val fontMetrics = mTextPaint.fontMetrics
        val top = fontMetrics.top//为基线到字体上边框的距离,即上图中的top
        val bottom = fontMetrics.bottom//为基线到字体下边框的距离,即上图中的bottom
        val baseLineY = measuredHeight / 2 - top / 2 - bottom / 2//基线中间点的y轴计算公式

        if (mTotalCellNum == 48) {
            if (i % 2 == 0) {
                if (i < 20) {
                    canvas.drawText("0" + (i / 2).toString() + ":00", moveX, baseLineY, mTextPaint)
                } else {
                    canvas.drawText((i / 2).toString() + ":00", moveX, baseLineY, mTextPaint)
                }
            }
        } else if (mTotalCellNum == 24) {
            if (i < 10) {
                canvas.drawText("0$i:00", moveX, baseLineY, mTextPaint)
            } else {
                canvas.drawText("$i:00", moveX, baseLineY, mTextPaint)
            }
        }
    }

    override fun onMove(distanceX: Float) {
        mMoveDistance += distanceX
        mCalendar.timeInMillis = initMillisecond - (mMoveDistance * mMillisecondPerPixel).toLong()

        if (onSelectTimeListener != null) {
            val time = mCalendar.timeInMillis
            onSelectTimeListener!!.onSelectTime(time)
        }

        invalidate()
    }

    var onSelectTimeListener: OnSelectTimeListener? = null

    interface OnSelectTimeListener {
        fun onSelectTime(time: Long)
    }







    
}