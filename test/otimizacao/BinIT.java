/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otimizacao;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author igor
 */
public class BinIT {
    
    Bin b=new Bin();
    
    Item i;
    
    @Before 
    public void setUp() throws Exception{
        i = new Item(0);
    }
    
    @Test
    public void testAddItem() throws Exception {
        Bin.capacity=100;
        i.setWeight(100);
        b.addItem(i);
        assertEquals(b.getItems().size(), 1);
        assertEquals(i.getBin(), b);
    }
    @Test(expected=Exception.class)
    public void testAddItemFail() throws Exception {
        Bin.capacity=100;
        Item i2 = new Item(101);
        b.addItem(i2);
        
    }
    @Test
    public void testRemoveItem() throws Exception {
        Bin.capacity=100;
        i.setWeight(100);
        b.addItem(i);
        assertEquals(b.getItems().size(), 1);
        b.removeItem(i);
        assertEquals(b.getItems().size(), 0);
        b.addItem(i);
        assertEquals(b.getItems().size(), 1);
        b.removeItem(0);
        assertEquals(b.getItems().size(), 0);
    }
    @Test(expected=Exception.class)
    public void testRemoveItemFail() throws Exception {
        Bin.capacity=100;
        i.setWeight(100);
        b.addItem(i);
        assertEquals(b.getItems().size(), 1);
        assertEquals(i.getBin(), b);
        b.removeItem(new Item(2));
    }
    @Test
    public void changeItemBin() throws Exception{
        Bin otherBin = new Bin();
        i.setWeight(4);
        b.addItem(i);
        Bin.changeItemBin(i, otherBin);
        assertEquals(b.getItems().size(),0);
        assertEquals(otherBin.getItems().size(),1);
        assertEquals(i.getBin(),otherBin);
    }
}
