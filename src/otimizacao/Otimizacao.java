/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otimizacao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.random;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
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

    public static float RANDOMNESS = 1.0F;

    public static int GRASP_ITERATIONS = 10;
    public static int SEARCH_ITERATIONS = 1000;

    public static void main(String[] args) {
        for (String s : args) {
            System.out.println(s);
        }
//        if (args.length != 5 || args.length != 2) {
//            System.out.println("Usage: $ java -jar otimizacao.jar <outputFile> <inputFile> <alpha> <graspIterations> <localSearchIterations>");
//            System.out.println("or: $ java -jar otimizacao.jar <outputFile> <inputFile> ");
//            return;
//        }
        if (args.length == 5) {
            RANDOMNESS = Float.parseFloat(args[2]);
            GRASP_ITERATIONS = Integer.parseInt(args[3]);
            SEARCH_ITERATIONS = Integer.parseInt(args[4]);
        }

        List<Integer> n = Parser.readFile(args[1]);

        Bin.capacity = n.remove(0);

        Otimizacao o = new Otimizacao();
        long startTime = System.nanoTime();
        List<Bin> b = o.GRASP(n, GRASP_ITERATIONS);
        long elapsedTime = (System.nanoTime() - startTime) / 1000000;
        System.out.println("Total execution time: " + elapsedTime + " ms");
//        b.stream().forEach(bin -> {
//            System.out.println(b.toString());
//        });
        System.out.println("Best solution found size: " + b.size());
        File f = new File(args[0]);
        try {
            f.createNewFile();

            FileWriter fw = new FileWriter(f.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Bin bin : b) {
                bw.write(bin.toString());
                bw.newLine();
            }
            bw.newLine();
            bw.write("Best solution size: " + b.size());
            bw.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Generates a solution using the first fit algorithm with the given items
     *
     * @param items
     * @param alpha must be between [0,1]
     * @return list of bins containing the items given
     */
    public List<Bin> generateSolution(List<Item> items, float alpha) {
        List<Bin> solution = new ArrayList<>();
        for (Item i : items) {
            Map<Bin, Float> candidates = new HashMap<>();
            for (Bin b : solution) {
                int sum = b.getWeightUsed() + i.getWeight();
                float residue = (1 - sum / (float) Bin.capacity);
                if (sum <= Bin.capacity) {
                    candidates.put(b, residue);
                }
            }
            //To give more randomness
//            candidates.put(new Bin(),1.0F);
            Comparator<Entry<Bin, Float>> valueComparator
                    = (e1, e2) -> e1.getValue().compareTo(e2.getValue());
            candidates = candidates
                    .entrySet()
                    .stream()
                    .sorted(valueComparator)
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            try {
                int index = rand.nextInt((int) Math.ceil(candidates.size() * alpha));
                Bin chosen = (Bin) candidates.keySet().toArray()[index];
//                System.out.println("Candidates :"+candidates.size()+" chosen: "+(index+1));
//                for(Bin k:candidates.keySet()){
//                    System.out.println(candidates.get(k));
//                }
                chosen.addItem(i);
            } catch (Exception ex) {
            }

            if (i.getBin() == null) {
                Bin b = new Bin();
                try {
                    b.addItem(i);
                    solution.add(b);
                } catch (Exception ex) {
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
        for (int i = 0; i < iterations; ++i) {
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
                Bin newBin = currentSolution.get(toBinrandIndex);
                Bin.changeItemBin(it, newBin);
                if (previousBin.isEmpty()) {
                    newSolution.remove(previousBin);
                }
                if (newSolution.size() < currentSolution.size()
                        || residualSquareSum(newSolution)
                        .compareTo(residualSquareSum(currentSolution)) > 0) {
                    currentSolution = newSolution;
                }
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
//            s.stream().forEach(a -> {
//                System.out.println(a.toString());
//            });
            System.out.print("Iteration " + iterations + " generated solution size: " + s.size());
            s = localSearch(s, this.SEARCH_ITERATIONS);
            if (s.size() < bestSolutionSize) {
                bestSolution = s;
                bestSolutionSize = s.size();
            }
            System.out.print(" best solution found size: " + s.size() + "\n");
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
