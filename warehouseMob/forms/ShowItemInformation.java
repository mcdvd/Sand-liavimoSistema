package com.example.warehouse.forms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.warehouse.R;
import com.example.warehouse.apiCalls.ApiCallFunctions;
import com.example.warehouse.objects.Barcode;
import com.example.warehouse.objects.Item;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ShowItemInformation extends Activity {
    private LinearLayout itemInformationRows;

    private List<EditText> viewEditTexts = new ArrayList<EditText>();

    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.show_item_information);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Item item = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("barcodeInformation")) {
                Barcode barcodeInformation = (Barcode) intent.getSerializableExtra("barcodeInformation");
                item = barcodeInformation.getItem();
            }
            if (extras.containsKey("itemInformation")) {
                item = (Item) intent.getSerializableExtra("itemInformation");
            }
        }
        itemInformationRows = this.findViewById(R.id.itemInformationRows);
        if (item != null) {
            createDataRow(item);
        }
    }

    private void createDataRow(Object item) {
        int i = 0;
        for (Field field : item.getClass().getDeclaredFields()) {
            i++;
            field.setAccessible(true);

            if(field.getName().equals("ID")){
                continue;
            }
            LinearLayout itemRow = new LinearLayout(this);
            itemRow.setOrientation(LinearLayout.HORIZONTAL);

            TextView infoColumn = new TextView(this);
            infoColumn.setText(field.getName());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1.0f;
            params.gravity = Gravity.CENTER;

            infoColumn.setLayoutParams(params);

            EditText valueColumn = new EditText(this);

            if (field.getName().equals("ItemName")) {
                viewEditTexts.add(valueColumn);
            }
            try {
                String fieldValue = field.get(item).toString();
                if (field.getName().equals("ID")) {
                    itemId = Integer.parseInt(fieldValue);
                }
                valueColumn.setText(field.get(item).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    7.0f
            );

            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );
            infoColumn.setLayoutParams(param);
            valueColumn.setLayoutParams(param2);
            itemRow.addView(infoColumn);
            itemRow.addView(valueColumn);
            itemInformationRows.addView(itemRow);
        }
    }

    public void onUpdateItemInfoClick(View v) {
/*        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        ListIterator<EditText> iterator = viewEditTexts.listIterator();
        EditText test = iterator.next();
        Log.d("test", test.getText().toString());
        apiCallFunctions.updateItem(itemId, test.getText().toString());
        finish();*/
    }

    public void onDeleteItemInfoClick(View v) {
/*        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        apiCallFunctions.deleteItem(itemId);
        finish();*/
    }
}
