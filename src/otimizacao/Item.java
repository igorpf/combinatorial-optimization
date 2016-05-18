/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otimizacao;

/**
 *
 * @author igor
 */
public class Item {

    private int weight;

    private Bin bin;
    
    public Item(int weight) throws Exception{
        this.setWeight(weight);
    }
    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(final int weight) throws Exception {
        if (weight >= 0) {
            this.weight = weight;
        } else {
            throw new Exception("Weight must be positive!");
        }
    }

}
