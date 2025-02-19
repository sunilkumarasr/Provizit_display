package com.provizit.provizitscheduler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.provizitscheduler.Services.CompanyData;
import com.provizit.provizitscheduler.Services.DataManger;
import com.provizit.provizitscheduler.Services.Model;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button login;

    //privacy policy
    LinearLayout linear_privacy;
    CheckBox checkbox;

    SharedPreferences.Editor editor1;
    SharedPreferences sharedPreferences1;
    AESUtil aesUtil;
    String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        login = findViewById(R.id.login);
        email = findViewById(R.id.userid);
        password = findViewById(R.id.password);
        linear_privacy = findViewById(R.id.linear_privacy);
        checkbox = findViewById(R.id.checkbox);
        sharedPreferences1 =  MainActivity.this.getSharedPreferences("Provizit_Schedular", MODE_PRIVATE);
        editor1 = sharedPreferences1.edit();
        aesUtil = new AESUtil(MainActivity.this);

        //privacy policy
        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                checkbox.setChecked(checkbox.isChecked());
            }else {
                checkbox.setChecked(false);
            }
        });
        linear_privacy.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(),PrivacyPolicyActivity.class);
            startActivity(i);
        });

        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (email.getText().toString().equals("")) {
                email.setError("Required");
                email.requestFocus();
            }
        });

        login.setOnClickListener(v -> {
//

//                if (company_id == null) {
//
//                } else
             if (email.getText().toString().equals("")) {
                 Toast.makeText(getApplicationContext(),"Enter Email",Toast.LENGTH_SHORT).show();
            } else if (password.getText().toString().equals("")) {
                 Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
            } else {
                 if (checkbox.isChecked()){
                     JsonObject gsonObject = new JsonObject();
                     JSONObject jsonObj_ = new JSONObject();
                     try {
                         jsonObj_.put("val", email.getText().toString().trim().toLowerCase());
                         jsonObj_.put("type", "email");
//                           jsonObj_.put("pwd", password.getText().toString().trim());
                         pwd = aesUtil.encrypt(password.getText().toString().trim(),email.getText().toString().trim().toLowerCase());
                         jsonObj_.put("password",pwd );

                         JsonParser jsonParser = new JsonParser();
                         gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     Log.e("Password",aesUtil.decrypt(email.getText().toString().trim(),pwd));
                     Login(gsonObject);
//                       progress.show();
                 }else {
                     Toast.makeText(getApplicationContext(),"Please Agree & Continue",Toast.LENGTH_SHORT).show();
                 }
            }
        });




    }
    private void Login(JsonObject jsonObject) {

        DataManger dataManager = DataManger.getDataManager();
        dataManager.userLogin(new Callback<Model>() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                final Model model = response.body();
//               progressDialog.dismiss();

//                progress.dismiss();
                if (model != null) {
//                                                approve_btn.setEnabled(true);
                    Integer statuscode = model.getResult();
                    Integer successcode = 200;
                    Integer failurecode = 201;
                    Integer not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                        new AlertDialog.Builder(MainActivity.this)
//                                    .setTitle("ACCESS DENIED")
                                .setMessage( "Presently, this app is accessible by only the enterprise users of PROVIZIT.\n" +
                                        "\n" +
                                                "We couldn’t find you as an enterprise user.\n" +
                                                "\n" +
                                                "You may contact your organization or write to info@provizit.com for more information." )
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    } else if (statuscode.equals(not_verified)) {
//                        progressBar.setVisibility(View.GONE);
                        new AlertDialog.Builder(MainActivity.this)
//                                    .setTitle("ACCESS DENIED")
                                .setMessage( "Invalid Password" )
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    } else if (statuscode.equals(successcode)) {

                        CompanyData items = new CompanyData();
                        items = model.getItems();
                            editor1.putInt("isloginProvizit", 1);
                            editor1.putString("comp_id", items.getComp_id());
                            editor1.putString("email", email.getText().toString().trim().toLowerCase());
                            editor1.apply();
                            Intent intent = new Intent(MainActivity.this, MeetingsRoomMeetings.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
//                                            progressDialog.dismiss();


            }
        },MainActivity.this, jsonObject);

//
    }
    public void incorrectTimePopup(){
       new AlertDialog.Builder(MainActivity.this)
                .setTitle("ACCESS DENIED")
                .setMessage( "The device date / time is incorrect.\n" +
                        "Please set it to \"Automatic\" and try again." )
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}