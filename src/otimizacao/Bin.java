/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otimizacao;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author igor
 */
public class Bin {
    public static int capacity=100;
    
    private List<Item> items;
    
    private int weightUsed;
    
    public Bin(){
        this.items=new ArrayList<>();
        this.weightUsed=0;
    }
    public void removeItem(Item i) throws Exception{
        if(!items.remove(i)){
            throw new Exception("Item not found");
        }
        i.setBin(null);
        this.weightUsed-=i.getWeight();
    }
    public void removeItem(int index) throws Exception{
        Item removed=items.remove(index) ;        
        if(removed==null){
            throw new Exception("Item not found");
        }
        removed.setBin(null);
        this.weightUsed-=removed.getWeight();
    }
    public void addItem(Item i) throws Exception{
        if((i.getWeight()+this.weightUsed)<=capacity){
            items.add(i);
            i.setBin(this);
            weightUsed+=i.getWeight();
        }
        else
            throw new Exception("Bin already full!");
    }
    
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    public boolean contains(Item i){
        return this.items.contains(i);
    }
    public boolean isEmpty(){
        return this.items.isEmpty();
    }
    public int getWeightUsed(){
        return this.weightUsed;
    }
    public static void changeItemBin(Item i, Bin destination) throws Exception{
        if(destination.getWeightUsed()+i.getWeight()<=capacity){
            i.getBin().removeItem(i);
            destination.addItem(i);
        }
        else
            throw new Exception("Could not change bin!");
        
    }
    @Override
    public String toString(){
        StringBuilder b=new StringBuilder();
        items.stream().forEach(i->{
            b.append(i.getWeight());
            b.append(" ");
        });
        return b.toString();
    }
}
