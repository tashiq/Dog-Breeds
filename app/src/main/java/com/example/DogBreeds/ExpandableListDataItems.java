package com.example.mynewthreelevellistview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataItems {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableDetailList = new HashMap<String, List<String>>();

        // As we are populating List of fruits, vegetables and nuts, using them here
        // We can modify them as per our choice.
        // And also choice of fruits/vegetables/nuts can be changed
        List<String> dhan= new ArrayList<String>();
        dhan.add("dhshs");
        dhan.add("sjsj");


        List<String> gom = new ArrayList<String>();
        gom.add("Tomato");
        gom.add("Potato");


        List<String> nuts = new ArrayList<String>();
        nuts.add("Cashews");
        nuts.add("Badam");


        // Fruits are grouped under Fruits Items. Similarly the rest two are under
        // Vegetable Items and Nuts Items respectively.
        // i.e. expandableDetailList object is used to map the group header strings to
        // their respective children using an ArrayList of Strings.
        expandableDetailList.put("ধানের রোগ", dhan);
        expandableDetailList.put("গমের রোগ", gom);
        expandableDetailList.put("বাদামের রোগ", nuts);
        return expandableDetailList;
    }
}
