package com.deniszbecskei.personaldata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.deniszbecskei.personaldata.person.HumanName;
import com.deniszbecskei.personaldata.person.Identifier;
import com.deniszbecskei.personaldata.person.Person;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DetailsActivity extends AppCompatActivity {
    public static final String LOG_TAG = DetailsActivity.class.getName();
    public final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    TextView nameEditText;
    TextView idEditText;
    TextView birthdateEditText;
    TextView genderEditText;
    ImageView personPhoto;

    private FirebaseFirestore db;
    private final ArrayList<SearchItem> mPersonList = new ArrayList<>();
    private SearchItemAdapter mSearchItemAdapter;
    private CollectionReference mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mSearchItemAdapter = new SearchItemAdapter(this, mPersonList, DetailsActivity.class.getName());

        db = FirebaseFirestore.getInstance();
        mItems = db.collection("Personnel");

        nameEditText = findViewById(R.id.nameQuery);
        idEditText = findViewById(R.id.idQuery);
        birthdateEditText = findViewById(R.id.birthDateQuery);
        genderEditText = findViewById(R.id.genderQuery);
        personPhoto = findViewById(R.id.person_avatar);

        String name = getIntent().getStringExtra("STUFF_I_NEED");
        name = name.substring(0, name.length()-3);
        nameEditText.setText(name);

        queryData(name);
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

    private void queryData(String name) {
        mPersonList.clear();
        mItems.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        SearchItem item = document.toObject(SearchItem.class);
                        Log.d(LOG_TAG, item.toString());
                        mPersonList.add(item);
                    }

                    if (mPersonList.size() == 0) {
                        initializeData();
                        FirestoreOps.put("Personnel", mPersonList);
                        queryData(name);
                    }

                    for (SearchItem si: mPersonList) {
                        if (si.getPerson().getName().getText().equals(name)) {
                            Log.e(LOG_TAG, si.getPerson().getName().getText());
                            changeDataOnScreen(si.getPerson());
                        }
                    }

                    mSearchItemAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> {
                    Log.e(LOG_TAG, "Shit hit the fan");
        });
    }

    private int getAgeFromDate(String date) {
        Date today = new Date();
        Date birthDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthDate = format.parse(date);
            if (birthDate == null) {
                return 0;
            }
        } catch (ParseException e) {
            return 0;
        }
        long diff = today.getTime() - birthDate.getTime();

        TimeUnit time = TimeUnit.DAYS;
        long difference = time.convert(diff, TimeUnit.MILLISECONDS);
        return (int)Math.floor(difference / 365.0);
    }

    private void changeDataOnScreen(Person person) {
        idEditText.setText(person.getIdentifier().getValue());
        String birthdateText = person.getBirthDateString() + " (" + getAgeFromDate(person.getBirthDateString()) + ")";
        birthdateEditText.setText(birthdateText);
        genderEditText.setText(person.getGender());
        Glide.with(this).load(person.getPhoto()).into(personPhoto);
    }

    public void checkUserPermission(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SET_WALLPAPER) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.SET_WALLPAPER}, REQUEST_CODE_ASK_PERMISSIONS);
            }
        }

        setAsBackground();
    }

    private void setAsBackground() {
        personPhoto.buildDrawingCache();
        Bitmap bitmap = personPhoto.getDrawingCache();
        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        int width = bitmap.getWidth();
        width = (width * height) / bitmap.getHeight();
        try {
            manager.setBitmap(Bitmap.createScaledBitmap(bitmap, width, height, true));
            Toast.makeText(this, "Photo was set as your wallpaper!", Toast.LENGTH_SHORT)
            .show();
        } catch (IOException e) {
            Toast.makeText(this, "Unable to set as your wallpaper! Contact developers!",
                    Toast.LENGTH_SHORT).show();
        }
    }


}