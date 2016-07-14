import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Grant.Hugh on 5/25/2016.
 */
public class Query {

    /**
     *  This method does the actual querying. When the above methods want to query for integer values
     *  they call this method.
     *
     *  @param param The parameter to be queried for.
     *  @param low The lower boundary for the value.
     *  @param high The higher boundary for the value.
     *  @param jArray The array to query in.
     *  @return The new filtered JSONArray.
     */
    public static JSONArray queryInt(String param, int low, int high, JSONArray jArray)  {

        ArrayList<JSONObject> vals = new ArrayList<>();
        for(int ii = 0; ii < jArray.length(); ii++) {
            try {
                JSONObject j = (JSONObject) jArray.get(ii);
                Object o = j.get(param);
                if(o instanceof Integer) {
                    if (j.get(param) != null && j.getInt(param) >= low && j.getInt(param) <= high) {
                        vals.add(j);
                    }
                }
            }
            catch(JSONException e) {
                System.out.println("Parameter does not exist.");
                e.printStackTrace();
                break;
            }
        }
        return new JSONArray(vals);
    }

    /**
     *  This method does the actual querying. When the above methods want to query for double values
     *  they call this method.
     *
     *  @param param The parameter to be queried for.
     *  @param low The lower boundary for the value.
     *  @param high The higher boundary for the value.
     *  @param jArray The array to query in.
     *  @return The new filtered JSONArray.
     */
    public static JSONArray queryDouble(String param, double low, double high, JSONArray jArray)  {

        ArrayList<JSONObject> vals = new ArrayList<>();
        for(int ii = 0; ii < jArray.length(); ii++) {
            try {
                JSONObject j = (JSONObject) jArray.get(ii);
                Object o = j.getDouble(param);
                if(o instanceof Double) {
                    if (j.get(param) != null && j.getDouble(param) >= low && j.getDouble(param) <= high) {
                        vals.add(j);
                    }
                }
            }
            catch(JSONException e) {
                System.out.println("Parameter does not exist.");
                break;
            }
        }
        return new JSONArray(vals);
    }

    /**
     *  This method does the actual querying. When the above methods want to query for String values
     *  they call this method.
     *
     *  @param param The parameter to be queried for.
     *  @param query The value of the parameter that we want.
     *  @param jArray The array to query in.
     *  @return The new filtered JSONArray.
     */
    public static JSONArray queryString(String param, String query, JSONArray jArray) {

        ArrayList<JSONObject> vals = new ArrayList<>();
        for(int ii = 0; ii < jArray.length(); ii++) {
            try {
                JSONObject j = (JSONObject) jArray.get(ii);
                Object o = j.get(param);
                if(o instanceof String) {
                    if (param.equals("GradeSpan")) {
                        String data = j.getString(param).replaceAll("\\s+", "");
                        if (data.equals(query)) {
                            vals.add(j);
                        }
                    }
                    if (j.getString(param).equals(query)) {
                        vals.add(j);
                    }
                }
            }
            catch(JSONException e) {
                System.out.println("Parameter does not exist");
                break;
            }
        }
        return new JSONArray(vals);
    }

    /**
     *  This method queries a dataset based off of a parameter.
     *
     *  @param console The console to be used to interact with the user.
     *  @param param The parameter to be queried for.
     *  @param dataSet The data to be query out of.
     *  @return The new queried JSONArray.
     */
    public static JSONArray getStringParam(Scanner console, String param, JSONArray dataSet) {

        System.out.print("Value: ");
        String value = console.nextLine();
        if (queryString(param, value, dataSet).length() != 0) {
            dataSet = queryString(param, value, dataSet);
        } else {
            System.out.println("No schools fulfill your specified queries");
        }
        return trim(console, dataSet);

    }

    /**
     *  This method queries a dataset based off of a parameter.
     *
     *  @param console The console to be used to interact with the user.
     *  @param param The parameter to be queried for.
     *  @param dataSet The data to be query out of.
     *  @return The new queried JSONArray.
     */
    public static JSONArray getIntParam(Scanner console, String param, JSONArray dataSet) {
        int low = Inputter.getPositiveInt(console, "Lower Bound: ", "Please enter an integer greater than zero");
        int high = Inputter.getPositiveInt(console, "Upper Bound: ", "Please enter an integer greater than zero");
        if (queryInt(param, low, high, dataSet).length() != 0) {
            dataSet = queryInt(param, low, high, dataSet);
        } else {
            System.out.println("No schools fulfill your specified queries");
        }
        return trim(console, dataSet);
    }

    /**
     *  This method queries a dataset based off of a parameter.
     *
     *  @param console The console to be used to interact with the user.
     *  @param param The parameter to be queried for.
     *  @param dataSet The data to be query out of.
     *  @return The new queried JSONArray.
     */
    public static JSONArray getDoubleParam(Scanner console, String param, JSONArray dataSet) {
        double low = Inputter.getPositiveDouble(console, "Lower Bound: ", "Please enter a double greater than zero");
        double high = Inputter.getPositiveDouble(console, "Upper Bound: ", "Please enter a double greater than zero");
        if (queryDouble(param, low, high, dataSet).length() != 0) {
            dataSet = queryDouble(param, low, high, dataSet);
        } else {
            System.out.println("No schools fulfill your specified queries");
        }
        return trim(console, dataSet);
    }

    /**
     *  This method looks for the data type of the object given by a query.
     *
     *  @param query The query that should be used to search for the object.
     *  @param j The JSONArray to query out of.
     *  @param count Which JSONObject to look at.
     *  @return The data type of the specified query.
     */
    public static String lookUp(String query, JSONArray j, int count) {
        try {
            JSONObject jObj = (JSONObject) j.get(count);
            Object o = jObj.get(query);
            if(o instanceof String) {
                return "String";
            }
            else if(o instanceof Integer) {
                return "Integer";
            }
            else if(o instanceof Double) {
                return "Double";
            }
            else if(o  == null) {
                lookUp(query, j, count++);
            }
            else {
                return null;
            }
        }
        catch(JSONException e) {
            System.out.println("Array is null");
        }
        return null;
    }

    /**
     *  This method puts together the querying methods above and produces a final result.
     *
     *  @param console The console to be used to interact with the user..
     *  @param dataSet The data to query out of.
     *  @return The new queried JSONArray.
     */
    public static JSONArray trim(Scanner console, JSONArray dataSet) {

        String param = Inputter.getString(console, "Parameter Name: ");

        if (param.equals("Help")) {
            printHeaders(dataSet);
            return trim(console, dataSet);
        }

        else if(param.equals("end")) {
            return dataSet;
        }

        else if(isValidParam(param, dataSet)) {
            String queryType = lookUp(param, dataSet, 0);
            if (queryType.equals("String")) {
                return getStringParam(console, param, dataSet);
            } else if (queryType.equals("Integer")) {
                return getIntParam(console, param, dataSet);
            } else if (queryType.equals("Double")) {
                return getDoubleParam(console, param, dataSet);
            }
            else {
                System.out.println("Not a valid query type.");
                return trim(console, dataSet);
            }
        }
        else {
            System.out.println("Parameter does not exist. Try again.");
            return trim(console, dataSet);
        }

    }

    /**
     *  This method checks whether or not the parameter is valid.
     *
     *  @param param The parameter to be checked to see if it exists or not.
     *  @param jsonArray The data to check for the parameter in.
     *  @return Whether the parameter is valid or not.
     */
    public static boolean isValidParam(String param, JSONArray jsonArray) {
        try {
            JSONObject j = (JSONObject) jsonArray.get(0);
            Iterator<?> keys = j.keys();
            while(keys.hasNext()) {
                String key = (String) keys.next();
                if(key.equals(param)) {
                    return true;
                }
            }
        }
        catch(JSONException e) {
            System.out.println("Array is null.");
        }
        return false;
    }

    /**
     *  This method prints the headers of a JSONObject in a JSONArray. Called by typing in "help" at some places.
     *
     *  @param jsonArray The data to be used.
     */
    public static void printHeaders(JSONArray jsonArray) {

        try {
            JSONObject j = (JSONObject) jsonArray.get(0);
            Iterator<?> keys = j.keys();
            while(keys.hasNext()) {
                String key = (String) keys.next();
                System.out.print(key);
                Object o = j.get(key);
                if(o instanceof String) {
                    System.out.println(" Type: String");
                }
                else if(o instanceof Integer) {
                    System.out.println(" Type: Integer");
                }
                else if(o instanceof Double) {
                    System.out.println(" Type: Double");
                }
                else {
                    System.out.println(" Type: Other");
                }
            }
        }
        catch(JSONException e) {
            System.out.println("Array is null.");
        }

    }

}
