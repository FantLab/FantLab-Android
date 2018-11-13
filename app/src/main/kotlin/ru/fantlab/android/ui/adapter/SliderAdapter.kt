package ru.fantlab.android.ui.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.widget.CircularProgressDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.slider_item_layout.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.SliderModel


class SliderAdapter(private var mContext: Context, private val photoSlide: ArrayList<SliderModel>?) : PagerAdapter() {

	override fun isViewFromObject(view: View, `object`: Any): Boolean {
		return view == `object`
	}

	override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
		container.removeView(`object` as View)
	}

	override fun getCount(): Int {
		return photoSlide!!.size
	}

	override fun instantiateItem(container: ViewGroup, position: Int): Any {
		val view: View = LayoutInflater.from(mContext).inflate(R.layout.slider_item_layout, container, false)
		val circularProgressDrawable = CircularProgressDrawable(view.context)
		circularProgressDrawable.strokeWidth = 5f
		circularProgressDrawable.centerRadius = 30f
		circularProgressDrawable.start()
		Glide.with(mContext)
				.load(photoSlide?.get(position)?.imageUrl)
				.placeholder(circularProgressDrawable)
				.into(view.imageView)
		view.imageText.text = photoSlide?.get(position)?.text
		container.addView(view, 0)
		return view
	}
}