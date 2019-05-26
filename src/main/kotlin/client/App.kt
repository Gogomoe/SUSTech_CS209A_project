package client

import com.teamdev.jxbrowser.chromium.Browser
import com.teamdev.jxbrowser.chromium.events.ScriptContextAdapter
import com.teamdev.jxbrowser.chromium.events.ScriptContextEvent
import com.teamdev.jxbrowser.chromium.javafx.BrowserView
import controller.Controller
import javafx.application.Platform
import javafx.scene.Group
import javafx.scene.Scene
import javafx.stage.Screen
import javafx.stage.Stage
import server.Server
import store.JsonCategoryStore
import store.JsonProductStore
import store.JsonTagWeightStore


class App(val primaryStage: Stage) {

    val screen = Screen.getPrimary()

    val browser = Browser()
    val view = BrowserView(browser)

    private val productStore = JsonProductStore()
    private val categoryStore = JsonCategoryStore()
    private val tagWeightStore = JsonTagWeightStore()

    val controller = Controller(categoryStore, productStore, tagWeightStore)

    init {

        val visual = screen.visualBounds
        view.setPrefSize(visual.width * 1, visual.height * 0.9)

        browser.addScriptContextListener(object : ScriptContextAdapter() {
            override fun onScriptContextCreated(event: ScriptContextEvent?) {
                val browser = event!!.browser
                val window = browser.executeJavaScriptAndReturnValue("window")
                window.asObject().setProperty("Controller", controller)
            }
        })

        browser.addConsoleListener { event -> println(event!!.message) }

        val root = Group()
        val scene = Scene(root)

        root.children.add(view)

        primaryStage.scene = scene
        primaryStage.setOnCloseRequest {
            Platform.exit()
            Server.stop()
            Thread { browser.dispose() }.start()
            Thread {
                Thread.sleep(100)
                System.exit(0)
            }.start()
        }
        primaryStage.show()

        browser.loadURL("http://localhost:9090/index.html")
    }

}