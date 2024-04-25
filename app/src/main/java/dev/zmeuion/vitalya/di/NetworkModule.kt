package dev.zmeuion.vitalya.di

import dev.zmeuion.vitalya.network.HttpRoutes
import dev.zmeuion.vitalya.network.api.ScheduleApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val NetworkModule = module {

    single {
        HttpClient(OkHttp) {
            defaultRequest { url(HttpRoutes.BASE_URL) }

            install(ContentNegotiation) {
                json(
                    Json
                )
            }
            install(Logging) {
                logger = Logger.SIMPLE

            }
        }
    }

    single {
        ScheduleApi(get())
    }
}