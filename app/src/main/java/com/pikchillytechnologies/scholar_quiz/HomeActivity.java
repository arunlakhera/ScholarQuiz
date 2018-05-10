package com.pikchillytechnologies.scholar_quiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    Dialog menuDialog;
    Bundle userBundle;
    String userId;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        menuDialog = new Dialog(this);

        userBundle = getIntent().getExtras();

        userName = userBundle.getString("userName","User Name");
/*
        // Direct User to channels user has subscribed to
        findViewById(R.id.myChannelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UserChannelActivity.class));
            }
        });

        // Direct User to all the channels available
        findViewById(R.id.allChannelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AllChannelListActivity.class));
            }
        });

        // Direct User to list of all Scores in all quiz user has participated
        findViewById(R.id.myScorecardButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MyScorecardChannelActivity.class));
            }
        });

        // Direct User to a list of Leaderboard showing the highest score in a particular quiz
        findViewById(R.id.leaderboardButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LeaderboardChannelActivity.class));
            }
        });
*/
    }

    /**
     * Menu Functions
     * */

    /**
     * 1. Function to Show Popup Menu
     * */
    public void showPopUp(View view) {

        menuDialog.setContentView(R.layout.menupopup);
        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtUserName = (TextView) menuDialog.getWindow().findViewById(R.id.textview_UserName);
        txtUserName.setText(userName);

        menuDialog.show();
    }

    /**
     * 2. Function to close Popup Menu
     * */
    public void closeMenu(View view) {
        menuDialog.dismiss();
    }

    /**
     * 3. Function to Show logout user
     * */

    public void logout(View view) {
        if (isNetworkAvailable()) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, SignInActivity.class));
            finish();
        } else {

            Toast.makeText(HomeActivity.this, "To Log Out, Please Connect your Phone to Internet..", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 4. Function to execute when user presses MyChannel
     * */

    public void myChannelPressed(View view) {

        Intent myChannelIntent = new Intent(HomeActivity.this, UserChannelActivity.class);

        myChannelIntent.putExtra("userName", userName);
        startActivity(myChannelIntent);

        finish();
    }

    /**
     * 5. Function to execute when user presses AllChannel
     * */

    public void allChannelPressed(View view) {

        Intent allChannelIntent = new Intent(HomeActivity.this, AllChannelListActivity.class);

        allChannelIntent.putExtra("userName", userName);
        startActivity(allChannelIntent);

        finish();
    }

    /**
     * 6. Function to execute when user presses MyScorecard
     * */

    public void myScorecardPressed(View view) {

        Intent myScorecardIntent = new Intent(HomeActivity.this, MyScorecardChannelActivity.class);

        myScorecardIntent.putExtra("userName", userName);
        startActivity(myScorecardIntent);

        finish();
    }

    /**
     * 7. Function to execute when user presses Leaderboard
     * */

    public void leaderboardPressed(View view) {

        Intent myLeaderboardIntent = new Intent(HomeActivity.this, LeaderboardChannelActivity.class);

        myLeaderboardIntent.putExtra("userName", userName);
        startActivity(myLeaderboardIntent);

        finish();
    }

    /**
     * Function to check if Device is connected to Internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
