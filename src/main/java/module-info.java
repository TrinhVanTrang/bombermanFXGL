open module com.example.bombermanfxgl {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.almasb.fxgl.entity;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    //opens com.example.bombermanfxgl to javafx.fxml;
    exports com.example.bombermanfxgl;
}