package com.deniszbecskei.personaldata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.deniszbecskei.personaldata.person.HumanName;
import com.deniszbecskei.personaldata.person.Identifier;
import com.deniszbecskei.personaldata.person.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListPeopleActivity extends AppCompatActivity {

    private static final String LOG_TAG = ListPeopleActivity.class.getName();

    private RecyclerView mRecyclerView;
    private ArrayList<SearchItem> mPersonList = new ArrayList<>();
    private SearchItemAdapter mSearchItemAdapter;
    private CollectionReference mItems;
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;

    private final int gridNumber = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_people);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        for (SearchItem si: mPersonList) {
            Log.d(LOG_TAG, si.toString());
        }

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));

        mSearchItemAdapter = new SearchItemAdapter(this, mPersonList, ListPeopleActivity.class.getName());
        mRecyclerView.setAdapter(mSearchItemAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Personnel");

        queryData();
    }

    private void initializeData() {
        String[] personNameArray = getResources().getStringArray(R.array.person_name_array);
        String[] personBirthDateArray = getResources().getStringArray(R.array.person_birth_date_array);
        TypedArray personImageResources = getResources().obtainTypedArray(R.array.person_images);
        String[] personIdentifierArray = getResources().getStringArray(R.array.person_identity_array);
        String[] personGenderArray = getResources().getStringArray(R.array.person_gender_array);

        mPersonList.clear();

        for (int i = 0; i < personNameArray.length; i++) {
            Person person = new Person();
            person.setName(new HumanName(personNameArray[i]));
            if (FirestoreOps.stringToDate(personBirthDateArray[i]) == null) {
                person.setBirthDate(new Date());
            } else {
                person.setBirthDate(FirestoreOps.stringToDate(personBirthDateArray[i]));
            }

            Identifier id = new Identifier();
            id.setValue(personIdentifierArray[i]);
            person.setIdentifier(id);

            person.setPhoto(personImageResources.getResourceId(i, 0));
            person.setGender(personGenderArray[i]);

            SearchItem si = new SearchItem();
            si.setPerson(person);
            mPersonList.add(si);
        }

        personImageResources.recycle();
    }

    private void queryData() {
        mPersonList.clear();
        mItems.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        SearchItem item = document.toObject(SearchItem.class);
                        mPersonList.add(item);
                    }

                    if (mPersonList.size() == 0) {
                        initializeData();
                        FirestoreOps.put("Personnel", mPersonList);
                        queryData();
                    }

                    mSearchItemAdapter.notifyDataSetChanged();
                });
    }
}