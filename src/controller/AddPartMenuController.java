package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 This controller class controls the screen to add parts.
 */
public class AddPartMenuController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private RadioButton partInHouseRBtn, partOutsourcedRBtn;
    @FXML private TextField partInvField, partMaxField,partMinField, partNameField, partPriceField, partUniqueIdField;
    @FXML private Label partMachineIdLbl;

    /**
     This save a part when the button to save a part is clicked.
     @param event - action event on the button to save a part.
     */
    @FXML void onActionAddPartSave(ActionEvent event) throws IOException {

            try{
                //create auto-generated partID for new parts
                int addedPartId = 1;
                for(Part part : Inventory.getAllParts()){
                    if(part.getId() >= addedPartId){
                        addedPartId = part.getId() + 1;
                    }
                }
                //obtain user generated data
                String partName = partNameField.getText();
                int partInv = Integer.parseInt(partInvField.getText());
                Double partPrice = Double.parseDouble(partPriceField.getText());
                int partMax = Integer.parseInt(partMaxField.getText());
                int partMin = Integer.parseInt(partMinField.getText());
                Boolean partInHouseSelected = partInHouseRBtn.isSelected();
                Boolean partOutsourcedSelected = partOutsourcedRBtn.isSelected();

                if(partMax < partMin){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Max must be greater than min.");
                    alert.setTitle("Invalid entries");
                    alert.showAndWait();
                } else if(partInv < partMin || partInv > partMax){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Inv must be between min and max.");
                    alert.setTitle("Invalid entries");
                    alert.showAndWait();
                } else {
                    //save the added part data into the Inventory
                    if(partInHouseSelected){
                        int partMachineId = Integer.parseInt(partUniqueIdField.getText());
                        Inventory.addPart(new InHouse(addedPartId, partName, partPrice, partInv, partMin, partMax, partMachineId));
                    }
                    if(partOutsourcedSelected) {
                        String partCompanyName = partUniqueIdField.getText();
                        Inventory.addPart(new Outsourced(addedPartId, partName, partPrice, partInv, partMin, partMax, partCompanyName));
                    }

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
                alert.setContentText("Please enter a valid value for each text field. All entries must be numeric except Name and Company Name (for Outsourced parts).");
                alert.showAndWait();
            }
    }
    /**
     This changes the label to "Machine ID" when in-house radio button is clicked.
     @param event - action event on the radio button to reset a label.
     */
    @FXML void onActionInhouseRBtn(ActionEvent event) {
        partMachineIdLbl.setText("Machine ID");
    }
    /**
     This changes the label to "Company Name" when outsourced radio button is clicked.
     @param event - action event on the radio button to reset a label.
     */
    @FXML void onActionOutsourcedRBtn(ActionEvent event) {
        partMachineIdLbl.setText("Company Name");
    }


    /**
     This switches to the main screen when the cancel button is clicked.
     @param event - action event on the cancel button.
     */
    @FXML void onActionAddPartCancelBtn(ActionEvent event) throws IOException {
        //cast as a button then as a stage to get the stage for the button
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        //load the new scene we want to show after create button is clicked
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     To implement the Initializable interface, a default override function to initialize the class.
     @param url, rb - location and bundle of resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
