package com.example.divya.sparkssmiapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    TwitterLoginButton LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends","user_gender"));

        LoginButton = findViewById(R.id.tlogin_button);


        LoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();

                loginMethod(session);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_LONG).show();
            }
        });
        checkLoginStatus();

      loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
            }
            @Override
            public void onCancel()
            {
            }
            @Override
            public void onError(FacebookException error)
            {
            }
        });
    }

   AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
        {
            if(currentAccessToken==null)
            {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);

                Toast.makeText(MainActivity.this,"User Logged out",Toast.LENGTH_LONG).show();
            }
            else
                loadUserProfile(currentAccessToken);
        }
    };

    private void loadUserProfile(AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                try {
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/"+id+ "/picture?width=250&height=250";
                    String birth="Birthday: "+object.getString("birthday");
                    String friend="Number Of Friends: " + object.getJSONObject("friends").getJSONObject("summary").getString("total_count");
                    String mail="Email: "+object.getString("email");
                    String sex="Gender: "+object.getString("gender");
                    String fname="Name: "+object.getString("first_name")+" "+object.getString("last_name");

                    Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                    Bundle bun=new Bundle();
                    bun.putString("birth1",birth);
                    bun.putString("friend1",friend);
                    bun.putString("mail1",mail);
                    bun.putString("sex1",sex);
                    bun.putString("fname1",fname);
                    bun.putString("image",image_url);
                    i.putExtras(bun);
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","id,email,birthday,friends,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void checkLoginStatus()
    {
        if(AccessToken.getCurrentAccessToken()!=null)
        {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }

    public void loginMethod(TwitterSession twitterSession){
        String userName=twitterSession.getUserName();
        TwitterCore.getInstance().getApiClient(twitterSession).getAccountService().verifyCredentials(false,true,false).enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                try {
                    User userInfo=userResult.data;
                    String email="Email: "+userInfo.email;
                    String fn="Name: "+userInfo.name;
                    String des=userInfo.description;
                    String loc="Location: "+userInfo.location;
                    Integer fc=userInfo.friendsCount;
                    Integer fw=userInfo.friendsCount;
                    String pic=userInfo.profileImageUrl.replace("_normal","");

                    Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                    Bundle bun=new Bundle();
                    bun.putString("birth1","FollowersCount: "+fw.toString());
                    bun.putString("friend1","FriendsCount: "+fc.toString());
                    bun.putString("mail1",des);
                    bun.putString("sex1",loc);
                    bun.putString("fname1",fn);
                    bun.putString("image",pic);

                    i.putExtras(bun);
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(TwitterException e) {
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        LoginButton.onActivityResult(requestCode, resultCode, data);
    }

}