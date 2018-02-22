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
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private Sandwich sandwich;

    //Using Butter knife
    @BindView(R.id.origin_tv)TextView mTextViewOrigin;
    @BindView(R.id.description_tv)TextView mTextViewDescription;
    @BindView(R.id.ingredients_tv)TextView mTextViewIngredients;
    @BindView(R.id.also_known_tv)TextView mTextViewAlsoKnow;
    @BindView(R.id.image_iv) ImageView mImageViewPicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Bind Activity with ButterKnife
        ButterKnife.bind(this);

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
        String json= sandwiches[position];
        sandwich = JsonUtils.parseSandwichJson( json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mImageViewPicture);
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        //Checking is there any field empty fill it with "UnKnown" value
        // else fill it with value from sandwich object
        if (sandwich.getPlaceOfOrigin().isEmpty()){
            mTextViewOrigin.setText(getString(R.string.no_data));
        }else {
            mTextViewOrigin.setText(sandwich.getPlaceOfOrigin());
        }
        if (sandwich.getAlsoKnownAs().isEmpty()){
            mTextViewAlsoKnow.setText(getString(R.string.no_data));
        }else {
            mTextViewAlsoKnow.setText(getStringFromList(sandwich.getAlsoKnownAs()));
        }

        mTextViewDescription.setText(sandwich.getDescription());
        mTextViewIngredients.setText(getStringFromList(sandwich.getIngredients()));
    }

    // this method will convert Lists<String> to String so we can fill it in it's place "TextView"
    public StringBuilder getStringFromList(List<String> list){
        StringBuilder stringBuilder= new StringBuilder();
        for (int i=0;i<list.size();i++){
            if(i == list.size()-1){
                stringBuilder.append(list.get(i));
            }else{
                stringBuilder.append(list.get(i)).append("\n");
            }
        }
        return stringBuilder;
    }

}
