module ToughBankATM {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires com.google.gson;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.commons.io;
    requires lombok;


    opens view to javafx.graphics, javafx.fxml;
    opens launcher to javafx.graphics, javafx.fxml;
    opens controller to javafx.fxml;
}