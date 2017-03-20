
package buaa.buaahelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


/**
 * Created by alan_yang on 2017/d1/31.
 */

public class BUAAContentProvider extends ContentProvider {
    //TODO: Accomplish all the methods here, all content in each method should be overwritten
    //TODO:(2017.2.8) 提供type为Trash的实例
    public static final String FAV = "FAV", Notice = "NOTICE", Trash = "TRASH";
    private List<CommonItemForList> ITEMS = new ArrayList<CommonItemForList>();
    String type;
    private boolean isfirst = true;
    private static SQLiteUtils SQLiteLink;
    private BUAA_RecyclerViewAdapter buaa_recyclerViewAdapter;
    private Context context;

    public static void setSQLiteLink(SQLiteUtils link) {
        SQLiteLink = link;
    }

    public String getType() {
        return type;
    }

    public void setBuaa_recyclerViewAdapter(BUAA_RecyclerViewAdapter buaa_recyclerViewAdapter) {
        this.buaa_recyclerViewAdapter = buaa_recyclerViewAdapter;
    }

    public BUAA_RecyclerViewAdapter getBuaa_recyclerViewAdapter() {
        return buaa_recyclerViewAdapter;
    }

    BUAAContentProvider(String type) {
        super(null); // do not delete this part
        this.type = type;
        getInitialDataList();

    }

    @Override
    public int getDataSize() {
        return ITEMS.size();
    }

    public void ForceFreshDataList() {

        updateNotifications();

    }

    private void getInitialDataList() {

        updateNotifications();
        /** 初始化列表 */
        /*
        for (int i = d1; i <= 25; i++) {
            ITEMS.add(createCommonItemForList(i));
        }
        */
        return;
    }

    public List<CommonItemForList> getPreDataList() {
        /** 获取较新内容 */

        updateNotifications();


        List<CommonItemForList> newlist = new ArrayList<CommonItemForList>();
        //for (int i = d1; i <= 10; i++)
        //   newlist.add(createCommonItemForList(-d1));
        return newlist;
    }
/*
    public List<CommonItemForList> getPostDataList() {  //获取新的几个


        List<CommonItemForList> newlist = new ArrayList<CommonItemForList>();
        for (int i = 0; i <= 10; i++)
            newlist.add(createCommonItemForList(i, "title", "", 0));
        return newlist;

    }
    */

    public void LoadPreData()  //keep it
    {
        if (!isfirst) {
            updateNotifications();
            //clear();
            //addAll(getPreDataList());
        }
        isfirst = false;
    }

    public void LoadPostData()  //keep it
    {
        /**
        if (ClientUtils.getLog_state())
            addMoreItems(25);
        else updateNotifications();
         */
        addMoreItems(25);
        //ITEMS.addAll(getPostDataList());
        //updateNotifications();
        //updateNotifications();
    }

    @Override
    public void addItem(CommonItemForList item) {
        ITEMS.add(item);
    }


    protected CommonItemForList createCommonItemForList(long id, String label, String imageURI, long timestamp, long department, long read) {

        Date time = new Date(timestamp);
        return new CommonItemForList(id, label, imageURI, time, department, read);
    }


    @Override
    public List<CommonItemForList> getDataList() {
        return ITEMS;
    }

    @Override
    protected void clear() {
        ITEMS.clear();
    }

    @Override
    protected void addAll(List<CommonItemForList> list) {
        ITEMS.addAll(list);
    }

    public void setContext(Context context)
    {
      this.context=context;
    }

    public void updateNotifications() {
        /**TODO:Context*/
//        if (ClientUtils.getLog_state()) Toast.makeText(context, "正在刷新...", Toast.LENGTH_LONG).show();
        BUAAContentProvider.UpdateNotificationsTask updateNotificationsTask = new BUAAContentProvider.UpdateNotificationsTask();
        updateNotificationsTask.execute();

    }

    class UpdateNotificationsTask extends AsyncTask<String, Void, StringBuffer> {


        @Override
        protected StringBuffer doInBackground(String... strings) {
            StringBuffer errormsg = new StringBuffer("");


            if(ClientUtils.getLog_state())ClientUtils.FetchNewNotification();

            return errormsg;
        }

        @Override
        protected void onPostExecute(StringBuffer s) {
            String t = "";
            switch (type) {
                default:

                    ITEMS.clear();
                    //  //bug fixed

                    t = SQLiteLink.GetNotificationsOrderByUpdateTime(0, 25, ClientUtils.getUser());
                    switch (t) {
                        case SQLiteUtils.SQL_NONE:
                            break;
                        default:

                            try {
                                JSONArray mJSONArray = new JSONArray(t);

                                for (int i = 0; i < mJSONArray.length(); i++) {
                                    JSONObject j = mJSONArray.getJSONObject(i);

                                    CommonItemForList c = createCommonItemForList(j.getLong("id"), j.getString("title"), "", j.getLong("updated_at"), j.getLong("department"), j.getLong("read"));
                                    ITEMS.add(c);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            buaa_recyclerViewAdapter.notifyDataSetChanged();
                            break;
                    }
                    break;
                case FAV:

                    ITEMS.clear();
                    t = SQLiteLink.GetFAVNotificationsOrderByUpdateTime(0, 25, ClientUtils.getUser());
                    switch (t) {
                        case SQLiteUtils.SQL_NONE:
                            break;
                        default:
                            try {
                                JSONArray mJSONArray = new JSONArray(t);

                                for (int i = 0; i < mJSONArray.length(); i++) {
                                    JSONObject j = mJSONArray.getJSONObject(i);

                                    CommonItemForList c = createCommonItemForList(j.getLong("id"), j.getString("title"), "", j.getLong("updated_at"), j.getLong("department"), j.getLong("read"));
                                    ITEMS.add(c);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            buaa_recyclerViewAdapter.notifyDataSetChanged();
                            break;
                    }
                    break;
                case Trash:

                    ITEMS.clear();
                    t = SQLiteLink.GetTrashNotificationsOrderByUpdateTime(0, 25, ClientUtils.getUser());
                    switch (t) {
                        case SQLiteUtils.SQL_NONE:
                            break;
                        default:
                            try {
                                JSONArray mJSONArray = new JSONArray(t);

                                for (int i = 0; i < mJSONArray.length(); i++) {
                                    JSONObject j = mJSONArray.getJSONObject(i);

                                    CommonItemForList c = createCommonItemForList(j.getLong("id"), j.getString("title"), "", j.getLong("updated_at"), j.getLong("department"), j.getLong("read"));
                                    ITEMS.add(c);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            buaa_recyclerViewAdapter.notifyDataSetChanged();
                            break;
                    }
                    break;
            }

         //   if (ClientUtils.getLog_state()) Toast.makeText(context, "刷新完成！", Toast.LENGTH_LONG).show();
        }

    }

    public void deleteDataInList(int position) {
        ITEMS.remove(position);
    }

    private void addMoreItems(int dsize) {
        String t = "";
        switch (type) {
            default:
                t = SQLiteLink.GetNotificationsOrderByUpdateTime(ITEMS.size(), dsize, ClientUtils.getUser());
                switch (t) {
                    case SQLiteUtils.SQL_NONE:
                        break;
                    default:
                        try {
                            JSONArray mJSONArray = new JSONArray(t);
                            for (int i = 0; i < mJSONArray.length(); i++) {
                                JSONObject j = mJSONArray.getJSONObject(i);
                                CommonItemForList c = createCommonItemForList(j.getLong("id"), j.getString("title"), "", j.getLong("updated_at"), j.getLong("department"), j.getLong("read"));
                                ITEMS.add(c);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
            case FAV:

                t = SQLiteLink.GetFAVNotificationsOrderByUpdateTime(ITEMS.size(), dsize, ClientUtils.getUser());
                switch (t) {
                    case SQLiteUtils.SQL_NONE:
                        break;
                    default:
                        try {
                            JSONArray mJSONArray = new JSONArray(t);

                            for (int i = 0; i < mJSONArray.length(); i++) {

                                JSONObject j = mJSONArray.getJSONObject(i);
                                CommonItemForList c = createCommonItemForList(j.getLong("id"), j.getString("title"), "", j.getLong("updated_at"), j.getLong("department"), j.getLong("read"));
                                ITEMS.add(c);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
            case Trash:

                t = SQLiteLink.GetTrashNotificationsOrderByUpdateTime(ITEMS.size(), dsize, ClientUtils.getUser());
                switch (t) {
                    case SQLiteUtils.SQL_NONE:
                        break;
                    default:
                        try {
                            JSONArray mJSONArray = new JSONArray(t);

                            for (int i = 0; i < mJSONArray.length(); i++) {

                                JSONObject j = mJSONArray.getJSONObject(i);
                                CommonItemForList c = createCommonItemForList(j.getLong("id"), j.getString("title"), "", j.getLong("updated_at"), j.getLong("department"), j.getLong("read"));
                                ITEMS.add(c);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
        }

        //if(ClientUtils.getLog_state())Toast.makeText(, "", Toast.LENGTH_LONG).show();
    }
}
