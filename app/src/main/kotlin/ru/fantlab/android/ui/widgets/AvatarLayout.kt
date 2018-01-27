package ru.fantlab.android.ui.widgets

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.widget.TooltipCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.fantlab.android.R
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.PrefGetter

/**
 * Created by Kosh on 14 Nov 2016, 7:59 PM
 */

class AvatarLayout : FrameLayout {

	@BindView(R.id.avatar)
	lateinit var avatar: ShapedImageView

	private var login: String? = null
	private var isOrg: Boolean = false
	private var isEnterprise: Boolean = false

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

	override fun onFinishInflate() {
		super.onFinishInflate()
		View.inflate(context, R.layout.avatar_layout, this)
		if (isInEditMode) return
		ButterKnife.bind(this)
		setBackground()
		if (PrefGetter.isRectAvatar()) {
			avatar.setShape(ShapedImageView.SHAPE_MODE_ROUND_RECT, 15f)
		}
	}

	@OnClick(R.id.avatar)
	internal fun onClick(view: View) {
		if (InputHelper.isEmpty(login)) return
		//UserPagerActivity.startActivity(view.context, login, isOrg, isEnterprise, -1)
	}

	fun setUrl(url: String?, login: String?, isOrg: Boolean, isEnterprise: Boolean) {
		this.login = login
		this.isOrg = isOrg
		this.isEnterprise = isEnterprise
		avatar.contentDescription = login
		if (login != null) {
			TooltipCompat.setTooltipText(avatar, login)
		} else {
			avatar.setOnClickListener(null)
			avatar.setOnLongClickListener(null)
		}
		Glide.with(context)
				.load(url)
				// todo добавить заглушку
				//.fallback(ContextCompat.getDrawable(context, R.drawable.ic_fasthub_mascot))
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.into(avatar)
	}

	private fun setBackground() {
		if (PrefGetter.isRectAvatar()) {
			setBackgroundResource(R.drawable.rect_shape)
		} else {
			setBackgroundResource(R.drawable.circle_shape)
		}
	}
}
