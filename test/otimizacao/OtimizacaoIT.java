/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otimizacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author igor
 */
public class OtimizacaoIT {
    
    List<Bin> solution = new ArrayList<>();
    
    List<Item> items = new ArrayList<>();
    
    List<Integer> i = Arrays.asList(100,100,100,30,30,30,10,20,10);
            
    Otimizacao instance;
    @Before
    public void setUp() throws Exception {
        Item i1=new Item(100);
        Item i2=new Item(100);
        Item i3=new Item(100);
        Item i4=new Item(30);
        Item i5=new Item(30);
        Item i6=new Item(30);
        Item i7=new Item(10);
        Item i8=new Item(20);
        Item i9=new Item(10);
        items.add(i1);
        items.add(i2);
        items.add(i3);
        items.add(i4);
        items.add(i5);
        items.add(i6);
        items.add(i7);
        items.add(i8);
        items.add(i9);
        instance = new Otimizacao();
    }
  
    @Test
    public void testGenerateSolution() {
        float randomness = 1.0F;
        solution=instance.generateSolution(items, randomness);
        assertTrue(solution.size() >=4);
        solution.stream().forEach(b->{
            System.out.println(b.toString());
        });
    }
    @Test
    public void testLocalSearch(){
        float randomness = 0.5F;
        solution=instance.generateSolution(items, randomness);
        assertTrue(solution.size() >=4);
        solution.stream().forEach(b->{
            System.out.println(b.toString());
        });
        List<Bin> newSol = instance.localSearch(solution,20);
        assertTrue(newSol.size()<=solution.size());
        System.out.println("------------\n");
        newSol.stream().forEach(b->{
            System.out.println(b.toString());
        });
    }
    @Test
    public void testGRASP(){
        List<Bin> bins = instance.GRASP(i, 5);
        bins.stream().forEach(b->{
            System.out.println(b.toString());
        });
    }
}
