package zysq.cxsw.com.cn.ckzj.login;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import zysq.cxsw.com.cn.ckzj.MainActivity;
import zysq.cxsw.com.cn.ckzj.R;

public class LoginActivity extends AppCompatActivity {
    private  EditText editText_1;
    private EditText editText_2;
    private String name;
    private String password;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_login);
        editText_1=(EditText)findViewById(R.id.user);
        editText_2=(EditText)findViewById(R.id.password);
        requestQueue= Volley.newRequestQueue(this);


    }

    public void login(View view) {
        name=editText_1.getText().toString().trim();
        password=editText_2.getText().toString().trim();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("name",name);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url="http://192.168.1.156:8080/Shopping_CKZJ/LoginServlet";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("1",jsonObject.toString());
                try {
                    if(jsonObject.getString("biaoshi").equals("登录成功")){
                        Toast.makeText(getApplication(),jsonObject.toString(),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.setClass(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("101111111",volleyError.toString());
                Toast.makeText(getApplication(),"登录失败",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void register(View view) {
        Intent intent=new Intent();
        intent.setClass(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void forgetPassword(View view) {
        Intent intent=new Intent();
        intent.setClass(this,ForgetPasswordActivity.class);
        startActivity(intent);
    }
}
