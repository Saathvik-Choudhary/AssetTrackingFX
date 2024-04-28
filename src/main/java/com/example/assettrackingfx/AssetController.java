package com.example.assettrackingfx;

import javafx.scene.layout.Pane;
import org.json.JSONException;
import org.json.JSONObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AssetController {

    @FXML
    Button HomeButton;

    @FXML
    Button AssetButton;

    @FXML
    TextField countValue;

    @FXML
    TextField currentValue;

    @FXML
    TextField costValue;

    @FXML
    Pane asset;

    @FXML
    TextField title;

    @FXML
    TextField rate;

    @FXML
    TextField age;

    @FXML
    TextField cost;

    @FXML
    TextField value;


    @FXML
    public void goToHome() throws IOException {
        Stage stage=new Stage();

        setSummaryData();

        FXMLLoader fxmlLoader = new FXMLLoader(AssetTracker.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Asset Tracker");
        stage.setScene(scene);
        stage.show();

        setSummaryData();
    }

    public void goToAsset() throws IOException {
        Stage stage=new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(AssetTracker.class.getResource("assetlist.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Asset Tracker");
        stage.setScene(scene);
        stage.show();

        setAssetListData();
    }


    public void goToSave() throws IOException {
        Stage stage=new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(AssetTracker.class.getResource("save.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Asset Tracker");
        stage.setScene(scene);
        stage.show();
    }

    private void setAssetListData() {
        try {
            // Specify the URL of the API
            URL url = new URL("http://localhost:8080/asset/all");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line="";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response
            JSONObject jsonObject = new JSONObject(response.toString());

            System.out.println(response);



        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveAsset(){

    }

    private void setSummaryData() {
        try {
            // Specify the URL of the API
            URL url = new URL("http://localhost:8080/asset/summary");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line="sdfsdf";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response
            JSONObject jsonObject = new JSONObject(response.toString());

            // Extract values from JSON and set them to labels
            double cost = jsonObject.getDouble("cost");
            double value = jsonObject.getDouble("value");
            int count = jsonObject.getInt("count");

            costValue.setText(String.valueOf(cost));
            currentValue.setText(String.valueOf(value));
            countValue.setText(String.valueOf(count));

            System.out.println(response);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}