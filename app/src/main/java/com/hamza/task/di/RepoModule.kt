package com.hamza.task.di

import com.hamza.task.data.repoImpl.PlayersRepoImpl
import com.hamza.task.domain.repo.PlayersRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {


    @Provides
    fun providePlayersRepo(): PlayersRepo {
        return PlayersRepoImpl()
    }


}
