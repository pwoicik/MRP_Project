package di

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import data.MrpDatabase
import data.repository.MrpRepositoryImpl
import domain.repository.MrpRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectory

val mrpModule = module {
    single {
        val dbFile = Path(System.getProperty("user.home"), "AppData", "Roaming", "MRP_Project")
            .run {
                try {
                    createDirectory()
                } catch (_: Exception) {}
                File(absolutePathString(), "db.db")
            }
        val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${dbFile.absolutePath}")
        if (!dbFile.exists()) {
            MrpDatabase.Schema.create(driver)
        }
        MrpDatabase(driver)
    }

    single { MrpRepositoryImpl(get()) } bind MrpRepository::class
}
