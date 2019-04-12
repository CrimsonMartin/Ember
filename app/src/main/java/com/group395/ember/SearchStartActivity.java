package com.group395.ember;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class SearchStartActivity extends AppCompatActivity {

    private boolean actorNotTitle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_start);
        if(!HistoryActivity.searchWorks){ ((Button) findViewById(R.id.searchButton)).setText("View example"); }
    }

    public void switchOnClick(View v) {
        actorNotTitle = ((Switch) findViewById(R.id.searchBySwitch)).isChecked();
        if (actorNotTitle) {
            (findViewById(R.id.byActor)).setVisibility(View.VISIBLE);
            (findViewById(R.id.byTitle)).setVisibility(View.INVISIBLE);
        } else {
            (findViewById(R.id.byActor)).setVisibility(View.INVISIBLE);
            (findViewById(R.id.byTitle)).setVisibility(View.VISIBLE);
        }
    }

    public void resetOnClick(View v){
        EditText nameText = findViewById(R.id.nameText);
        nameText.setHint(R.string.enter_name);
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setText(R.string.search_button);
    }

    public void searchOnClick(View v){
        if(HistoryActivity.searchWorks) {
            EditText nameText = findViewById(R.id.nameText);
            if (!(nameText.getText().toString().equals(""))) {
                System.out.println("searchOnClick started");
                SearchResultsActivity.search(nameText.getText().toString(), actorNotTitle);
                System.out.println("searchOnClick finished");
                startActivity(new Intent(SearchStartActivity.this, SearchResultsActivity.class));
            }
        }else{
            startActivity(new Intent(SearchStartActivity.this, SearchResultsActivity.class));
        }
    }

    public void backOnClick(View v){ startActivity(new Intent(SearchStartActivity.this, HistoryActivity.class)); }

}
