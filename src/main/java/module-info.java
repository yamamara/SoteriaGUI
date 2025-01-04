module com.soteria.soteriagui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens com.soteria to javafx.fxml;
    exports com.soteria;
}