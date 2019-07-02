package com.erlei.musicdownloader.http.responses;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

import java.io.Serializable;

/**
 * Created by lll on 2019/4/12
 * Email : lllemail@foxmail.com
 * Describe : 音乐对象
 */
@Keep
public class MusicBean extends BaseResponse implements Parcelable, Serializable {
    private long boxId;
    private String source;
    private String link;
    private String songid;
    private String title;
    private String author;
    private String lrc;
    private String url;
    private String pic;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getBoxId() {
        return boxId;
    }

    public void setBoxId(long boxId) {
        this.boxId = boxId;
    }


    @Override
    public String toString() {
        return "MusicBean{" +
                "boxId=" + boxId +
                ", source='" + source + '\'' +
                ", link='" + link + '\'' +
                ", songid='" + songid + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", lrc='" + lrc + '\'' +
                ", url='" + url + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.boxId);
        dest.writeString(this.source);
        dest.writeString(this.link);
        dest.writeString(this.songid);
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeString(this.lrc);
        dest.writeString(this.url);
        dest.writeString(this.pic);
    }

    public MusicBean() {
    }

    protected MusicBean(Parcel in) {
        this.boxId = in.readLong();
        this.source = in.readString();
        this.link = in.readString();
        this.songid = in.readString();
        this.title = in.readString();
        this.author = in.readString();
        this.lrc = in.readString();
        this.url = in.readString();
        this.pic = in.readString();
    }

    public static final Creator<MusicBean> CREATOR = new Creator<MusicBean>() {
        @Override
        public MusicBean createFromParcel(Parcel source) {
            return new MusicBean(source);
        }

        @Override
        public MusicBean[] newArray(int size) {
            return new MusicBean[size];
        }
    };
}
