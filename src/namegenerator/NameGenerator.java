/*
 * Michael McClellan
 * February 2017
 */
package namegenerator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * The agent responsible for creating a Markov model of a list of names, and
 * for generating new names based on its model. The model is a hash map, where
 * each key is a string that precedes other characters (the length of the key
 * is the same as the order of the Markov model), and each value is an 
 * ArrayList of the characters that follow that key, as trained by the names
 * fed into the model.
 * 
 * @author Michael
 */
public class NameGenerator {
    
    private final String BOYNAMES = "namesBoys.txt";
    private final String GIRLNAMES = "namesGirls.txt";
    private final HashMap<String,ArrayList<String>> model;
    private String buffer;
    
    /**
     * Public constructor. Instantiates the model for the name generator.
     */
    public NameGenerator() {
        model = new HashMap<>();
    }
    
    /**
     * Creates a Markov model using a hash map to store strings that precede
     * other characters as key, and those characters as the values to that
     * key (in an ArrayList). No probabilities are stored; it is expected that
     * when generating new names, the value from the key is selected at random,
     * thus ensuring that more common values to the key have a greater chance
     * at being selected; in other words, the probability takes care of itself.
     * 
     * @param gender Which list of names should be used to train the model
     * @param order Order of Markov model; the length of the preceding string
     * used as the hash map's key. E.g. A second order model will consider store
     * two-character strings as its keys.
     */
    public void createModel(int gender, int order) {
        
        // Reset the model
        model.clear();
        
        String filename;
        if (gender == 1) filename = BOYNAMES;
        else if (gender == 2) filename = GIRLNAMES;
        else filename = BOYNAMES;
        
        // The length of the buffer needs to match the order
        buffer = "";
        for (int i = 0; i < order; i++) buffer += "_";
        
        // Read through the inputs and create the model
        try (Scanner s = new Scanner(
                new BufferedReader(new FileReader(filename)))) {
            while (s.hasNext()) {
                // Buffer prededes name; exclamation point is end marker
                String name = buffer + s.next() + "!";
                
                // Uncomment below to produce stranger-beginning names
                // name = name.toLowerCase();
                
                // Main model build happens here
                // Iterate across each name to consider substrings
                for (int i = order; i < name.length(); i++) {
                    // current state is a string of length order
                    String currentState = name.substring(i - order, i);
                    ArrayList<String> set = 
                            model.get(currentState);
                    if (set != null) // add following character to value
                        model.get(currentState).add(name.substring(i, i+1));
                    else { // key not yet added
                        model.put(currentState, new ArrayList<>());
                        model.get(currentState).add(name.substring(i, i+1));
                    }
                }
            }
            //this.printModel(); // for debugging
        } catch (FileNotFoundException fe) {
            System.out.println("File not found");
        }
    }
    
    /**
     * Generate random names from a Markov model created with createModel().
     * Names are generated until enough have been created to satisfy the 
     * requirements set the by caller.
     * 
     * @param minLength the min length of the names to be generated
     * @param maxLength the max length of the names to be generated
     * @param numNames the number of names to be generated
     */
    public void generateNames(int minLength, int maxLength, int numNames) {
        int namesCreated = 0;
        String[] names = new String[numNames];
        while (namesCreated < numNames) {
            String newName = generateName();    // See private function
            if (newName.length() < minLength || newName.length() > maxLength)
                continue; // Discard names too short or too long
            names[namesCreated] = newName;
            namesCreated++;
        }
        
        // Print out the names
        for (String name: names) {
            System.out.println(name);
        }
    }
    
    // Creates individual name
    private String generateName() {
        String newName = buffer;        // start name with buffer
        int order = buffer.length();    // infer order from buffer length
        Random random = new Random();   // for random number generation
        
        // Generate characters for the name until an end marker is generated
        while (!"!".equals(newName.substring(newName.length()-1))) {
            ArrayList<String> possibilities = model.get
                (newName.substring(newName.length()-order));
            // Pick a random character from the list of characters that follow
            // the current "state" (the substring of length order that ends 
            // our current work-in-progress name). Add to the name.
            newName += possibilities.get(random.nextInt(possibilities.size()));
        }
        
        // Return the name minus the buffer and end marker
        return newName.substring(order, newName.length()-1);
    }
    
    // Use for debugging to see how the model is being generated
    // Print out every key and, for each key, all the values in the Array List
    private void printModel() {
        model.entrySet().stream().forEach((pair) -> {
            System.out.println(pair.getKey());
            pair.getValue().stream().forEach((value) -> {
                System.out.println("\t" + value);
            });
        });
    }
}
