package ubuntudroid.chimney

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.with
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.test.KoinTest
import org.koin.test.dryRun
import ubuntudroid.chimney.di.ApplicationComponent
import ubuntudroid.chimney.di.repositoryModule
import ubuntudroid.chimney.di.steamApiModule
import ubuntudroid.chimney.di.steamNetworkModule
import ubuntudroid.chimney.profile.profileModule

@RunWith(AndroidJUnit4::class)
class ChimneyKoinTest : KoinTest {

        @Test
        fun testKoin(){
            // doesn't work at the moment due to usage of application context - see https://github.com/Ekito/koin/issues/95 for a discussion of the issue
            startKoin(ApplicationComponent.modules)
            dryRun()
        }
}