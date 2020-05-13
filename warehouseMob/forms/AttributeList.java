package com.example.warehouse.forms;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.example.warehouse.R;
import com.example.warehouse.apiCalls.ApiCallFunctions;
import com.example.warehouse.objects.Attribute;
import com.example.warehouse.objects.Category;
import com.example.warehouse.objects.Item;

import org.w3c.dom.Attr;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class AttributeList extends Activity {

    private List<Attribute> attributes;
    private HashMap<Integer, Integer> arrayIDtoDatabaseID = new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.attribute_list);
        super.onCreate(savedInstanceState);

        ListView listView = this.findViewById(R.id.attributeList);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
                try {
                    attributes = apiCallFunctions.getAttributes();
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

        String[] attributesArray = new String[attributes.size()];
        ListIterator<Attribute> attributesIterator = attributes.listIterator();

        int i = 0;
        while (attributesIterator.hasNext()) {
            Attribute nextAttribute = attributesIterator.next();
            arrayIDtoDatabaseID.put(i, nextAttribute.getId());
            attributesArray[i] = nextAttribute.getAttribute_name();
            i++;
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                attributesArray
        );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Attribute result = attributes.stream().filter(attribute -> arrayIDtoDatabaseID.get(position).equals(attribute.getId())).collect(Collectors.toList()).listIterator().next();
                Intent intent = new Intent(AttributeList.this, AttributeCreate.class);
                intent.putExtra("attributeInformation", result);
                startActivity(intent);
            }
        });
    }

    public void onAttributeCreateInManagementButtonClick(View v) {
        Intent intent = new Intent(this, AttributeCreate.class);
        startActivity(intent);
    }
}
