package buaa.buaahelper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NoticeListFragment.OnListFragmentInteractionListener, View.OnClickListener {
    private ImageButton form, notice, query;
    private NoticeListFragment noticeFragment, FavFragment,trashFragment;
    private BlankFragment FormblankFragment, QueryblankFragment,PersonalInfoFragment,MessageFragment,SettingPasswordFragment;
    private String username, password;
    private BUAAContentProvider buaaContentProvider, buaaFavContentProvider,buaaTrashContentProvider;
    private RelativeLayout bottombar;
    public SQLiteUtils mySQLite;
    private boolean NowForm = false, NowNotice = false, NowQuery = false,NowFav = false,NowTrash = false;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private Fragment nowFragment;
    public Boolean Logining;
    private Boolean hasLogged;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //public Boolean LoginState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notice = (ImageButton) findViewById(R.id.Notice);
        form = (ImageButton) findViewById(R.id.Form);
        query = (ImageButton) findViewById(R.id.Query);

        notice.setOnClickListener(this);
        form.setOnClickListener(this);
        query.setOnClickListener(this);

        fm = getFragmentManager();
        hasLogged = false;

        //准备垃圾箱
        //buaaTrashContentProvider=DataUtils.getContentProvider(BUAAContentProvider.TRASH);


        bottombar = (RelativeLayout) findViewById(R.id.bottom_bar_main_act);

        //initialize drawer header


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mySQLite = SQLiteUtils.getInstance(MainActivity.this);
        ClientUtils.setSQLiteLink(mySQLite);
        LoginActivity.setSQLiteLink(mySQLite);
        BUAAContentProvider.setSQLiteLink(mySQLite);
        DetailActivity.setSQLiteLink(mySQLite);


       //TODO : 2017.2.16 debug it
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(intent);

        setDefaultFragment();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        //完成主界面更新,拿到数据
                        String data = (String) msg.obj;
                        TextView nav_header_name = (TextView) findViewById(R.id.nav_name);
                        TextView nav_header_department = (TextView) findViewById(R.id.nav_department);

                        if (ClientUtils.getLog_state()) {

                            if(!hasLogged){

                                hasLogged = true;
                                PersonalInfoFragment.LoadUrl("https://www.ourbuaa.com/mobile/account?access_token="+ClientUtils.getAccess_token());
                                MessageFragment.LoadUrl("https://www.ourbuaa.com/mobile/inquiry?access_token="+ClientUtils.getAccess_token());
                                //SettingPasswordFragment.LoadUrl();
                                //FormblankFragment, QueryblankFragment,PersonalInfoFragment,MessageFragment,SettingPasswordFragment;

                                try {
                                    JSONObject mJSONObject = new JSONObject(data);
                                    String name = mJSONObject.getString("name");
                                    long department = mJSONObject.getLong("department");
                                    nav_header_name.setText(name);
                                    nav_header_department.setText(DepartmentNLong.Department_Long2String(department));


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


/*
                                String getPersonalInfoUrl()  //个人信息
                                {

                                    return "https://www.ourbuaa.com/mobile/account";
                                }
                                String getRegisterUrl()  //注册
                                {
                                    return "https://www.ourbuaa.com/mobile/register";
                                }
                                String getMessageUrl()  // 留言
                                {
                                    return "https://www.ourbuaa.com/mobile/inquiry";
                                }
*/

                            }



                            //TODO: 上面两个分别是滑动侧栏的名字和所属部门，登录以后设置一下
                        } else {
                            //nav_header_name.setText("离线模式");
                            //nav_header_department.setText("");
                        }

                        break;
                    default:
                        break;
                }
            }

        };
        new Thread(new Runnable() {

            @Override
            public void run() {


                //需要数据传递，用下面方法；
                Message msg = new Message();
                if (ClientUtils.getLog_state()) msg.obj = ClientUtils.GetUserInfo();
                mHandler.sendMessage(msg);
            }

        }).start();
        buaaContentProvider.ForceFreshDataList();

    }

    private void setDefaultFragment() {
        transaction = fm.beginTransaction();

        notice.setImageResource(R.mipmap.icon_notice_blue);
        NowNotice = true;
        NowForm = false;
        NowQuery = false;
        noticeFragment = new NoticeListFragment();
        noticeFragment.setSqLiteUtils(mySQLite);
        syncFramentAndIconChange_Notice(1);
        syncFramentAndIconChange_Form(0);
        syncFramentAndIconChange_Query(0);
       // Bundle bundle = getIntent().getExtras();
        //username = bundle.getString("Username");
        //password = bundle.getString("Password");
        //Toast.makeText(this,username+" "+password,Toast.LENGTH_LONG).show();
        buaaContentProvider = DataUtils.getContentProvider(BUAAContentProvider.Notice);


        noticeFragment.setProvider(buaaContentProvider);
        noticeFragment.setOnScrollListener(new ListScrollListener());
        noticeFragment.setSqLiteUtils(mySQLite);
        //TODO:TRASH PROVIDER SETTING
        //  noticeFragment.setTrash(buaaTrashContentProvider);
        transaction.add(R.id.FragmentContainer, noticeFragment);

            FormblankFragment = BlankFragment.newInstance(getFormUrl());
            QueryblankFragment = BlankFragment.newInstance(getQueryUrl());
            MessageFragment = BlankFragment.newInstance(getMessageUrl());
            SettingPasswordFragment = BlankFragment.newInstance(getRegisterUrl());
            PersonalInfoFragment = BlankFragment.newInstance(getPersonalInfoUrl());
            transaction.add(R.id.FragmentContainer,QueryblankFragment);
            transaction.add(R.id.FragmentContainer, FormblankFragment);
            transaction.add(R.id.FragmentContainer, MessageFragment);
            transaction.add(R.id.FragmentContainer,PersonalInfoFragment);
            transaction.add(R.id.FragmentContainer,SettingPasswordFragment);

            FavFragment = new NoticeListFragment();
            FavFragment.setSqLiteUtils(mySQLite);
            buaaFavContentProvider = DataUtils.getContentProvider(BUAAContentProvider.FAV);
            FavFragment.setProvider(buaaFavContentProvider);
            FavFragment.setOnScrollListener(new ListScrollListener());
            transaction.add(R.id.FragmentContainer, FavFragment);


            trashFragment = new NoticeListFragment();
            trashFragment.setSqLiteUtils(mySQLite);
            buaaTrashContentProvider = DataUtils.getContentProvider(BUAAContentProvider.Trash);
            trashFragment.setProvider(buaaTrashContentProvider);
            trashFragment.setOnScrollListener(new ListScrollListener());
            transaction.add(R.id.FragmentContainer, trashFragment);
        transaction.hide(QueryblankFragment);
        transaction.hide(FormblankFragment);
        transaction.hide(SettingPasswordFragment);
        transaction.hide(MessageFragment);
        transaction.hide(PersonalInfoFragment);
        transaction.hide(trashFragment);
        transaction.hide(FavFragment);

        transaction.show(noticeFragment);
        nowFragment = noticeFragment;
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (nowFragment != noticeFragment)
            {
                //还原主界面
                transaction = fm.beginTransaction();
                bottombar.setVisibility(View.VISIBLE);
                transaction.hide(nowFragment);
                nowFragment =noticeFragment;
                transaction.show(noticeFragment);
                NowNotice = true;
                NowQuery = NowForm = false;
                NowTrash = false;
                NowFav = false;
                transaction.commit();
            }
            else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.notice_fetch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.Notice:
                /*if (noticeFragment == null) {
                    noticeFragment = new NoticeListFragment();
                }*/
                // 使用当前Fragment的布局替代id_content的控件
               // if (NowQuery) transaction.hide(QueryblankFragment);
               // if (NowForm) transaction.hide(FormblankFragment);
                if (!NowNotice) {
                    transaction = fm.beginTransaction();
                    transaction.hide(nowFragment);
                    transaction.show(noticeFragment);
                    nowFragment = noticeFragment;
                    syncFramentAndIconChange_Notice(1);
                    syncFramentAndIconChange_Form(0);
                    syncFramentAndIconChange_Query(0);
                    NowNotice = true;
                    NowForm = false;
                    NowQuery = false;
                    transaction.commit();
                }
                break;
            case R.id.Form:
                if (!ClientUtils.getLog_state())
                {
                    Toast.makeText(getApplicationContext(),"您正处于离线模式，无法使用当前功能",Toast.LENGTH_SHORT).show();
                }
              //  if (NowQuery) transaction.hide(QueryblankFragment);
               // if (NowNotice) transaction.hide(noticeFragment);
                else {
                    transaction = fm.beginTransaction();
                    syncFramentAndIconChange_Notice(0);
                    syncFramentAndIconChange_Form(1);
                    syncFramentAndIconChange_Query(0);
                   // FormblankFragment.LoadUrl("http://www.qq.com");
                    transaction.hide(nowFragment);
                    transaction.show(FormblankFragment);
                    nowFragment = FormblankFragment;
                    NowNotice = false;
                    NowForm = true;
                    NowQuery = false;
                    transaction.commit();

                }
                break;
            case R.id.Query:
                if (!ClientUtils.getLog_state())
                {
                    Toast.makeText(getApplicationContext(),"您正处于离线模式，无法使用当前功能",Toast.LENGTH_SHORT).show();
                }
                else {
                    transaction = fm.beginTransaction();
                    syncFramentAndIconChange_Notice(0);
                    syncFramentAndIconChange_Form(0);
                    syncFramentAndIconChange_Query(1);
                    NowNotice = false;
                    NowForm = false;
                    NowQuery = true;
                    transaction.hide(nowFragment);
                    transaction.show(QueryblankFragment);
                    nowFragment = QueryblankFragment;
                    transaction.commit();
                }
                break;
        }


    }

    //TODO:完成修改资料和留言两个功能
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (!ClientUtils.getLog_state())
        {
            Toast.makeText(getApplicationContext(),"您正处于离线模式，无法使用当前功能",Toast.LENGTH_SHORT).show();
        }
        else {
            bottombar.setVisibility(View.GONE);
            if (id == R.id.nav_message) {
                transaction = fm.beginTransaction();
                transaction.hide(nowFragment);
                transaction.show(MessageFragment);
                nowFragment = MessageFragment;
                NowTrash = false;
                NowFav = false;
                transaction.commit();
            } else if (id == R.id.nav_personal_info) {
                //Uri uri = Uri.parse(getPersonalInfoUrl());
                //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                //startActivity(intent);
                transaction = fm.beginTransaction();
                transaction.hide(nowFragment);
                transaction.show(PersonalInfoFragment);
                nowFragment = PersonalInfoFragment;
                NowTrash = false;
                NowFav = false;
                transaction.commit();
            } else if (id == R.id.nav_fav) {
                transaction = fm.beginTransaction();
                transaction.hide(nowFragment);
                transaction.show(FavFragment);
                nowFragment = FavFragment;
                NowTrash = false;
                NowFav = true;
                transaction.commit();
            } else if (id == R.id.nav_back) {
                //还原主界面
                transaction = fm.beginTransaction();
                bottombar.setVisibility(View.VISIBLE);
                transaction.hide(nowFragment);
                if (NowForm) {
                    transaction.show(FormblankFragment);
                    nowFragment = FormblankFragment;
                }
                if (NowNotice) {
                    transaction.show(noticeFragment);
                    nowFragment = noticeFragment;
                }
                if (NowQuery) {
                    transaction.show(QueryblankFragment);
                    nowFragment = QueryblankFragment;
                }
                NowTrash = false;
                NowFav = false;
                transaction.commit();
            } else if (id == R.id.nav_trash) {
                transaction = fm.beginTransaction();

                transaction.hide(nowFragment);
                transaction.show(trashFragment);
                nowFragment = trashFragment;
                NowTrash = true;
                NowFav = false;
                transaction.commit();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void syncFramentAndIconChange_Notice(int flags) {
        if (flags == 0) {
            notice.setImageResource(R.mipmap.icon_notice_gray);
            ((TextView) findViewById(R.id.bottom_notice_main_act)).setTextColor(getResources().getColor(R.color.Black));
        } else {
            notice.setImageResource(R.mipmap.icon_notice_blue);
            ((TextView) findViewById(R.id.bottom_notice_main_act)).setTextColor(getResources().getColor(R.color.buaa_custom_blue));
        }
    }

    private void syncFramentAndIconChange_Form(int flags) {
        if (flags == 0) {
            form.setImageResource(R.mipmap.icon_service_gray);
            ((TextView) findViewById(R.id.bottom_form_main_act)).setTextColor(getResources().getColor(R.color.Black));
        } else {
            form.setImageResource(R.mipmap.icon_service_blue);
            ((TextView) findViewById(R.id.bottom_form_main_act)).setTextColor(getResources().getColor(R.color.buaa_custom_blue));
        }
    }

    private void syncFramentAndIconChange_Query(int flags) {
        if (flags == 0) {
            query.setImageResource(R.mipmap.icon_search_gray);
            ((TextView) findViewById(R.id.bottom_query_main_act)).setTextColor(getResources().getColor(R.color.Black));
        } else {
            query.setImageResource(R.mipmap.icon_search_blue);
            ((TextView) findViewById(R.id.bottom_query_main_act)).setTextColor(getResources().getColor(R.color.buaa_custom_blue));
        }
    }

    public void onListFragmentInteraction(CommonItemForList item) {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    //TODO: (2017.2.13) 获取网址的方法，除了注册的都要access_token
    String getFormUrl()  //填表，目前罗dalao还没做
    {

        return "https://www.baidu.com/";
    }
    String getQueryUrl()  //事务查询，目前没有拿到具体内容
    {

        return "https://www.sina.com/";
    }
    String getPersonalInfoUrl()  //个人信息
    {

        return "https://www.ourbuaa.com/mobile/account";
    }
    String getRegisterUrl()  //注册
    {
        return "https://www.ourbuaa.com/mobile/register";
    }
    String getMessageUrl()  // 留言
    {
       return "https://www.ourbuaa.com/mobile/inquiry";
    }
}
