package com.qtec.assistant;

import java.util.List;

/**
 * @author gongw
 * @date 2019/1/11
 */
public class ChatData {

    public static class Style {
        public static final int CHAT_BUBBLE_LEFT = 0;
        public static final int CHAT_BUBBLE_RIGHT = 1;
    }

    private String content;
    private List<ExtraContent> extraContent;
    private long time;
    private boolean sent;
    private String image;
    private int style;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ExtraContent> getExtraContent() {
        return extraContent;
    }

    public void setExtraContent(List<ExtraContent> extraContent) {
        this.extraContent = extraContent;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
