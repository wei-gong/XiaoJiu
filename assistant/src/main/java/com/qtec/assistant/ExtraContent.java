package com.qtec.assistant;

/**
 * @author gongw
 * @date 2019/1/17
 */
public class ExtraContent {

    public static class Style {
        public static final int SAY_SAMPLE = 0;
        public static final int SELECTOR_CONTACT = 1;
        public static final int TODO_CREATE = 2;
    }

    private String title;
    private String subTitle;
    private String image;
    private int style;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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
