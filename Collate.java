import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Grant.Hugh on 5/25/2016.
 */
public class Collate {

    /**
     *  This method collates two JSONArrays based off of school name.
     *
     *  @param json The first JSONArray to be collated.
     *  @param satScores The second JSONArray to be collated.
     *  @return The collated JSONArray.
     */
    public static JSONArray collate(JSONArray json, JSONArray satScores) {

        JSONArray collate = new JSONArray();
        ArrayList<String> schoolNames = paramSet("School", "all", json);
        for(int ii = 0; ii < satScores.length(); ii++) {
            try {
                JSONObject j = (JSONObject) satScores.get(ii);
                if (schoolNames.contains(j.getString("High School"))) {
                    JSONObject k = find("School", j.getString("High School"), json);
                    Iterator<?> keys = k.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        if (!key.equals("School")) {
                            j.put(key, k.get(key));
                        }
                    }
                    collate.put(j);
                }
            }
            catch(JSONException e) {
                System.out.println("Error.");
            }
        }
        return collate;
    }

    /**
     *  This method collates two JSONArrays based off of school year and district.
     *
     *  @param json The first JSONArray to be collated.
     *  @param perPupilE The second JSONArray to be collated.
     *  @return The collated JSONArray.
     */
    public static JSONArray collate2(JSONArray json, JSONArray perPupilE) {
        JSONArray collate2 = new JSONArray();
        for(int ii = 0; ii < json.length(); ii++) {
            try {
                JSONObject demog = (JSONObject) json.get(ii);
                for (int jj = 0; jj < perPupilE.length(); jj++) {
                    JSONObject perPupil = (JSONObject) perPupilE.get(jj);
                    if ((demog.getInt("SchoolYear") == perPupil.getInt("Year")) && (demog.getString("District").equals(perPupil.getString("District")))) {
                        demog.put("Per Pupil Expenditure", perPupil.getDouble("Per Pupil Expenditure"));
                        collate2.put(demog);
                    }
                }
            }
            catch(JSONException e) {
                System.out.println("Error.");
            }
        }
        return collate2;
    }



    /**
     *  This method searches through the JSONObjects of a JSONArray for a certain parameter and then lumps all those parameters
     *  into an Arraylist. This method is used by the collate method above.
     *
     *  @param param The parameter to be queried for.
     *  @param j The data to be query out of.
     *  @return An arraylist of the values.
     */
    public static ArrayList<String> paramSet(String param, String value, JSONArray j)  {
        ArrayList<String> results = new ArrayList<>();
        for(int ii = 0; ii < j.length(); ii++) {
            try {
                JSONObject json = (JSONObject) j.get(ii);
                Object o = json.get(param);
                if(o instanceof String) {
                    String k = json.getString(param);
                    if (value.equals("all") || k.equals(value)) {
                        results.add(k);
                    }
                }
            }
            catch (JSONException e) {
                System.out.println("Parameter does not exist");
            }
        }
        return results;
    }

    /**
     *  This method finds a JSONObject based off of a parameter and a value for that parameter.
     *
     *  @param param The parameter to be queried for.
     *  @param value The value to search for.
     *  @param j The dataset to look in.
     *  @return The JSONObject that we have been searching for.
     */
    public static JSONObject find(String param, String value, JSONArray j)  {
        for(int ii = 0; ii < j.length(); ii++) {
            try {
                JSONObject json = (JSONObject) j.get(ii);
                if (json.getString(param).equals(value)) {
                    return json;
                }
            }
            catch(JSONException e) {
                System.out.println("Parameter does not exist.");
            }
        }
        return null;
    }

    /**
     *  This method turns a file with JSON in it into an actual JSONArray.
     *
     *  @param fileName The name of the file to be read.
     *  @return The JSONArray that is the contents of the file.
     */
    public static JSONArray read(String fileName)  {
        File f = new File(fileName);
        Scanner fileScan = null;
        JSONArray json = null;

        try {
            fileScan = new Scanner(f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String fileContent = "";
        while (fileScan.hasNextLine()) {
            String nextLine = fileScan.nextLine();
            fileContent += nextLine;
        }
        try {
            json = new JSONArray(fileContent);
        }
        catch(JSONException e) {
            System.out.println("Error reading file.");
        }
        return json;
    }

}
