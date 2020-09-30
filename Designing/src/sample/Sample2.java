package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class Sample2 implements Initializable {
    @FXML
    private AnchorPane connecter;

    @FXML
    private AnchorPane inscrire;

    @FXML
    private Button Inscrire,Connecter;

    @FXML
    private void prout(ActionEvent event){
        if(event.getSource()==Inscrire){
            inscrire.setVisible(true);
        }
    }
    @FXML
    private void prout2(ActionEvent event){
        if(event.getSource()== Connecter){
            inscrire.setVisible(false);
        }
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        inscrire.setVisible(false);
        connecter.setVisible(true);
    }

}
