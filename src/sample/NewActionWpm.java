package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.ResourceBundle;

public class NewActionWpm {
    @FXML
    private DatePicker dtae_picker;

    @FXML
    private TextField textfiel_document;

    @FXML
    private ComboBox<String> combobox_methode;

    @FXML
    private Button Valider;


    @FXML
    void charger_document(ActionEvent event) throws IOException {
        charger_doc();
        init_combobox();
    }
    @FXML
    void envoyer_doc(ActionEvent event) throws IOException {
        envoyer_document(Data.file);

    }

    public File charger_doc(){
        FileChooser fc = new FileChooser();
        Stage stage = null;
        File selecFile = fc.showOpenDialog(null);
        if (selecFile != null) {
            textfiel_document.setText(selecFile.getName());
        }
        Data.file=selecFile;
        return selecFile;
    }
    public void envoyer_document(File selecFile) throws IOException {
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
        public void init_combobox() throws IOException {
            String url = "http://localhost:8082/getUsers";
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
            ObservableList<String> tableau = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String value1 = jsonObject1.optString("nom");
                String value2 = jsonObject1.optString("prenom");
                //Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(value1);

                tableau.add(value1 +" "+value2);
                //System.out.println(value1 + value2);
            }
            combobox_methode.setItems(tableau);
        }
    }
