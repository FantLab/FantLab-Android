package ru.fantlab.android.ui.widgets.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.RatingBar;

import ru.fantlab.android.R;

public class FlexibleRatingBar extends RatingBar {

	private int colorOutlineOn = Color.rgb(0x11, 0x11, 0x11);
	private int colorOutlineOff = Color.rgb(0x61, 0x61, 0x61);
	private int colorOutlinePressed = Color.rgb(0xFF, 0xB7, 0x4D);
	private int colorFillOn = Color.rgb(0xFF, 0x98, 0x00);
	private int colorFillOff = Color.TRANSPARENT;
	private int colorFillPressedOn = Color.rgb(0xFF, 0xB7, 0x4D);
	private int colorFillPressedOff = Color.TRANSPARENT;
	private int polygonVertices = 5;
	private int polygonRotation = 0;
	private int strokeWidth;
	private final Paint paintInside = new Paint();
	private final Paint paintOutline = new Paint();
	private Path path = new Path();
	private final RectF rectangle = new RectF();
	private final Matrix matrix = new Matrix();
	private float interiorAngleModifier = 2.2F;
	private final float dp = getResources().getDisplayMetrics().density;
	private float starSize;
	private Bitmap colorsJoined;


	public FlexibleRatingBar(Context context) {
		this(context, null);
	}

	public FlexibleRatingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		getXmlAttrs(context, attrs);
		init();
	}

	public FlexibleRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getXmlAttrs(context, attrs);
		init();
	}

	private void init() {
		paintInside.setAntiAlias(true);
		paintOutline.setStrokeWidth(strokeWidth);
		paintOutline.setStyle(Paint.Style.STROKE);
		paintOutline.setStrokeJoin(Paint.Join.ROUND);
		paintOutline.setAntiAlias(true);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int desiredWidth = (int) (50 * dp * getNumStars());
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = Math.min(desiredWidth, widthSize);
		} else {
			width = desiredWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = Math.min(heightSize, width / getNumStars());
		} else {
			height = width / getNumStars();
		}

		starSize = Math.min(height, width / getNumStars());
		if (strokeWidth < 0) strokeWidth = (int) (starSize / 15);
		starSize -= strokeWidth;

		setMeasuredDimension(width, height);
	}

	private Path createStarBySize(float size, int steps) {
		if (steps == 0) {
			path.addOval(new RectF(0, 0, size, size), Path.Direction.CW);
			path.close();
			return path;
		}
		float halfSize = size / 2.0F;
		float radius = halfSize / interiorAngleModifier; //Adjusts "pointiness" of stars
		float degreesPerStep = (float) Math.toRadians(360.0F / (float) steps);
		float halfDegreesPerStep = degreesPerStep / 2.0F;
		path.setFillType(Path.FillType.EVEN_ODD);
		float max = (float) (2.0F * Math.PI);
		path.moveTo(halfSize, 0);
		for (double step = 0; step < max; step += degreesPerStep) {
			path.lineTo((float) (halfSize - halfSize * Math.sin(step)), (float) (halfSize - halfSize * Math.cos(step)));
			path.lineTo((float) (halfSize - radius * Math.sin(step + halfDegreesPerStep)), (float) (halfSize - radius * Math.cos(step + halfDegreesPerStep)));
		}
		path.close();
		return path;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		//Default RatingBar changes color when pressed. This replicates the effect.
		BitmapShader shaderFill = updateShader(colorFillOn, colorFillOff);
		BitmapShader shaderFillPressed = updateShader(colorFillPressedOn, colorFillPressedOff);
		paintInside.setShader(isPressed() ? shaderFillPressed : shaderFill);

		path.rewind();
		path = createStarBySize(starSize, polygonVertices);

		if (polygonRotation != 0) {
			path.computeBounds(rectangle, true);
			float maxDimension = Math.max(rectangle.height(), rectangle.width());
			matrix.setScale(starSize / (1.15F * maxDimension), starSize / (1.15F * maxDimension));
			matrix.preRotate(polygonRotation);
			path.transform(matrix);
		}

		for (int i = 0; i < getNumStars(); ++i) {
			paintOutline.setColor(isPressed() ? colorOutlinePressed : i < getRating() ? colorOutlineOn : colorOutlineOff);

			path.computeBounds(rectangle, true);
			path.offset((i + .5F) * getWidth() / getNumStars() - rectangle.centerX(), getHeight() / 2 - rectangle.centerY());
			canvas.drawPath(path, paintInside);
			canvas.drawPath(path, paintOutline);
		}
	}

	private BitmapShader updateShader(int colorOn, int colorOff) {

		int ratingWidth = (int) (getRating() * getWidth() / getNumStars());

		if (ratingWidth <= 0 || getWidth() - ratingWidth <= 0) {
			colorsJoined = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
			colorsJoined.eraseColor(ratingWidth <= 0 ? colorOff : colorOn);
		} else {
			Bitmap colorLeft = Bitmap.createBitmap(ratingWidth, getHeight(), Bitmap.Config.ARGB_8888);
			colorLeft.eraseColor(colorOn);
			Bitmap colorRight = Bitmap.createBitmap(getWidth() - ratingWidth, getHeight(), Bitmap.Config.ARGB_8888);
			colorRight.eraseColor(colorOff);
			colorsJoined = combineBitmaps(colorLeft, colorRight);
		}
		return new BitmapShader(colorsJoined, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
	}

	private Bitmap combineBitmaps(Bitmap leftBitmap, Bitmap rightBitmap) {
		colorsJoined = Bitmap.createBitmap(leftBitmap.getWidth() + rightBitmap.getWidth(), leftBitmap.getHeight(), Bitmap.Config.ARGB_8888);

		Canvas comboImage = new Canvas(colorsJoined);
		comboImage.drawBitmap(leftBitmap, 0f, 0f, null);
		comboImage.drawBitmap(rightBitmap, leftBitmap.getWidth(), 0f, null);

		return colorsJoined;
	}

	private void getXmlAttrs(Context context, AttributeSet attrs) {
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlexibleRatingBar, 0, 0);
		try {
			colorOutlineOn = a.getInteger(R.styleable.FlexibleRatingBar_colorOutlineOn, Color.rgb(0x11, 0x11, 0x11));
			colorOutlineOff = a.getInteger(R.styleable.FlexibleRatingBar_colorOutlineOff, Color.rgb(0x61, 0x61, 0x61));
			colorOutlinePressed = a.getInteger(R.styleable.FlexibleRatingBar_colorOutlinePressed, Color.rgb(0xFF, 0xB7, 0x4D));
			colorFillOn = a.getInteger(R.styleable.FlexibleRatingBar_colorFillOn, Color.rgb(0xFF, 0x98, 0x00));
			colorFillOff = a.getInteger(R.styleable.FlexibleRatingBar_colorFillOff, Color.TRANSPARENT);
			colorFillPressedOn = a.getInteger(R.styleable.FlexibleRatingBar_colorFillPressedOn, Color.rgb(0xFF, 0xB7, 0x4D));
			colorFillPressedOff = a.getInteger(R.styleable.FlexibleRatingBar_colorFillPressedOff, Color.TRANSPARENT);
			polygonVertices = a.getInteger(R.styleable.FlexibleRatingBar_polygonVertices, 5);
			polygonRotation = a.getInteger(R.styleable.FlexibleRatingBar_polygonRotation, 0);
			strokeWidth = (int) a.getDimension(R.styleable.FlexibleRatingBar_strokeWidth, -1);
		} finally {
			a.recycle();
		}
	}

	public void setColorOutlineOn(int colorOutlineOn) {
		this.colorOutlineOn = colorOutlineOn;
	}

	public void setColorOutlineOff(int colorOutlineOff) {
		this.colorOutlineOff = colorOutlineOff;
	}

	public void setColorOutlinePressed(int colorOutlinePressed) {
		this.colorOutlinePressed = colorOutlinePressed;
	}

	public void setColorFillOn(int colorFillOn) {
		this.colorFillOn = colorFillOn;
	}

	public void setColorFillOff(int colorFillOff) {
		this.colorFillOff = colorFillOff;
	}

	public void setColorFillPressedOn(int colorFillPressedOn) {
		this.colorFillPressedOn = colorFillPressedOn;
	}

	public void setColorFillPressedOff(int colorFillPressedOff) {
		this.colorFillPressedOff = colorFillPressedOff;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public void setPolygonVertices(int polygonVertices) {
		this.polygonVertices = polygonVertices;
	}

	public void setPolygonRotation(int polygonRotation) {
		this.polygonRotation = polygonRotation;
	}

	public void setInteriorAngleModifier(float interiorAngleModifier) {
		this.interiorAngleModifier = interiorAngleModifier;
	}

}
