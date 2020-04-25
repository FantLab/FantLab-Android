package ru.fantlab.android.ui.widgets.filepicker.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import ru.fantlab.android.R
import ru.fantlab.android.helper.ViewHelper

class MaterialCheckbox : View {
	private var ctx: Context? = null
	private var minDim = 0
	private var paint: Paint? = null
	private var bounds: RectF? = null
	private var checked = false
	private var onCheckedChangeListener: OnCheckedChangeListener? = null
	private var tick: Path? = null

	constructor(context: Context?) : super(context) {
		initView(context)
	}

	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
		initView(context)
	}

	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		initView(context)
	}

	fun initView(context: Context?) {
		this.ctx = context
		checked = false
		tick = Path()
		paint = Paint()
		bounds = RectF()
		val onClickListener = OnClickListener {
			setChecked(!checked)
			onCheckedChangeListener!!.onCheckedChanged(this, isChecked())
		}
		setOnClickListener(onClickListener)
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		if (isChecked()) {
			paint!!.reset()
			paint!!.isAntiAlias = true
			bounds!![minDim / 10.toFloat(), minDim / 10.toFloat(), minDim - (minDim / 10).toFloat()] = minDim - (minDim / 10).toFloat()
			paint!!.color = ViewHelper.getAccentColor(ctx!!)
			canvas.drawRoundRect(bounds, minDim / 8.toFloat(), minDim / 8.toFloat(), paint)
			paint!!.color = Color.parseColor("#FFFFFF")
			paint!!.strokeWidth = minDim / 10.toFloat()
			paint!!.style = Paint.Style.STROKE
			paint!!.strokeJoin = Paint.Join.BEVEL
			canvas.drawPath(tick, paint)
		} else {
			paint!!.reset()
			paint!!.isAntiAlias = true
			bounds!![minDim / 10.toFloat(), minDim / 10.toFloat(), minDim - (minDim / 10).toFloat()] = minDim - (minDim / 10).toFloat()
			paint!!.color = Color.parseColor("#C1C1C1")
			canvas.drawRoundRect(bounds, minDim / 8.toFloat(), minDim / 8.toFloat(), paint)
			bounds!![minDim / 5.toFloat(), minDim / 5.toFloat(), minDim - (minDim / 5).toFloat()] = minDim - (minDim / 5).toFloat()
			paint!!.color = Color.parseColor("#FFFFFF")
			canvas.drawRect(bounds, paint)
		}
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		val height = measuredHeight
		val width = measuredWidth
		minDim = Math.min(width, height)
		bounds!![minDim / 10.toFloat(), minDim / 10.toFloat(), minDim - (minDim / 10).toFloat()] = minDim - (minDim / 10).toFloat()
		tick!!.moveTo(minDim / 4.toFloat(), minDim / 2.toFloat())
		tick!!.lineTo(minDim / 2.5f, minDim - (minDim / 3).toFloat())
		tick!!.moveTo(minDim / 2.75f, minDim - minDim / 3.25f)
		tick!!.lineTo(minDim - (minDim / 4).toFloat(), minDim / 3.toFloat())
		setMeasuredDimension(width, height)
	}

	fun isChecked(): Boolean {
		return checked
	}

	fun setChecked(checked: Boolean) {
		this.checked = checked
		invalidate()
	}

	fun setOnCheckedChangedListener(onCheckedChangeListener: OnCheckedChangeListener?) {
		this.onCheckedChangeListener = onCheckedChangeListener
	}
}