package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private TextView mAlsoKnownAsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constraint_detail);

        mDescriptionTextView = (TextView) findViewById(R.id.description_tv);
        mPlaceOfOriginTextView = (TextView) findViewById(R.id.origin_tv);
        mAlsoKnownAsTextView = (TextView) findViewById(R.id.also_known_tv);

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
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        // Add the description
        mDescriptionTextView.setText(sandwich.getDescription());

        // Add place of origin
        mPlaceOfOriginTextView.setText(sandwich.getPlaceOfOrigin());

        // Add aliases
        String formattedAliases = formatAliases(sandwich.getAlsoKnownAs());
        mAlsoKnownAsTextView.setText(formattedAliases);


    }


    /**
     * Format the output string for improved readability of short lists and longer lists
     * @param names
     * @return
     */
    private String formatAliases(List<String> names){
        String result;
        switch (names.size()){
            case 0: result= "";
                    break;
            case 1: result = names.get(0);
                    break;
            case 2: result = names.get(0) + "or " + names.get(1);
                    break;
            default: // Insert commas between names
                     String separator = ", ";
                     StringBuilder builder = new StringBuilder();
                     for (String name : names){
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
