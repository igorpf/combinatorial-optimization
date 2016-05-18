/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otimizacao;

import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author igor
 */
public class Otimizacao {

    public List<Bin> bins=new ArrayList<>();
    
    public Random rand = new Random();
    
    public float RANDOMNESS = 0.5F;
    
    public static void main(String[] args) {
        List<Integer> n=Parser.readFile(args[0]);
        Bin.capacity=n.remove(0);
        
        Otimizacao o = new Otimizacao();
        List<Bin> b = o.GRASP(n, 5);
        b.stream().forEach(bin->{
            System.out.println(b.toString());
        });
        System.out.println("Solution size: "+b.size());
    }
    /** Generates a solution using the first fit algorithm
     *  with the given items
     * 
     * @param items
     * @param randomness must be between [0,1]
     * @return list of bins containing the items given
     */
    public List<Bin> generateSolution(List<Item> items, float randomness){
        List<Bin> solution=new ArrayList<>();
        
        for(Item i:items){
            for(Bin b: solution){
                try {//nextFloat() is always between [0.0,1.0]
                    if(rand.nextFloat()<randomness)//skip to next bin
                        continue;
                    b.addItem(i);
                    break;
                } catch (Exception ex) {
                    
                }
            }
            if(i.getBin()==null){
                Bin b = new Bin();
                try {
                    b.addItem(i);
                    solution.add(b);
                } catch (Exception ex) {
                    Logger.getLogger(Otimizacao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return solution;
    }
    public List<Bin> localSearch(List<Bin> currentSolution){
        if(currentSolution.isEmpty())
            return currentSolution;
        Bin lastBin = currentSolution.remove(currentSolution.size()-1);
        List<Item> lastBinItems=new ArrayList<>(lastBin.getItems());
        Item lastItem=null;
        while(!lastBinItems.isEmpty()){
            Item i = lastBinItems.get(0);
            if(i.equals(lastItem))//avoiding loops
                break;
            for(Bin b:currentSolution){
                try {
                    Bin.changeItemBin(i, b);
                } catch (Exception ex) {
                }
                if(b.equals(i.getBin())){
                    lastBinItems.remove(i);
                    break;
                }
                lastItem=i;
            }
        }
        if(lastBinItems.isEmpty()) //succeeded removing a bin
            return currentSolution;
        else{
            currentSolution.add(lastBin);
            return currentSolution;
        } 
            
    }
    private List<Item> createNewItemInstances(List<Integer> values){
        List<Item> i= new ArrayList<>();
        values.stream().forEach(v->{
            try {
                i.add(new Item(v));
            } catch (Exception ex) {
            }
        });
        return i;
    }
    public List<Bin> GRASP(List<Integer> values, int iterations){
        List<Bin> bestSolution = new ArrayList<>();
        int bestSolutionSize = Integer.MAX_VALUE;
        
        while(iterations-- > 0){
            List<Item> i = createNewItemInstances(values);
            List<Bin> s = generateSolution(i, this.RANDOMNESS);
            s = localSearch(s);
            if(s.size()<bestSolutionSize){
                bestSolution=s;
                bestSolutionSize=s.size();
            }
        }
        return bestSolution;
    }
}
