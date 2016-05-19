/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otimizacao;

import static java.lang.Math.random;
import java.math.BigDecimal;
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

    public List<Bin> bins = new ArrayList<>();

    public Random rand = new Random();

    public float RANDOMNESS = 0.89F;
    
    public static int GRASP_ITERATIONS=10;
    public int SEARCH_ITERATIONS=1000;

    public static void main(String[] args) {
        List<Integer> n = Parser.readFile(args[0]);
        Bin.capacity = n.remove(0);

        Otimizacao o = new Otimizacao();
        List<Bin> b = o.GRASP(n, GRASP_ITERATIONS);
        b.stream().forEach(bin -> {
            System.out.println(b.toString());
        });
        System.out.println("Solution size: " + b.size());
    }

    /**
     * Generates a solution using the first fit algorithm with the given items
     *
     * @param items
     * @param randomness must be between [0,1]
     * @return list of bins containing the items given
     */
    public List<Bin> generateSolution(List<Item> items, float randomness) {
        List<Bin> solution = new ArrayList<>();

        for (Item i : items) {
            for (Bin b : solution) {
                try {//nextFloat() is always between [0.0,1.0]
                    if (rand.nextFloat() < randomness)//skip to next bin
                    {
                        continue;
                    }
                    b.addItem(i);
                    break;
                } catch (Exception ex) {

                }
            }
            if (i.getBin() == null) {
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

    public List<Bin> localSearch(List<Bin> currentSolution, int iterations) {
        if (currentSolution.isEmpty()) {
            return currentSolution;
        }
        List<Bin> newSolution = new ArrayList<>(currentSolution);
        for (int i = 0;i<iterations;++i) {
            int fromBinrandIndex = this.rand.nextInt(newSolution.size());
            int toBinrandIndex = this.rand.nextInt(newSolution.size());
            int itemrandIndex = this.rand.
                    nextInt(newSolution
                            .get(fromBinrandIndex)
                            .getItems()
                            .size());
            try {
                Item it = newSolution
                                 .get(fromBinrandIndex)
                                 .getItems()
                                 .get(itemrandIndex);
                Bin previousBin = it.getBin();
                Bin newBin=currentSolution.get(toBinrandIndex);
                Bin.changeItemBin(it, newBin);
                if(previousBin.isEmpty())
                    newSolution.remove(previousBin);
                if(newSolution.size()<currentSolution.size()||
                   residualSquareSum(newSolution)
                           .compareTo(residualSquareSum(currentSolution))>0)
                    currentSolution=newSolution;
            } catch (Exception ex) {
            }
        }
        return currentSolution;
    }

    private List<Item> createNewItemInstances(List<Integer> values) {
        List<Item> i = new ArrayList<>();
        values.stream().forEach(v -> {
            try {
                i.add(new Item(v));
            } catch (Exception ex) {
            }
        });
        return i;
    }

    public List<Bin> GRASP(List<Integer> values, int iterations) {
        List<Bin> bestSolution = new ArrayList<>();
        int bestSolutionSize = Integer.MAX_VALUE;

        while (iterations-- > 0) {
            List<Item> i = createNewItemInstances(values);
            List<Bin> s = generateSolution(i, this.RANDOMNESS);
            System.out.print("Iteration "+iterations+" generated solution size: "+s.size());
            s = localSearch(s,this.SEARCH_ITERATIONS);
            if (s.size() < bestSolutionSize) {
                bestSolution = s;
                bestSolutionSize = s.size();
            }
            System.out.print(" best solution found size: "+s.size()+"\n");
        }
        return bestSolution;
    }

    private BigDecimal residualSquareSum(List<Bin> bins) {
        BigDecimal sum = new BigDecimal(0);
        bins.stream().forEach(b -> {
            BigDecimal cap = new BigDecimal(Bin.capacity);
            BigDecimal used = new BigDecimal(b.getWeightUsed());
            BigDecimal residue = BigDecimal.ONE.subtract(used.divide(cap));
            sum.add(residue.pow(2));
        });
        return sum;
    }
}
