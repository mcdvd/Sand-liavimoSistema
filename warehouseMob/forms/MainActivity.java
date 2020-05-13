package com.example.warehouse.forms;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.warehouse.R;
import com.example.warehouse.apiCalls.ApiCallFunctions;
import com.example.warehouse.objects.Item;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class MainActivity extends Activity {
    private List<Item> items;

    //createdArray ID and databaseID for item selected action
    private HashMap<Integer, Integer> arrayIDtoDatabaseID = new HashMap<Integer, Integer>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.item_list);
        super.onCreate(savedInstanceState);

        ListView listView = this.findViewById(R.id.itemList);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
                try {
                    items = apiCallFunctions.getItems();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String[] itemsArray = new String[items.size()];
        ListIterator<Item> itemsIterator = items.listIterator();

        int i = 0;
        while (itemsIterator.hasNext()) {
            Item nextItem = itemsIterator.next();
            arrayIDtoDatabaseID.put(i, nextItem.getID());
            itemsArray[i] = nextItem.getItemName();
            i++;
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                itemsArray
        );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item result = items.stream().filter(item -> arrayIDtoDatabaseID.get(position).equals(item.getID())).collect(Collectors.toList()).listIterator().next();
                Intent intent = new Intent(MainActivity.this, ItemCreate.class);
                intent.putExtra("itemInformation", result);
                startActivity(intent);
            }
        });
    }

    public void onItemCreateInManagementButtonClick(View view) {
        Intent intent = new Intent(this, ItemCreate.class);
        intent.putExtra("source", "management");
        startActivity(intent);
    }


}

/*    public void setLanguage(){
        Configuration config = new Configuration();

        String languageToLoad = "en"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_main);
    }*/


