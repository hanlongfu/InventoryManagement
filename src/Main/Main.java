package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * @author Hanlong Fu
 */

/**
  The entry point of the whole application.
 */
public class Main extends Application {

    /**
     This gets the scene by reading in the startup screen fxml.
     And then set the scene to the right stage
     @param primaryStage - this is the stage that contains the scene
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1190, 550));
        primaryStage.show();
    }

    /**
     This launches the application with initialized data if any.
     @param args - this is the default param for the main method
     */
    public static void main(String[] args) {

        //prepopulated data for products
        Product product1 = new Product(1, "Giant Bicyles", 299.99, 15, 0, 20);
        Product product2 = new Product(2, "Scott Bicyles", 199.99, 15, 0, 19);
        Product product3 = new Product(3, "GT Bike", 99.99, 15, 0, 18);
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);

        //prepopulated data for parts
        Part part1 = new InHouse(1, "Brakes", 12.99, 15, 5, 20, 1);
        Part part2 = new InHouse(2, "Tire", 14.99, 15, 10, 30, 10);
        Part part3 = new InHouse(3, "Rim", 56.99, 15, 12, 33, 12);
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);


        launch(args);
    }
}
