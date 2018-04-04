package ubuntudroid.chimney.di

import org.koin.dsl.module.applicationContext
import ubuntudroid.chimney.data.user.UserRepository

val repositoryModule = applicationContext {

    // UserRepository
    bean {
        UserRepository(get())
    }
}