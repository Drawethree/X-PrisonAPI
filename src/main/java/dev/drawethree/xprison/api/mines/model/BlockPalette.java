package dev.drawethree.xprison.api.mines.model;

import com.cryptomorin.xseries.XMaterial;

public interface BlockPalette {

    boolean contains(XMaterial material);

    double getPercentage(XMaterial material);

    void setPercentage(XMaterial material, double percentage);

    void add(XMaterial material, double percentage);

    void remove(XMaterial material);

}