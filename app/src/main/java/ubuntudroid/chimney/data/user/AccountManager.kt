package ubuntudroid.chimney.data.user

import ubuntudroid.chimney.BuildConfig

class AccountManager {

    fun getUserId(): String = BuildConfig.STEAM_USER_ID
}