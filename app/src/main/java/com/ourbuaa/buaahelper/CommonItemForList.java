package com.ourbuaa.buaahelper;

import java.util.Date;

/**
 * Created by alan_yang on 2017/1/22.
 */

public class CommonItemForList {
    //private static int nextid = 0;
    String label;
    Date ReceiveTime;
    String PathToImage;
    StringBuffer Detail; //put your HTML content here
    Boolean read;
    long department;
    //long id = ++nextid;
    private long id;

    CommonItemForList() {
        PathToImage = null;
        ReceiveTime = null;
        label = null;
    }

    CommonItemForList(long sid, String Label, String ImageUri, Date Receive, long sdepartment, long sread) {
        id = sid;
        PathToImage = ImageUri;
        label = Label;
        ReceiveTime = Receive;
        department = sdepartment;
        read = sread != 0;
    }

    public StringBuffer getDetail() {
        return Detail;
    }

    public void setDetail(StringBuffer detail) {
        Detail = detail;
    }

    public long getId() {
        return id;
    }

    public void setId(long sid) {
        id = sid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getReceiveTime() {
        return ReceiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        ReceiveTime = receiveTime;
    }

    //TODO (YCH,2017.2.8) Tell me the filename(including the suffix name) of the department icon
    public String getPathToImage() {


        return "d" + department;
        //return "ic_star_border_black_24dp";
    }

    public void setPathToImage(String pathToImage) {
        PathToImage = pathToImage;
    }

    //TODO (YCH,2017.2.8) Tell me that whether it has been read
    public boolean isread() {
        return read;
    }


}
