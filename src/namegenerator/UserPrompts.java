/*
 * Michael McClellan
 * February 2017
 */
package namegenerator;

import java.util.Scanner;

/**
 * Generates novel names based on a list of other popular names. This program 
 * seeds a Markov model using the names, then generates new names according
 * to user-prompted parameters. For how this model is represented, and how the
 * names are generated, see NameGenerator.java. This class simply serves as
 * an entry-point and an interface for the user.
 * 
 * @author Michael
 */
public class UserPrompts {
    
    private final Scanner input;
    private final NameGenerator generator;
    
    public UserPrompts() {
        input = new Scanner(System.in);
        generator = new NameGenerator();
    }
    
    /**
     * Prompts the user to select the options for the baby names to be
     * generated.
     */
    private void selectOptions() {
        
        // Prompt user to select gender of names to be generated
        System.out.println("Select name gender:");
        System.out.println("(1) male   (2) female");
        int gender = input.nextInt();
        while (gender > 3 || gender < 1) {
            System.out.println("Invalid Choice; try again");
            System.out.println("Select name gender:");
            System.out.println("(1) male   (2) female");
            gender = input.nextInt();
        }
        
        // Prompt user to select minimum length of names to be generated
        System.out.println("Select minimum name length:");
        int minLength = input.nextInt();
        while (minLength < 1) {
            System.out.println("Invalid Choice; try again");
            System.out.println("Select minimum name length:");
            minLength = input.nextInt();
        }
        
        // Prompt user to select maximum length of names to be generated
        System.out.println("Select maximum name length:");
        int maxLength = input.nextInt();
        while (maxLength < minLength + 1) {
            System.out.println("Invalid Choice; try again");
            System.out.println("Select maximum name length:");
            maxLength = input.nextInt();
        }
        
        // Prompt user to select order of Markov model to be used
        System.out.println("Select the order of the Markov model used:");
        System.out.println("(greater than 0; higher means more typical"
                + " names)");
        int order = input.nextInt();
        while (order < 1) {
            System.out.println("Invalid Choice; try again");
            System.out.println("Select the order of the Markov model used:");
            System.out.println("(greater than 1, no more than max name length"
                + " in seeded names)");
            order = input.nextInt();
        }
        
        // Prompt user to select maximum length of names to be generated
        System.out.println("Select how many names you'd like generated:");
        int numNames = input.nextInt();
        while (numNames < 1) {
            System.out.println("Invalid Choice; try again");
            System.out.println("Select how many names you'd like generated:");
            numNames = input.nextInt();
        }
        
        System.out.println();
        
        // First create a model for name generation
        generator.createModel(gender, order);
        // Then generate new names using that model
        generator.generateNames(minLength, maxLength, numNames);
        
        System.out.println();
    }
    
    /**
     * Entry point for program.
     * 
     * @param args unused
     */
    public static void main(String[] args) {
        UserPrompts program = new UserPrompts();
        program.selectOptions();
        // Prompt play again
        Scanner runAgain = new Scanner(System.in);
        while (true) {
            System.out.println("\nWould you like to run the program again?");
            System.out.println("(1) YES    (2) NO");
            String ans = runAgain.next();
            if (ans.equals("1")) program.selectOptions();
            else break;     // with any other input
        }
    }   
}
