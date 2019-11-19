package com.hand.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Distribution {
    EXPRESS(1, "EXPRESS", "快递配送"),
    PICKUP(2, "PICKUP", "门店自提"),
    SITE_SEND(3, "SITE_SEND", "门店急送");

    private int id;
    private String distribution;
    private String description;

    @Override
    public String toString() {
        return this.distribution + "<" + this.description + ">";
    }
}
