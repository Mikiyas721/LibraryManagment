import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class SignIn {

    ResultSet resultSet;

    Application sign = new Application() {
        @Override
        public void start(Stage primaryStage) throws Exception {
            Label userName = new Label("User Name");
            TextField userNameField = new TextField();
            userNameField.setPromptText("User Name");

            Label passWord = new Label("Password");
            TextField passWordField = new PasswordField();
            passWordField.setPromptText("PassWord");

            Button login = new Button("Sign Up");
            login.setOnAction(e -> {
                String username = userNameField.getText();
                String password = passWordField.getText();
                if (username != null && password != null) {
                    String query ="insert into accounts (Username,Password) values('"+username+"','"+password+"')";

                    try {
                        DatabaseManager.getInstance().getStatement().executeUpdate(query);
                        new Search();
                        primaryStage.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        System.out.println("Unable to subscribe");
                    }
                }
            });
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(0, 0, 0, 140));
            hBox.getChildren().add(login);


            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            GridPane.setConstraints(userName, 0, 0);
            GridPane.setConstraints(userNameField, 1, 0);
            GridPane.setConstraints(passWord, 0, 1);
            GridPane.setConstraints(passWordField, 1, 1);
            GridPane.setConstraints(hBox, 1, 2);


            gridPane.getChildren().addAll(userName, userNameField, passWord, passWordField, hBox);


            Scene scene = new Scene(gridPane, 300, 200);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        {
            try {
                start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
