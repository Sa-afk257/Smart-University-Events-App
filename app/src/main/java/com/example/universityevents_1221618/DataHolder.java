package com.example.universityevents_1221618;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataHolder {
    private static final DataHolder instance = new DataHolder();

    private JSONArray categories;
    private JSONArray events;

    private DataHolder() {}

    public static DataHolder getInstance() {
        return instance;
    }

    public void setData(JSONObject data) {
        try {
            JSONObject record = data.getJSONObject("record");
            this.categories = record.getJSONArray("categories");
            this.events = record.getJSONArray("events");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getCategories() {
        return categories;
    }

    public JSONArray getevents() {
        return events;
    }
}
