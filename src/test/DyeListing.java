package test;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** 
 * Application DyeListing.
 * @author Fabrice Bouyé
 */
public final class DyeListing extends Application {

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final ResourceBundle bundle = ResourceBundle.getBundle("test.DyeListing"); // NOI18N.
        final URL fxmlURL = getClass().getResource("DyeListing.fxml"); // NOI18N.
        final FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL, bundle);
        final Parent root = fxmlLoader.load();
        //
        final Scene scene = new Scene(root);
        final URL cssURL = getClass().getResource("DyeListing.css"); // NOI18N.
        scene.getStylesheets().add(cssURL.toExternalForm());
        primaryStage.setTitle(bundle.getString("app.title")); // NOI18N.
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String... args) {
        Application.launch(args);
    }
}
