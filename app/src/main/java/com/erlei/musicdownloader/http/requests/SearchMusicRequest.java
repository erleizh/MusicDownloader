package com.erlei.musicdownloader.http.requests;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

@Keep
public class SearchMusicRequest extends BaseRequest implements Parcelable {

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
    private String source;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "SearchMusicRequest{" +
                "input='" + input + '\'' +
                ", filter='" + filter + '\'' +
                ", source='" + source + '\'' +
                ", page=" + page +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.input);
        dest.writeString(this.filter);
        dest.writeString(this.source);
        dest.writeInt(this.page);
    }

    public SearchMusicRequest() {
    }

    protected SearchMusicRequest(Parcel in) {
        this.input = in.readString();
        this.filter = in.readString();
        this.source = in.readString();
        this.page = in.readInt();
    }

    public static final Creator<SearchMusicRequest> CREATOR = new Creator<SearchMusicRequest>() {
        @Override
        public SearchMusicRequest createFromParcel(Parcel source) {
            return new SearchMusicRequest(source);
        }

        @Override
        public SearchMusicRequest[] newArray(int size) {
            return new SearchMusicRequest[size];
        }
    };
}
