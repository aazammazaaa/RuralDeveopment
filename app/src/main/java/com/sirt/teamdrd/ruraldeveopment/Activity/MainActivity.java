package com.sirt.teamdrd.ruraldeveopment.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sirt.teamdrd.ruraldeveopment.Activity.Util.Constant;
import com.sirt.teamdrd.ruraldeveopment.Activity.Util.SharedPrefrencesManager;
import com.sirt.teamdrd.ruraldeveopment.Activity.Util.app.RuralDevelopment;
import com.sirt.teamdrd.ruraldeveopment.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity {
    JSONObject loginJsonObj;
    Button btnLogin;
    TextView txtSignUp;
    EditText edUsername;
    EditText edPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        if(!SharedPrefrencesManager.getStringPreference(Constant.USER_ID, "").equals("")){
            Intent in = new Intent(MainActivity.this, FrontActivity.class);

            startActivity(in);
            this.finish();

        }
        edUsername = (EditText) findViewById(R.id.username);
        edPassword = (EditText) findViewById(R.id.password);
        loginJsonObj = new JSONObject();


        btnLogin = (Button) findViewById(R.id.loginbutton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(edUsername.getText().toString(), edPassword.getText().toString(), loginJsonObj);

            }
        });
        txtSignUp= (TextView) findViewById(R.id.signup_textview);
        txtSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(in);
            }
        });
    }

    @Override
    public void showProgress(Boolean show, String tag) {
        return;
    }

    int status = 0;
    String msg = null;
    String userid = null;

    @Override
    public void onSuccess(JSONObject response, String tag) {
        //Toast.makeText(MainActivity.this, response.toString(),Toast.LENGTH_LONG).show();
        Log.e("msg",response.toString());
       // if(tag.equals(Constant.LOGIN_RURAL)){
            try {
                status = response.getInt("status");
                msg = response.getString("msg");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(status == 402){
                //Toast.makeText(MainActivity.this, "w")
                //edUsername.setError(getResources().getString(R.string.user_not_exists));
                Toast.makeText(MainActivity.this, getResources().getString(R.string.user_not_exists), Toast.LENGTH_SHORT).show();

            }
            if(status == 401){
                //Toast.makeText(MainActivity.this, "w")
                //edPassword.setError(getResources().getString(R.string.wrong_user_error));
                Toast.makeText(MainActivity.this, getResources().getString(R.string.wrong_user_error), Toast.LENGTH_SHORT).show();
            }
            if(status == 200){
                //Toast.makeText(MainActivity.this, "w")

                try {
                    SharedPrefrencesManager.setPreference(Constant.USER_NAME, edUsername.getText().toString());
                    SharedPrefrencesManager.setPreference(Constant.USER_PASSWORD, edPassword.getText().toString());
                    SharedPrefrencesManager.setPreference(Constant.USER_ID, response.getString("user_id").toString());
                    SharedPrefrencesManager.setPreference(Constant.USER_EXPERTISELVL, response.getString("user_expertieslvl").toString());
                    SharedPrefrencesManager.setPreference(Constant.USER_TYPE, response.getString("user_type").toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent in = new Intent(MainActivity.this, FrontActivity.class);
                startActivity(in);
                this.finish();
            }
            if(status == 400){
                Toast.makeText(MainActivity.this, "Server Error",Toast.LENGTH_LONG).show();


            }

        //}
        return;
    }

    @Override
    public void onError(VolleyError error, String message, String tag) {
        Toast.makeText(MainActivity.this, "message Error", Toast.LENGTH_SHORT).show();
    }

    private void login(String userName, String password, JSONObject loginJsonObj){

        if(!RuralDevelopment.getInstance().isNetworkAvailable()){
            Toast.makeText(MainActivity.this, "No connection Available", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userName.isEmpty() && password.isEmpty() ){
            //edUsername.setError(getResources().getString(R.string.null_user_error));
            //edPassword.setError(getResources().getString(R.string.null_password_error));
            Toast.makeText(MainActivity.this, getResources().getString(R.string.null_user_error), Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, getResources().getString(R.string.null_password_error), Toast.LENGTH_SHORT).show();
        }else  if(userName.isEmpty() ){
            //edUsername.setError(getResources().getString(R.string.null_user_error));
            Toast.makeText(MainActivity.this, getResources().getString(R.string.null_user_error), Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty() ){
            //edPassword.setError(getResources().getString(R.string.null_password_error));
            Toast.makeText(MainActivity.this, getResources().getString(R.string.null_password_error), Toast.LENGTH_SHORT).show();
        }else{

            try {
                loginJsonObj.put("login_username",userName);
                loginJsonObj.put("login_password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadJsonData(Constant.LOGIN_RURAL, loginJsonObj.toString(),Constant.LOGIN_RURAL);
        }
    }
}
