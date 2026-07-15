package org.createmixam;

import java.awt.Desktop;
import java.net.URI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LauncherController {


    @FXML
    private void openReleasePage(ActionEvent event) {

        try {

            Desktop.getDesktop().browse(
                new URI(
                    "https://github.com/Le-Laboratoire-du-DrEmixam/Createmixam-launcher/releases/latest"
                )
            );

        } catch(Exception e) {

            e.printStackTrace();

        }

    }



    @FXML
    private void quit(ActionEvent event) {

        System.exit(0);

    }

}