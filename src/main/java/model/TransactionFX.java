package model;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionFX {
    private Transaction transaction;
    private IntegerProperty id;
    private StringProperty recipient;
    private StringProperty payer;
    private ObjectProperty amount;
    private StringProperty timestamp;
    private StringProperty description;

    public TransactionFX(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public int getId() {
        return new SimpleIntegerProperty(transaction.getId()).get();
    }

    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(transaction.getId());
    }

    public void setId(int id) {
        this.transaction.setId(id);
    }

    public String getRecipient() {
        return new SimpleStringProperty(transaction.getRecipient()).get();
    }

    public StringProperty recipientProperty() {
        return new SimpleStringProperty(transaction.getRecipient());
    }

    public void setRecipient(String recipient) {
        this.transaction.setRecipient(recipient);
    }

    public String getPayer() {
        return new SimpleStringProperty(transaction.getPayer()).get();
    }

    public StringProperty payerProperty() {
        return new SimpleStringProperty(transaction.getPayer());
    }

    public void setPayer(String payer) {
        this.transaction.setPayer(payer);
    }

    public Object getAmount() {
        return new SimpleObjectProperty<>(transaction.getAmount()).get();
    }

    public ObjectProperty amountProperty() {
        return new SimpleObjectProperty<>(transaction.getAmount());
    }

    public void setAmount(Object amount) {
        transaction.setAmount((BigDecimal) amount);
    }

    public String getTimestamp() {
        return new SimpleStringProperty(transaction.getTimestamp().toString()).get();
    }

    public StringProperty timestampProperty() {
        return new SimpleStringProperty(transaction.getTimestamp().toString());
    }

    public void setTimestamp(String timestamp) {
        //todo
        this.timestamp.set(timestamp);
    }

    public String getDescription() {
        return new SimpleStringProperty(transaction.getDescription()).get();
    }

    public StringProperty descriptionProperty() {
        return new SimpleStringProperty(transaction.getDescription());
    }

    public void setDescription(String description) {
        this.transaction.setDescription(description);
    }
}
