package di

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import data.MrpDatabase
import data.adapters.mutableProductTreeNodeAdapter
import data.entity.ProductTree
import data.repository.MrpRepositoryImpl
import domain.repository.MrpRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.bind
import org.koin.dsl.module

val mrpModule = module {
    single {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        MrpDatabase.Schema.create(driver)
        MrpDatabase(
            driver = driver,
            productTreeAdapter = ProductTree.Adapter(
                nodeAdapter = mutableProductTreeNodeAdapter
            )
        )
    }

    single {
        MrpRepositoryImpl(
            db = get(),
            dbCoroutineDispatcher = Dispatchers.IO
        )
    } bind MrpRepository::class
}
