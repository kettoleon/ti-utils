package com.github.kettoleon.ti.dashboard.view.util;

import java.awt.*;

public class DarculaColorPalette {

    public static final Color BLUE_NOTIFICATION = new Color(53, 145, 195);
    public static final Color ORANGE_NOTIFICATION = new Color(238, 166, 51);
    public static final Color RED_NOTIFICATION = new Color(197, 83, 80);
    public static final Color GREEN_NOTIFICATION = new Color(72, 155, 84);

    public static final Color BLUE_PALE = new Color(62, 133, 160);
    public static final Color ORANGE_PALE = new Color(169, 129, 64);
    public static final Color RED_PALE = new Color(176, 90, 47);
    public static final Color GREEN_PALE = new Color(82, 133, 66);
    public static final Color PURPLE_PALE = new Color(134, 117, 175);
    public static final Color PINK_PALE = new Color(172, 107, 121);
    public static final Color GRAY_PALE = new Color(174, 176, 179);

    public static final Color BLUE_INTENSE = new Color(45, 204, 255);
    public static final Color ORANGE_INTENSE = new Color(255, 150, 0);
    public static final Color RED_INTENSE = new Color(255, 60, 46);
    public static final Color GREEN_INTENSE = new Color(0, 253, 1);

    public static final Color BLUE_FONT = new Color(103, 150, 187);
    public static final Color ORANGE_FONT = new Color(202, 119, 51);
    public static final Color RED_FONT = new Color(187, 63, 61);
    public static final Color GREEN_FONT = new Color(105, 134, 89);
    public static final Color PURPLE_FONT = new Color(151, 117, 170);
    public static final Color YELLOW_INTENSE_FONT = new Color(183, 165, 42);
    public static final Color YELLOW_FONT = new Color(247, 192, 107);
    public static final Color WHITE_FONT = new Color(168, 182, 197);
    public static final Color GRAY_FONT = new Color(113, 114, 122);

    public static String toHtmlColor(Color c) {
        return "#" + String.format("%06X", c.getRGB() & 0x00ffffff);
    }
}
