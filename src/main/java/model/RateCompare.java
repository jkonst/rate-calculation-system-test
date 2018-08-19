package model;

import java.util.Comparator;

/**
 * Created by JK on 8/19/2018.
 * This is for personal use. All Rights Reserved.
 */
public class RateCompare implements Comparator<Lender> {

    public int compare(Lender l1, Lender l2) {
        return l1.getRate().compareTo(l2.getRate());
    }

}
