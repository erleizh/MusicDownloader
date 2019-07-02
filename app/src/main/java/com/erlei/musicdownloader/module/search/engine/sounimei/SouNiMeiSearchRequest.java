package com.erlei.musicdownloader.module.search.engine.sounimei;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.erlei.musicdownloader.http.requests.BaseRequest;
import com.erlei.musicdownloader.http.requests.SearchMusicRequest;

public class SouNiMeiSearchRequest extends BaseRequest implements Parcelable {
    /**
     * 输入
     */
    private String input;
    /**
     * 过滤器
     */
    private String filter;
    /**
     * 来源
     */
    private String type;
    /**
     * 游标
     */
    private int page;


    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "SouNiMeiSearchRequest{" +
                "input='" + input + '\'' +
                ", filter='" + filter + '\'' +
                ", type='" + type + '\'' +
                ", page=" + page +
                '}';
    }


    @NonNull
    public static SouNiMeiSearchRequest from(@NonNull SearchMusicRequest request) {
        SouNiMeiSearchRequest searchRequest = new SouNiMeiSearchRequest();
        searchRequest.setFilter(request.getFilter());
        searchRequest.setInput(request.getInput());
        searchRequest.setPage(request.getPage());
        searchRequest.setType(request.getSource());
        return searchRequest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.input);
        dest.writeString(this.filter);
        dest.writeString(this.type);
        dest.writeInt(this.page);
    }

    public SouNiMeiSearchRequest() {
    }

    protected SouNiMeiSearchRequest(Parcel in) {
        this.input = in.readString();
        this.filter = in.readString();
        this.type = in.readString();
        this.page = in.readInt();
    }

    public static final Parcelable.Creator<SouNiMeiSearchRequest> CREATOR = new Parcelable.Creator<SouNiMeiSearchRequest>() {
        @Override
        public SouNiMeiSearchRequest createFromParcel(Parcel source) {
            return new SouNiMeiSearchRequest(source);
        }

        @Override
        public SouNiMeiSearchRequest[] newArray(int size) {
            return new SouNiMeiSearchRequest[size];
        }
    };
}
