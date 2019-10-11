import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class LogIn extends Application {
    ResultSet resultSet;
    Label messageLabel = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception {
        messageLabel.getStylesheets().add("clickLabel.css");
        Label userName = new Label("User Name");
        TextField userNameField = new TextField();
        userNameField.setPromptText("User Name");

        Label passWord = new Label("Password");
        TextField passWordField = new PasswordField();
        passWordField.setPromptText("PassWord");

        Button login = new Button("Log In");

        login.setOnAction(e -> {
            boolean check = true;
            String usernameinput = userNameField.getText();
            String passwordinput = passWordField.getText();

            if (usernameinput != null && passwordinput != null) {
                String query = "select * from accounts";

                try {
                    resultSet = DatabaseManager.getInstance().getStatement().executeQuery(query);
                    while (resultSet.next()) {
                        String username = resultSet.getString("Username");
                        String password = resultSet.getString("Password");
                        if (usernameinput.equals(username)) {
                            check = false;
                            if (passwordinput.equals(password)) {
                                new Search();
                                primaryStage.close();
                                break;
                            } else messageLabel.setText("Invalid PassWord");
                        }
                    }
                    if (check) messageLabel.setText("No Such User");
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
        gridPane.setPadding(new Insets(30, 10, 10, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label clickLabel = new Label("Please Sign Up first if you don't have an account");
        clickLabel.getStylesheets().add("clickLabel.css");
        clickLabel.setOnMouseClicked(event -> {
            new SignIn();
            primaryStage.close();
        });


        GridPane.setConstraints(userName, 0, 0);
        GridPane.setConstraints(userNameField, 1, 0);
        GridPane.setConstraints(passWord, 0, 1);
        GridPane.setConstraints(passWordField, 1, 1);
        GridPane.setConstraints(hBox, 1, 2);
        GridPane.setConstraints(clickLabel, 0, 3, 2, 1);
        GridPane.setConstraints(messageLabel, 1, 4, 2, 1);


        gridPane.getChildren().addAll(userName, userNameField, passWord, passWordField, hBox, clickLabel, messageLabel);


        Scene scene = new Scene(gridPane, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
