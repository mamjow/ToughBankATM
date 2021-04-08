package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.DailyPage;
import model.JwtResponse;
import model.Transaction;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static model.UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter;


public class homeController implements Initializable {
    @FXML
    AnchorPane anchorPane;

    @FXML
    Label ibanLabel;
    @FXML
    Label ownerLabel;
    @FXML
    Button koppelbtn;

    @FXML
    TextField payerCardCode;
    @FXML
    TextField payerIban;

    @FXML
    Label priceLabel;

    @FXML
    Label resultLabel;

    private GlyphFont glyphFont;
    private JwtResponse jwt;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        glyphFont = GlyphFontRegistry.font("FontAwesome");
        jwt = new JwtResponse();
        jwt.setJwtToken("");
        jwt.setExprireDate(new Date(-1900));
        checkTokenStats();
    }

    /**
     * @param header of the popover
     * @return popover object
     * @author M.J. Moshiri
     * Creates a general popover with preferred attribute
     */
    private PopOver getConfiguredPopover(String header) {
        PopOver popOver = new PopOver();
        popOver.setArrowSize(0);
        popOver.setTitle(header);
        popOver.setDetachable(false);
        popOver.setHeaderAlwaysVisible(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        return popOver;
    }

    private GridPane getConfiguredGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

//        ColumnConstraints col1 = new ColumnConstraints();
//        col1.setPercentWidth(50);
//
//        ColumnConstraints col2 = new ColumnConstraints();
//        col2.setPercentWidth(50);
//        gridPane.getColumnConstraints().addAll(col1, col2);
        return gridPane;
    }


    /**
     * Action handler of daily page
     */
    public void openDailyPage() {
        Map<String, String> transactionMap = new HashMap<>();
        transactionMap.put("iban", jwt.getIban() );
        String response = makePostRequest(transactionMap,"http://127.0.0.1:8080/Client/api/getDayTransactions");

        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, getUnixEpochDateTypeAdapter())
                .create();

        Transaction[] list= gson.fromJson(response, Transaction[].class);

        System.out.println(list);


    }


    /**
     * Action handler of New Pay request
     */
    public void newTransactionRequestBtn() {
        PopOver p = transactionPricePopOver();
        p.show(koppelbtn);
    }

    /**
     * Action handler of Koppelen btn
     */
    public void linkATMAction() {
        if(!checkTokenStats()){
            PopOver p = pinKoppelenPopOver();
            p.show(koppelbtn);
        }
    }

    /**
     * Action handler of Pay button
     */
    public void PayActionbtn() {
        Map<String, String> transactionMap = new HashMap<>();
        transactionMap.put("price",this.priceLabel.getText());
        transactionMap.put("p-iban",this.payerIban.getText() );
        transactionMap.put("r-iban", jwt.getIban() );
        transactionMap.put("description", "ATM PAY" );
        // TODO check pin ?
        System.out.println(transactionMap.toString());
        String response = makePostRequest(transactionMap,"http://127.0.0.1:8080/Client/api/make-transaction");

        if(response != null){
            resultLabel.setText("Gelukt");
        }
        System.out.println("result :" + response);
    }


    private PopOver transactionPricePopOver(){
        GridPane gridPane = getConfiguredGridPane();
        Label priceLabel = new Label("Price :");
        gridPane.add(priceLabel, 0, 0);

        TextField priceTextField = new TextField();
        gridPane.add(priceTextField, 0, 1);

        Button sendPrice = new Button("Send request");
        sendPrice.setDefaultButton(true);
        sendPrice.setMinWidth(150);
        gridPane.add(sendPrice, 0, 2);


        PopOver popOver = getConfiguredPopover("New Transaction");
        popOver.setHeaderAlwaysVisible(false);
        popOver.setContentNode(gridPane);

        sendPrice.setOnAction(actionEvent -> {
            BigDecimal price = new BigDecimal(priceTextField.getText());
            price = price.setScale(2, RoundingMode.HALF_UP);
            this.priceLabel.setText(price.toString());
            this.payerIban.setText("");
            this.payerCardCode.setText("");
            resultLabel.setText("");
            popOver.hide();
        });

        return popOver;
    }


    /**
     * Create PopOver for koppeling the Atm
     * @return
     */
    private PopOver pinKoppelenPopOver() {
        GridPane gridPane = getConfiguredGridPane();

        Label ibanLabel = new Label("IBAN :");
        gridPane.add(ibanLabel, 0, 0);

        TextField ibanTextField = new TextField();
        gridPane.add(ibanTextField, 1, 0);

        Label securityCodeLabel = new Label("Security Code :");
        gridPane.add(securityCodeLabel, 0, 1);

        TextField codeTextField = new TextField();
        gridPane.add(codeTextField, 1, 1);

        Button connectBtn = new Button("Koppel");
        connectBtn.setMinWidth(150);
        gridPane.add(connectBtn, 1, 3);

        PopOver popOver = getConfiguredPopover("Koppeling");
        popOver.setContentNode(gridPane);

        connectBtn.setOnAction(actionEvent -> {
            checkAtmMachine(ibanTextField.getText(), codeTextField.getText());
            popOver.hide();
        });
        return popOver;
    }


    /**
     * Make a post request with the given body to the given url and
     * will return the result body as string;
     */
    private String makePostRequest(Map<String, String> body, String url) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(body);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            StringEntity entity = new StringEntity(json);
            httppost.setEntity(entity);
            httppost.setHeader("Content-type", "application/json");
            httppost.setHeader("Content-type", "application/json");
            httppost.setHeader("Authorization", "Bearer " + jwt.getJwtToken());

            CloseableHttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Failed to linked ");
                return null;
            }
            InputStream inputStream = response.getEntity().getContent();
            String responseBody = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            httpclient.close();
            return responseBody;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * check the expiration date of the stored token.
     *
     * @return true if the token is still valid
     */
    private boolean checkTokenStats() {
        if (this.jwt.getExprireDate().after(new Date())) {
            koppelbtn.setStyle("-fx-background-color: green");
            koppelbtn.setTextFill(Color.WHITE);
            koppelbtn.setText("Gekoppeld");

            this.ibanLabel.setText(jwt.getIban());
            this.ownerLabel.setText(jwt.getOwner().getFullName());

            return true;
        } else {
            koppelbtn.setStyle("-fx-background-color: #ff7d7d");
            koppelbtn.setTextFill(Color.BLACK);
            koppelbtn.setText("koppelen");

            this.ibanLabel.setText("");
            this.ownerLabel.setText("");

            return false;
        }
    }

    /**
     * make a post request to active de atm request and get a token for authentication
     *
     * @param iban
     * @param securityCode
     */
    private void checkAtmMachine(String iban, String securityCode) {
        Map<String, String> request = new HashMap<>();
        request.put("iban", iban);
        request.put("securityCode", securityCode);
        String response = makePostRequest(request, "http://127.0.0.1:8080/api/authenticate-atm");
        final Gson gson1 = new GsonBuilder()
                .registerTypeAdapter(Date.class, getUnixEpochDateTypeAdapter())
                .create();
        JwtResponse jwtResponse = gson1.fromJson(response, JwtResponse.class);
        if (response != null) {
            this.jwt = jwtResponse;
            checkTokenStats();
        }
    }




}
