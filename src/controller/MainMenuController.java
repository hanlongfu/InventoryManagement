package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * this controller controls the main screen
 */

public class MainMenuController implements Initializable {

    Stage stage;
    Parent scene;
    @FXML private TextField partSearchField, productSearchField;

    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIdCol;
    @FXML private TableColumn<Part, Integer> partInvCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Double> partPriceCol;

    @FXML private TableView<Product> productTableView;
    @FXML private TableColumn<Product, Integer> productIdCol;
    @FXML private TableColumn<Product, Integer> productInvCol;
    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Double> productPriceCol;

    /**
      This switch to add part screen when the button to add parts is clicked.
     @param event - action event on the add button
     */
    @FXML void onActionAddPart(ActionEvent event) throws IOException {
        //cast as a button then as a stage to get the stage for the button
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        //load the new scene
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     This switch to add product screen when the button to add products is clicked.
     @param event - action event on the add button
     */
    @FXML void onActionAddProduct(ActionEvent event) throws IOException {
        //cast as a button then as a stage to get the stage for the button
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        //load the new scene
        scene = FXMLLoader.load(getClass().getResource("/view/AddProductMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     This searches and returns the parts that match the search query.
     @param event - action event in the parts search field
     */
    @FXML void onActionPartSearch(ActionEvent event) {
        //get the string from the part search textfield
        String partString = partSearchField.getText();
        //search by a query string
        ObservableList<Part> filteredParts = Inventory.lookupPart(partString);

        //no result from string(name) query, use id to query instead
        if (filteredParts.isEmpty()) {
            try {
                int id = Integer.parseInt(partString);
                Part filteredPart = Inventory.lookupPart(id);
                //this step is important to avoid inserting null
                if (filteredPart != null){
                    filteredParts.add(filteredPart);
                }
            } catch (NumberFormatException e) {
                //ignore
                // }
            }
        }
        //populate the parts table with filtered parts
        partTableView.setItems(filteredParts);
    }


    /**
     This searches and returns the list products that match the search query.
     @param event - action event in the products search field
     */
    @FXML void onActionProductSearch(ActionEvent event) {
        //get the string from the product search textfield
        String productString = productSearchField.getText();
        //search by a query string
        ObservableList<Product> filteredProducts = Inventory.lookupProduct(productString);

        //no result from string query, use integer id to query instead
        if(filteredProducts.isEmpty()){
            try{
                int id = Integer.parseInt(productString);
                Product filteredProduct = Inventory.lookupProduct(id);
                if(filteredProduct != null)
                    filteredProducts.add(filteredProduct);
            }catch(NumberFormatException e){
                //ignore
            }
        }
        //populate the products table with filtered products
        productTableView.setItems(filteredProducts);
    }


    /**
     This deletes a part when the button to delete a part is clicked.
     @param event - action event on the button to delete a part.
     */
    @FXML void onActionDeletePart(ActionEvent event) throws IOException{
        //get selected Part
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        //result.get() to get the button type
        //when "OK" button is clicked
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
        }
    }

    /**
     This deletes a product when the button to delete a product is clicked.
     @param event - action event on the button to delete a product.
     */
    @FXML void onActionDeleteProduct(ActionEvent event) throws IOException{
        //get selected product
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this product?");
        Optional<ButtonType> result = alert.showAndWait();
        //result.get() to get the button type
        //when "OK" button is clicked
        if(result.isPresent() && result.get() == ButtonType.OK) {
            if(!selectedProduct.getAllAssociatedParts().isEmpty()){
                Alert associatedAlert = new Alert(Alert.AlertType.WARNING);
                associatedAlert.setTitle("Deletion unsuccessful");
                associatedAlert.setContentText("The selected product has associated parts that need to be removed first.");
                associatedAlert.showAndWait();
            } else {
                Inventory.deleteProduct(selectedProduct);
            }
        }
    }


    /**
     This modifies a part when the button to modify a part is clicked.
     @param event - action event on the button to modify a part.
     */
    @FXML void onActionModifyPart(ActionEvent event) throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation((getClass().getResource("/view/ModifyPartMenu.fxml")));
            loader.load();

            ModifyPartMenuController MPMController = loader.getController();
            MPMController.setPart(partTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("You need to select an item");
            alert.showAndWait();
        }
    }

    /**
     This modifies a product when the button to modify a product is clicked.
     @param event - action event on the button to modify a product.
     */
    @FXML void onActionModifyProduct(ActionEvent event) throws IOException {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation((getClass().getResource("/view/ModifyProductMenu.fxml")));
            loader.load();

            ModifyProductMenuController MPMController = loader.getController();
            Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
            MPMController.setProduct(selectedProduct);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
    }

    /**
     This stops the application.  When exit button is clicked, the program stops executing.
     @param event - action event on the button to exit the application.
     */
    @FXML void onActionExit(ActionEvent event) throws IOException {
        //exit the program
        System.exit(0);
    }

    /**
     This stops the application.  When exit button is clicked, the program stops executing.
     @param url, rb - location and bundle of resources.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //obtain data for parts tableView using getAllParts
        //obtain data for individual tableColumns using setCellValueFactory(internally
        //it calls the getters of the object to set data
        partTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //obtain data for products tableView using getAllProducts
        productTableView.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("inv"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }


}
