/**
 * Created by Grant.Hugh on 4/26/2016.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.CDL;

import java.io.*;
import java.util.*;

public class JsonParsing {

    //These JSONArrays are declared publicly so that they can be accessed by methods defined below.

    /** Class constant to hold the demographic information.
     */
    public static JSONArray json;

    /** Class constant to hold the SAT score information.
     */
    public static JSONArray satScores;

    /** Class constant to hold the Per Pupil Expenditure information.
     */
    public static JSONArray perPupilE;

    /** Class constant to hold the combination of the three.
     */
    public static JSONArray totalDataSet;

    /** Class constant to hold the demographic + SAT score information.
     */
    public static JSONArray demogAndSat;

    /** Class constant to hold the demographic + Per Pupil Expenditure information.
     */
    public static JSONArray demogandPerPupilE;

    /**
     *  Main method reads the text files with the data and collates them using the appropriate collate methods.
     *  The main method also initializes the scanner and calls the run method which starts the program.
     */
    public static void main(String[] args)  {

        json = Collate.read("src\\DemographicInfo.txt");
        satScores = Collate.read("src\\SATScores.txt");
        perPupilE = Collate.read("src\\PerPupilExpenditure.txt");
        totalDataSet = Collate.collate2(Collate.collate(json, satScores), perPupilE);
        demogAndSat = Collate.collate(json, satScores);
        demogandPerPupilE = Collate.collate2(json, perPupilE);

        Scanner console = new Scanner(System.in);

        run(console);

    }

    /**
     *  This method first prints a short intro to the program, and then it enters a while loop where it keeps on prompting the user for
     *  queries and data until the user stops the program.
     *
     *  @param console The console to be used to interact with the user.
     */
    public static void run(Scanner console) {

        System.out.println("This program queries for specified data. You will probably need to read the README in order to use this program.");

        boolean con = true;

        while(con) {
            JSONArray j = getJsonArray(console);
            JSONArray allParams = Query.trim(console, j);
            JSONArray finalParams = new JSONArray();
            for (int ii = 0; ii < allParams.length(); ii++) {
                finalParams.put(new JSONObject());
            }
            j = putParams(console, finalParams, allParams);
            System.out.print("If you would like to print the data, please type in *print*. Otherwise, press enter: ");
            if (console.nextLine().equals("print") || console.nextLine().equals("*print*")) {
                printAll(j);
            }
            System.out.println();
            dataConversion(console, j);
            String foo = Inputter.getString(console, "Do you have additional queries? ");
            if(foo.equals("Yes") || foo.equals("yes")) {

            }
            else {
                con = false;
            }
        }

    }

    /**
     *  This method prompts the user for the original dataset to be used.
     *
     *  @param console The console to be used to interact with the user.
     *  @return The JSONArray with the starting data (either D, S, P, DP, DS, or T).
     */
    public static JSONArray getJsonArray(Scanner console) {
        String dataSetI = Inputter.getString(console, "Initial Data Set (Demographics: (D), Sat Scores (S), Per Pupil Expenditure (P), Demographics and Per Pupil Expenditure (DP), Demographics and SAT (DS), Total Data Set (T)): ");
        JSONArray j = null;
        if (dataSetI.equals("D")) {
            j = json;
        } else if (dataSetI.equals("S")) {
            j = satScores;
        } else if (dataSetI.equals("P")) {
            j = perPupilE;
        } else if(dataSetI.equals("DP")) {
            j = demogandPerPupilE;
        } else if(dataSetI.equals("DS")) {
            j = demogAndSat;
        } else if (dataSetI.equals("T")) {
            j = totalDataSet;
        } else {
            System.out.println("Invalid Data Set. Try again.");
            return getJsonArray(console);
        }
        return j;
    }

    /**
     *  This method takes in a queried set of data and asks which fields in the data the user wants to keep.
     *
     *  @param console The console to be used to interact with the user.
     *  @param newData The JSONArray which will hold the modified data with only certain specified parameters.
     *  @param j The original JSONArray which has all the data.
     *  @return The final JSONArray with the user-specified parameters and queries.
     */
    public static JSONArray putParams(Scanner console, JSONArray newData, JSONArray j) {
        String param = Inputter.getString(console, "Field you want to keep: ");
        if(param.equals("all")) {
            return j;
        }
        else if(param.equals("help")) {
            Query.printHeaders(j);
            return putParams(console, newData, j);
        }
        else if(param.equals("end")) {
            return newData;
        }
        else {
            if(Query.isValidParam(param, j)) {
                for(int jj = 0; jj < j.length(); jj++) {
                    try {
                        JSONObject j1 = (JSONObject) j.get(jj);
                        JSONObject j2 = (JSONObject) newData.get(jj);
                        Object o = j1.get(param);
                        j2.put(param, o);
                    }
                    catch(JSONException e) {
                        System.out.println("Array is null.");
                    }
                }
                return putParams(console, newData, j);
            }
            else {
                System.out.println("Not a valid parameter");
                return putParams(console, newData, j);
            }
        }
    }

    /**
     *  This method takes in a JSONArray, prompts the user for a filename, and then calls the directoryFile method to
     *  for the directory name and conversion. 
     *
     *  @param console The console to be used to interact with the user.
     *  @param j The JSONArray to be converted to a CSV file.
     */
    public static void dataConversion(Scanner console, JSONArray j) {
        System.out.print("If you would like to convert the data into a CSV file, please enter *convert*. Otherwise, hit enter. ");
        if (console.nextLine().equals("convert") || console.nextLine().equals("*convert*")) {
            System.out.println();
            String fileName = Inputter.getString(console, "File Name: ");
            System.out.println();
            directoryFile(console, j, fileName);
        }
    }

    /**
     *  This method takes in a console to prompt the user for a directory and checks if the directory exists.
     *  If it doesn't, this method will re-prompt the user but if the directory name is valid, it will create a file
     *  with the file name containing the JSONArray information.
     *
     *  @param console The console to be used to interact with the user.
     *  @param j The JSONArray to be converted to a CSV file.
     *  @param fileName The file name to be used.
     */
    public static void directoryFile(Scanner console, JSONArray j, String fileName) {
        String dirName = Inputter.getString(console, "Directory Name: ");
        String csv = null;
        try {
            csv = CDL.toString(j);
            try {
                JSON2CSV(csv, fileName, dirName);
            }
            catch(Exception e) {
                System.out.println("You probably entered the directory incorrectly. Make sure replace all \\ with \\\\.");
                directoryFile(console, j, fileName);
            }
        } catch (JSONException e) {
            System.out.println("Error converting array to string CSV");
        }
    }

    /**
     *  This method takes a CSV string, a file name and a directory name and creates a new file with the specified file name
     *  at the directory and puts in the CSV information.
     *
     *  @param csv The CSV string to be put into the file.
     *  @param fileName The name of the file.
     *  @param dirName The name of the directory.
     */
    public static void JSON2CSV(String csv, String fileName, String dirName) throws JSONException {

        File dirFile = new File(dirName);
        File file = new File(dirFile, fileName);
        PrintStream output = null;
        try {
            output = new PrintStream(file);
        }
        catch(Exception e) {
            System.out.println("error writing to file");
        }
        output.println(csv);

    }

    /**
     *  This method takes in a JSONArray and prints it out with one object on each line.
     *
     *  @param j The JSONArray to be printed.
     */
    public static void printAll(JSONArray j) {
        try {
            for (int ii = 0; ii < j.length(); ii++) {
                Object k = j.get(ii);
                System.out.println(k);
            }
        }
        catch(JSONException e) {
            System.out.println("Array is null.");
        }
    }

}
