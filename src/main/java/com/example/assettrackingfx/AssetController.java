package com.example.assettrackingfx;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import static sun.security.util.KnownOIDs.ContentType;

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
    TextField depreciationRate;

    @FXML
    ChoiceBox<String> currency;

    @FXML
    DatePicker date;

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

    public void goToAsset() throws IOException, JSONException {
        Stage stage=new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(AssetTracker.class.getResource("assetlist.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Asset Tracker");
        stage.setScene(scene);
        stage.show();

        setAssetListData(new Stage());
    }


    public void goToSave() throws IOException {
        Stage stage=new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(AssetTracker.class.getResource("save.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Asset Tracker");
        stage.setScene(scene);
        stage.show();
    }

    private void setAssetListDatata() {
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
         //   e.printStackTrace();
        }
    }


    private static final String API_URL = "http://localhost:8080/asset/all";

    public void setAssetListData(Stage primaryStage) throws JSONException {
        VBox root = new VBox(10); // Vertical box to hold containers

        // Retrieve JSON data from the API
        JSONArray jsonArray = getJSONArrayFromAPI();

        // Iterate over each object in the JSON array
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            // Create a container for each JSON object
            VBox container = new VBox(5);
            container.setStyle("-fx-border-color: black; -fx-padding: 10px;");

            // Populate the container with data from the JSON object
            jsonObject.keys().forEachRemaining(key -> {
                Label label = null;
                try {
                    label = new Label(key + ": " + jsonObject.get((String) key));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                container.getChildren().add(label);
            });

            // Add the container to the root layout
            root.getChildren().add(container);
        }

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JSON Data Display");
        primaryStage.show();
    }

    // Method to retrieve JSON array from the API
    private JSONArray getJSONArrayFromAPI() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }

            return new JSONArray(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray(); // return empty array in case of error
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public void saveDataToJson() throws JSONException {
        // Gather data from UI components
        String titleText = title.getText();
        String costText = cost.getText();
        String depreciationRateText = depreciationRate.getText();
        String currencyText = currency.getValue(); // Assuming currency is String type
        String dateText = date.getValue().toString(); // Convert DatePicker value to String

        // Create JSON object and put data into it
        JSONObject json = new JSONObject();
        json.put("title", titleText);
        json.put("cost", costText);
        json.put("depreciationRate", depreciationRateText);
        json.put("currency", currencyText);
        json.put("date", dateText);

        // Convert JSON object to string
        String jsonString = json.toString();

        // You can now use the jsonString as needed, such as sending it over a network or storing it in a file.
        System.out.println(jsonString);
    }

    public static void sendDataToAPI(String jsonData) throws UnsupportedEncodingException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/asset/save");

        // Set headers if needed
        httpPost.setHeader("Content-Type", "application/json");

        // Set JSON data to send
        StringEntity requestEntity = new StringEntity(jsonData);
        httpPost.setEntity(requestEntity);

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            // Handle response if needed

            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            String line="";
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