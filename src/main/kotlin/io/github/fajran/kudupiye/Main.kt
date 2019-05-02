package io.github.fajran.kudupiye

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val main = Main()
            main.start()
        }
    }

    fun start() {
        val nodes = TreeBuilder()
                .add(0, 1)
                .add(1, 2)
                .add(2, 3)
                .add(2, 4)
                .add(1, 5)
                .add(0, 6)
                .add(6, 7)
                .build()

        val agg = Aggregate(nodes)

        embeddedServer(Netty, 8080) {
            install(ContentNegotiation) {
                jackson { }
            }
            routing {
                get("/n/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                            ?: return@get call.respond(HttpStatusCode.NotFound)
                    val data = agg[id]
                            ?: return@get call.respond(HttpStatusCode.NotFound)
                    call.respond(data)
                }
                put("/n/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                            ?: return@put call.respond(HttpStatusCode.NotFound)
                    val data = call.receive<Data>()
                    agg.set(id, data)
                            ?: return@put call.respond(HttpStatusCode.NotFound)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }.start(wait = true)
    }
}
