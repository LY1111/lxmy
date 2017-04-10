package com.tuansbook.lvxing.Model;

/**
 * Created by Administrator on 2017/2/23.
 * 资讯类帖子-RecyclerView-item
 */
public class TieZi {

    private String id;
    private String pic;
    private String publish_time;
    private String push_time;
    private String title;
    private String desc;
    private int type = 0; // 发布的时候区分添加的是文字、图片或者视频

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getPush_time() {
        return push_time;
    }

    public void setPush_time(String push_time) {
        this.push_time = push_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
