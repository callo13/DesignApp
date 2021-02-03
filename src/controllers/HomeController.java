package controllers;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.Data;
import sample.Tableau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class HomeController implements Initializable {
    @FXML
    private Label label_nom;
    @FXML
    private TextField txt_document;
    @FXML
    private DatePicker txt_date_creation;
    @FXML
    private Button btnSave;
    @FXML
    private ComboBox<String> txt_methode;
    @FXML
    private TableView<Tableau> table;
    @FXML
    private TableColumn<Tableau, String> col_id;
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
    @FXML
    private TableColumn<Tableau, String> col_fin;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Affichage_nom();
        Data.size_tab_debut=table.getItems().size();
        //System.out.println(table.getColumns().get(1).getCellObservableValue(1).getValue());

        try {
            init_combobox();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            charger_tableau();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ObservableList<Tableau> tb =table.getItems();
        for(int i=0;i<tb.size();i++){
            System.out.println(tb.get(i).getProduit());
        }
    }
    @FXML
    public void changer(ActionEvent event){
        //Integer tab_debut=Data.size_tab_début;
        /*ObservableList<Tableau> tb =table.getItems();
        int i=table.getItems().size()-1;
        System.out.println("i = "+i);
        //for(int i=Data.size_tab_début;i<=table.getItems().size();i++){
            JSONObject json = new JSONObject();
            System.out.println("Date creation " + tb.get(i).getDate_creation());
            System.out.println(tb.get(i).getProduit());
            System.out.println(tb.get(i).getCi());

            json.put("date_creation", tb.get(i).getDate_creation());
            json.put("produit", tb.get(i).getProduit());
            json.put("ci", tb.get(i).getCi());
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            try {
                HttpPost request = new HttpPost("http://localhost:8081/addRow");
                StringEntity params = new StringEntity(json.toString());
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                httpClient.execute(request);
// handle response here...
            } catch (Exception ex) {
                // handle exception here
            }

        //}*/
        /*System.out.println(Data.id_change.size());
        for(int i=0;i<Data.id_change.size();i++){
            System.out.println("Valeur liste "+i+" : " + Data.id_change.get(i));
        }*/
        ObservableList<Tableau> tb =table.getItems();
        for(int i=0;i<tb.size();i++){
            System.out.println(tb.get(i).getProduit());
        }

        Set set = new HashSet() ;
        set.addAll(Data.id_change) ;
        ArrayList<Integer> distinctList = new ArrayList<Integer>(set) ;
        for(int i=0;i<distinctList.size();i++){
            System.out.println("Valeur liste sans doublon "+i+" : " + distinctList.get(i));
        }
        if(distinctList.size()!=0){
            for(int i=0;i<distinctList.size();i++){
                if(distinctList.get(i)<=Data.size_tab_debut) {
                    //System.out.println("Valeur liste sans doublon "+i+" : " + distinctList.get(i));
                    JSONObject json = new JSONObject();
                    json.put("date_creation", tb.get(distinctList.get(i)).getDate_creation());
                    json.put("produit", tb.get(distinctList.get(i)).getProduit());
                    json.put("wpm", tb.get(distinctList.get(i)).getWpm());
                    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                    try {
                        HttpPost request = new HttpPost("http://localhost:8081/addRow");
                        StringEntity params = new StringEntity(json.toString());
                        request.addHeader("content-type", "application/json");
                        request.setEntity(params);
                        httpClient.execute(request);
// handle response here...
                    } catch (Exception ex) {
                        // handle exception here
                    }
                }
            }
        }

    }
    @FXML
    public void add_row(ActionEvent event){
        Tableau tab = new Tableau();
        String date = txt_date_creation.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println("tg" + date);
        tab.setDate_creation(date);
        tab.setCi(txt_document.getText());
        tab.setMethode(txt_methode.getValue());
        tab.setId(table.getItems().size()+1);
        table.getItems().add(tab);
        txt_document.clear();
        txt_methode.setAccessibleText("");
        txt_date_creation.setAccessibleText("");
        /*Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Row Ajouté");
        //errorAlert.setContentText("The size of First Name must be between 2 and 25 characters");
        errorAlert.showAndWait();*/
        //System.out.println("Derniere ligne du tab" + table.getItems().size());
    }

    public void charger_tableau() throws IOException, ParseException {
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
            String value2 = jsonObject1.optString("produit");
            String value3 = jsonObject1.optString("wpm");
            String value4 = jsonObject1.optString("ci");
            String value5 = jsonObject1.optString("methode");
            String value6 = jsonObject1.optString("plannifiation");
            String value7 = jsonObject1.optString("debut");
            String value8 = jsonObject1.optString("fin");
            String value9 = jsonObject1.optString("id");
            Integer id = Integer.parseInt(value9);
            //Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(value1);

            tableau.add(new Tableau(value1, value2, value3, value4, value5, value6, value7, value8, id));
            //System.out.println(value1 + value2);
        }
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_produit.setCellValueFactory(new PropertyValueFactory<>("produit"));
        col_wpm.setCellValueFactory(new PropertyValueFactory<>("wpm"));
        col_ci.setCellValueFactory(new PropertyValueFactory<>("ci"));
        col_date_creation.setCellValueFactory(new PropertyValueFactory<>("date_creation"));
        col_methode.setCellValueFactory(new PropertyValueFactory<>("methode"));
        col_plannif.setCellValueFactory(new PropertyValueFactory<>("plannif"));
        col_fin.setCellValueFactory(new PropertyValueFactory<>("fin"));
        col_debut.setCellValueFactory(new PropertyValueFactory<>("debut"));
        //col_fc.setVisible(false);
        table.setItems(tableau);
        table.setEditable(true);

        col_ci.setCellFactory(TextFieldTableCell.forTableColumn());
        col_ci.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setCi(e.getNewValue());
        });
        col_date_creation.setCellFactory(TextFieldTableCell.forTableColumn());
        col_date_creation.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setCi(e.getNewValue());
        });
        col_produit.setCellFactory(TextFieldTableCell.forTableColumn());
        col_produit.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setProduit(e.getNewValue());
        });
        col_wpm.setCellFactory(TextFieldTableCell.forTableColumn());
        col_wpm.setOnEditCommit(e -> {
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
        Object object = table.getSelectionModel().selectedItemProperty().get();
        int index = table.getSelectionModel().selectedIndexProperty().get();
        table.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tableau> observable, Tableau oldValue, Tableau newValue) -> {
            //System.out.println("Select row" + table.getSelectionModel().getSelectedIndex());

        });
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {

                //System.out.println("Row selectionné Id :"
                        //+ newValue.getId());

            }
        });
        col_produit.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Tableau, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Tableau, String> t) {
                        if (t.getNewValue().equals("") || t.getNewValue().length() < 3) {
                            //info.setText("Book name must be greater than 3 characters.");

                            // workaround for refreshing rendered values
                            t.getTableView().getColumns().get(0).setVisible(false);
                            t.getTableView().getColumns().get(0).setVisible(true);

                            // set the old value or do not update the cell.
                            return;
                        }
                        ((Tableau) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setProduit(t.getNewValue());
                        System.out.println(t.getTablePosition().getRow());
                        Data.id_change.add(t.getTablePosition().getRow());
                    }
                }
        );
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
        txt_methode.setItems(tableau);
    }
    public void Affichage_nom() {
        label_nom.setText(Data.prenom + " " + Data.nom);
    }
}
