package com.ourbuaa.buaahelper;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Button button;
    private EditText Username, Password;
    private ProgressDialog pdiaLog;
    private String username, password;

    private static SQLiteUtils SQLiteLink;

    public static void setSQLiteLink(SQLiteUtils link) {
        SQLiteLink = link;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = (Button) findViewById(R.id.LoginButton);
        Username = (EditText) findViewById(R.id.LoginUsername);
        Password = (EditText) findViewById(R.id.LoginPassword);
        Button RegisterButton = (Button)findViewById(R.id.RegisterButton);

        Button OfflinModeButton = (Button)findViewById(R.id.OfflineModeBtn);
        Button AutoLoginButton = (Button)findViewById(R.id.AutoLoginButton);
        OfflinModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ClientUtils.getLog_state()) {

                    String t = SQLiteLink.GetLastUserNToken();
                    try {
                        JSONObject j = new JSONObject(t);
                        String user = j.getString("user");
                        ClientUtils.setUser(user);
                        Toast.makeText(getApplicationContext(), "正在离线查看历史消息", Toast.LENGTH_LONG).show();
                        LoginActivity.this.finish();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "离线登陆失败，请确认此前在线登陆过应用", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    //android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
                    //System.exit(0);
                }
            }
        });
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.ourbuaa.com/mobile/register");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        AutoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testToken();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void testToken() {
        pdiaLog = ProgressDialog.show(this, "登录", "验证登录中...", true, false);
        TestTokenTask testTokenTask = new TestTokenTask();
        testTokenTask.execute();

    }

    class TestTokenTask extends AsyncTask<String, Void, StringBuffer> {
        boolean LoginFlag = true;

        @Override
        protected StringBuffer doInBackground(String... strings) {
            StringBuffer errormsg = new StringBuffer("");


            Boolean relogin = true;
            try {
                String t = SQLiteLink.GetLastUserNToken();
                if (t != null) {
                    JSONObject lastuser = new JSONObject(t);

                    //Log.d("TestAccess_token", "" + ClientUtils.TestToken(lastuser.getString("access_token")));
                    if (ClientUtils.TestToken(lastuser.getString("access_token"))) {
                        ClientUtils.setAccess_token(lastuser.getString("access_token"));
                        ClientUtils.setUser(lastuser.getString("user"));
                        ClientUtils.setLog_state(true);
                        relogin = false;
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }

            LoginFlag = relogin;
            return errormsg;
        }

        @Override
        protected void onPostExecute(StringBuffer s) {
            pdiaLog.dismiss();

            if (LoginFlag) {
                Toast.makeText(getApplicationContext(), "Error:登录状态失效，请重新登录", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "恢复登录成功", Toast.LENGTH_LONG).show();
                attemptStartService();
                LoginActivity.this.finish();

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
    private void attemptLogin() {
        pdiaLog = ProgressDialog.show(this, "登录", "请稍等，正在登录中...", true, false);
        username = Username.getText().toString();
        password = Password.getText().toString();
        LoginTask logintask = new LoginTask();
        logintask.execute();
    }

    class LoginTask extends AsyncTask<String, Void, StringBuffer> {
        boolean LoginFlag = true;

        @Override
        protected StringBuffer doInBackground(String... strings) {
            StringBuffer errormsg = new StringBuffer("");
            LoginFlag = DataUtils.TemptLogin(username, password, errormsg);
            return errormsg;
        }

        @Override
        protected void onPostExecute(StringBuffer s) {
            pdiaLog.dismiss();

            if (!LoginFlag) {
                StringBuffer tmp = null;
                if (s == null) tmp = new StringBuffer("UNKNOWN");
                else tmp = new StringBuffer(s.toString());
                Toast.makeText(getApplicationContext(), "Error:" + tmp.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Login Successfully!", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                //intent.putExtra("Username",username);
                //intent.putExtra("Password",password);
                //startActivity(intent);
                attemptStartService();

                LoginActivity.this.finish();

            }
        }
    }

    private void attemptStartService() {
        if (ClientUtils.getLog_state()) {
            Log.d("Service","Start!");
            Intent intent = new Intent(LoginActivity.this, NotificationUpdateQueryService.class);
            LoginActivity.this.startService(intent);
        }
    }
}
