package dev.drawethree.xprison.api.multipliers.model;

import java.util.Date;

public interface Multiplier {

    double getMultiplier();

    void setMultiplier(double newValue);

    void addMultiplier(double addition);

    boolean isActive();

    void reset();

    Date getEndDate();
}