package com.github.kettoleon.ti.dashboard.model.tables;

import javax.swing.*;
import java.util.function.Function;

public interface Column<T, V> {
    String getHeader();

    ImageIcon getIcon();

    Class<V> getType();

    Function<T, V> getGetter();

}