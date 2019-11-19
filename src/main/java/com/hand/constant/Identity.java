package com.hand.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Identity {
    PICK_UP(1, "pickUp", "门店自提"),
    CONCESSIONAL_RATE(2, "concessional_rate", "超值精选"),
    TIME_DOPTIMAL(3, "time_doptimal", "限时特优"),
    NEW_PRODUCT(4, "new_product", "新品"),
    CUSTOM_MADE(5, "custom_made", "合身选"),
    REVISION(6, "revision", "修改裤长"),
    ACTIVE_TAGS(7, "active_tags", "活动角标");

    private int id;
    private String identity;
    private String description;

    @Override
    public String toString() {
        return this.getIdentity();
    }
}
