package com.crew.lucas.intercorpuser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private EditText mNameEditText;
    private EditText mLastNameEditText;
    private EditText mAgeEditText;
    private EditText mBirthdateEditText;
    private Button mRecordInfoButton;

    private FirebaseUser mFBUser;
    private DatabaseReference mUsersDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabaseReference.addChildEventListener(mUsersListener);
        mFBUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFBUser != null) {
            mAgeEditText = findViewById(R.id.age_label_et);
            mNameEditText = findViewById(R.id.name_label_et);
            mLastNameEditText = findViewById(R.id.lastName_label_et);
            mBirthdateEditText = findViewById(R.id.birthdate_label_et);
            mRecordInfoButton = findViewById(R.id.record_info_button);
            checkUserAvailableInfo();
        } else {
            goLoginScreen();
        }
    }


    private void checkUserAvailableInfo() {
        final DatabaseReference usersReference = mUsersDatabaseReference.child(mFBUser.getUid());
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    populateUserInfo(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void populateUserInfo(User usersReference) {

        mRecordInfoButton.setText(R.string.save_changes);

        mNameEditText.setText(usersReference.getName());
        mLastNameEditText.setText(usersReference.getLastName());
        mAgeEditText.setText(Integer.toString(usersReference.getAge()));
        mBirthdateEditText.setText(usersReference.getBirthdate());
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginAuthUi.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    public void recordInfo(View view) {
        User userReady = new User(mNameEditText.getText().toString(),
                mLastNameEditText.getText().toString(),
                Integer.parseInt(mAgeEditText.getText().toString()),
                mBirthdateEditText.getText().toString());
        mUsersDatabaseReference.child(mFBUser.getUid()).setValue(userReady);
    }

    private ChildEventListener mUsersListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            //Toast.makeText(getApplicationContext(), R.string.new_user_added, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Toast.makeText(getApplicationContext(), R.string.user_modified, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            //User remove implementation
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
