package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
  This defines the Inventory class
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * This method adds a part to the parts list
     @param newPart - new part to be add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * This method adds a product to the product list
     @param newProduct - new product to be add
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }


    /**
     * This method search and returns a part by ID
     @param partId - the ID of the part to be returned
     @return part, null - returns either a part or null
     */
    public static Part lookupPart(int partId) {
        if (!allParts.isEmpty()) {
            for (Part part : Inventory.getAllParts()) {
                if (part.getId() == partId)
                    return part;
            }
        }
        return null;
    }

    /**
     * This method search and returns a product by ID
     @param productId - the ID of the product to be returned
     @return product, null - returns either a product or null
     */
    public static Product lookupProduct(int productId){
        if(!allProducts.isEmpty()){
            for(Product product : Inventory.getAllProducts()){
                if(product.getId() == productId)
                    return product;
            }
        }
        return null;
    }

    /**
     * This method search and returns a list of parts by name
     @param partName - the name of the part to be returned
     @return filteredParts, null - returns either a list of filtered parts or null
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();
        if(!allParts.isEmpty()){
            for(Part part : Inventory.getAllParts()){
                if(part.getName().contains(partName)){
                    filteredParts.add(part);
                }
            }
            return filteredParts;
        }
        return null;
    }

    /**
     * This method search and returns a list of products by name
     @param productName - the name of the product to be returned
     @return filteredProducts, null - returns either a list of filtered products or null
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        if(!allProducts.isEmpty()){
            for(Product product : Inventory.getAllProducts()) {
                if(product.getName().contains(productName)){
                    filteredProducts.add(product);
                }
            }
            return filteredProducts;
        }
        return null;
    }

    /**
     * This method updates a selected part
     @param selectedPart - the part to be updated
     */
     public static void updatePart(Part selectedPart){
         for (int i = 0; i < allParts.size(); i++) {
             if (allParts.get(i).getId() == selectedPart.getId()) {
                 allParts.set(i, selectedPart);
             }
         }
    }

    /**
     * This method replaces a selected product
     @param selectedProduct  - the product to be updated
     */
    public static void updateProduct(Product selectedProduct){
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == selectedProduct.getId()) {
                allProducts.set(i, selectedProduct);
            }
        }
    }


    /**
     * This method delete a part
     @param selectedPart - the part to be removed
     @return true - successful removal if true
     */

    public static boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        return true;
    }

    /**
     * This method delete a product
     @param selectedProduct - the product to be removed
     @return true - successful removal if true
     */
    public static boolean deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
        return true;
    }


    /**
     * This method returns the list of all parts
     @return allParts - the list of all parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * This method returns the list of all products
     @return allParts - the list of all products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
