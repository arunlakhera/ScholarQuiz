package com.pikchillytechnologies.scholar_quiz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminCreateChannelActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mChannelRef;
    private DatabaseReference mChannelListRef;
    private FirebaseUser user;
    String moderatorId;
    String moderatorName;
    String channelName;
    Boolean channelFlag = false;
    EditText channel_EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_channel);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mChannelRef = mDatabase.child("SQ_ChannelList/");

        user = FirebaseAuth.getInstance().getCurrentUser();
        channel_EditText = findViewById(R.id.editText_Channel);

        if (!isNetworkAvailable()) {
            Toast.makeText(AdminCreateChannelActivity.this,"To Create channel, Please Connect your Phone to Internet..",Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.button_CreateNewChannel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewChannel();

                if (channelFlag) {
                    Toast.makeText(AdminCreateChannelActivity.this, "Channel Created Successfully..", Toast.LENGTH_SHORT).show();

                    //Show Dialog box to Admin to Assign the Moderator now or later. If now take Admin to users Screen to select user

                    // Set Channel Edit text to blank
                    channel_EditText.setText("");

                    // Move the user to Channel List if user wants to assign the moderator to channel created

                    startActivity(new Intent(AdminCreateChannelActivity.this, AdminChannelListActivity.class));
                }
            }
        });

    }

    private void createNewChannel() {

        mChannelRef = mDatabase.child("ChannelList/").push();

        String bookKey = String.valueOf(mChannelRef.getKey());

        moderatorId = user.getUid();
        moderatorName = "Admin";

        // Get the values entered by user
        channelName = String.valueOf(channel_EditText.getText());

        if(channelName.isEmpty()) {
            Toast.makeText(AdminCreateChannelActivity.this,"Please Provide Channel Name..",Toast.LENGTH_SHORT).show();
        }else {
            // Save the Users information in Users table in Firebase
            mChannelListRef = mDatabase.child("SQ_ChannelList/"+ bookKey);

            mChannelListRef.child("Moderator").setValue(moderatorName);
            mChannelListRef.child("ModeratorID").setValue(moderatorId);
            mChannelListRef.child("Name").setValue(channelName);

            channelFlag = true;
        }

    }

    public void backButton(View view){
        startActivity(new Intent(AdminCreateChannelActivity.this, AdminHomeActivity.class));
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
