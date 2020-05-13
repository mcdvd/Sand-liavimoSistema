package com.example.warehouse.forms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehouse.R;
import com.example.warehouse.apiCalls.ApiCallFunctions;
import com.example.warehouse.objects.Attribute;
import com.example.warehouse.objects.Barcode;
import com.example.warehouse.objects.Category;
import com.example.warehouse.objects.Item;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class CategoryCreate extends Activity {
    private List<Category> categories;
    HashMap<String, Integer> spinnerMap = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.category_create);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Category categoryFormList = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("categoryInformation")) {
                categoryFormList = (Category) intent.getSerializableExtra("categoryInformation");
            }
        }
        if (categoryFormList != null) {
            findViewById(R.id.category_information_create_button).setVisibility(View.GONE);
            findViewById(R.id.category_information_update_button).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.category_information_create_button).setVisibility(View.VISIBLE);
            findViewById(R.id.category_information_update_button).setVisibility(View.GONE);
            findViewById(R.id.category_information_delete_button).setVisibility(View.INVISIBLE);
        }
        findViewById(R.id.create_category_id).setVisibility(View.GONE);

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

        String[] categoryArray = new String[categories.size() + 1];
        ListIterator<Category> iterator = categories.listIterator();

        spinnerMap.put("-", 0);
        categoryArray[0] = "-";
        int i = 1;
        while (iterator.hasNext()) {
            Category iteratorNext = iterator.next();
            spinnerMap.put(iteratorNext.getCategory_name(), iteratorNext.getId());
            categoryArray[i] = iteratorNext.getCategory_name();
            i++;
        }

        Spinner categorySpinner = findViewById(R.id.parent_category_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categoryArray);
        categorySpinner.setAdapter(adapter);

        if (categoryFormList != null) {
            EditText categoryID = this.findViewById(R.id.create_category_id);
            categoryID.setText(categoryFormList.getId().toString());

            if (categoryFormList.getCategory_name() != null) {
                EditText categoryName = this.findViewById(R.id.create_category_title);
                categoryName.setText(categoryFormList.getCategory_name());
            }

            if (categoryFormList.getParentID() != null) {
                categorySpinner.setSelection(categoryFormList.getParentID());
            }
        }
    }

    public void onCategoryManagementCreateClick(View v) {
        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        EditText categoryName = this.findViewById(R.id.create_category_title);
        final Spinner parentCategory = this.findViewById(R.id.parent_category_list);

        int categoryIndex = spinnerMap.get(parentCategory.getSelectedItem().toString());
        if (categoryIndex == 0) {
            apiCallFunctions.addNewCategory(categoryName.getText().toString(), null);
        } else {
            apiCallFunctions.addNewCategory(categoryName.getText().toString(), categoryIndex);
        }
        finish();
    }

    public void onDeleteCategoryInfoClick(View v){
        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        EditText categoryID = this.findViewById(R.id.create_category_id);
        apiCallFunctions.deleteCategory(Integer.parseInt(categoryID.getText().toString()));
        finish();
    }

    public void onUpdateCategoryInfoClick(View v){
        EditText categoryID = this.findViewById(R.id.create_category_id);
        EditText categoryName = this.findViewById(R.id.create_category_title);
        Spinner categoryParentId = this.findViewById(R.id.parent_category_list);

        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        Category updateingCategory =
                new Category(Integer.parseInt(
                        categoryID.getText().toString()),
                        categoryName.getText().toString(),
                        categoryParentId.getSelectedItemPosition()
                );
        apiCallFunctions.updateCategory(updateingCategory);
        finish();
    }
}
