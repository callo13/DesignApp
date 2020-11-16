package sample;

import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.util.*;

public class Sample2 implements Initializable {
    @FXML
    private AnchorPane connecter;
    @FXML
    private TextField Email;
    @FXML
    private TextField password;
    @FXML
    private TextField password2;
    @FXML
    private TextField emailC;
    @FXML
    private PasswordField passwordC;
    @FXML
    private Button Valider;
    @FXML
    private AnchorPane inscrire;
    @FXML
    private ComboBox<String> ListeMetier;
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
        init_combobox();
    }
    @FXML
    private void Valider(ActionEvent event) {
        JSONObject json = new JSONObject();
        String metier = ListeMetier.getSelectionModel().getSelectedItem();
        if("Wpm" == metier) {
            json.put("metier",1);
        }
        else if ("Technicien-methode" == metier){
            json.put("metier",2);
        }
        else if ("Ordonnanceur" == metier){
            json.put("metier",3);
        }
        json.put("nom", "Callonico");
        json.put("prenom", "Florian");
        json.put("mail", Email.getText());
        json.put("password", password.getText());
        /*switch (metier){
            case "Wpm":
                System.out.println("1");
                json.put("metier",1);
            case "Technicien-methode":
                System.out.println("2");
                json.put("metier",2);
            case "Ordonnanceur":
                //json.put("metier",3);
            default:
        }*/
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("http://localhost:8080/addUser");
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            httpClient.execute(request);
// handle response here...
        } catch (Exception ex) {
            // handle exception here
    }
        reset_field();
        inscrire.setVisible(false);
        connecter.setVisible(true);
    }
    @FXML
    void Connecter(ActionEvent event) throws IOException {
        String mail = emailC.getText();
        String url = "http://localhost:8080/getUserbymail?mail="+ mail;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = 0;
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print in String
        String re;
        re = response.toString().substring(1);
        re = re.substring(0, re.length() - 1);
        System.out.println(re);
        //Read JSON response and print
        JSONObject myResponse = new JSONObject(re.toString());
        System.out.println(passwordC.getText());
        System.out.println(myResponse.getString("password"));
        if(myResponse.getString("password").equals(passwordC.getText())){
            Data.nom =  myResponse.getString("nom");
            Data.prenom= myResponse.getString(("prenom"));
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Dashboard.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Deuxième Windows");
            stage.setScene(scene);
            stage.show();
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Mot de passe invalide");
            //errorAlert.setContentText("The size of First Name must be between 2 and 25 characters");
            errorAlert.showAndWait();
        }
        System.out.println("result after Reading JSON Response");
        //JSONArray arr = obj.getJSONArray("");
        System.out.println("mail = " + myResponse.getString("mail"));
        //System.out.println("password = " + myResponse.getString("password"));
    }
    private void init_combobox(){
        ObservableList<String> list = FXCollections.observableArrayList();
        ListeMetier.getItems().addAll("Wpm", "Technicien-methode", "Ordonnanceur");
    }
    private void reset_field(){
        password.setText("");
        password2.setText("");
        Email.setText("");
    }




}
