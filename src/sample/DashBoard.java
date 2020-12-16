package sample;

import com.sun.jna.platform.FileUtils;
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
import org.json.JSONObject;

import javax.swing.text.html.parser.Entity;
import java.awt.*;
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
import java.util.Arrays;
import java.util.Base64;
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
    public void choosedoc(ActionEvent event) throws IOException, SQLException {
        //sendPost();
        FileChooser fc = new FileChooser();
        Stage stage = null;
        File selecFile =fc.showOpenDialog(null);
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
        //System.out.println();
        /*recuperer_fichier();
        String yo = Data.nom;
        byte[] decoded = Base64.getDecoder().decode(yo);
        System.out.println(Data.nom);*/
        //byte[] b =yo.getBytes();
        //System.out.println(b);
        /*byte[] fileContent = Files.readAllBytes(selecFile.toPath());
        String str = new String(fileContent);
        System.out.println(str);*/
        /*
        FileInputStream is = new FileInputStream(selecFile);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        System.out.println("buffer\n"+buffer.toByteArray());
        String sttr= new String(buffer.toByteArray());
        System.out.println("Contenu\n"+sttr);
        String str = new String(byteArray);
       /* Blob blob = "";
        byte [] bytes = blob.getBytes(1l, (int)bl  ob.length());
        for(int i=0; i<bytes.length;i++) {
            System.out.println(Arrays.toString(bytes));
        }*/


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
