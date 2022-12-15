package com.example.oderingfood.models;

import java.sql.Time;

public class NotificationItem {
    String noticeImg;
    String noticeLabel;
    String noticeContent;
    String timeNotice;
    public boolean isRead;
    public NotificationItem(String img, String label, String content,String time){
        noticeImg = img;
        noticeLabel = label;
        noticeContent = content;
       timeNotice = time;
        isRead = false;
    }

    public void setNoticeImg(String noticeImg) {
        this.noticeImg = noticeImg;
    }

    public String getNoticeImg() {
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
        return timeNotice;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public void setNoticeLabel(String noticeLabel) {
        this.noticeLabel = noticeLabel;
    }

    public void setTimeNotice(String timeNotice) {
        this.timeNotice = timeNotice;
    }
}