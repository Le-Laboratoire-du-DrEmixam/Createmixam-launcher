package org.createmixam;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LauncherApplication extends Application {

    @Override
    public void start(Stage splashStage) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/splash.fxml"));

            Parent root = loader.load();

            splashStage.initStyle(StageStyle.TRANSPARENT);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            splashStage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/icon.png")));

            splashStage.setScene(scene);
            splashStage.show();

            Platform.runLater(() -> {
                splashStage.setAlwaysOnTop(true);
                splashStage.toFront();
                splashStage.requestFocus();
                splashStage.setAlwaysOnTop(false);
            });

        } catch (IOException e) {

            e.printStackTrace();

        }

        Task<Boolean> checkManifest = new Task<>() {

            @Override
            protected Boolean call() throws Exception {

                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(
                                "https://api.github.com/repos/Le-Laboratoire-du-DrEmixam/Createmixam-launcher/releases/latest"))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

                JSONObject json = new JSONObject(response.body());

                String remoteVersion = json
                        .getString("tag_name")
                        .replace("v", "");

                JSONObject localManifest = new JSONObject(
                        new String(
                                getClass()
                                        .getResourceAsStream("/manifest.json")
                                        .readAllBytes()));

                String localVersion = localManifest.getString("version");

                System.out.println("Server version: " + remoteVersion);
                System.out.println("Local version: " + localVersion);

                return !remoteVersion.equals(localVersion);
            }
        };

        checkManifest.setOnSucceeded(event -> {

            if (checkManifest.getValue()) {

                showUpdateWindow(splashStage);

            } else {

                openLauncher(splashStage);

            }

        });

        checkManifest.setOnFailed(event -> {
            checkManifest.getException().printStackTrace();
        });

        Thread thread = new Thread(checkManifest);
        thread.setDaemon(true);
        thread.start();
    }

    private void showUpdateWindow(Stage splashStage) {

        try {

            splashStage.close();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/update.fxml"));

            Parent root = loader.load();

            Stage updateStage = new Stage();

            Scene scene = new Scene(root);

            updateStage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/icon.png")));

            updateStage.setScene(scene);
            updateStage.show();

            Platform.runLater(() -> {
                updateStage.setAlwaysOnTop(true);
                updateStage.toFront();
                updateStage.requestFocus();
                updateStage.setAlwaysOnTop(false);
            });

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private void openLauncher(Stage splashStage) {

        PauseTransition delay = new PauseTransition(Duration.seconds(2));

        delay.setOnFinished(event -> {

            splashStage.close();

            // TODO: Open the main launcher window here
        });

        delay.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}