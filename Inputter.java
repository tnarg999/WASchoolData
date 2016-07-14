// Grant Hugh
// M750c
// Assignment 5
// October 30, 2015

import java.util.*;
import java.io.*;

public class Inputter {

    /**
     *  This method returns a String that is prompted for.
     *  @param console The console used to interact with the user.
     *  @param prompt The prompt to be displayed to the user.
     *  @return The String that is entered.
     */
    public static String getString(Scanner console, String prompt) {
        System.out.print(prompt);
        return console.nextLine();
    }

    /**
     *  This method returns a positive integer that is prompted for.
     *  @param console The console used to interact with the user.
     *  @param prompt The prompt to be displayed to the user.
     *  @param error The error message to be displayed if the user enters a negative number.
     *  @return The integer that is entered.
     */
    public static int getPositiveInt(Scanner console, String prompt, String error) {

        while(true) {

            System.out.print(prompt);

            if(console.hasNextInt()) {
                int input = Integer.parseInt(console.nextLine());
                if(input >= 0) {
                    return input;
                }
                else {
                    System.out.println(error);
                }
            }

            else {
                console.nextLine();
                System.out.println(error);
            }

        }

    }

    /**
     *  This method returns a Double that is prompted for.
     *  @param console The console used to interact with the user.
     *  @param prompt The prompt to be displayed to the user.
     *  @param error The error to be displayed if the user enters a negative number.
     *  @return The double that is entered.
     */
    public static double getPositiveDouble(Scanner console, String prompt, String error) {

        while(true) {

            System.out.print(prompt);

            if(console.hasNextDouble()) {
                double input = Double.parseDouble(console.nextLine());
                if(input >= 0) {
                    return input;
                }
                else {
                    System.out.println(error);
                }
            }

            else {
                console.nextLine();
                System.out.println(error);
            }

        }

    }

}
