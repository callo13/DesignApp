package sample;

import com.sun.javafx.stage.EmbeddedWindow;
import com.sun.jna.platform.FileUtils;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.embed.swing.SwingFXUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.ContentBody;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.text.html.parser.Entity;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
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
    private Label labelAffichage, labelNom;
    @FXML
    private TableView<Tableau> table;
    @FXML
    private TableColumn<Tableau, String> col_date_creation;
    @FXML
    private TableColumn<Tableau, String> col_produit;
    @FXML
    private TableColumn<Tableau, String> col_wpm;
    @FXML
    private TableColumn<Tableau, String> col_ci;
    @FXML
    private TableColumn<Tableau, String> col_methode;
    @FXML
    private TableColumn<Tableau, String> col_fc;
    @FXML
    private TableColumn<Tableau, String> col_plannif;
    @FXML
    private TableColumn<Tableau, String> col_debut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        panel_deconnecter.setVisible(false);
        panel_option.setVisible(false);
        Affichage_nom();
        try {
            charger_tableau();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
        System.out.println("Response Code : ");

    }
    @FXML
    public void choosedoc(ActionEvent event) throws IOException, SQLException {
        //sendPost();
        FileChooser fc = new FileChooser();
        Stage stage = null;
        File selecFile = fc.showOpenDialog(null);
        if (selecFile != null) {
            lstV_doc.getItems().add(selecFile.getName());
        }
        //sendPost(selecFile.getAbsolutePath());*/
        //fc.showSaveDialog(stage);
        //ouvrir nimporte qu'elle file
        /*if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(new File("D:\\Documents\\Bureau\\valeur-stock-AGs.xlsm"));
        }*/
        byte[] fileContent = Files.readAllBytes(selecFile.toPath());
        String data = Base64.getEncoder().encodeToString(fileContent);
        JSONObject json = new JSONObject();
        json.put("data", data);
        json.put("docName", selecFile.getName());
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("http://localhost:8080/addDocs");
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            httpClient.execute(request);
// handle response here...
        } catch (Exception ex) {
            // handle exception here
        }
    }
    public void charger_tableau() throws IOException, ParseException{
        String url = "http://localhost:8081/getRows";
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
        re = response.toString();
        //String yo =" {\"user\":" + re + "}";
        //re = response.toString().substring(1);
        //re = re.substring(0, re.length() - 1);
        //System.out.println(re);
        JSONArray jsonArray = new JSONArray(re);
        ObservableList<Tableau> tableau = FXCollections.observableArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String value1 = jsonObject1.optString("date_creation");
            String value2 = jsonObject1.optString("dispo");
            String value3 = jsonObject1.optString("mail");
            String value4 = jsonObject1.optString("password");
            //Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(value1);

            //tableau.add(new Tableau(value1,value2, value3, value4));
            //System.out.println(value1 + value2);
        }
        col_produit.setCellValueFactory(new PropertyValueFactory<>("produit"));
        col_wpm.setCellValueFactory(new PropertyValueFactory<>("wpm"));
        col_ci.setCellValueFactory(new PropertyValueFactory<>("ci"));
        col_date_creation.setCellValueFactory(new PropertyValueFactory<>("date_creation"));
        col_fc.setVisible(false);
        table.setItems(tableau);
        table.setEditable(true);

        col_ci.setCellFactory(TextFieldTableCell.forTableColumn());
        col_ci.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setCi(e.getNewValue());
        });
        col_date_creation.setCellFactory(TextFieldTableCell.forTableColumn());
        col_date_creation.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setCi(e.getNewValue());
        });
        col_produit.setCellFactory(TextFieldTableCell.forTableColumn());
        col_produit.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setProduit(e.getNewValue());
        });
        col_wpm.setCellFactory(TextFieldTableCell.forTableColumn());
        col_wpm.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setCi(e.getNewValue());
        });
        /*table.setRowFactory(tv -> {
            TableRow<Tableau> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Tableau clickedRow = row.getItem();
                    System.out.println(row);
                }
            });
            return row ;
        });*/
        Object object =  table.getSelectionModel().selectedItemProperty().get();
        int index = table.getSelectionModel().selectedIndexProperty().get();
        table.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tableau> observable, Tableau oldValue, Tableau newValue) -> {
            System.out.println(table.getSelectionModel().getSelectedIndex());

        });
        /*table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {

                System.out.println("Selected Person: "
                        + newValue.getProduit()+ " | "
                        + " " + newValue.getDate_creation()

            }
        });*/
    }
    @FXML
    public void envoyer_doc() throws IOException, ParseException {
        String url = "http://localhost:8081/getRows";
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
        re = response.toString();
        //String yo =" {\"user\":" + re + "}";
        //re = response.toString().substring(1);
        //re = re.substring(0, re.length() - 1);
        System.out.println(re);
        JSONArray jsonArray = new JSONArray(re);
        ObservableList<Tableau> tableau = FXCollections.observableArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

            String value1 = jsonObject1.optString("date_creation");
            String value2 = jsonObject1.optString("dispo");
            String value3 = jsonObject1.optString("mail");
            String value4 = jsonObject1.optString("password");
            //Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(value1);
            //System.out.println(date1);
            //tableau.add(new Tableau(value1,value2, value3, value4));
            System.out.println(value1 + value2);
        }
        col_produit.setCellValueFactory(new PropertyValueFactory<>("produit"));
        col_wpm.setCellValueFactory(new PropertyValueFactory<>("wpm"));
        col_ci.setCellValueFactory(new PropertyValueFactory<>("ci"));
        col_date_creation.setCellValueFactory(new PropertyValueFactory<>("date_creation"));
        col_fc.setVisible(false);
        table.setItems(tableau);
        table.setEditable(true);

        col_ci.setCellFactory(TextFieldTableCell.forTableColumn());
        col_ci.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setCi(e.getNewValue());
        });

    }


    @FXML
    public void telecharger_document(ActionEvent event) throws IOException {
        recuperer_fichier();
        String yo = Data.nom;
        byte[] decoded = Base64.getDecoder().decode(yo);
        File someFile = new File(Data.prenom);
        FileOutputStream fos = new FileOutputStream("D://Documents//Bureau//Test_PFE//"+Data.prenom);
        fos.write(decoded);
        fos.flush();
        fos.close();
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(new File("D://Documents//Bureau//Test_PFE//"+Data.prenom));
        }
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
    public void sendPost() throws IOException {
        /*CloseableHttpClient httpClient = HttpClients.createDefault();
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
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", new FileBody(file))
                .build();

        HttpPost request = new HttpPost(url);
        request.setEntity(entity);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);*/
        /*Runtime runtime = Runtime.getRuntime();
        runtime.exec(new String[] {"curl -F 'file=@D:\\Documents\\Bureau\\Qualité.docx' http://localhost:8080/addDocs"});
        String command = "http://localhost:8080/getDocs/2";
        Process process = Runtime.getRuntime().exec("ls");*/
        ProcessBuilder pb = new ProcessBuilder("curl", "-F", "'file=@D:\\Documents\\Bureau\\OUI.txt'", "http://localhost:8080/addDocs ");
        pb.directory(new File("H:/"));
        pb. redirectErrorStream(true);
        Process p = pb.start();

    }
    public void recuperer_fichier() throws IOException {
        String url = "http://localhost:8080/getDocs/1";
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
        re = response.toString();
        System.out.println(re);
        //Read JSON response and print
        JSONObject myResponse = new JSONObject(re.toString());
        //System.out.println(passwordC.getText());
        //System.out.println(myResponse.getString("password"))
        Data.nom =  myResponse.getString("data");
        Data.prenom = myResponse.getString("docName");
        System.out.println("result after Reading JSON Response");
        //JSONArray arr = obj.getJSONArray("");
        //System.out.println("mail = " + myResponse.getString("mail"));
        //System.out.println("password = " + myResponse.getString("password"));
    }


}
