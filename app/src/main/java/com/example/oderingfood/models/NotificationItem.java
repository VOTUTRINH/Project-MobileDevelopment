package com.example.oderingfood.models;

import java.sql.Time;

public class NotificationItem {
    Integer noticeImg;
    String noticeLabel;
    String noticeContent;
    Time timeNotice;
    public boolean isRead;
    public NotificationItem(int img, String label, String content){
        noticeImg = img;
        noticeLabel = label;
        noticeContent = content;
//        timeNotice = time;
        isRead = false;
    }

    public void setNoticeImg(Integer noticeImg) {
        this.noticeImg = noticeImg;
    }

    public Integer getNoticeImg() {
        return noticeImg;
    }

    public String getNoticeLabel() {
        return noticeLabel;
    }

    public String getNoticeContent() {
        return noticeContent;
    }


    public String getTimeString(){
        //todo tinh thoi gian tu time den hien tai
        return "10 phút trước.";
    }
}