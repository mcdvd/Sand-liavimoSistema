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
import com.example.warehouse.objects.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class UserList extends Activity {

    private List<User> users;
    private HashMap<Integer, Integer> arrayIDtoDatabaseID = new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.user_list);
        super.onCreate(savedInstanceState);

        ListView listView = this.findViewById(R.id.userList);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
                try {
                    users = apiCallFunctions.getUsers();
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

        String[] usersArray = new String[users.size()];
        ListIterator<User> usersIterator = users.listIterator();

        int i = 0;
        while (usersIterator.hasNext()) {
            User nextUser = usersIterator.next();
            String postfix = "";
            if (nextUser.getRole() == 0) {
                postfix = " (Administratorius)";
            } else if (nextUser.getRole() == 1) {
                postfix = " (Sandėlininkas)";
            } else if (nextUser.getRole() == 2) {
                postfix = " (Pardavėjas)";
            }
            arrayIDtoDatabaseID.put(i, nextUser.getId());
            usersArray[i] = nextUser.getUsername() + postfix;
            i++;
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                usersArray
        );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User result = users.stream().filter(attribute -> arrayIDtoDatabaseID.get(position).equals(attribute.getId())).collect(Collectors.toList()).listIterator().next();
                Intent intent = new Intent(UserList.this, UserCreate.class);
                intent.putExtra("userInformation", result);
                startActivity(intent);
            }
        });
    }

    public void onUserCreateInManagementButtonClick(View v) {
        Intent intent = new Intent(this, UserCreate.class);
        startActivity(intent);
    }
}
