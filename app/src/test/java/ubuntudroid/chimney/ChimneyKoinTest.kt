package ubuntudroid.chimney

import org.junit.Assert.*
import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.test.KoinTest
import org.koin.test.dryRun
import ubuntudroid.chimney.di.repositoryModule
import ubuntudroid.chimney.di.steamApiModule
import ubuntudroid.chimney.di.steamNetworkModule

class ChimneyKoinTest : KoinTest {

        @Test
        fun testKoin(){
            startKoin(listOf(steamNetworkModule, steamApiModule, repositoryModule))
            dryRun()
        }
}