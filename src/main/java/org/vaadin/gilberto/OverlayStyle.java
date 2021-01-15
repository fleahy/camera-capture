package org.vaadin.gilberto;

import java.io.Serializable;

public class OverlayStyle implements Serializable {

    private String top = "0";
    private String left = "0";
    private String bottom = "0";
    private String right = "0";
    private String border = "2px solid green";
    private String borderRadius = "100%";
    private String boxShadow = "none";

    public String getTop() {
        return top;
    }

    public OverlayStyle setTop(String top) {
        this.top = top;
        return this;
    }

    public String getLeft() {
        return left;
    }

    public OverlayStyle setLeft(String left) {
        this.left = left;
        return this;
    }

    public String getBottom() {
        return bottom;
    }

    public OverlayStyle setBottom(String bottom) {
        this.bottom = bottom;
        return this;
    }

    public String getRight() {
        return right;
    }

    public OverlayStyle setRight(String right) {
        this.right = right;
        return this;
    }

    public String getBorder() {
        return border;
    }

    public OverlayStyle setBorder(String border) {
        this.border = border;
        return this;
    }

    public String getBorderRadius() {
        return borderRadius;
    }

    public OverlayStyle setBorderRadius(String borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    public String getBoxShadow() {
        return boxShadow;
    }

    public OverlayStyle setBoxShadow(String boxShadow) {
        this.boxShadow = boxShadow;
        return this;
    }

}
