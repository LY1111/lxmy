package com.tuansbook.lvxing.Model;

/**
 * Created by Administrator on 2016/12/13.
 * 图片
 */
public class Photo {

    private int id;
    private String path;
    private boolean isCamera;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCamera() {
        return isCamera;
    }

    public void setCamera(boolean camera) {
        isCamera = camera;
    }
}
