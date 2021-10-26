package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * this defines the product class
 */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int inv;
    private int min;
    private int max;


    public Product(int id, String name, double price, int inv, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.min = min;
        this.max = max;
    }

    /**
     * @param id the id to set
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @param inv the stock/inv to set
     */
    public void setInv(int inv) {
        this.inv = inv;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the id
     */

    public int getId() {
        return id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }
    /**
     * @return the inventory
     */
    public int getInv() {
        return inv;
    }
    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }
    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param newPart - this adds newPart to associatedParts
     */
    public void addAssociatedPart(Part newPart) {
        associatedParts.add(newPart);
    }

    /**
     * @param selectedAssociatedPart - selected associated part to be deleted
     * @return the selected associated parts
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * @return all the associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }


}
