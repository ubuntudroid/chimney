package ubuntudroid.chimney.di

import ubuntudroid.chimney.profile.profileModule

object ApplicationComponent {

    val modules = listOf(authModule, steamNetworkModule, steamApiModule, repositoryModule, profileModule)

}