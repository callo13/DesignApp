package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.json.JSONObject;


import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.http.HttpClient;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ResourceBundle;

public class DashBoard implements Initializable {

    @FXML
    private Button btn_projet;

    @FXML
    private Button btn_message;

    @FXML
    private Button bnt_deconnecter;
    @FXML
    private Button btn_option;
    @FXML
    private Pane panel_deconnecter;
    @FXML
    private Pane panelAffichage;
    @FXML
    private Pane panel_option;
    @FXML
    private ListView lstV_doc;
    @FXML
    private Pane panel_projet;
    @FXML
    private Label labelAffichage,labelNom;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        panel_deconnecter.setVisible(false);
        panel_option.setVisible(false);
        Affichage_nom();
    }

    @FXML
    public void choosedoc(ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        File selecFile =fc.showOpenDialog(null);
        if (selecFile != null) {
            lstV_doc.getItems().add(selecFile.getName());
        }
        System.out.println(selecFile.getAbsolutePath());
        //sendPost(selecFile.getAbsolutePath());

        GFG("D:/Bureau",);
        URL website = new URL("http://localhost:8080/getDocs/1");
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream("information.html");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

    public void handle(ActionEvent event){
        if(event.getSource()==btn_projet){

            Visibility(1);
            labelAffichage.setText("Suivi de projet");
            panelAffichage.setBackground(new Background(new BackgroundFill(Color.rgb(113,86,221), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else if(event.getSource()==btn_message){
            Visibility(2);
            labelAffichage.setText("Message reçu");
            panelAffichage.setBackground(new Background(new BackgroundFill(Color.rgb(43,63,99), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else if(event.getSource()==btn_option){
            labelAffichage.setText("Options");
            panelAffichage.setBackground(new Background(new BackgroundFill(Color.rgb(43,99,63), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else if(event.getSource()==bnt_deconnecter){
            labelAffichage.setText("Bye bye fdp");
            panelAffichage.setBackground(new Background(new BackgroundFill(Color.rgb(43,63,99), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    public void Visibility(int i){
            if(i==1) {
                panel_deconnecter.setVisible(false);
                panel_option.setVisible(true);
            }
            else{
                panel_deconnecter.setVisible(true);
                panel_option.setVisible(false);
            }
    }
    public void Affichage_nom() {
        labelNom.setText(Data.prenom + " " + Data.nom);
    }
    public void sendPost(String Url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost("http://localhost:8080/addDocs");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
        // This attaches the file to the POST:
        File f = new File(Url);
        builder.addBinaryBody(
                "file",
                new FileInputStream(f),
                ContentType.APPLICATION_OCTET_STREAM,
                f.getName()
        );
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(uploadFile);
        HttpEntity responseEntity = response.getEntity();

    }
    public void GFG(String FILEPATH, byte[] bytes){

        // Path of a file
         FILEPATH = "";
         File file = new File("D:/Bureau");

        // Method which write the bytes into a fil
            try {

                // Initialize a pointer
                // in file using OutputStream
                OutputStream
                        os
                        = new FileOutputStream(file);

                // Starts writing the bytes in it
                os.write(bytes);
                System.out.println("Successfully"
                        + " byte inserted");

                // Close the file
                os.close();
            }

            catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }
}
