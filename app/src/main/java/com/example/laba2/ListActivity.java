package com.example.laba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class ListActivity extends AppCompatActivity {
    ArrayList<String> myStringArray;
    String userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Bundle arguments = getIntent().getExtras();
        userLogin = arguments.get("userLogin").toString();
        myStringArray = new ArrayList<>();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        Set<String> stringSet = sharedPref.getStringSet(userLogin, Collections.emptySet());
        myStringArray.addAll(stringSet);

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText(userLogin);

        ListView textList = findViewById(R.id.textList);

        Button buttonLang = findViewById(R.id.buttonSettings);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        Button buttonRm = findViewById(R.id.buttonRm);
        EditText editText = findViewById(R.id.editText);
        Intent settingsIntent = new Intent(this, SettingsActivity.class);


        ArrayList<String> selectedElements = new ArrayList<>();
        ArrayAdapter TextAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, myStringArray);
        textList.setAdapter(TextAdapter);

        textList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String element = (String) TextAdapter.getItem(position);
                if (textList.isItemChecked(position))
                    selectedElements.add(element);
                else
                    selectedElements.remove(element);
            }
        });

        buttonLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsIntent.putExtra("userLogin", userLogin);
                startActivity(settingsIntent);
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String elementName = editText.getText().toString();
                TextAdapter.add(elementName);
                editText.setText("");
                TextAdapter.notifyDataSetChanged();
            }
        });
        buttonRm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myStringArray.size()!=0) {
                    textList.clearChoices();
                    for(int i=0; i < selectedElements.size();i++)
                    {
                        TextAdapter.remove(selectedElements.get(i));
                    }
                    selectedElements.clear();
                    TextAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(userLogin);
        editor.putStringSet(userLogin, myStringArray.stream().collect(Collectors.toSet()));
        editor.apply();
        super.onDestroy();
    }
}