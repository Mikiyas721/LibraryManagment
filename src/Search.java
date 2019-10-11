import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.sql.*;


public class Search implements UpdateListner {
    private Add add1;

    public void showAddDialog() {
        add1 = new Add(1);
        add1.setUpdateListener(this);
    }

    public void showDialog(String name, String author, String edition, String available) {
        add1 = new Add(2);
        add1.getNameField().setText(name);
        add1.getAuthorField().setText(author);
        add1.getEditionField().setText(edition);
        add1.getAmountField().setText(available);
        add1.setUpdateListener(this);
    }

    private TableView<Book> books;
    Application application = new Application() {
        @Override
        public void start(Stage primaryStage) throws Exception {

            TextField searchBar = new TextField();
            searchBar.setPromptText("Search");
            searchBar.setMinWidth(400);
            searchBar.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        System.out.println(newValue);
                        books.setItems(searchFromDatabase(newValue));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            Button add = new Button("Add");
            add.setOnAction(e -> showAddDialog());

            Button remove = new Button("Remove");
            remove.setOnAction(e -> onDeleteButtonClicked());

            Label message = new Label();
            //message.setMinWidth(200);

            Button edit = new Button("Edit");
            edit.setOnAction(e -> {
                ObservableList<Book> selected = books.getSelectionModel().getSelectedItems();
                if (selected.size() == 0) message.setText("Please select a book first");
                else if (selected.size() > 1) message.setText("You can only update one book at a time");
                else {
                    selected.stream().forEach(book -> {
                        showDialog(book.getName(),book.getAuthor(),book.getEdition().toString(),book.getAvailable().toString());
                    });
                    try {
                        DatabaseManager.getInstance().getStatement().executeUpdate("DELETE from books where Book='"+selected.get(0).getName()+"'");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            HBox manipulate = new HBox();
            manipulate.getChildren().addAll(add, edit, remove, message);
            manipulate.setSpacing(10);
            manipulate.setPadding(new Insets(10, 10, 10, 10));

            HBox search = new HBox();

            search.setSpacing(20);
            search.setPadding(new Insets(10, 10, 10, 10));
            search.getChildren().addAll(searchBar);


            TableColumn<Book, Integer> noColumn = new TableColumn<>("No");
            noColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));

            TableColumn<Book, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setMinWidth(200);
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
            authorColumn.setMinWidth(200);
            authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

            TableColumn<Book, Integer> editionColumn = new TableColumn<>("Edition");
            editionColumn.setMaxWidth(100);
            editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));

            TableColumn<Book, Integer> availableColumn = new TableColumn<>("Available");
            availableColumn.setMaxWidth(100);
            availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

            books = new TableView<>();

            books.setItems(loadDatabase());
            books.getColumns().addAll(noColumn, nameColumn, authorColumn, editionColumn, availableColumn);

            BorderPane borderPane = new BorderPane();
            borderPane.setTop(search);
            borderPane.setCenter(books);
            borderPane.setBottom(manipulate);

            Scene scene = new Scene(borderPane, 600, 600);
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
    ResultSet resultSet;

    public void onDeleteButtonClicked() {
        ObservableList<Book> selected = books.getSelectionModel().getSelectedItems();
        selected.stream().forEach(book -> {
            String delete = "delete from books where Book=" + "'" + book.getName() + "'";
            try {
                int rows = DatabaseManager.getInstance().getStatement().executeUpdate(delete);
                System.out.println("rows affected" + rows);
            } catch (SQLException e) {
                System.out.println("Couldn't delete column");
            }
        });
        try {
            books.setItems(loadDatabase());
        } catch (SQLException e) {
            System.out.println("Unable to read updated database");
        }
    }

    @Override
    public void updateTable() throws SQLException {
        books.setItems(loadDatabase());
    }

    public ObservableList<Book> loadDatabase() throws SQLException {
        ObservableList<Book> books = FXCollections.observableArrayList();
        resultSet = DatabaseManager.getInstance().getStatement().executeQuery("SELECT * from books");
        while (resultSet.next()) {
            books.add(new Book(resultSet.getString("Book"), resultSet.getString("Author"), Integer.parseInt(resultSet.getString("Edition")), Integer.parseInt(resultSet.getString("Available"))));
        }
        return books;
    }

    public ObservableList<Book> searchFromDatabase(String new_Value) throws SQLException {
        ObservableList<Book> books = FXCollections.observableArrayList();
        if (new_Value.isEmpty()) {
            resultSet = DatabaseManager.getInstance().getStatement().executeQuery("SELECT * from books");
        } else {

            resultSet = DatabaseManager.getInstance().getStatement().executeQuery("select Book, Author ,Edition, Available from books where Book like '%" + new_Value + "%' or Author like '%" + new_Value + "%'");
        }
        while (resultSet.next()) {
            books.add(new Book(resultSet.getString("Book"), resultSet.getString("Author"), Integer.parseInt(resultSet.getString("Edition")), Integer.parseInt(resultSet.getString("Available"))));
        }
        return books;
    }


}
