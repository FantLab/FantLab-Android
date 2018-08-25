package ru.fantlab.android.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import ru.fantlab.android.R
import java.util.*

class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {

	private var mFlow = DEFAULT_FLOW
	private var mChildSpacing = DEFAULT_CHILD_SPACING
	private var mMinChildSpacing = DEFAULT_CHILD_SPACING
	private var mChildSpacingForLastRow = DEFAULT_CHILD_SPACING_FOR_LAST_ROW
	private var mRowSpacing = DEFAULT_ROW_SPACING
	private var mAdjustedRowSpacing = DEFAULT_ROW_SPACING
	private var mRtl = DEFAULT_RTL
	private var mMaxRows = DEFAULT_MAX_ROWS
	private var mGravity = UNSPECIFIED_GRAVITY
	private var mRowVerticalGravity = ROW_VERTICAL_GRAVITY_AUTO
	private var mExactMeasuredHeight: Int = 0

	private val mHorizontalSpacingForRow = ArrayList<Float>()
	private val mHeightForRow = ArrayList<Int>()
	private val mWidthForRow = ArrayList<Int>()
	private val mChildNumForRow = ArrayList<Int>()

	var isFlow: Boolean
		get() = mFlow
		set(flow) {
			mFlow = flow
			requestLayout()
		}

	var childSpacing: Int
		get() = mChildSpacing
		set(childSpacing) {
			mChildSpacing = childSpacing
			requestLayout()
		}

	var childSpacingForLastRow: Int
		get() = mChildSpacingForLastRow
		set(childSpacingForLastRow) {
			mChildSpacingForLastRow = childSpacingForLastRow
			requestLayout()
		}

	var rowSpacing: Float
		get() = mRowSpacing
		set(rowSpacing) {
			mRowSpacing = rowSpacing
			requestLayout()
		}

	var maxRows: Int
		get() = mMaxRows
		set(maxRows) {
			mMaxRows = maxRows
			requestLayout()
		}

	var isRtl: Boolean
		get() = mRtl
		set(rtl) {
			mRtl = rtl
			requestLayout()
		}

	var minChildSpacing: Int
		get() = mMinChildSpacing
		set(minChildSpacing) {
			this.mMinChildSpacing = minChildSpacing
			requestLayout()
		}

	init {

		val a = context.theme.obtainStyledAttributes(
				attrs, R.styleable.FlowLayout, 0, 0)
		try {
			mFlow = a.getBoolean(R.styleable.FlowLayout_flFlow, DEFAULT_FLOW)
			try {
				mChildSpacing = a.getInt(R.styleable.FlowLayout_flChildSpacing, DEFAULT_CHILD_SPACING)
			} catch (e: NumberFormatException) {
				mChildSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_flChildSpacing, dpToPx(DEFAULT_CHILD_SPACING.toFloat()).toInt())
			}

			try {
				mMinChildSpacing = a.getInt(R.styleable.FlowLayout_flMinChildSpacing, DEFAULT_CHILD_SPACING)
			} catch (e: NumberFormatException) {
				mMinChildSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_flMinChildSpacing, dpToPx(DEFAULT_CHILD_SPACING.toFloat()).toInt())
			}

			try {
				mChildSpacingForLastRow = a.getInt(R.styleable.FlowLayout_flChildSpacingForLastRow, SPACING_UNDEFINED)
			} catch (e: NumberFormatException) {
				mChildSpacingForLastRow = a.getDimensionPixelSize(R.styleable.FlowLayout_flChildSpacingForLastRow, dpToPx(DEFAULT_CHILD_SPACING.toFloat()).toInt())
			}

			try {
				mRowSpacing = a.getInt(R.styleable.FlowLayout_flRowSpacing, 0).toFloat()
			} catch (e: NumberFormatException) {
				mRowSpacing = a.getDimension(R.styleable.FlowLayout_flRowSpacing, dpToPx(DEFAULT_ROW_SPACING))
			}

			mMaxRows = a.getInt(R.styleable.FlowLayout_flMaxRows, DEFAULT_MAX_ROWS)
			mRtl = a.getBoolean(R.styleable.FlowLayout_flRtl, DEFAULT_RTL)
			mGravity = a.getInt(R.styleable.FlowLayout_android_gravity, UNSPECIFIED_GRAVITY)
			mRowVerticalGravity = a.getInt(R.styleable.FlowLayout_flRowVerticalGravity, ROW_VERTICAL_GRAVITY_AUTO)
		} finally {
			a.recycle()
		}
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)

		val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
		val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
		val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
		val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

		mHorizontalSpacingForRow.clear()
		mHeightForRow.clear()
		mWidthForRow.clear()
		mChildNumForRow.clear()

		var measuredHeight = 0
		var measuredWidth = 0
		val childCount = childCount
		var rowWidth = 0
		var maxChildHeightInRow = 0
		var childNumInRow = 0
		val rowSize = widthSize - paddingLeft - paddingRight
		var rowTotalChildWidth = 0
		val allowFlow = widthMode != View.MeasureSpec.UNSPECIFIED && mFlow
		val childSpacing = if (mChildSpacing == SPACING_AUTO && widthMode == View.MeasureSpec.UNSPECIFIED)
			0
		else
			mChildSpacing
		val tmpSpacing = (if (childSpacing == SPACING_AUTO) mMinChildSpacing else childSpacing).toFloat()

		for (i in 0 until childCount) {
			val child = getChildAt(i)
			if (child.visibility == View.GONE) {
				continue
			}

			val childParams = child.layoutParams
			var horizontalMargin = 0
			var verticalMargin = 0
			if (childParams is ViewGroup.MarginLayoutParams) {
				measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, measuredHeight)
				horizontalMargin = childParams.leftMargin + childParams.rightMargin
				verticalMargin = childParams.topMargin + childParams.bottomMargin
			} else {
				measureChild(child, widthMeasureSpec, heightMeasureSpec)
			}

			val childWidth = child.measuredWidth + horizontalMargin
			val childHeight = child.measuredHeight + verticalMargin
			if (allowFlow && rowWidth + childWidth > rowSize) { // Need flow to next row
				// Save parameters for current row
				mHorizontalSpacingForRow.add(
						getSpacingForRow(childSpacing, rowSize, rowTotalChildWidth, childNumInRow))
				mChildNumForRow.add(childNumInRow)
				mHeightForRow.add(maxChildHeightInRow)
				mWidthForRow.add(rowWidth - tmpSpacing.toInt())
				if (mHorizontalSpacingForRow.size <= mMaxRows) {
					measuredHeight += maxChildHeightInRow
				}
				measuredWidth = Math.max(measuredWidth, rowWidth)

				childNumInRow = 1
				rowWidth = childWidth + tmpSpacing.toInt()
				rowTotalChildWidth = childWidth
				maxChildHeightInRow = childHeight
			} else {
				childNumInRow++
				rowWidth += (childWidth + tmpSpacing).toInt()
				rowTotalChildWidth += childWidth
				maxChildHeightInRow = Math.max(maxChildHeightInRow, childHeight)
			}
		}

		if (mChildSpacingForLastRow == SPACING_ALIGN) {
			if (mHorizontalSpacingForRow.size >= 1) {
				mHorizontalSpacingForRow.add(
						mHorizontalSpacingForRow[mHorizontalSpacingForRow.size - 1])
			} else {
				mHorizontalSpacingForRow.add(
						getSpacingForRow(childSpacing, rowSize, rowTotalChildWidth, childNumInRow))
			}
		} else if (mChildSpacingForLastRow != SPACING_UNDEFINED) {
			mHorizontalSpacingForRow.add(
					getSpacingForRow(mChildSpacingForLastRow, rowSize, rowTotalChildWidth, childNumInRow))
		} else {
			mHorizontalSpacingForRow.add(
					getSpacingForRow(childSpacing, rowSize, rowTotalChildWidth, childNumInRow))
		}

		mChildNumForRow.add(childNumInRow)
		mHeightForRow.add(maxChildHeightInRow)
		mWidthForRow.add(rowWidth - tmpSpacing.toInt())
		if (mHorizontalSpacingForRow.size <= mMaxRows) {
			measuredHeight += maxChildHeightInRow
		}
		measuredWidth = Math.max(measuredWidth, rowWidth)

		if (childSpacing == SPACING_AUTO) {
			measuredWidth = widthSize
		} else if (widthMode == View.MeasureSpec.UNSPECIFIED) {
			measuredWidth = measuredWidth + paddingLeft + paddingRight
		} else {
			measuredWidth = Math.min(measuredWidth + paddingLeft + paddingRight, widthSize)
		}

		measuredHeight += paddingTop + paddingBottom
		val rowNum = Math.min(mHorizontalSpacingForRow.size, mMaxRows)
		val rowSpacing: Float = if (mRowSpacing == SPACING_AUTO.toFloat() && heightMode == View.MeasureSpec.UNSPECIFIED)
			0f
		else
			mRowSpacing
		if (rowSpacing == SPACING_AUTO.toFloat()) {
			if (rowNum > 1) {
				mAdjustedRowSpacing = ((heightSize - measuredHeight) / (rowNum - 1)).toFloat()
			} else {
				mAdjustedRowSpacing = 0f
			}
			measuredHeight = heightSize
		} else {
			mAdjustedRowSpacing = rowSpacing
			if (rowNum > 1) {
				measuredHeight = if (heightMode == View.MeasureSpec.UNSPECIFIED)
					(measuredHeight + mAdjustedRowSpacing * (rowNum - 1)).toInt()
				else
					Math.min((measuredHeight + mAdjustedRowSpacing * (rowNum - 1)).toInt(),
							heightSize)
			}
		}

		mExactMeasuredHeight = measuredHeight

		measuredWidth = if (widthMode == View.MeasureSpec.EXACTLY) widthSize else measuredWidth
		measuredHeight = if (heightMode == View.MeasureSpec.EXACTLY) heightSize else measuredHeight

		setMeasuredDimension(measuredWidth, measuredHeight)
	}

	override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
		val paddingLeft = paddingLeft
		val paddingRight = paddingRight
		val paddingTop = paddingTop
		val paddingBottom = paddingBottom

		var x = if (mRtl) width - paddingRight else paddingLeft
		var y = paddingTop

		val verticalGravity = mGravity and Gravity.VERTICAL_GRAVITY_MASK
		val horizontalGravity = mGravity and Gravity.HORIZONTAL_GRAVITY_MASK

		when (verticalGravity) {
			Gravity.CENTER_VERTICAL -> {
				val offset = (b - t - paddingTop - paddingBottom - mExactMeasuredHeight) / 2
				y += offset
			}
			Gravity.BOTTOM -> {
				val offset = b - t - paddingTop - paddingBottom - mExactMeasuredHeight
				y += offset
			}
			else -> {
			}
		}

		val horizontalPadding = paddingLeft + paddingRight
		val layoutWidth = r - l
		x += getHorizontalGravityOffsetForRow(horizontalGravity, layoutWidth, horizontalPadding, 0)

		val verticalRowGravity = mRowVerticalGravity and Gravity.VERTICAL_GRAVITY_MASK

		val rowCount = mChildNumForRow.size
		var childIdx = 0
		for (row in 0 until rowCount) {
			val childNum = mChildNumForRow[row]
			val rowHeight = mHeightForRow[row]
			val spacing = mHorizontalSpacingForRow[row]
			var i = 0
			while (i < childNum && childIdx < childCount) {
				val child = getChildAt(childIdx++)
				if (child.visibility == View.GONE) {
					continue
				} else {
					i++
				}

				val childParams = child.layoutParams
				var marginLeft = 0
				var marginTop = 0
				var marginBottom = 0
				var marginRight = 0
				if (childParams is ViewGroup.MarginLayoutParams) {
					marginLeft = childParams.leftMargin
					marginRight = childParams.rightMargin
					marginTop = childParams.topMargin
					marginBottom = childParams.bottomMargin
				}

				val childWidth = child.measuredWidth
				val childHeight = child.measuredHeight
				var tt = y + marginTop
				if (verticalRowGravity == Gravity.BOTTOM) {
					tt = y + rowHeight - marginBottom - childHeight
				} else if (verticalRowGravity == Gravity.CENTER_VERTICAL) {
					tt = y + marginTop + (rowHeight - marginTop - marginBottom - childHeight) / 2
				}
				val bb = tt + childHeight
				if (mRtl) {
					val l1 = x - marginRight - childWidth
					val r1 = x - marginRight
					child.layout(l1, tt, r1, bb)
					x -= (childWidth.toFloat() + spacing + marginLeft.toFloat() + marginRight.toFloat()).toInt()
				} else {
					val l2 = x + marginLeft
					val r2 = x + marginLeft + childWidth
					child.layout(l2, tt, r2, bb)
					x += (childWidth.toFloat() + spacing + marginLeft.toFloat() + marginRight.toFloat()).toInt()
				}
			}
			x = if (mRtl) width - paddingRight else paddingLeft
			x += getHorizontalGravityOffsetForRow(
					horizontalGravity, layoutWidth, horizontalPadding, row + 1)
			y += (rowHeight + mAdjustedRowSpacing).toInt()
		}
	}

	private fun getHorizontalGravityOffsetForRow(horizontalGravity: Int, parentWidth: Int, horizontalPadding: Int, row: Int): Int {
		if (mChildSpacing == SPACING_AUTO || row >= mWidthForRow.size
				|| row >= mChildNumForRow.size || mChildNumForRow[row] <= 0) {
			return 0
		}

		var offset = 0
		when (horizontalGravity) {
			Gravity.CENTER_HORIZONTAL -> offset = (parentWidth - horizontalPadding - mWidthForRow[row]) / 2
			Gravity.RIGHT -> offset = parentWidth - horizontalPadding - mWidthForRow[row]
			else -> {
			}
		}
		return offset
	}

	override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
		return ViewGroup.MarginLayoutParams(p)
	}

	override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
		return ViewGroup.MarginLayoutParams(context, attrs)
	}

	fun setGravity(gravity: Int) {
		if (mGravity != gravity) {
			mGravity = gravity
			requestLayout()
		}
	}

	fun setRowVerticalGravity(rowVerticalGravity: Int) {
		if (mRowVerticalGravity != rowVerticalGravity) {
			mRowVerticalGravity = rowVerticalGravity
			requestLayout()
		}
	}

	private fun getSpacingForRow(spacingAttribute: Int, rowSize: Int, usedSize: Int, childNum: Int): Float {
		val spacing: Float
		if (spacingAttribute == SPACING_AUTO) {
			if (childNum > 1) {
				spacing = ((rowSize - usedSize) / (childNum - 1)).toFloat()
			} else {
				spacing = 0f
			}
		} else {
			spacing = spacingAttribute.toFloat()
		}
		return spacing
	}

	private fun dpToPx(dp: Float): Float {
		return TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
	}

	companion object {
		private val LOG_TAG = FlowLayout::class.java.simpleName

		val SPACING_AUTO = -65536

		val SPACING_ALIGN = -65537

		private val SPACING_UNDEFINED = -65538

		private val UNSPECIFIED_GRAVITY = -1

		private val ROW_VERTICAL_GRAVITY_AUTO = -65536

		private val DEFAULT_FLOW = true
		private val DEFAULT_CHILD_SPACING = 0
		private val DEFAULT_CHILD_SPACING_FOR_LAST_ROW = SPACING_UNDEFINED
		private val DEFAULT_ROW_SPACING = 0f
		private val DEFAULT_RTL = false
		private val DEFAULT_MAX_ROWS = Integer.MAX_VALUE
	}
}
