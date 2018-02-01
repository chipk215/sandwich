package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mDescriptionTextView;
    private TextView mPlaceOfOriginTextView;
    private TextView mPlaceOfOriginLabelTextView;
    private TextView mAlsoKnownAsTextView;
    private TextView mAlsoKnownLabelTextView;
    private TextView mIngredientsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constraint_detail);

        mDescriptionTextView =   findViewById(R.id.description_tv);
        mPlaceOfOriginTextView = findViewById(R.id.origin_tv);
        mPlaceOfOriginLabelTextView = findViewById(R.id.origin_label);
        mAlsoKnownAsTextView =  findViewById(R.id.also_known_tv);
        mAlsoKnownLabelTextView = findViewById(R.id.known_label);
        mIngredientsTextView =  findViewById(R.id.ingredients_tv);

        ImageView ingredientsIv = findViewById(R.id.image_iv);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);

        // Retrieve the sandwich image
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    /**
     * Hydrate the UI with model data
     * @param sandwich
     */
    private void populateUI(Sandwich sandwich) {

        final String andString = " and ";
        final String orString = " or ";

        // Add the description
        mDescriptionTextView.setText(sandwich.getDescription());

        // Add place of origin
        if (sandwich.getPlaceOfOrigin().isEmpty()){
            //Hide views with no data
            mPlaceOfOriginLabelTextView.setVisibility(View.GONE);
            mPlaceOfOriginTextView.setVisibility(View.GONE);
        }else {
            mPlaceOfOriginLabelTextView.setVisibility(View.VISIBLE);
            mPlaceOfOriginTextView.setVisibility(View.VISIBLE);
            mPlaceOfOriginTextView.setText(sandwich.getPlaceOfOrigin());
        }


        // Add aliases
        if (sandwich.getAlsoKnownAs().size() == 0){
            //Hide views with no data
            mAlsoKnownAsTextView.setVisibility(View.GONE);
            mAlsoKnownLabelTextView.setVisibility(View.GONE);

        }else {
            mAlsoKnownAsTextView.setVisibility(View.VISIBLE);
            mAlsoKnownLabelTextView.setVisibility(View.VISIBLE);
            String formattedAliases = listToString(sandwich.getAlsoKnownAs(), orString);
            mAlsoKnownAsTextView.setText(formattedAliases);
        }

        //Add ingredients -- Note: Using text view rather than a list since the starter code
        // defined a text view for ingredients
        String formattedIngredients = listToString(sandwich.getIngredients(), andString);
        mIngredientsTextView.setText(formattedIngredients);

    }


    /**
     * Format the output string for improved readability of short lists and longer lists
     * @param items the list
     * @param compoundConjunction conjunction used when only two items are in the list
     * @return formatted text string containing items
     */
    private String listToString(List<String> items, String compoundConjunction){
        String result;
        switch (items.size()){
            case 0: result= "";
                    break;
            case 1: result = items.get(0);
                    break;
            case 2: result = items.get(0) + compoundConjunction + items.get(1);
                    break;
            default: // Insert commas between items
                     String separator = ", ";
                     StringBuilder builder = new StringBuilder();
                     for (String name : items){
                         builder.append(name);
                         builder.append(separator);
                     }
                     result = builder.toString();
                     // remove trailing comma
                     result = result.substring(0, result.length() - separator.length());
        }

        return result;
    }
}
