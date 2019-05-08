package client;

import com.teamdev.jxbrowser.chromium.bb;
import javafx.application.Application;
import javafx.stage.Stage;
import server.Server;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;

public class Main extends Application {

    static {
        try {
            Field e = bb.class.getDeclaredField("e");
            e.setAccessible(true);
            Field f = bb.class.getDeclaredField("f");
            f.setAccessible(true);
            Field modifersField = Field.class.getDeclaredField("modifiers");
            modifersField.setAccessible(true);
            modifersField.setInt(e, e.getModifiers() & ~Modifier.FINAL);
            modifersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            e.set(null, new BigInteger("1"));
            f.set(null, new BigInteger("1"));
            modifersField.setAccessible(false);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server.INSTANCE.start();
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new App(primaryStage);
    }
}
