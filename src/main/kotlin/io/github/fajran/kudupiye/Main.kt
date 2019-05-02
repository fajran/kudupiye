package io.github.fajran.kudupiye

import com.fasterxml.jackson.annotation.JsonInclude
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
import java.io.File
import kotlin.system.measureTimeMillis

class Main(
        private val nodesTsvFile: File
) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val nodesTsv = File(args[0])

            val main = Main(nodesTsv)
            main.start()
        }
    }

    lateinit var agg: Aggregate

    fun start() {
        loadHierarchy()

        embeddedServer(Netty, 8080) {
            install(ContentNegotiation) {
                jackson { }
            }
            routing {
                get("/n/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                            ?: return@get call.respond(HttpStatusCode.NotFound)
                    val data = agg[id]?.data
                            ?: return@get call.respond(HttpStatusCode.NotFound)
                    call.respond(data.values)
                }

                put("/n/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                            ?: return@put call.respond(HttpStatusCode.NotFound)

                    val values = call.receive<Map<String, Int>>()
                    val data = Data(values.toMutableMap())

                    agg.set(id, data)
                            ?: return@put call.respond(HttpStatusCode.NotFound)
                    call.respond(HttpStatusCode.OK)
                }

                get("/t/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                            ?: return@get call.respond(HttpStatusCode.NotFound)
                    val node = agg[id]
                            ?: return@get call.respond(HttpStatusCode.NotFound)

                    val children = node.children
                            .map { c -> Tree(c.id, c.data.values.toMap()) }
                    val tree = Tree(node.id, node.data.values.toMap(), children)
                    call.respond(tree)
                }
            }
        }.start(wait = true)
    }

    private fun loadHierarchy() {
        println("Loading hierarchy data..")

        val tb = TreeBuilder()
        val durationMs = measureTimeMillis {
            nodesTsvFile.useLines { lines ->
                lines.forEachIndexed { idx, line ->
                    if (idx == 0) // skip header
                        return@forEachIndexed

                    val p = line.split("\t", limit = 2)
                    tb.add(p[0].toInt(), p[1].toInt())
                }
                return@useLines tb.build()
            }
        }
        println("Hierarchy data loaded in $durationMs ms")

        val nodes = tb.build()
        agg = Aggregate(nodes)
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Tree(
        val id: Int,
        val data: Map<String, Int>,
        val children: List<Tree>? = null
)
