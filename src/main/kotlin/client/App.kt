package client

import javafx.concurrent.Worker
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.web.WebView
import javafx.stage.Screen
import javafx.stage.Stage
import netscape.javascript.JSObject

class App(val primaryStage: Stage) {

    val screen = Screen.getPrimary()

    val browser = WebView()
    val engine = browser.engine

    init {
        val visual = screen.visualBounds
        browser.setPrefSize(visual.width * 1, visual.height * 0.9)

        engine.loadWorker.stateProperty()
                .addListener { obs, oldValue, newValue ->
                    if (newValue == Worker.State.SUCCEEDED) {
                        val window = engine.executeScript("window") as JSObject
                    }
                }

        engine.load("http://localhost:9090/index.html")

        val root = Group()
        val scene = Scene(root)

        root.children.add(browser)

        primaryStage.scene = scene
        primaryStage.show()
    }

}