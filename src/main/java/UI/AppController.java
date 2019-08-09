package UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @FXML
    private Label label;

    public void initialize(URL location, ResourceBundle resources) {
        label.setText("Merhaba");

    }

    @FXML
    public void setLabelText(ActionEvent event) {
        label.setText("Merhabe dünya");
    }
}
