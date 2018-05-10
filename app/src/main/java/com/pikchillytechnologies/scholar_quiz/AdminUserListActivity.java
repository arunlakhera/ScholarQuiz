package com.pikchillytechnologies.scholar_quiz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminUserListActivity extends AppCompatActivity {

    ListView userListView;

    CustomAdminAllUserAdapter customAdapter;
    List<UserList> allUserList;
    UserList user = new UserList();

    private DatabaseReference mDatabase;
    private DatabaseReference mUsersRef;
    private DatabaseReference mChannelListRef;

    String channelId;
    String channelName;
    String moderatorName;
    String moderatorId;

    String adminFlag;
    String emailId;
    String moderatorFlag;
    String userName;
    String slackId;
    String userId;

    Bundle channelBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_listview);

        channelBundle = getIntent().getExtras();
        channelId = channelBundle.getString("channelId","Channel Id");
        channelName = channelBundle.getString("channelName","Channel Name");
        moderatorName = channelBundle.getString("moderatorName","Moderator Name");
        moderatorId = channelBundle.getString("moderatorId","Moderator Id");

        userListView = findViewById(R.id.listView_AllUsers);
        allUserList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUsersRef = mDatabase.child("SQ_Users/");
        mChannelListRef = mDatabase.child("SQ_ChannelList/");

        if (!isNetworkAvailable()) {
            Toast.makeText(AdminUserListActivity.this,"Please Connect your Phone to Internet..",Toast.LENGTH_SHORT).show();
        }


        mUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersListSnapshot) {
                allUserList.clear();

                if (usersListSnapshot.getChildrenCount() < 1) {
                    Toast.makeText(AdminUserListActivity.this, "No Users Available", Toast.LENGTH_LONG).show();

                } else {

                    for(DataSnapshot usersListSnap : usersListSnapshot.getChildren()) {

                        adminFlag = String.valueOf(usersListSnap.child("AdminFlag").getValue());
                        emailId = String.valueOf(usersListSnap.child("EmailId").getValue());
                        moderatorFlag = String.valueOf(usersListSnap.child("ModeratorFlag").getValue());
                        userName = String.valueOf(usersListSnap.child("Name").getValue());
                        slackId = String.valueOf(usersListSnap.child("SlackId").getValue());
                        userId = String.valueOf(usersListSnap.getKey());

                        user = usersListSnap.getValue(UserList.class);
                        allUserList.add(new UserList(adminFlag,emailId,moderatorFlag,userName,slackId,userId,channelId,channelName));
                        customAdapter = new CustomAdminAllUserAdapter(getApplicationContext(), allUserList);
                        userListView.setAdapter(customAdapter);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Variable to store selected item
                UserList itemSelected = allUserList.get(position);

                // Variables to store Channel Information to pass to quizList Activity
                channelId = String.valueOf(itemSelected.getChannelId());
                channelName = String.valueOf(itemSelected.getChannelName());
                moderatorName = String.valueOf(itemSelected.getName());
                moderatorId = String.valueOf(itemSelected.getUserId());

                userId = String.valueOf(itemSelected.getUserId());
                adminFlag = String.valueOf(itemSelected.getAdminFlag());
                moderatorFlag = String.valueOf(itemSelected.getModeratorFlag());

                if (moderatorFlag.equals("No")){

                    Toast.makeText(AdminUserListActivity.this,"You selected " + moderatorName + " as Moderator for Channel " + channelName,Toast.LENGTH_SHORT).show();

                    updateData();

                    Toast.makeText(AdminUserListActivity.this, moderatorName + " is now Moderator of channel " + channelName,Toast.LENGTH_SHORT).show();



                } else {
                    Toast.makeText(AdminUserListActivity.this,moderatorName + " is already a Moderator. Please select other User" ,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void updateData(){

        mUsersRef.child(moderatorId).child("ModeratorFlag").setValue("Yes");
        mChannelListRef.child(channelId).child("Moderator").setValue(moderatorName);
        mChannelListRef.child(channelId).child("ModeratorID").setValue(moderatorId);

    }

    public void goBackButton(View view) {

        Intent backIntent = new Intent(AdminUserListActivity.this, AdminChannelListActivity.class);
        backIntent.putExtra("userName", userName);
        startActivity(backIntent);

    }

    public void homeButton(View view){
        Intent homeIntent = new Intent(AdminUserListActivity.this, AdminHomeActivity.class);
        homeIntent.putExtra("userName", userName);
        startActivity(homeIntent);
    }
    /**
     * Function to check if Device is connected to Internet
     * */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
