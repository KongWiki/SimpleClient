package com.example.weikunkun.democlient;


import android.arch.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private EditText userNameEdit, passwordEdit;
    private TextView re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        re = findViewById(R.id.respnose);
        userNameEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        login  = findViewById(R.id.login);
        login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String userName = userNameEdit.getEditableText().toString();
        String passWord = passwordEdit.getEditableText().toString();
        if(userName.equals("") || passWord.equals("")){
            Toast.makeText(MainActivity.this,"不能为空", Toast.LENGTH_SHORT).show();
        }
        switch(v.getId()){
            case R.id.login:
                String url = "http://10.0.2.2:5000/user";
                getCheckFromServer(url,userName,passWord);
//                sendRequestWithOkHttp();
                break;
             default:
                 break;
        }

    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://www.baidu.com")
                            .build();
                    Response response = client.newCall(request).execute();
                    String respnsesData = response.body().string();
                    showRespone(respnsesData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showRespone(final String respnse){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                re.setText(respnse);
            }
        });

    }

    private void getCheckFromServer(final String url, final String userName, final String passWord) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    OkHttpClient client = new OkHttpClient();
////        FormBody.Builder formBuilder = new FormBody.Builder();
////        formBuilder.add("username", userName);
////        formBuilder.add("password", passWord);
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("username", userName)
//                            .add("password", passWord)
//                            .build();
//
//                    Request request = new Request.Builder()
//                            .url(url)
//                            .post(requestBody)
//                            .build();
//
//                    Response response = client.newCall(request).execute();
//                    String respnsesData = response.body().string();
//                    showRespone(respnsesData);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userName);
        formBuilder.add("password", passWord);
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback()
        {
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String res = response.body().string();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (res.equals("0"))
                        {
                            AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
                            dialog.setMessage("无此账号,请先注册");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            dialog.show();
                        }
                        else if(res.equals("1"))

                        {
                            AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
                            dialog.setMessage("密码不正确");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            dialog.show();
                        }
                        else//成功
                        {
                            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                            startActivity(intent);
                        }

                    }
                });
            }

            @Override
            public void onFailure(final Call call, IOException e)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
                        dialog.setMessage("迷之错误");

                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();

                    }
                });

            }


        });
    }




}

