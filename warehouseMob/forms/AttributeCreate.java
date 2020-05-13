package com.example.warehouse.forms;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehouse.R;
import com.example.warehouse.apiCalls.ApiCallFunctions;
import com.example.warehouse.objects.Attribute;
import com.example.warehouse.objects.AttributeValue;
import com.example.warehouse.objects.Barcode;
import com.example.warehouse.objects.Item;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Objects;

public class AttributeCreate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.attribute_create);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Attribute attributeFromList = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("attributeInformation")) {
                attributeFromList = (Attribute) intent.getSerializableExtra("attributeInformation");
            }
        }
        if (attributeFromList != null) {
            findViewById(R.id.attribute_information_create_button).setVisibility(View.GONE);
            findViewById(R.id.attribute_information_update_button).setVisibility(View.VISIBLE);
            addDataToTextFields(attributeFromList);
        } else {
            findViewById(R.id.attribute_information_create_button).setVisibility(View.VISIBLE);
            findViewById(R.id.attribute_information_update_button).setVisibility(View.GONE);
            findViewById(R.id.attribute_information_delete_button).setVisibility(View.INVISIBLE);
        }
        findViewById(R.id.create_attribute_id).setVisibility(View.GONE);
    }

    public void onAttributeManagementCreateClick(View v) {
        EditText attributeName = this.findViewById(R.id.create_attribute_title);
        EditText attributeLOV = this.findViewById(R.id.create_attribute_lov);

        String attrName = attributeName.getText().toString();
        AttributeValue[] lovs = new AttributeValue[50];
        String[] lovsStringArray = attributeLOV.getText().toString().split(";");
        int i = 0;
        for (String value : lovsStringArray) {
            AttributeValue newAttributeValue = new AttributeValue(null, null, value);
            lovs[i] = newAttributeValue;
        }
        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        apiCallFunctions.addNewAttribute(attrName, lovs);
        finish();
    }

    private void addDataToTextFields(Attribute attribute) {
        EditText attributeID = this.findViewById(R.id.create_attribute_id);
        EditText attributeName = this.findViewById(R.id.create_attribute_title);
        EditText attributeLOV = this.findViewById(R.id.create_attribute_lov);

        String lovString = "";
        if (attribute.getAttribute_lov() != null) {
            for (AttributeValue attributeValue : attribute.getAttribute_lov()) {
                if (lovString.equals("")) {
                    lovString += attributeValue.getValue();
                } else {
                    lovString += ";" + attributeValue.getValue();
                }
            }
        }

        attributeID.setText(attribute.getId().toString());
        attributeName.setText(attribute.getAttribute_name());
        attributeLOV.setText(lovString);
    }

    public void onDeleteAttributeInfoClick(View v){
        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        EditText attributeID = this.findViewById(R.id.create_attribute_id);
        apiCallFunctions.deleteAttribute(Integer.parseInt(attributeID.getText().toString()));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onUpdateAttributeInfoClick(View v) {
        EditText attributeName = this.findViewById(R.id.create_attribute_title);
        EditText attributeLOV = this.findViewById(R.id.create_attribute_lov);
        EditText attributeIDView = this.findViewById(R.id.create_attribute_id);

        Integer attributeID = Integer.parseInt(attributeIDView.getText().toString());

        String attrName = attributeName.getText().toString();
        AttributeValue[] lovs = new AttributeValue[50];
        String[] lovsStringArray = attributeLOV.getText().toString().split(";");
        int i = 0;
        for (String value : lovsStringArray) {
            AttributeValue newAttributeValue = new AttributeValue(null, attributeID, value);
            lovs[i] = newAttributeValue;
            i++;
        }

        lovs = Arrays.stream(lovs).filter(Objects::nonNull).toArray(AttributeValue[]::new);

        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        Attribute updateingAttribute = new Attribute(attributeID, attrName, lovs);
        apiCallFunctions.updateAttribute(updateingAttribute);
        finish();
    }
}
