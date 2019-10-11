import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;


public class Add {
    public void setUpdateListener(UpdateListner updateListner) {
        this.updateListner = updateListner;
    }

    UpdateListner updateListner;
    private TextField nameField;
    private TextField authorField;
    private TextField editionField;
    private TextField amountField;

    public TextField getNameField() {
        return nameField;
    }

    public TextField getAuthorField() {
        return authorField;
    }

    public TextField getEditionField() {
        return editionField;
    }

    public TextField getAmountField() {
        return amountField;
    }

    private int identify;


    Application application = new Application() {
        @Override
        public void start(Stage primaryStage) throws Exception {
            Label nameLabel = new Label("Name  ");
            nameField = new TextField();
            HBox name = new HBox();
            name.setPadding(new Insets(20, 20, 20, 20));
            name.setSpacing(20);
            name.getChildren().addAll(nameLabel, nameField);

            Label authorLabel = new Label("Author");
            authorField = new TextField();
            HBox author = new HBox();
            author.setPadding(new Insets(20, 20, 20, 20));
            author.setSpacing(20);
            author.getChildren().addAll(authorLabel, authorField);

            Label editionLabel = new Label("Edition");
            editionField = new TextField();
            HBox edition = new HBox();
            edition.setPadding(new Insets(20, 20, 20, 20));
            edition.setSpacing(20);
            edition.getChildren().addAll(editionLabel, editionField);

            Label message = new Label();

            Label amountLabel = new Label("Available");
            amountField = new TextField();
            HBox available = new HBox();
            available.setPadding(new Insets(20, 20, 20, 20));
            available.setSpacing(10);
            available.getChildren().addAll(amountLabel, amountField);

            Button addButton = new Button("");

            if (identify == 1) {
                addButton.setText("Add");
                addButton.setOnAction(e -> {
                    String nameinput = nameField.getText();
                    String authorinput = authorField.getText();
                    int editioninput = Integer.parseInt(editionField.getText());
                    int availableinput = Integer.parseInt(amountField.getText());
                    if (nameinput.isEmpty() || authorinput == null) {
                        message.setText("The name and Author fields shouldn't be left empty");
                    } else {
                        String query = "insert into books (Book,Author,Edition,Available) values(" + "'" + nameinput + "','" + authorinput + "','" + editioninput + "','" + availableinput + "')";
                        try {
                            DatabaseManager.getInstance().getStatement().executeUpdate(query);
                            nameField.clear();
                            authorField.clear();
                            amountField.clear();
                            editionField.clear();
                            updateListner.updateTable();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        message.setText("");
                    }

                });

            } else if (identify==2) {
                addButton.setText("Edit");
                addButton.setOnAction(e -> {
                    String nameinput = nameField.getText();
                    String authorinput = authorField.getText();
                    int editioninput = Integer.parseInt(editionField.getText());
                    int availableinput = Integer.parseInt(amountField.getText());
                    if (nameinput.isEmpty()|| authorinput == null) {
                        message.setText("The name and Author fields shouldn't be left empty");
                    } else {
                        String query = "insert into books (Book,Author,Edition,Available) values(" + "'" + nameinput + "','" + authorinput + "','" + editioninput + "','" + availableinput + "')";
                        try {
                            DatabaseManager.getInstance().getStatement().executeUpdate(query);
                            updateListner.updateTable();
                            primaryStage.close();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        message.setText("");
                    }
                });
            }

            Button cancelButton = new Button("Clear");
            cancelButton.setOnAction(e -> {
                nameField.clear();
                authorField.clear();
                amountField.clear();
                editionField.clear();
            });
            HBox button = new HBox();
            button.setPadding(new Insets(20, 20, 20, 120));
            button.setSpacing(20);
            button.getChildren().addAll(cancelButton, addButton);


            VBox vBox = new VBox();
            vBox.setPadding(new Insets(20, 20, 20, 20));
            vBox.getChildren().addAll(name, author, edition, available, button, message);


            //BorderPane borderPane = new BorderPane();
            //borderPane.setCenter(vBox);
            Scene scene = new Scene(vBox, 500, 500);
           /* Dialog<Book> d=new Dialog<>();
            DialogPane dp=new DialogPane();
            dp.setContent(vBox);
            dp.getButtonTypes().addAll(ButtonType.FINISH,ButtonType.CANCEL);
            d.setResultConverter(param -> new Book());
            Book b=d.showAndWait().get();
        d.setDialogPane(dp);
           */
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    };
    public Add(int i) {
        identify = i;



        {
            try {
                application.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
