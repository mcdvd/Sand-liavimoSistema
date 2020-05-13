package com.example.warehouse.forms;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehouse.R;
import com.example.warehouse.apiCalls.ApiCallFunctions;
import com.example.warehouse.objects.Barcode;
import com.example.warehouse.objects.Category;
import com.example.warehouse.objects.Item;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class ItemCreate extends Activity {
    private List<Category> categories;
    HashMap<String, Integer> spinnerMap = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.item_create);
        super.onCreate(savedInstanceState);

        TextView barcodeValue = findViewById(R.id.create_item_barcode);

        Bundle extras = getIntent().getExtras();
        Barcode barcode = null;
        Item item = null;
        if (extras != null) {
            if (extras.containsKey("barcodeValue")) {
                barcodeValue.setText(getIntent().getStringExtra("barcodeValue"));
                barcodeValue.setEnabled(false);
            }
            if (extras.containsKey("barcodeWithItem")) {
                barcode = (Barcode) getIntent().getSerializableExtra("barcodeWithItem");
            }
            if (extras.containsKey("itemInformation")) {
                item = (Item) getIntent().getSerializableExtra("itemInformation");
            }
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
                try {
                    categories = apiCallFunctions.getCategories();
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

        String[] categoryArray = new String[categories.size()];
        ListIterator<Category> iterator = categories.listIterator();

        int i = 0;
        while (iterator.hasNext()) {
            Category iteratorNext = iterator.next();
            spinnerMap.put(iteratorNext.getCategory_name(), iteratorNext.getId());
            categoryArray[i] = iteratorNext.getCategory_name();
            i++;
        }

        Spinner categorySpinner = findViewById(R.id.create_item_category_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categoryArray);

        categorySpinner.setAdapter(adapter);

        EditText itemName = findViewById(R.id.create_item_title);
        EditText itemBarcode = findViewById(R.id.create_item_barcode);
        EditText itemID = findViewById(R.id.item_management_id);
        findViewById(R.id.item_management_id).setVisibility(View.GONE);

        if (barcode != null || item != null) {
            findViewById(R.id.item_information_create_button).setVisibility(View.GONE);
            findViewById(R.id.item_information_update_button).setVisibility(View.VISIBLE);
        }

        if (barcode != null) {
            itemID.setText(barcode.getID().toString());
            itemName.setText(barcode.getItem().getItemName());
            if (barcode.getBarcode() != null) {
                itemBarcode.setText(barcode.getBarcode());
            }
            if (barcode.getItem().getCategory() != null) {
                int spinnerPosition = adapter.getPosition(barcode.getItem().getCategory().getCategory_name());
                categorySpinner.setSelection(spinnerPosition);
            }
        }
        if (item != null) {
            itemID.setText(item.getID().toString());
            itemName.setText(item.getItemName());
            if (item.getCategory() != null) {
                int spinnerPosition = adapter.getPosition(item.getCategory().getCategory_name());
                categorySpinner.setSelection(spinnerPosition);
            }
        }
        if (item == null && barcode == null) {
            findViewById(R.id.item_information_create_button).setVisibility(View.VISIBLE);
            findViewById(R.id.item_information_update_button).setVisibility(View.GONE);
            findViewById(R.id.item_information_delete_button).setVisibility(View.INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onItemCreateButtonClick(View v) {
        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        EditText itemName = this.findViewById(R.id.create_item_title);
        EditText itemBarcode = this.findViewById(R.id.create_item_barcode);
        final Spinner itemCategory = this.findViewById(R.id.create_item_category_list);

        Category category =
                categories.stream()
                        .filter(category1 -> category1.getCategory_name().equalsIgnoreCase(itemCategory.getSelectedItem().toString()))
                        .collect(Collectors.toList())
                        .listIterator()
                        .next();

        apiCallFunctions.addNewItem(itemName.getText().toString(), itemBarcode.getText().toString(), category.getId());
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onUpdateItemInfoClick(View v) {
        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        EditText itemID = findViewById(R.id.item_management_id);
        EditText itemName = findViewById(R.id.create_item_title);
        EditText itemBarcode = findViewById(R.id.create_item_barcode);

        final Spinner itemCategory = this.findViewById(R.id.create_item_category_list);

        Category category =
                categories.stream()
                        .filter(category1 -> category1.getCategory_name().equalsIgnoreCase(itemCategory.getSelectedItem().toString()))
                        .collect(Collectors.toList())
                        .listIterator()
                        .next();

        Item item = new Item(Integer.parseInt(itemID.getText().toString()), itemName.getText().toString(), category);
        apiCallFunctions.updateItem(item, itemBarcode.getText().toString());
        finish();
    }

    public void onDeleteItemInfoClick(View v) {
        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        EditText itemID = findViewById(R.id.item_management_id);
        apiCallFunctions.deleteItem(Integer.parseInt(itemID.getText().toString()));
        finish();
    }
}
