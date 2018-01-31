package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String TAG = "JsonUtils";

    /**
     * Deserializes a json string into a Sandwich object.
     * @param jsonString - serialized json Sandwich object
     * @return a Sandwich object
     */
    public static Sandwich parseSandwichJson(String jsonString) {

        final String nameField = "name";
        final String mainNameField = "mainName";
        final String alsoKnownAsField = "alsoKnownAs";
        final String placeOfOriginField = "placeOfOrigin";
        final String descriptionField = "description";
        final String imageField = "image";
        final String ingredientsField = "ingredients";

        if (jsonString == null) {
            return null;
        }

        try{
            // Using the no args constructor due to serialization note in Sandwich class
            Sandwich sandwich = new Sandwich();

            JSONObject jsonObject = new JSONObject(jsonString);

            JSONObject nameObject = jsonObject.getJSONObject(nameField);
            sandwich.setMainName(nameObject.getString(mainNameField));

            JSONArray alsoKnownAsNamesArray = nameObject.getJSONArray(alsoKnownAsField);
            // Retrieve list of sandwich aliases
            sandwich.setAlsoKnownAs(jsonStringArrayToList(alsoKnownAsNamesArray));

            sandwich.setPlaceOfOrigin(jsonObject.getString(placeOfOriginField));

            sandwich.setDescription(jsonObject.getString(descriptionField));

            sandwich.setImage(jsonObject.getString(imageField));

            JSONArray ingredientsArray = jsonObject.getJSONArray(ingredientsField);
            sandwich.setIngredients(jsonStringArrayToList(ingredientsArray));

            return sandwich;

        } catch (final JSONException je){
            Log.e(TAG, "JSON parsing error: " + je);
            return null;
        }

    }


    /**
     * Help method to convert json array of strings to java list
     * @param jsonArray of strings
     * @return jave list of strings
     * @throws JSONException - if strings cannot be parsed from json array
     */
    static private List<String> jsonStringArrayToList(JSONArray jsonArray) throws JSONException{

        List<String> result = new ArrayList<>();

        if ( jsonArray != null){
            for (int i=0; i< jsonArray.length(); i++){
                result.add(jsonArray.getString(i));
            }
        }
        return result;
    }
}
