package com.ourbuaa.buaahelper;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.opengl.Visibility;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends Activity implements View.OnClickListener {


    private static SQLiteUtils SQLiteLink;

    public static void setSQLiteLink(SQLiteUtils link) {
        SQLiteLink = link;
    }

    protected long id;
    private boolean FAV;
    private long downloadRefs[] = null;
    private int downloadStatus[] = null;
    private HashMap<Long,Integer> downLoadRefToID = new HashMap<Long,Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Light_NoTitleBar);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        //TextView textView_Detail_title = (TextView) findViewById(R.id.Detail_title);
        TextView textView_Detail_title = (TextView) findViewById(R.id.titlebarText);
        TextView textView_Detail_updateat = (TextView) findViewById(R.id.Detail_updateat);
        TextView textView_Detail_content = (TextView) findViewById(R.id.Detail_content);
        TextView textView_Detail_file = (TextView) findViewById(R.id.Detail_file);
        final ImageButton Fav = (ImageButton) findViewById(R.id.fav);
        ImageButton getNext, getLast, ReturnToMain;
        getNext = (ImageButton) findViewById(R.id.getNext);
        getLast = (ImageButton) findViewById(R.id.getBefore);

        RelativeLayout Bottom_bar = (RelativeLayout) findViewById(R.id.bottom_bar_detail_act);
        LinearLayout DownLoadList = (LinearLayout) findViewById(R.id.DownLoadList);
        ArrayList<Button> buttons = new ArrayList<Button>();
        ReturnToMain = (ImageButton) findViewById(R.id.goback);
        ReturnToMain.setOnClickListener(this);
        //Button button = (Button) findViewById(R.id.back);
        //button.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        //BUAAItemTouchHelperCallback buaaItemTouchHelperCallback = NoticeListFragment.buaaItemTouchHelperCallback;
        //textView.setText("Position:" + bundle.getInt("Position") + " "+"ID:"+bundle.getLong("ID")+" ");
        id = bundle.getLong("ID");

        String t = SQLiteLink.GetNotificationByID(id);

        try {
            JSONObject j = new JSONObject(t);

            textView_Detail_title.setText(j.getString("title"));
            textView_Detail_updateat.setText(SQLiteUtils.TimeStamp2Time(j.getLong("updated_at")));
            textView_Detail_content.setText(Html.fromHtml(j.getString("content")));


            String file_str = "<p>附件：</p>";
            JSONArray mJSONArray = new JSONArray(j.getString("files"));
            downloadRefs = new long[mJSONArray.length()*2];
            downloadStatus = new int[mJSONArray.length()*2];
            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    int id = downLoadRefToID.get(reference);
                    downloadStatus[id]=2;

                }
            };
            registerReceiver(receiver, filter);

            for (int i = 0; i < mJSONArray.length(); i++) {
                downloadRefs[i] = -1;
                downloadStatus[i] = 0;
                // 0--never touched
                //1--downloading
                //2--download task complete
                JSONObject mJSONObject = mJSONArray.getJSONObject(i);
                String title = mJSONObject.getString("title");
                String href = mJSONObject.getString("href");
                Button button = new Button(this);
                button.setText(title);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id=buttons.indexOf(button);
                        //Toast.makeText(getApplicationContext(),"now:"+(Integer)(downloadStatus[id]),Toast.LENGTH_LONG);
                        if (downloadStatus[id]==0) {
                            String serviceString = Context.DOWNLOAD_SERVICE;
                            DownloadManager downloadManager;
                            downloadManager = (DownloadManager) getSystemService(serviceString);

                            downloadStatus[id] = 1;

                            Uri uri = Uri.parse(href);
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            long reference = downloadManager.enqueue(request);
                            downloadRefs[id] = reference;
                            downLoadRefToID.put(reference,id);
                        }
                        if (downloadStatus[id]==2)
                        {
                            String serviceString = Context.DOWNLOAD_SERVICE;
                            DownloadManager downloadManager;
                            downloadManager = (DownloadManager) getSystemService(serviceString);
                            try {
                                downloadManager.openDownloadedFile(downloadRefs[id]);
                            }catch(FileNotFoundException e)
                            {
                                downloadStatus[id]=0;
                                new  AlertDialog.Builder(getApplicationContext())
                                        .setTitle("文件下载失败" )

                                        .setPositiveButton("是" ,  null )

                                        .show();
                            }
                        }
                    }
                });
                buttons.add(button);
                DownLoadList.addView(button);

                file_str += "<p><a href=\"" + href + "\">" + title + "</a></p>";
            }


            //textView_Detail_file.setText(Html.fromHtml(file_str));
            //textView_Detail_file.setMovementMethod(LinkMovementMethod.getInstance());
            if (j.getLong("star") == 1) {
                Fav.setImageResource(R.drawable.ic_star_black_24dp);
                FAV = true;
            } else {
                Fav.setImageResource(R.mipmap.ic_star_border_black_24dp);
                FAV = false;
            }

            /**
             * j.put("id", cursor.getLong(0));
             j.put("updated_at", cursor.getLong(1));
             j.put("title", cursor.getString(2));
             j.put("author", cursor.getString(3));
             j.put("content", cursor.getString(4));
             j.put("files", cursor.getString(5));
             j.put("star", cursor.getLong(6));
             *
             * */


        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO:由于这次讨论的结果是不做上下翻页，这里就留作以后用好了，就是hasMoreXXX方法不用写了（暂时）

        if (!hasMoreBeforeNotification()) getLast.setVisibility(View.GONE);
        if (!hasMoreNextNotification()) getNext.setVisibility(View.GONE);

        if (hasMoreNextNotification() && hasMoreBeforeNotification()) {
            Bottom_bar.setBackgroundColor(0XEEEEEE);
        }
        //TODO:完成以下三个监听事件，从上往下：上一个，下一个，收藏（前两个暂时不写）

        getLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasMoreBeforeNotification()) {

                }
            }
        });

        getNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasMoreNextNotification()) {

                }
            }
        });

        Fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ClientUtils.getLog_state()) {
                    Toast.makeText(getApplicationContext(), "当前处于离线模式，无法进行此操作", Toast.LENGTH_SHORT).show();
                } else {
                    if (!FAV) {
                        Fav.setImageResource(R.drawable.ic_star_black_24dp);
                        FAV = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ClientUtils.StarNotification(id);
                                SQLiteLink.StarNotification(id);
                            }
                        }).start();
                        //第一次点击，收藏操作
                    } else {
                        Fav.setImageResource(R.mipmap.ic_star_border_black_24dp);
                        FAV = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ClientUtils.UnstarNotification(id);
                                SQLiteLink.UnStarNotification(id);
                            }
                        }).start();
                        //第二次点击，取消收藏

                    }
                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        this.finish();
    }

    private boolean hasMoreBeforeNotification()  //TODO：前面是否还有（暂时不写）
    {
        return false;
    }

    private boolean hasMoreNextNotification()  //TODO:后面是否还有（暂时不写）
    {
        return false;
    }
}
