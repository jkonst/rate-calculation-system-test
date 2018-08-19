package model;

import java.math.BigDecimal;


/**
 * Created by JK on 8/18/2018.
 * This is for personal use. All Rights Reserved.
 */
public class Lender {
    private int available;
    private String name;
    private BigDecimal rate;
    private boolean isOnTheList;

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public boolean isOnTheList() {
        return isOnTheList;
    }

    public void setOnTheList(boolean onTheList) {
        isOnTheList = onTheList;
    }
}
