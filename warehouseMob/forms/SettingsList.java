package com.example.warehouse.forms;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.FragmentTransaction;

import com.example.warehouse.R;

public class SettingsList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.settings_list);
        super.onCreate(savedInstanceState);

        setSettingsChoices();
    }

    private void setSettingsChoices(){
        String[] stringArray = {getString(R.string.settings)};

        ListView listView = this.findViewById(R.id.listView);

        ArrayAdapter adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                stringArray
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
