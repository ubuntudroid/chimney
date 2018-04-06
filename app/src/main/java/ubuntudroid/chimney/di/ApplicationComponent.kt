package ubuntudroid.chimney.di

import ubuntudroid.chimney.profile.profileModule

object ApplicationComponent {

    val modules = listOf(
            systemModule,
            dbModule,
            authModule,
            steamNetworkModule,
            steamApiModule,
            repositoryModule,
            profileModule
    )

}