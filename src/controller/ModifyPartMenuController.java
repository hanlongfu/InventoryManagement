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
 This controller controls the screen to modify parts.
 */
public class ModifyPartMenuController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private RadioButton partOutsourcedRBtn, partInHouseRBtn;
    @FXML private Label partMachineIdLbl;
    @FXML private TextField partIDField, partInvField, partMaxField, partMinField, partNameField, partPriceField, partUniqueIdField;

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

    @FXML void onActionModifyPartCancel(ActionEvent event) throws IOException {
        //cast as a button then as a stage to get the stage for the button
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        //load the new scene
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     This will save the modified parts when the save button is clicked.
     @param event - action event on the save button.
     */
    @FXML void onActionModifyPartSave(ActionEvent event) throws IOException {
        try{
            int partId = Integer.parseInt(partIDField.getText());
            String partName = partNameField.getText();
            int partInv = Integer.parseInt(partInvField.getText());
            Double partPrice = Double.parseDouble(partPriceField.getText());
            int partMax = Integer.parseInt(partMaxField.getText());
            int partMin = Integer.parseInt(partMinField.getText());

            if(partMax < partMin){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Max must be greater than min.");
                alert.setTitle("Invalid entries");
                alert.showAndWait();
            } else if(partInv < partMin || partInv > partMax){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inv must be between min and max.");
                alert.setTitle("Invalid entries");
                alert.showAndWait();
            } else {
                if(partInHouseRBtn.isSelected()){
                    int partMachineId = Integer.parseInt(partUniqueIdField.getText());
                    Inventory.updatePart(new InHouse(partId, partName, partPrice, partInv, partMin, partMax, partMachineId));
                }
                if(partOutsourcedRBtn.isSelected()){
                    String companyName = partUniqueIdField.getText();
                    Inventory.updatePart(new Outsourced(partId, partName, partPrice, partInv, partMin, partMax, companyName));
                }

                //switch to the main menu screen
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
     This method resets the attributes of a part object
     @param part - part whose attributes are to be reset.
     */

    public void setPart(Part part){
        partIDField.setText(String.valueOf(part.getId()));
        partInvField.setText(String.valueOf(part.getStock()));
        partMaxField.setText(String.valueOf(part.getMax()));
        partMinField.setText(String.valueOf(part.getMin()));
        partNameField.setText(part.getName());
        partPriceField.setText(Double.toString(part.getPrice()));
        if(part instanceof InHouse){
            partInHouseRBtn.setSelected(true);
            partUniqueIdField.setText(Integer.toString(((InHouse) part).getMachineId()));
        }
        if(part instanceof Outsourced){
            partOutsourcedRBtn.setSelected(true);
            partMachineIdLbl.setText("Company Name");
            partUniqueIdField.setText(((Outsourced) part).getCompanyName());
        }
    }

    /**
     To implement the Initializable interface, a default override function to initialize the class.
     @param url, rb - location and bundle of resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
