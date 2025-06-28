package dev.drawethree.xprison.api.enchants.model;

public interface RefundableEnchant {

    boolean isRefundEnabled();

    int getRefundGuiSlot();

    double getRefundPercentage();
}