package controller;

import javafx.collections.FXCollections;
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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This controller controls the add product menu
 */

public class AddProductMenuController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private TextField partSearchField, productInvField, productMaxField, productMinField, productNameField, productPriceField;

    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIdCol, partInvCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Double> partPriceCol;

    @FXML private TableView<Part> associatedPartTableView;
    @FXML private TableColumn<Part, Integer> associatedPartIdCol, associatedPartInvCol;
    @FXML private TableColumn<Part, String> associatedPartNameCol;
    @FXML private TableColumn<Part, Double> associatedPartPriceCol;

    private ObservableList<Part> tmpAssociatedParts = FXCollections.observableArrayList();

    /**
     This searches and returns the parts that match the search query within the screen to add product.
     @param event - action event in part search field.
     */
    @FXML void onActionAddProductSearch(ActionEvent event) throws IOException{
        String searchString = partSearchField.getText();
        ObservableList<Part> returnedParts = Inventory.lookupPart(searchString);

        if (returnedParts.size() == 0) {
            try {
                int id = Integer.parseInt(searchString);
                Part returnedPart = Inventory.lookupPart(id);
                //this step is important to avoid inserting null
                if (returnedPart != null){
                    returnedParts.add(returnedPart);
                }
            } catch (NumberFormatException e) {
            }
        }
        //populate the parts table with filtered parts
        partTableView.setItems(returnedParts);
    }

    /**
     This add an selected part to the associated parts table below.
     @param event - action event on the add button.
     */
    @FXML void onActionAddProductAdd(ActionEvent event) throws IOException {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        tmpAssociatedParts.add(selectedPart);
    }

    /**
     This removes a part from the associated parts table.
     @param event - action event on button to remove associated part
     */
    @FXML void onActionAddProductRemovePart(ActionEvent event) {
        Part selectedPart = associatedPartTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            tmpAssociatedParts.remove(selectedPart);
        }
    }

    /**
     This switches the screen to the main screen when the cancel button is clicked.
     @param event - action event on the cancel button.
     */
    @FXML void onActionAddProductCancel(ActionEvent event) throws IOException {
        //cast as a button then as a stage to get the stage for the button
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        //load the new scene
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     This saves the product and associated parts data to the inventory.
     @param event - action event on the save button.
     */
    @FXML void onActionAddProductSave(ActionEvent event) throws IOException {

        try{
            //create auto-generated product ID for new products
            int addedProductId = 1;
            for(Product product : Inventory.getAllProducts()){
                if(product.getId() >= addedProductId){
                    addedProductId = product.getId() + 1;
                }
            }

            //obtain user generated data
            String productName = productNameField.getText();
            int productInv = Integer.parseInt(productInvField.getText());
            Double productPrice = Double.parseDouble(productPriceField.getText());
            int productMax = Integer.parseInt(productMaxField.getText());
            int productMin = Integer.parseInt(productMinField.getText());

            if(productMax < productMin){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Max must be greater than min.");
                alert.setTitle("Invalid entries");
                alert.showAndWait();
            } else if(productInv < productMin || productInv > productMax) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inv must be between min and max.");
                alert.setTitle("Invalid entries");
                alert.showAndWait();
            } else {
                //save the added part data into the Inventory
                //instantiate a new product
                Product newProduct = new Product(addedProductId, productName, productPrice,productInv, productMin, productMax);

                //add the list of associated parts to the product
                newProduct.getAllAssociatedParts().addAll(tmpAssociatedParts);

                //add product to inventory
                Inventory.addProduct(newProduct);

                // switch the screen
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                //load the new scene
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Entries");
            alert.setContentText("Please enter a valid value for each text field. All entries must be numeric except Name.");
            alert.showAndWait();
        }

    }

    /**
     This sets the tableview and tablecolumn fields with relevant data from inventory holders.
     */
    public void setTableItems(){
        //obtain data for parts tableView using getAllParts
        //obtain data for individual tableColumns using setCellValueFactory(internally
        //it calls the getters of the object to set data
        partTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //initialize is called with every button action
        associatedPartTableView.setItems(tmpAssociatedParts);
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     To implement the Initializable interface, a default override function to initialize the class.
     @param url, rb - location and bundle of resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTableItems();
    }


}
