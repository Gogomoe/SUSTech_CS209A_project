package server

import io.javalin.Javalin
import io.javalin.staticfiles.Location

object Server {

    private val app = Javalin.create()

    fun start() {
        app.get("/") { ctx ->
            ctx.result("Hello World")
        }

        val classpath = System.getProperty("java.class.path").split(";")

        app.enableStaticFiles(classpath.find { it.contains("resources") }!!, Location.EXTERNAL)
        app.start(9090)
    }

    fun stop() {
        app.stop()
    }
}
