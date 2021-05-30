package com.deniszbecskei.personaldata;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FirestoreOps {
    public static Date stringToDate(String date) {

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void put(String collection, ArrayList<SearchItem> data) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (SearchItem si: data) {
            db.collection(collection).add(si.toCRUD());
        }
    }
}
