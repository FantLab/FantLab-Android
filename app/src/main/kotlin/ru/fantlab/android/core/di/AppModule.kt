package ru.fantlab.android.core.di

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.fantlab.android.core.db.MainDatabase
import ru.fantlab.android.core.network.INetworkChecker
import ru.fantlab.android.core.network.NetworkChecker
import javax.inject.Singleton

@Module
class AppModule(internal val context: Context) {

	@Singleton
	@Provides
	fun getContext(): Context = context

	@Singleton
	@Provides
	fun provideNetworkChecker(): INetworkChecker = NetworkChecker()

	@Singleton
	@Provides
	fun provideMainDatabase(): MainDatabase = Room
			.databaseBuilder(context, MainDatabase::class.java, "main.db")
			.build()
}
