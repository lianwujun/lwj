package zysq.cxsw.com.cn.ckzj.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Method;

import zysq.cxsw.com.cn.ckzj.R;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText editText_1;
    private EditText editText_2;
    private EditText editText_3;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        editText_1=(EditText)findViewById(R.id.password);
        editText_2=(EditText)findViewById(R.id.conformPassword);
        editText_3=(EditText)findViewById(R.id.telephone);
        requestQueue= Volley.newRequestQueue(this);
    }

    public void forgetPassword(View view) {
        String password=editText_1.getText().toString();
        String conformPassword=editText_2.getText().toString();
        final String telephone=editText_3.getText().toString();
        StringRequest stringRequest = null;
        if(password.equals(conformPassword)){
            String url="http://192.168.1.156:8080/Shopping_CKZJ/ForgetPasswordServlet";
            stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if(telephone.equals("手机号码一致")){
                        Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.setClass(getApplication(),LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(),"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
        }
        requestQueue.add(stringRequest);
    }


}
