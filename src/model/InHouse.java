package model;

/**
 This defines the InHouse Parts
 */
public class InHouse extends Part {
    private int machineId;

    //constructor
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     @return the machineId
    */
    public int getMachineId() {
        return machineId;
    }

    /**
     @param machineId  - sets the machineId attribute
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
