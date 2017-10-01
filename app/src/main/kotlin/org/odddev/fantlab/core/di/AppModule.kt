package org.odddev.fantlab.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.TableCreationMode
import org.odddev.fantlab.Models
import org.odddev.fantlab.core.network.INetworkChecker
import org.odddev.fantlab.core.network.NetworkChecker
import javax.inject.Singleton

@Module
class AppModule(internal val context: Context) {

	private val dataStore: KotlinReactiveEntityStore<Persistable> by lazy {
		val source = DatabaseSource(context, Models.DEFAULT, 1)
		source.setTableCreationMode(TableCreationMode.DROP_CREATE)
		source.setLoggingEnabled(true)
		KotlinReactiveEntityStore<Persistable>(KotlinEntityDataStore(source.configuration))
	}

	@Singleton
	@Provides
	fun getContext(): Context = context

	@Singleton
	@Provides
	fun provideNetworkChecker(): INetworkChecker = NetworkChecker()

	@Singleton
	@Provides
	fun provideRequery(): KotlinReactiveEntityStore<Persistable> = dataStore
}
