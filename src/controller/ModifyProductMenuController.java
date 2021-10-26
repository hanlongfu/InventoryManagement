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
 * This controller controls the modify product screen
 */
public class ModifyProductMenuController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML private TextField pPartSearchField, productIdField, productInvField, productMaxField, productMinField, productNameField, productPriceField;

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
     This searches and returns the parts that match the search query within the screen to modify product.
     @param event - action event in part search field.
     */
    @FXML void onActionPPartSearch(ActionEvent event) throws IOException{
        String searchStr = pPartSearchField.getText();
        ObservableList<Part> searchedParts = Inventory.lookupPart(searchStr);

        if (searchedParts.isEmpty()) {
            try {
                int id = Integer.parseInt(searchStr);
                Part returnedPart = Inventory.lookupPart(id);
                if (returnedPart != null){
                    searchedParts.add(returnedPart);
                }
            } catch (NumberFormatException e) {
            }
        }

        partTableView.setItems(searchedParts);
    }

    /**
     This adds a part to the associated parts table below
     @param event - action event on the add button
     */
    @FXML void onActionModifyProductAdd(ActionEvent event) {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        tmpAssociatedParts.add(selectedPart);
    }

    /**
     This switches to the main screen when the cancel button is clicked.
     @param event - action event on the cancel button.
     */
    @FXML void onActionModifyProductCancel(ActionEvent event) throws IOException {
        //cast as a button then as a stage to get the stage for the button
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        //load the new scene
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     This removes an associated part from the associated parts table.
     @param event - action event on the button to remove an associated part.
     */
    @FXML void onActionModifyProductRemovePart(ActionEvent event) {
        Part selectedPart = associatedPartTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            tmpAssociatedParts.remove(selectedPart);
        }
    }

    /**
     This saves the modified product and its associated parts to inventory.
     @param event - action event on the save button.

     RUNTIME ERROR - Whenever I added or deleted the associated parts and clicked "save", the associated parts data were not saved the corresponding product. After re-watching the webinars, I realized that I needed to create separate an ObservableList for associated parts, add the associated parts to a product first, and then add the product to the inventory. I was able to fix it that way.

     FUTURE ENHANCEMENT - It would be nice to separate all the data from the logic and save the data in a separate database so that data would not need to passed around. This would increase the data security and reduce the need for methods to pass data between controllers.

     */
    @FXML void onActionModifyProductSave(ActionEvent event) throws IOException {

        try{
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

                //save the updated data into the inventory
                Product newProduct = new Product(Integer.parseInt(productIdField.getText()), productName, productPrice,productInv, productMin, productMax);

                //save the updated list of associated parts to the product
                newProduct.getAllAssociatedParts().addAll(tmpAssociatedParts);

                //save the updated product to inventory
                Inventory.updateProduct(newProduct);

                // switch the screen
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                //load the new scene
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Entries");
            alert.setContentText("Please enter a valid value for each text field. All entries must be numeric except Name.");
            alert.showAndWait();
        }

    }

    /**
     This method resets the attributes of a product object.  It also adds the associated parts to
     the ObservableList and set the associated parts tableview with this observableList

     @param product - product object with its attributes to be reset

     RUNTIME ERROR - Related to the runtime error from above, I wasn't able to save the associated parts to a table. After watching the webinar, I realized that I had to add the associated parts to an observable list created in this controller, and then added the observableList to a product.

     */

    public void setProduct(Product product){
        //set the product fields
        productIdField.setText(String.valueOf(product.getId()));
        productNameField.setText(product.getName());
        productInvField.setText(String.valueOf(product.getInv()));
        productPriceField.setText(Double.toString(product.getPrice()));
        productMaxField.setText(String.valueOf(product.getMax()));
        productMinField.setText(String.valueOf(product.getMin()));

        //set the associated parts to the associatedPartTableView
        tmpAssociatedParts.addAll(product.getAllAssociatedParts());
        associatedPartTableView.setItems(tmpAssociatedParts);

    }
    /**
     This initialize the modify product screen with data already present in the parts and associated parts observableList
     @param url, rb - location and bundle of resources.
     */
    @Override public void initialize(URL url, ResourceBundle rb) {
        partTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //initialize is called with every button action
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}