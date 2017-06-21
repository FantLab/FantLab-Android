package org.odddev.fantlab.core.bind

import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup

class BindingFactory<B : ViewDataBinding>(private val type: Class<B>) : VHFactory<BindingHolder<B>> {

	override fun onCreateViewHolder(parent: ViewGroup): BindingHolder<B> {
		val method = type.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java,
				kotlin.Boolean::class.java)
		@Suppress("UNCHECKED_CAST")
		val binding = method.invoke(null, LayoutInflater.from(parent.context), parent, false) as B
		return BindingHolder(binding)
		/*try {
			Method method = type.getMethod("inflate", LayoutInflater.class);
			B binding = (B) method.invoke(null, LayoutInflater.from(parent.getContext()));
			return new BindingHolder<>(binding);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}*/
	}
}
