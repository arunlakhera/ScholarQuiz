package com.pikchillytechnologies.scholar_quiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyScorecardActivity extends AppCompatActivity {

    String channelId;
    String channelName;
    String moderatorName;
    String moderatorId;

    String quizListKey;
    String quizTitle;
    String totalQuestions;
    String correctAnswers;
    String totalNotAttemptedAnswers;
    String score;
    String wrong;
    String userName;
    String userEmail;

    StorageReference downloadImageStorageReference;
    FirebaseStorage storage;
    Bitmap bitmap;
    ImageView imageView_UserPhoto;

    Dialog menuDialog;

    FirebaseUser user;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scorecard);

        menuDialog = new Dialog(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = String.valueOf(user.getUid());

        storage = FirebaseStorage.getInstance();
        downloadImageStorageReference = storage.getReferenceFromUrl("gs://scholarquiz-e4b55.appspot.com").child("images/").child(userId);

        // Declare variable to get values passed from channelActivity
        Bundle quizScoreBundle = getIntent().getExtras();

        channelId = quizScoreBundle.getString("channelId", "Channel ID Default");
        channelName = quizScoreBundle.getString("channelName", "Channel Name Default");
        moderatorName = quizScoreBundle.getString("moderatorName", "Moderator Name Default");
        moderatorId = quizScoreBundle.getString("moderatorId", "Moderator ID Default");
        //quizListKey = quizScoreBundle.getString("quizListKey", "Quiz Key");
        quizTitle = quizScoreBundle.getString("quizTitle", "Quiz Title");
        totalQuestions = quizScoreBundle.getString("totalQuestions","Total Questions");
        correctAnswers = quizScoreBundle.getString("correct","Correct Answers");
        totalNotAttemptedAnswers = quizScoreBundle.getString("notAttempted", "Not Attempted");
        score = quizScoreBundle.getString("score","Score");
        wrong = quizScoreBundle.getString("wrong","Wrong");
        userName = quizScoreBundle.getString("userName","");
        userEmail = quizScoreBundle.getString("userEmail","User Email");

        showScore();

        findViewById(R.id.button_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent backIntent = new Intent(MyScorecardActivity.this, MyScoreQuizListActivity.class);

                backIntent.putExtra("channelId",channelId);
                backIntent.putExtra("channelName",channelName);
                backIntent.putExtra("moderatorName",moderatorName);
                backIntent.putExtra("moderatorId",moderatorId);
                startActivity(backIntent);

            }
        });

    }

    public void showScore(){

        Integer totalQues = Integer.valueOf(totalQuestions);
        Integer correctAns = Integer.valueOf(correctAnswers);
        Integer totalNotAttempted = Integer.valueOf(totalNotAttemptedAnswers);
        Integer wrongAns = (totalQues - (correctAns + totalNotAttempted));

        // Set the Title of the Quiz
        TextView quizTitleTextView = findViewById(R.id.textView_QuizTitle);
        quizTitleTextView.setText(quizTitle + " Score");

        //Set Name of User
        TextView nameTextView = findViewById(R.id.name_TextView);

        if(!userName.isEmpty()) {
            nameTextView.setText(userName.toUpperCase());
        }else {
            nameTextView.setText(userEmail.toUpperCase());
        }

        //Set Total Questions
        TextView totalQuestionTextView = findViewById(R.id.total_questions_TextView);
        totalQuestionTextView.setText(totalQuestions);

        //Set Correct Answers
        TextView correctAnswersTextView = findViewById(R.id.correct_answers_TextView);
        correctAnswersTextView.setText(correctAnswers);

        //Set Wrong Answers
        TextView wrongAnswersTextView = findViewById(R.id.wrong_Answers_TextView);
        wrongAnswersTextView.setText(String.valueOf(wrongAns));

        //Set Not Attempted Answers
        TextView notAttemptedTextView = findViewById(R.id.notAttempted_TextView);
        notAttemptedTextView.setText(totalNotAttemptedAnswers);


        //Set Not Attempted Answers
        TextView scoreTextView = findViewById(R.id.score_TextView);
        //scoreTextView.setText(score + " Points");

        // Setting RatingBar
        RatingBar scoreRatingBar = (RatingBar) findViewById(R.id.score_RatingBar);

        Integer totalpercent = Integer.valueOf((correctAns * 100)/totalQues);
        scoreTextView.setText(score + " Points");

        if (totalpercent > 90) {
            // 5 star
            scoreRatingBar.setRating(5);

        }else if (totalpercent > 80) {
            // 4.5 star
            scoreRatingBar.setRating((float) 4.5);

        }else if (totalpercent > 70) {
            // 4 star
            scoreRatingBar.setRating(4);

        }else if (totalpercent > 60) {
            // 3.5 star
            scoreRatingBar.setRating((float) 3.5);

        }else if (totalpercent > 50) {
            // 3 star
            scoreRatingBar.setRating(3);

        }else if (totalpercent > 40) {
            // 2 star
            scoreRatingBar.setRating(2);
        }else {
            // 1 star
            scoreRatingBar.setRating(1);
        }
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

        // Download User Image from Firebase and show it to User.
        final long ONE_MEGABYTE = 1024 * 1024;
        downloadImageStorageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView_UserPhoto.setImageBitmap(bitmap);

            }
        });

        imageView_UserPhoto = menuDialog.getWindow().findViewById(R.id.imageview_UserImage);

        if(bitmap != null) {
            imageView_UserPhoto.setImageBitmap(bitmap);
        }else {
            imageView_UserPhoto.setImageResource(R.drawable.userimage_default);
        }


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
            startActivity(new Intent(MyScorecardActivity.this, SignInActivity.class));
            finish();
        } else {

            Toast.makeText(MyScorecardActivity.this, "To Log Out, Please Connect your Phone to Internet..", Toast.LENGTH_SHORT).show();

        }
    }


    public void editProfilePressed(View view) {

        Intent editProfileIntent = new Intent(MyScorecardActivity.this, UserProfileActivity.class);
        startActivity(editProfileIntent);

        finish();

    }

    /**
     * 4. Function to execute when user presses MyChannel
     * */

    public void myChannelPressed(View view) {

        Intent myChannelIntent = new Intent(MyScorecardActivity.this, UserChannelActivity.class);
        myChannelIntent.putExtra("userName", userName);
        startActivity(myChannelIntent);

        finish();
    }

    /**
     * 5. Function to execute when user presses AllChannel
     * */

    public void allChannelPressed(View view) {

        Intent allChannelIntent = new Intent(MyScorecardActivity.this, AllChannelListActivity.class);
        allChannelIntent.putExtra("userName", userName);
        startActivity(allChannelIntent);

        finish();
    }

    /**
     * 6. Function to execute when user presses MyScorecard
     * */

    public void myScorecardPressed(View view) {

        Intent myScorecardIntent = new Intent(MyScorecardActivity.this, MyScorecardChannelActivity.class);

        myScorecardIntent.putExtra("userName", userName);
        startActivity(myScorecardIntent);

        finish();
    }

    /**
     * 7. Function to execute when user presses Leaderboard
     * */

    public void leaderboardPressed(View view) {

        Intent myLeaderboardIntent = new Intent(MyScorecardActivity.this, LeaderboardChannelActivity.class);

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

    public void homeButton(View view){
        Intent homeIntent = new Intent(MyScorecardActivity.this, HomeActivity.class);
        homeIntent.putExtra("userName", userName);
        startActivity(homeIntent);
    }

}
