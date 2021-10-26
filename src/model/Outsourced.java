package model;

/**
 This defines the Outsourced Parts
 */
public class Outsourced extends Part {
    private String companyName;

    //constructor
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * this sets the companyName attribute
     @param companyName - the name to be set to an object
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
