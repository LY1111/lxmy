package com.tuansbook.lvxing.Model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 * 相册
 */
public class PhotoFolder {

    private String name; // 相册名称
    private String dirPath; // 路径
    private boolean isSleected; // 是否选中
    private List<Photo> photoList; // 图片列表

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public boolean isSleected() {
        return isSleected;
    }

    public void setSleected(boolean sleected) {
        isSleected = sleected;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
