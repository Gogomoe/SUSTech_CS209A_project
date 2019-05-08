package client

import com.teamdev.jxbrowser.chromium.Browser
import com.teamdev.jxbrowser.chromium.events.ScriptContextAdapter
import com.teamdev.jxbrowser.chromium.events.ScriptContextEvent
import com.teamdev.jxbrowser.chromium.javafx.BrowserView
import controller.CategoryController
import javafx.application.Platform
import javafx.scene.Group
import javafx.scene.Scene
import javafx.stage.Screen
import javafx.stage.Stage
import server.Server
import store.FakeCategoryStore
import store.FakeProductStore


class App(val primaryStage: Stage) {

    val screen = Screen.getPrimary()

    val browser = Browser()
    val view = BrowserView(browser)

    val categoryController = CategoryController(FakeCategoryStore(), FakeProductStore())

    init {

        val visual = screen.visualBounds
        view.setPrefSize(visual.width * 1, visual.height * 0.9)

        browser.addScriptContextListener(object : ScriptContextAdapter() {
            override fun onScriptContextCreated(event: ScriptContextEvent?) {
                val browser = event!!.browser
                val window = browser.executeJavaScriptAndReturnValue("window")
                window.asObject().setProperty("Category", categoryController)
            }
        })

        val root = Group()
        val scene = Scene(root)

        root.children.add(view)

        primaryStage.scene = scene
        primaryStage.setOnCloseRequest {
            Platform.exit()
            Thread { browser.dispose() }.start()
            Server.stop()
        }
        primaryStage.show()

        browser.loadURL("http://localhost:9090/index.html")
    }


}