package com.group395.ember;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_start);
    }

    public void resetOnClick(View v){
        EditText nameText = findViewById(R.id.nameText);
        nameText.setHint(R.string.enter_name);
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setText(R.string.search_button);
    }

    public void searchOnClick(View v){
        EditText nameText = findViewById(R.id.nameText);
        SearchResultsActivity.search(nameText.getText().toString());
        startActivity(new Intent(SearchStartActivity.this, SearchResultsActivity.class));

    }

}
