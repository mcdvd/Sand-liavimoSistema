package com.example.warehouse.forms;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.warehouse.MultiLevelList.Itemer;
import com.example.warehouse.MultiLevelList.MyAdapter;
import com.example.warehouse.R;
import com.example.warehouse.apiCalls.ApiCallFunctions;
import com.example.warehouse.objects.Category;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryList extends Activity {

    private List<Category> categories;
    private List<Itemer> structuredCategories;
    private HashMap<String, Category> arrayIDtoDatabaseName = new HashMap<String, Category>();

    private Integer itemPosition = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.category_list);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras!= null && extras.containsKey("categoryInformation")) {
            intent.removeExtra("categoryInformation");
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

        itemPosition = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            structuredCategories = (List<Itemer>) getStructuredCategories(0, categories);
        }

        final MultiLevelRecyclerView multiLevelRecyclerView = (MultiLevelRecyclerView) findViewById(R.id.rv_list);
        multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter myAdapter = new MyAdapter(this, structuredCategories, multiLevelRecyclerView, arrayIDtoDatabaseName);
        multiLevelRecyclerView.setAdapter(myAdapter);

        //If you are handling the click on your own then you can
        // multiLevelRecyclerView.removeItemClickListeners();
        multiLevelRecyclerView.setToggleItemOnClick(false);
        multiLevelRecyclerView.setAccordion(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<?> getStructuredCategories(int levelNumber, List<Category> categoryLister) {
        List<RecyclerViewItem> categoryList = new ArrayList<>();
        class SortByParentID implements Comparator<Category> {
            public int compare(Category o1, Category o2) {
                if (o1.getParentID() == null) {
                    return (o2.getParentID() == null) ? 0 : -1;
                }
                if (o2.getParentID() == null) {
                    return 1;
                }
                return o1.getParentID().compareTo(o2.getParentID());
            }
        }

        Collections.sort(categoryLister, new SortByParentID());

        for (Category category : categoryLister) {
            if (category.getParentID() == null || levelNumber != 0) {
                Itemer itemerCat = new Itemer(levelNumber);
                String namePrefix = "";
                for (int i = 0; i < levelNumber; i++) {
                    namePrefix += "-";
                }
                arrayIDtoDatabaseName.put(category.getCategory_name(), category);
                itemPosition++;

                itemerCat.setText(namePrefix + " " + category.getCategory_name());
                List<Category> listChilds =
                        categories.
                                stream().
                                filter(category1 -> category1.getParentID() != null && category1.getParentID().equals(category.getId())).
                                collect(Collectors.toList());
                if (listChilds.size() != 0) {
                    itemerCat.addChildren((List<RecyclerViewItem>) getStructuredCategories(levelNumber + 1, listChilds));
                }
                categoryList.add(itemerCat);
            }
        }

        return categoryList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d("TESTr", String.valueOf(arrayIDtoDatabaseName.get(id)));


        return super.onOptionsItemSelected(item);
    }


    public void onItemCreateInManagementButtonClick(View view) {
        Intent intent = new Intent(this, CategoryCreate.class);
        startActivity(intent);
    }
}
