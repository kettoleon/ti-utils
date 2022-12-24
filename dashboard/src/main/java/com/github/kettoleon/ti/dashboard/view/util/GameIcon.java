package com.github.kettoleon.ti.dashboard.view.util;

import javax.swing.*;
import java.awt.font.TextAttribute;
import java.net.URL;
import java.util.HashMap;

public class GameIcon {

    private static final HashMap<String, GameIcon> staticCache = new HashMap<>();

    private final URL resource;
    private final ImageIcon icon;

    private GameIcon(String path) {
        String classpath = "icons/" + path + ".png";
        resource = getClass().getClassLoader().getSystemResource(classpath);
        if (resource == null) {
            throw new RuntimeException("Classpath resource not found: " + classpath);
        }
        icon = new ImageIcon(resource);
    }

    public URL getResource() {
        return resource;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public String toInnerHtmlString() {
        int size = ((Float) new JLabel().getFont().getAttributes().get(TextAttribute.SIZE)).intValue();
        return toInnerHtmlString(size, size);
    }

    public String toInnerHtmlString(int height, int width) {
        return "<img src=\"" + resource + "\" height=\"" + height + "\" width=\"" + width + "\"/>";
    }

    public static GameIcon getGameIcon(String path) {
        if (!staticCache.containsKey(path)) {
            staticCache.put(path, new GameIcon(path));
        }
        return staticCache.get(path);
    }

    public static GameIcon getFactionIcon(String templateName) {
        return getGameIcon("faction/" + templateName.toLowerCase().replace("council", ""));
    }

    public static GameIcon getMissionIcon(String templateName) {
        return getGameIcon("mission/70px-ICO_" + templateName.toLowerCase() + "_on");
    }
}
