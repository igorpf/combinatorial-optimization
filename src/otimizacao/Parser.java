/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otimizacao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author igor
 */
public class Parser {
    public static List<Integer> readFile(String path){
        BufferedReader br = null;
        List<Integer> numbers = new ArrayList<>();
        String currentLine;
        
        try {
            br = new BufferedReader(new FileReader(path));
            currentLine=br.readLine();
            currentLine=br.readLine();
            int i=0;
            while(currentLine!=null){
                //System.out.println(i++);
                numbers.add(Integer.parseInt(currentLine));
                currentLine=br.readLine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return numbers;
    }
}
