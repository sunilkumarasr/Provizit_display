  package com.provizit.provizitscheduler;

  import android.Manifest;
  import android.content.Context;
  import android.content.Intent;
  import android.content.IntentFilter;
  import android.content.SharedPreferences;
  import android.content.pm.PackageManager;
  import android.net.wifi.WifiManager;
  import android.os.Build;
  import android.os.Bundle;
  import android.util.Log;
  import android.view.animation.Animation;
  import android.view.animation.AnimationUtils;
  import android.widget.ImageView;

  import androidx.appcompat.app.AppCompatActivity;


  import java.io.BufferedReader;
  import java.io.DataOutputStream;
  import java.io.IOException;
  import java.io.InputStreamReader;

  import okhttp3.OkHttpClient;
  import okhttp3.Request;
  import okhttp3.Response;

  public class SplashActivity extends AppCompatActivity {
      ImageView upImage;
      Animation animationUp;
      IntentFilter intentfilter;
      private static final int REQUEST_WRITE_PERMISSION = 786;

      SharedPreferences.Editor editor1;
      SharedPreferences sharedPreferences1;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

          setContentView(R.layout.activity_splash);

          upImage = findViewById(R.id.upImage);
          sharedPreferences1 = SplashActivity.this.getSharedPreferences("Provizit_Schedular", MODE_PRIVATE);
          editor1 = sharedPreferences1.edit();


          splashAnimation();

      }


      @Override
      public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
          super.onRequestPermissionsResult(requestCode, permissions, grantResults);
          if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              splashAnimation();
          }
      }

      public void splashAnimation() {
          animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
          upImage.setAnimation(animationUp);
          Thread t = new Thread() {
              @Override
              public void run() {
                  try {
                      sleep(1200);
                  } catch (Exception ignored) {

                  } finally {
                      if (sharedPreferences1.getInt("isloginProvizit", 0) == 1) {
                          Intent intent = new Intent(SplashActivity.this, MeetingsRoomMeetings.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                          finish();
                      } else {
                          Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                          finish();
                      }
                  }
              }
          };
          t.start();
      }
  }

