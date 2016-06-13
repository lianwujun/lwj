package zysq.cxsw.com.cn.ckzj.login;

import android.app.AlertDialog;
import android.content.Intent;

import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import zysq.cxsw.com.cn.ckzj.R;


public class RegisterActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private EditText code;
    private String  getCode;
    private EditText name;
    private String  getName;
    private EditText password;
    private String  getPassword;
    private EditText conformPassword;
    private String  getConformPassword;
    private EditText telephone;
    private String  getTelephone;
    private TextView picture;
    private Bitmap getPicture;
    private Bitmap setPicture;
    private TextView checkName;
    private TextView checkPassword;
    private TextView checkConfirmPassword;
    private TextView checkTelephone;
    private TextView checkCode;
    private Uri uri;
    private Bitmap bitmap;
    /*相册请求码*/
    public static final int PICTURE_REQUEST_CODE = 111;
    /*相机请求码*/
    public static final int CAMERA_REQUEST_CODE = 222;
    /*裁剪照片请求码*/
    public static final int CUT_IMAGE_CODE = 333;
    private TextView camera;
    private CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*初始化*/
        requestQueue= Volley.newRequestQueue(this);
        code=(EditText)findViewById(R.id.code);
        name= (EditText) findViewById(R.id.name);
        password= (EditText) findViewById(R.id.password);
        conformPassword= (EditText) findViewById(R.id.conformPassword);
        telephone= (EditText) findViewById(R.id.telephone);

        checkCode= (TextView) findViewById(R.id.checkCode);
        checkName= (TextView) findViewById(R.id.checkName);
        checkPassword= (TextView) findViewById(R.id.conformPassword);
        checkConfirmPassword= (TextView) findViewById(R.id.checkConfirmPassword);
        checkTelephone= (TextView) findViewById(R.id.checkTelephone);
        checkCode= (TextView) findViewById(R.id.checkCode);

        /*circleImageView = (CircleImageView) this.findViewById(R.id.picture);*/
        /*获取数据*/
        getName=name.getText().toString().trim();
        getPassword=password.getText().toString().trim();
        getConformPassword=conformPassword.getText().toString().trim();
        getTelephone=telephone.getText().toString().trim();

        /*校验*/

        if(getName.equals("")&&getName.length()<3){
            checkName.setText("格式不对");
            return;
        }
        if(password.equals("")&&getPassword.length()<6){
            checkPassword.setText("格式不对");
            return;
        }
        if(conformPassword.equals("")){
            checkConfirmPassword.setText("格式不对");
            return;
        }else if(!conformPassword.equals(checkConfirmPassword)){
            checkConfirmPassword.setText("两次密码不一样");
            return;
        }
        if (getTelephone.equals("")&&getTelephone.length()!=11){
            checkTelephone.setText("格式不对");
            return;
        }


    }
    /*获取验证码*/
    public void getCode(View view) {
        getCode=Math.round(Math.random()*10000)+"";
        code.setText(getCode);

    }

    /*上传图片*/
    public void addPicture(View view) {
        /*弹出选择对话框（相册或者相机）*/
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        /*自定义对话框*/
        View dialogView = LayoutInflater.from(this).inflate(R.layout.image_choose_layout, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.show();
        picture = (TextView) dialogView.findViewById(R.id.picture);
        /*点击相册*/
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                pictureIntent.setType("image/*");
                startActivityForResult(pictureIntent, PICTURE_REQUEST_CODE);
                dialog.dismiss();
            }
        });
        camera = (TextView) dialogView.findViewById(R.id.camera);
        /*点击相机*/
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = getImageUri();
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "请插入SD卡", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PICTURE_REQUEST_CODE:
                    if(data != null){
                        resizeImage(data.getData());
                    }else{
                        Toast.makeText(getApplicationContext(), "图片出现错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    resizeImage(uri);
                    break;
                case CUT_IMAGE_CODE:
                    if(data != null){
                        showResizeImage(data);
                    }
                    break;
            }
        }else{
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*显示图片*/
    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            bitmap = extras.getParcelable("data");
            circleImageView.setImageBitmap(bitmap);
        }
    }
    /*获取图片的uri*/
    public Uri getImageUri(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String imageName = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        imageName = df.format(new Date());
        file = new File(file,imageName+"jpg");
        return Uri.fromFile(file);
    }
    /*裁剪图片*/
    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CUT_IMAGE_CODE);
    }

    /*注册*/
    public void register(View view) {
        /*验证验证码*/
        if(code.getText().toString().trim().equals(getCode)){

        }else{
            Toast.makeText(this,"验证码不正确", Toast.LENGTH_SHORT);
        }


    }


}
