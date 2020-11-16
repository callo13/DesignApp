package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class Inscription {
    @FXML
    private Pane panel_chien;

    @FXML
    private Pane panel_con;

    @FXML
    private Button button_chien;

    @FXML
    private Button button_con;

    @FXML
    private void handle(ActionEvent event){
        if(event.getSource()==button_chien){
            panel_chien.setVisible(true);
            panel_con.setVisible(false);
        }
        if(event.getSource()==button_con){
            panel_chien.setVisible(false);
            panel_con.setVisible(true);
        }
    }
}
