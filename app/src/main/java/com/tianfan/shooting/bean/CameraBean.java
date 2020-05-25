package com.tianfan.shooting.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/15 22:29
 * 修改人：Chen
 * 修改时间：2020/4/15 22:29
 */
public class CameraBean implements Parcelable {

    /**
     * camera_id : 1
     * camera_name : 摄像头名称1
     * camera_col : 1
     * camera_status : 1
     */

    private String camera_id;
    private String camera_name;
    private int camera_col;
    private String camera_status;
    public CameraBean(){

    }
    protected CameraBean(Parcel in) {
        camera_id = in.readString();
        camera_name = in.readString();
        camera_col = in.readInt();
        camera_status = in.readString();
    }

    public static final Creator<CameraBean> CREATOR = new Creator<CameraBean>() {
        @Override
        public CameraBean createFromParcel(Parcel in) {
            return new CameraBean(in);
        }

        @Override
        public CameraBean[] newArray(int size) {
            return new CameraBean[size];
        }
    };

    public String getCamera_id() {
        return camera_id;
    }

    public void setCamera_id(String camera_id) {
        this.camera_id = camera_id;
    }

    public String getCamera_name() {
        return camera_name;
    }

    public void setCamera_name(String camera_name) {
        this.camera_name = camera_name;
    }

    public int getCamera_col() {
        return camera_col;
    }

    public void setCamera_col(int camera_col) {
        this.camera_col = camera_col;
    }

    public String getCamera_status() {
        return camera_status;
    }

    public void setCamera_status(String camera_status) {
        this.camera_status = camera_status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(camera_id);
        dest.writeString(camera_name);
        dest.writeInt(camera_col);
        dest.writeString(camera_status);
    }
}
