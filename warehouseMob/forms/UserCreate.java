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
import com.example.warehouse.objects.Category;
import com.example.warehouse.objects.User;

import java.util.HashMap;

public class UserCreate extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.user_create);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        User userFromList = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("userInformation")) {
                userFromList = (User) intent.getSerializableExtra("userInformation");
            }
        }
        if (userFromList != null) {
            findViewById(R.id.user_information_create_button).setVisibility(View.GONE);
            findViewById(R.id.user_information_update_button).setVisibility(View.VISIBLE);
            EditText username = this.findViewById(R.id.create_user_username);
            username.setText(userFromList.getUsername());
            EditText password = this.findViewById(R.id.create_user_password);
            password.setText(userFromList.getPassword());
            EditText userID = this.findViewById(R.id.create_user_id);
            userID.setText(userFromList.getId().toString());
        } else {
            findViewById(R.id.user_information_create_button).setVisibility(View.VISIBLE);
            findViewById(R.id.user_information_update_button).setVisibility(View.GONE);
            findViewById(R.id.user_information_delete_button).setVisibility(View.INVISIBLE);
        }
        findViewById(R.id.create_user_id).setVisibility(View.GONE);

        String[] userRolesArray = {"Administratorius", "Sandėlininkas", "Pardavėjas"};

        Spinner userSpinner = findViewById(R.id.create_user_role);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, userRolesArray);
        userSpinner.setAdapter(adapter);
        if (userFromList != null) {
            userSpinner.setSelection(userFromList.getRole());
        }
    }

    public void onUserManagementCreateClick(View v) {
        EditText username = this.findViewById(R.id.create_user_username);
        EditText password = this.findViewById(R.id.create_user_password);
        Spinner userRole = this.findViewById(R.id.create_user_role);

        String usernameValue = username.getText().toString();
        String passwordValue = password.getText().toString();
        Integer userRoleValue = userRole.getSelectedItemPosition();

        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        User user = new User(null, usernameValue, passwordValue, null, null, userRoleValue);
        apiCallFunctions.addNewUser(user);
        Intent intent = new Intent(this, UserList.class);
        startActivity(intent);
        finish();
    }

    public void onDeleteUserInfoClick(View v) {
        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        EditText userID = this.findViewById(R.id.create_user_id);
        apiCallFunctions.deleteUser(Integer.parseInt(userID.getText().toString()));
        Intent intent = new Intent(this, UserList.class);
        startActivity(intent);
        finish();
    }

    public void onUpdateUserInfoClick(View v) {
        EditText userID = this.findViewById(R.id.create_user_id);
        EditText username = this.findViewById(R.id.create_user_username);
        EditText password = this.findViewById(R.id.create_user_password);
        Spinner userRole = this.findViewById(R.id.create_user_role);

        Integer usernameIDValue = Integer.parseInt(userID.getText().toString());
        String usernameValue = username.getText().toString();
        String passwordValue = password.getText().toString();
        Integer userRoleValue = userRole.getSelectedItemPosition();

        ApiCallFunctions apiCallFunctions = new ApiCallFunctions();
        User updateingUser = new User(usernameIDValue, usernameValue, passwordValue, null, null, userRoleValue);
        apiCallFunctions.updateUser(updateingUser);
        Intent intent = new Intent(this, UserList.class);
        startActivity(intent);
        finish();
    }
}
