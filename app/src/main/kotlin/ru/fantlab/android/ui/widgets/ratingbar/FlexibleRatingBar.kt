package ru.fantlab.android.ui.widgets.ratingbar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.RatingBar
import ru.fantlab.android.R
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class FlexibleRatingBar : RatingBar {

	private var colorOutlineOn = Color.rgb(0x11, 0x11, 0x11)
	private var colorOutlineOff = Color.rgb(0x61, 0x61, 0x61)
	private var colorOutlinePressed = Color.rgb(0xFF, 0xB7, 0x4D)
	private var colorFillOn = Color.rgb(0xFF, 0x98, 0x00)
	private var colorFillOff = Color.TRANSPARENT
	private var colorFillPressedOn = Color.rgb(0xFF, 0xB7, 0x4D)
	private var colorFillPressedOff = Color.TRANSPARENT
	private var polygonVertices = 5
	private var polygonRotation = 0
	private var strokeWidth: Int = 0
	private val paintInside = Paint()
	private val paintOutline = Paint()
	private var path = Path()
	private val rectangle = RectF()
	private val matrixs = Matrix()
	private var interiorAngleModifier = 2.2f
	private val dp = resources.displayMetrics.density
	private var starSize: Float = 0.toFloat()
	private var colorsJoined: Bitmap? = null

	@JvmOverloads
	constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
		getXmlAttrs(context, attrs)
		init()
	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		getXmlAttrs(context, attrs)
		init()
	}

	private fun init() {
		paintInside.isAntiAlias = true
		paintOutline.strokeWidth = strokeWidth.toFloat()
		paintOutline.style = Paint.Style.STROKE
		paintOutline.strokeJoin = Paint.Join.ROUND
		paintOutline.isAntiAlias = true
	}

	@Synchronized
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		val desiredWidth = (50f * dp * numStars.toFloat()).toInt()
		val widthMode = MeasureSpec.getMode(widthMeasureSpec)
		val widthSize = MeasureSpec.getSize(widthMeasureSpec)
		val heightMode = MeasureSpec.getMode(heightMeasureSpec)
		val heightSize = MeasureSpec.getSize(heightMeasureSpec)

		val width: Int
		val height: Int

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = min(desiredWidth, widthSize)
		} else {
			width = desiredWidth
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = min(heightSize, width / numStars)
		} else {
			height = width / numStars
		}

		starSize = min(height, width / numStars).toFloat()
		if (strokeWidth < 0) strokeWidth = (starSize / 15).toInt()
		starSize -= strokeWidth.toFloat()

		setMeasuredDimension(width, height)
	}

	private fun createStarBySize(size: Float, steps: Int): Path {
		if (steps == 0) {
			path.addOval(RectF(0f, 0f, size, size), Path.Direction.CW)
			path.close()
			return path
		}
		val halfSize = size / 2.0f
		val radius = halfSize / interiorAngleModifier
		val degreesPerStep = Math.toRadians((360.0f / steps.toFloat()).toDouble()).toFloat()
		val halfDegreesPerStep = degreesPerStep / 2.0f
		path.fillType = Path.FillType.EVEN_ODD
		val max = (2.0f * Math.PI).toFloat()
		path.moveTo(halfSize, 0f)
		var step = 0.0
		while (step < max) {
			path.lineTo((halfSize - halfSize * sin(step)).toFloat(), (halfSize - halfSize * cos(step)).toFloat())
			path.lineTo((halfSize - radius * sin(step + halfDegreesPerStep)).toFloat(), (halfSize - radius * cos(step + halfDegreesPerStep)).toFloat())
			step += degreesPerStep.toDouble()
		}
		path.close()
		return path
	}

	override fun onDraw(canvas: Canvas) {

		val shaderFill = updateShader(colorFillOn, colorFillOff)
		val shaderFillPressed = updateShader(colorFillPressedOn, colorFillPressedOff)
		paintInside.shader = if (isPressed) shaderFillPressed else shaderFill

		path.rewind()
		path = createStarBySize(starSize, polygonVertices)

		if (polygonRotation != 0) {
			path.computeBounds(rectangle, true)
			val maxDimension = Math.max(rectangle.height(), rectangle.width())
			matrixs.setScale(starSize / (1.15f * maxDimension), starSize / (1.15f * maxDimension))
			matrixs.preRotate(polygonRotation.toFloat())
			path.transform(matrixs)
		}

		for (i in 0 until numStars) {
			paintOutline.color = if (isPressed) colorOutlinePressed else if (i < rating) colorOutlineOn else colorOutlineOff

			path.computeBounds(rectangle, true)
			path.offset((i + .5f) * width / numStars - rectangle.centerX(), height / 2 - rectangle.centerY())
			canvas.drawPath(path, paintInside)
			canvas.drawPath(path, paintOutline)
		}
	}

	private fun updateShader(colorOn: Int, colorOff: Int): BitmapShader {

		val ratingWidth = (rating * width / numStars).toInt()

		if (ratingWidth <= 0 || width - ratingWidth <= 0) {
			colorsJoined = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
			colorsJoined!!.eraseColor(if (ratingWidth <= 0) colorOff else colorOn)
		} else {
			val colorLeft = Bitmap.createBitmap(ratingWidth, height, Bitmap.Config.ARGB_8888)
			colorLeft.eraseColor(colorOn)
			val colorRight = Bitmap.createBitmap(width - ratingWidth, height, Bitmap.Config.ARGB_8888)
			colorRight.eraseColor(colorOff)
			colorsJoined = combineBitmaps(colorLeft, colorRight)
		}
		return BitmapShader(colorsJoined!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
	}

	private fun combineBitmaps(leftBitmap: Bitmap, rightBitmap: Bitmap): Bitmap {
		colorsJoined = Bitmap.createBitmap(leftBitmap.width + rightBitmap.width, leftBitmap.height, Bitmap.Config.ARGB_8888)

		val comboImage = Canvas(colorsJoined!!)
		comboImage.drawBitmap(leftBitmap, 0f, 0f, null)
		comboImage.drawBitmap(rightBitmap, leftBitmap.width.toFloat(), 0f, null)

		return colorsJoined as Bitmap
	}

	private fun getXmlAttrs(context: Context, attrs: AttributeSet?) {
		val a = context.theme.obtainStyledAttributes(attrs, R.styleable.FlexibleRatingBar, 0, 0)
		try {
			colorOutlineOn = a.getInteger(R.styleable.FlexibleRatingBar_colorOutlineOn, Color.rgb(0x11, 0x11, 0x11))
			colorOutlineOff = a.getInteger(R.styleable.FlexibleRatingBar_colorOutlineOff, Color.rgb(0x61, 0x61, 0x61))
			colorOutlinePressed = a.getInteger(R.styleable.FlexibleRatingBar_colorOutlinePressed, Color.rgb(0xFF, 0xB7, 0x4D))
			colorFillOn = a.getInteger(R.styleable.FlexibleRatingBar_colorFillOn, Color.rgb(0xFF, 0x98, 0x00))
			colorFillOff = a.getInteger(R.styleable.FlexibleRatingBar_colorFillOff, Color.TRANSPARENT)
			colorFillPressedOn = a.getInteger(R.styleable.FlexibleRatingBar_colorFillPressedOn, Color.rgb(0xFF, 0xB7, 0x4D))
			colorFillPressedOff = a.getInteger(R.styleable.FlexibleRatingBar_colorFillPressedOff, Color.TRANSPARENT)
			polygonVertices = a.getInteger(R.styleable.FlexibleRatingBar_polygonVertices, 5)
			polygonRotation = a.getInteger(R.styleable.FlexibleRatingBar_polygonRotation, 0)
			strokeWidth = a.getDimension(R.styleable.FlexibleRatingBar_strokeWidth, -1f).toInt()
		} finally {
			a.recycle()
		}
	}

	fun setColorOutlineOn(colorOutlineOn: Int) {
		this.colorOutlineOn = colorOutlineOn
	}

	fun setColorOutlineOff(colorOutlineOff: Int) {
		this.colorOutlineOff = colorOutlineOff
	}

	fun setColorOutlinePressed(colorOutlinePressed: Int) {
		this.colorOutlinePressed = colorOutlinePressed
	}

	fun setColorFillOn(colorFillOn: Int) {
		this.colorFillOn = colorFillOn
	}

	fun setColorFillOff(colorFillOff: Int) {
		this.colorFillOff = colorFillOff
	}

	fun setColorFillPressedOn(colorFillPressedOn: Int) {
		this.colorFillPressedOn = colorFillPressedOn
	}

	fun setColorFillPressedOff(colorFillPressedOff: Int) {
		this.colorFillPressedOff = colorFillPressedOff
	}

	fun setStrokeWidth(strokeWidth: Int) {
		this.strokeWidth = strokeWidth
	}

	fun setPolygonVertices(polygonVertices: Int) {
		this.polygonVertices = polygonVertices
	}

	fun setPolygonRotation(polygonRotation: Int) {
		this.polygonRotation = polygonRotation
	}

	fun setInteriorAngleModifier(interiorAngleModifier: Float) {
		this.interiorAngleModifier = interiorAngleModifier
	}

}
