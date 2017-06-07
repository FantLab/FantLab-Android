package org.odddev.fantlab.core.bind

import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * @author Ivan Zolotarev
 * @since 05.06.17
 */
class BindingFactory<B : ViewDataBinding>(private val type: Class<B>) : VHFactory<BindingHolder<B>> {

	override fun onCreateViewHolder(parent: ViewGroup): BindingHolder<B> {
		val method = type.getMethod("inflate", LayoutInflater::class.java)
		@Suppress("UNCHECKED_CAST")
		val binding = method.invoke(null, LayoutInflater.from(parent.context)) as B
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
