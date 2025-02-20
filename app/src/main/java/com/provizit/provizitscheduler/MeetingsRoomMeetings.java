package com.provizit.provizitscheduler;

import android.annotation.SuppressLint;
import android.app.Dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.provizit.provizitscheduler.Services.CompanyData;
import com.provizit.provizitscheduler.Services.DataManger;
import com.provizit.provizitscheduler.Services.Model;
import com.provizit.provizitscheduler.Services.Model1;
import com.provizit.provizitscheduler.Services.OutlookItems;
import com.provizit.provizitscheduler.Services.OutlookModel;
import com.provizit.provizitscheduler.Utilities.CustomAdapter;
import com.provizit.provizitscheduler.Utilities.EmpData;
import com.provizit.provizitscheduler.Utilities.Invited;
import com.provizit.provizitscheduler.Utilities.LocationData;
import com.provizit.provizitscheduler.Utilities.PeopleAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingsRoomMeetings extends AppCompatActivity {
    static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    TextView presentTime;
    TextView txtTRDDate;
    TextView txtTRDTime;
    EthernetDataReceiver ethernetDataReceiver;
    TextView inviteesT;
    ImageView logo;
    TextView meetingRoom;
    TextView txtTRDmeetingRoom;
    TextView hostName;
    TextView hostNameTrd;
    TextView subjectTrd;
    TextView invitee1Name;
    TextView invitee2Name;
    TextView m_Status;
    TextView txtStatusTrd;
    TextView m_timeText;
    ImageView m_image;
    TextView m_timeTextTrd;
    TextView timeTrd;
    TextView count;
    TextView tag;
    TextView txtRuningTimer;
    CircleImageView hostImg;
    CircleImageView invitee1Image;
    CircleImageView invitee2Image;
    RecyclerView meetingsRecyclerView;
    LinearLayout cardNoMeetings;
    View viewTRD;
    LinearLayout linearListTRD;
    RecyclerView meetingsRecyclerViewTrd;
    RelativeLayout m_time;
    LinearLayout cardTrdStatus;
    ArrayList<CompanyData> meetingrooms;
    CustomAdapter customAdapter1;
    AutoCompleteTextView meeting_room;
    AutoCompleteTextView location;
    PeopleAdapter adapter1;
    ArrayList<LocationData> companyAddressArrayList;
    String meetingRoomId;
    String meetingRoomName;
    String meetingRoomEmail;
    SharedPreferences sharedPreferences1;
    String rm_id;
    SharedPreferences.Editor editor1;
    MeetingsDateAdapter meetingListAdapter;
    TrainingDateAdapter traingingAdapterTrd;
    LinearLayout invitee2layout;
    LinearLayout mrd;
    RelativeLayout trd;
    LinearLayout meetinglayout;
    LinearLayout meetinglayoutTrd;
    LinearLayout invitee1layout;
    int Position;
    long p_time;

    ArrayList<CompanyData> meetings;
    Dialog dialog;
    Dialog Nodialog;
    Dialog passwordDialog;
    ArrayList<OutlookItems> outLookMeetings;
    AlertDialog.Builder builder;
    Boolean comprobations = false;
    String meetingEmailPop;
    Boolean isTrd;
    Thread t;
    Thread t1;
    Thread t5;
    Thread t4;
    AESUtil aesUtil;
    TextView confidentialData;
    TextView confidentialDataTrd;

    LinearLayout linearMRD;
    LinearLayout linearTRD;

    //trd timer
    String T1 = "0";
    String T2 = "0";

    public static Calendar getEndOfADay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }

    public static String getDay() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return (dayOfWeek - 1) + "";
    }

    public static String millitotime(Long millSec, Boolean is24hours) {
        DateFormat simple = new SimpleDateFormat("hh:mm aa");
        if (is24hours) {
            simple = new SimpleDateFormat("HH:mm");
        }
        Date result = new Date(millSec);
        return simple.format(result) + "";
    }

    public static String millitoDate(Long millSec) {
        DateFormat simple = new SimpleDateFormat("yyyyMMdd");

        Date result = new Date(millSec);
        return simple.format(result) + "";
    }

    public static String checkNextDay(String todayEnd, String nextDayStart, String nextDay) {
        int numDays = 7; // Number of days to increment
//        adbcommand("/system/xbin/test 202303151333 202303151340 enable");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat day = new SimpleDateFormat("EEE");

        Calendar calendar = Calendar.getInstance();
        String today = sdf.format(calendar.getTime());
        String shutdowntime = todayEnd.replace(":", "");
        if (shutdowntime.length() == 3) {
            shutdowntime = 0 + shutdowntime;
        }


        String dateStr = today + shutdowntime + " ";
        String onTime = nextDayStart.replace(":", "");
        if (onTime.length() == 3) {
            onTime = 0 + onTime;
        }


        for (int i = 0; i < numDays; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            if ((day.format(calendar.getTime()).toUpperCase()).equals(nextDay)) {

                dateStr = dateStr + sdf.format(calendar.getTime()) + onTime;

                break;
            }
        }
        return dateStr;
    }

    public static String afterEndTime(String nextDayStart, String nextDay) {
        int numDays = 7; // Number of days to increment
//        adbcommand("/system/xbin/test 202303151333 202303151340 enable");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat shutdownFormat = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat day = new SimpleDateFormat("EEE");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        String today = shutdownFormat.format(calendar.getTime());
        String dateStr = today + " ";
        String onTime = nextDayStart.replace(":", "");
        if (onTime.length() == 3) {
            onTime = 0 + onTime;
        }
        for (int i = 0; i < numDays; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            if ((day.format(calendar.getTime()).toUpperCase()).equals(nextDay)) {

                dateStr = dateStr + sdf.format(calendar.getTime()) + onTime;

                break;
            }
        }
        return dateStr;
    }

    private static String TodayHolidayDeviceOpenned(String nextDayStart, String nextDay) {
        int numDays = 7; // Number of days to increment
//        adbcommand("/system/xbin/test 202303151333 202303151340 enable");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat shutdownFormat = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat day = new SimpleDateFormat("EEE");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        String today = shutdownFormat.format(calendar.getTime());

        String dateStr = today + " ";
        String onTime = nextDayStart.replace(":", "");
        if (onTime.length() == 3) {
            onTime = 0 + onTime;
        }


        for (int i = 0; i < numDays; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            if ((day.format(calendar.getTime()).toUpperCase()).equals(nextDay)) {

                dateStr = dateStr + sdf.format(calendar.getTime()) + onTime;

                break;
            }
        }
        return dateStr;
    }

    private static String beforeStartTime(String Start) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat shutdownFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        String today = shutdownFormat.format(calendar.getTime());
        String todayDate = sdf.format(calendar.getTime());
        String onTime = Start.replace(":", "");
        if (onTime.length() == 3) {
            onTime = 0 + onTime;
        }
        return today + " " + todayDate + onTime;
    }

    public static int timezone() {
        Date d = new Date();
        return d.getTimezoneOffset() * 60;
    }

    public static AnimationSet animation() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
//        animation.addAnimation(fadeOut);

        return animation;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BroadcastReceiver mNetworkReceiver;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_meetings_room_meetings);
        ResouresIds();

        builder = new AlertDialog.Builder(MeetingsRoomMeetings.this);
        outLookMeetings = new ArrayList<>();
        aesUtil = new AESUtil(MeetingsRoomMeetings.this);
//        mNetworkReceiver = new NetworkChangeReceiver();
//        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        ethernetDataReceiver = new EthernetDataReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(ethernetDataReceiver, filter);

        adbcommand("echo w 0x03 > ./sys/devices/platform/led_con_h/zigbee_reset");


        passwordDialog = new Dialog(MeetingsRoomMeetings.this);
        dialog = new Dialog(MeetingsRoomMeetings.this);
        Nodialog = new Dialog(MeetingsRoomMeetings.this);
        Nodialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Nodialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Nodialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Nodialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_item);
        meeting_room = dialog.findViewById(R.id.room_spinner);
        location = dialog.findViewById(R.id.location_spinner);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        meetings = new ArrayList<>();

        sharedPreferences1 = MeetingsRoomMeetings.this.getSharedPreferences("Provizit_Schedular", MODE_PRIVATE);
        meetingRoomId = sharedPreferences1.getString("rm_id", "");
        meetingRoomEmail = sharedPreferences1.getString("rm_email", "");
        isTrd = sharedPreferences1.getBoolean("isTrd", false);
        Position = sharedPreferences1.getInt("meetingRoomPosition", 0);

        Log.e("abc_",isTrd.toString());

        if (isTrd) {
            mrd.setVisibility(View.GONE);
            linearMRD.setVisibility(View.GONE);
            trd.setVisibility(View.VISIBLE);
            linearTRD.setVisibility(View.VISIBLE);
        } else {
            trd.setVisibility(View.GONE);
            linearTRD.setVisibility(View.GONE);
            mrd.setVisibility(View.VISIBLE);
            linearMRD.setVisibility(View.VISIBLE);
        }

        meetingRoomName = sharedPreferences1.getString("rm_name", "");
        editor1 = sharedPreferences1.edit();

        meetingRoom.setText(meetingRoomName);
        txtTRDmeetingRoom.setText(meetingRoomName);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MeetingsRoomMeetings.this);
        meetingsRecyclerView.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(MeetingsRoomMeetings.this);
        meetingsRecyclerViewTrd.setLayoutManager(linearLayoutManager1);
        Calendar cal = Calendar.getInstance();

        int dayInt = cal.get(Calendar.DAY_OF_WEEK) - 1;

        meetingRoom.setOnClickListener(v -> verifyPassword());
        txtTRDmeetingRoom.setOnClickListener(v -> verifyPassword());
        count.setOnClickListener(v -> inviteessPopup(0));
//        if (isDeviceOwnerApp(MeetingsRoomMeetings.this)){
//            Log.d("{TAG}", "is device owner");
//            Toast.makeText(this,"is device owner", Toast.LENGTH_LONG).show();
//        }else{
//            Log.d("TAG", "not device owner");
//            Toast.makeText(this,"not device owner", Toast.LENGTH_LONG).show();
//        }

        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000 * 30);
                        runOnUiThread(() -> {
//                                TextView tdate = (TextView) findViewById(R.id.date);


//                                mytextview.setText();
                            //                                if (secondString.equals("00")){
                            Calendar cal1 = Calendar.getInstance();
                            if (!meetingRoomEmail.equals("")) {
//                                    getrmslots(meetingRoomId,(cal.getTimeInMillis()/1000) - timezone(),(getEndOfADay(cal.getTime()).getTimeInMillis()/1000) - timezone());
                                getappointments(meetingRoomEmail, meetingRoomId, (cal1.getTimeInMillis() / 1000) - timezone(), (getEndOfADay(cal1.getTime()).getTimeInMillis() / 1000) - timezone());
//                                    getMicrosoftMeetings(meetingRoomEmail);


                            }


//                                }


                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
        t1 = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(() -> {
//                                TextView tdate = (TextView) findViewById(R.id.date);
                            long date = System.currentTimeMillis();

//                                mytextview.setText();

                            ledColors();

                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                            String dateString = sdf.format(date);
                            SimpleDateFormat sdf1 = new SimpleDateFormat("EE dd MMM yyyy");
                            String dateString1 = sdf1.format(date);
                            String sourceString = "<b>" + dateString + "</b> " + " | " + dateString1;
                            presentTime.setText(Html.fromHtml(sourceString));

                            //TRD date
                            String c_timeformattedTime = dateString.toUpperCase();
                            txtTRDTime.setText(c_timeformattedTime);
                            SimpleDateFormat TRDsdf1 = new SimpleDateFormat("EE, dd MMM yyyy");
                            String TRDDate = TRDsdf1.format(date);
                            txtTRDDate.setText(TRDDate);
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t1.start();

        logo.setOnClickListener(v -> {

//                PackageManager pm = getPackageManager();
//                pm.clearPackagePreferredActivities ("com.provizit.provizitscheduler");
//                finish();
//                Intent startMain = new Intent(Intent.ACTION_MAIN);
//                startMain.addCategory("android.intent.category.MONKEY");
//                startActivity(startMain);
        });


//        try {
//                    installPackage(MeetingsRoomMeetings.this,Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/provizit.apk");
//                } catch (IOException e) {
        //                    e.printStackTrace();
//                }
        if (meetingRoomEmail.equals("")) {
            getuserDetails(1);
        } else {
            getuserDetails(0);
            if (!meetingRoomEmail.equals("")) {
                getappointments(meetingRoomEmail, meetingRoomId, (cal.getTimeInMillis() / 1000) - timezone(), (getEndOfADay(cal.getTime()).getTimeInMillis() / 1000) - timezone());

//                getrmslots(meetingRoomId,(cal.getTimeInMillis()/1000) - timezone(),(getEndOfADay(cal.getTime()).getTimeInMillis()/1000) - timezone());
            }
        }
        autoShutdown(dayInt);
//        Thread t1 = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    while (!isInterrupted()) {
//                        Thread.sleep(60000);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Calendar cal = Calendar.getInstance();
//
//
//
//                                if(!meetingRoomId.equals("")){
//                                    getrmslots(meetingRoomId,(cal.getTimeInMillis()/1000)  ,(getEndOfADay(cal.getTime()).getTimeInMillis()/1000)  );
//
//                                }
////                                TextView tdate = (TextView) findViewById(R.id.date);
////                                long date = System.currentTimeMillis();
////                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a \n MMM dd yyyy");
////                                String dateString = sdf.format(date);
////                                System.out.println("Date & time"+dateString);
////                                presentTime.setText(dateString);
//////                                tdate.setText(dateString);
//                            }
//                        });
//                    }
//                } catch (InterruptedException e) {
//                }
//            }
//        };
//        t1.start();
    }

    private void setLeftData() {

        String s_time = millitotime((meetings.get(0).getStart() + timezone()) * 1000, false);
        String e_time = millitotime((meetings.get(0).getEnd() + 1 + timezone()) * 1000, false);
        m_timeText.setText(s_time + " - " + e_time);
        timeTrd.setText(s_time + " - " + e_time);


        if (meetings.get(0).getCategoryData() != null && meetings.get(0).getCategoryData().getCat_type()) {
            meetinglayout.setVisibility(View.GONE);
            meetinglayoutTrd.setVisibility(View.GONE);
            confidentialData.setVisibility(View.VISIBLE);
            confidentialDataTrd.setVisibility(View.VISIBLE);
        } else {
            confidentialData.setVisibility(View.GONE);
            confidentialDataTrd.setVisibility(View.GONE);
            meetinglayout.setVisibility(View.VISIBLE);
            meetinglayoutTrd.setVisibility(View.VISIBLE);
            hostName.setText(meetings.get(0).getHost().getName());
            hostNameTrd.setText(meetings.get(0).getHost().getName());
            subjectTrd.setText(meetings.get(0).getSubject());
            if (!meetings.get(0).getInvites().isEmpty()) {
                invitee1layout.setVisibility(View.VISIBLE);
                inviteesT.setVisibility(View.VISIBLE);
                invitee1Name.setText(meetings.get(0).getInvites().get(0).getName());
                if (meetings.get(0).getInvites().get(0).getName().equals("") || meetings.get(0).getInvites().get(0).getName().equals("x")) {
                    invitee1Name.setText(meetings.get(0).getInvites().get(0).getEmail());
                    if (meetings.get(0).getInvites().get(0).getEmail().equals("")) {
                        invitee1Name.setText(meetings.get(0).getInvites().get(0).getMobile());

                    }

                }
            } else {
                invitee1layout.setVisibility(View.GONE);
                inviteesT.setVisibility(View.GONE);
            }
            if (meetings.get(0).getInvites().size() > 1) {

                invitee2Name.setText(meetings.get(0).getInvites().get(1).getName());
                if (meetings.get(0).getInvites().get(1).getName().equals("") || meetings.get(0).getInvites().get(1).getName().equals("x")) {
                    invitee1Name.setText(meetings.get(0).getInvites().get(1).getEmail());
                    if (meetings.get(0).getInvites().get(1).getEmail().equals("")) {
                        invitee1Name.setText(meetings.get(0).getInvites().get(1).getMobile());

                    }
                }
                invitee2layout.setVisibility(View.VISIBLE);
                if (meetings.get(0).getInvites().size() > 2) {
                    count.setVisibility(View.VISIBLE);
                    int size = meetings.get(0).getInvites().size();
                    size = size - 2;
                    count.setText("+" + size + " more..");
                } else {
                    count.setVisibility(View.GONE);
                }
            } else {
                count.setVisibility(View.GONE);
                invitee2layout.setVisibility(View.GONE);
            }
        }
        meetingsRecyclerView.setAdapter(meetingListAdapter);
        meetingsRecyclerView.setVisibility(View.VISIBLE);

//        meetingsRecyclerViewTrd.setAdapter(traingingAdapterTrd);
//        viewTRD.setVisibility(View.VISIBLE);
//        linearListTRD.setVisibility(View.VISIBLE);

    }

    public String adbcommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        String excresult = "";
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line + " ");
            }
            excresult = stringBuffer.toString();
            os.close();
            // System.out.println(excresult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return excresult;
    }

    public void ResouresIds() {
        presentTime = findViewById(R.id.presentTime);
        txtTRDTime = findViewById(R.id.txtTRDTime);
        txtTRDDate = findViewById(R.id.txtTRDDate);
        invitee1layout = findViewById(R.id.invitee1layout);
        m_time = findViewById(R.id.m_time);
        logo = findViewById(R.id.logo);
        m_Status = findViewById(R.id.m_Status);
        meetingRoom = findViewById(R.id.meetingRoom);
        txtTRDmeetingRoom = findViewById(R.id.txtTRDmeetingRoom);
        txtRuningTimer = findViewById(R.id.txtRuningTimer);
        hostImg = findViewById(R.id.hostImg);
        hostName = findViewById(R.id.hostName);
        invitee1Image = findViewById(R.id.invitee1Image);
        invitee2Image = findViewById(R.id.invitee2Image);
        invitee1Name = findViewById(R.id.invitee1Name);
        invitee2Name = findViewById(R.id.invitee2Name);
        meetingsRecyclerView = findViewById(R.id.meetingsRecyclerView);
        cardNoMeetings = findViewById(R.id.cardNoMeetings);
        invitee2layout = findViewById(R.id.invitee2layout);
        m_timeText = findViewById(R.id.m_timeText);
        m_image = findViewById(R.id.m_image);
        meetinglayout = findViewById(R.id.meetinglayout);

        count = findViewById(R.id.count);
        mrd = findViewById(R.id.mrd);
        linearMRD = findViewById(R.id.linearMRD);
        trd = findViewById(R.id.trd);
        linearTRD = findViewById(R.id.linearTRD);
        count = findViewById(R.id.count);
        inviteesT = findViewById(R.id.inviteesT);
        confidentialData = findViewById(R.id.Confidential);

        //Trd
        cardTrdStatus = findViewById(R.id.cardTrdStatus);
        txtStatusTrd = findViewById(R.id.txtStatusTrd);

        hostNameTrd = findViewById(R.id.hostName_trd);
        subjectTrd = findViewById(R.id.subjectTrd);

        viewTRD = findViewById(R.id.viewTRD);
        linearListTRD = findViewById(R.id.linearListTRD);
        meetingsRecyclerViewTrd = findViewById(R.id.meetingsRecyclerViewTrd);

        m_timeTextTrd = findViewById(R.id.m_timeTextTrd);
        timeTrd = findViewById(R.id.timeTrd);
        meetinglayoutTrd = findViewById(R.id.meetinglayoutTrd);

//        inviteesT = findViewById(R.id.inviteesT);
        confidentialDataTrd = findViewById(R.id.confidentialDataTrd);

    }

    private void getmeetingrooms(String locationId) {

        DataManger dataManager = DataManger.getDataManager();
        dataManager.getMeetingRooms(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                Model1 model = response.body();


                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200;
                    Integer failurecode = 401;
                    Integer not_verified = 404;
                    if (statuscode.equals(failurecode)) {


                    } else if (statuscode.equals(not_verified)) {
//                        progressBar.setVisibility(View.GONE);

                    } else if (statuscode.equals(successcode)) {

                        meetingrooms = new ArrayList<>();

                        for (int i = 0; i < model.getItems().size(); i++) {
                            if (model.getItems().get(i).getActive()) {
                                meetingrooms.add(model.getItems().get(i));
                            }
                        }
//                        meetingrooms = model.getItems();


                        customAdapter1 = new CustomAdapter(MeetingsRoomMeetings.this, R.layout.row, R.id.lbl_name, meetingrooms, 0, "");
                        if (!meetingrooms.isEmpty()) {
                            meeting_room.setText(meetingRoomName);
                            //     meetingRoomId = meetingrooms.get(0).get_id().get$oid();
                            //     meetingRoomEmail = meetingrooms.get(0).getRm_email();
                            //   Position = 0;
                            meeting_room.setThreshold(1);//will start working from first character
                            meeting_room.setAdapter(customAdapter1);//setting the adapter data into the AutoCompleteTextView
                            meeting_room.setEnabled(true);
                            meeting_room.dismissDropDown();
                        }
                        //                        System.out.println("Cabin name"+meetingrooms.get(1).getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
//                                            progressDialog.dismiss();

            }
        }, MeetingsRoomMeetings.this, sharedPreferences1.getString("comp_id", ""), locationId);
    }

    private void getappointments(String rm_email, String rm_id, Long s_time, Long e_time) {

        DataManger dataManager = DataManger.getDataManager();
        dataManager.getappointments(new Callback<OutlookModel>() {
            @Override
            public void onResponse(Call<OutlookModel> call, Response<OutlookModel> response) {
                OutlookModel model = response.body();


                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200;
                    Integer failurecode = 401;
                    Integer not_verified = 404;
                    if (statuscode.equals(failurecode)) {


                    } else if (statuscode.equals(not_verified)) {
//                        progressBar.setVisibility(View.GONE);

                    } else if (statuscode.equals(successcode)) {
//                        PackageManager manager = MeetingsRoomMeetings.this.getPackageManager();
//                      PackageInfo info = null;
//                        try {
//                            info = manager.getPackageInfo( MeetingsRoomMeetings.this.getPackageName(), PackageManager.GET_ACTIVITIES);
//                            System.out.println("PackageName = " + info.packageName + "\nVersionCode = "
//                                    + info.versionCode + "\nVersionName = " + info.versionName + "\nPermissions = " + info.permissions);
//
//                            if(model.getIncomplete_data() != null){
//
//
//                          if(Float.parseFloat(info.versionName) < Float.parseFloat(model.getIncomplete_data().getScheduler())) {
//
//     //               new DownloadTask(MeetingsRoomMeetings.this, "http://10.183.249.27/apk/p.apk");
//
//
//
//
//                          }
//
//                                }
//                        } catch (PackageManager.NameNotFoundException e) {
//                            e.printStackTrace();
//                        }

                        outLookMeetings = model.getItems();
//                           ledColors();
                        getrmslots(rm_id, s_time, e_time);

                    }
                }
            }

            @Override
            public void onFailure(Call<OutlookModel> call, Throwable t) {
//                progressDialog.dismiss();

            }
        }, sharedPreferences1.getString("comp_id", ""), meetingRoomEmail, s_time, e_time, "timezone");
    }

    private void getrmslots(String rm_id, Long s_time, Long e_time) {

        DataManger dataManager = DataManger.getDataManager();
        dataManager.getrmslots(new Callback<Model1>() {
            @Override
            public void onResponse(Call<Model1> call, Response<Model1> response) {
                Model1 model = response.body();
//                                    progressDialog.dismiss();


                if (model != null) {
                    Integer statuscode = model.getResult();
                    Integer successcode = 200;
                    Integer failurecode = 401;
                    Integer not_verified = 404;
                    Integer invalid_time = 201;
                    if (statuscode.equals(failurecode)) {


                    } else if (statuscode.equals(invalid_time)) {
//                        incorrectTimePopup();
                    } else if (statuscode.equals(not_verified)) {
//                        progressBar.setVisibility(View.GONE);

                    } else if (statuscode.equals(successcode)) {
//                        progress.show();
                        ArrayList<CompanyData> meetingsArraylist = new ArrayList<>();

                        if (!outLookMeetings.isEmpty()) {
                            for (int i = 0; i < outLookMeetings.size(); i++) {
                                CompanyData meeting = new CompanyData();
                                meeting.setStart(outLookMeetings.get(i).t_start);
                                meeting.setEnd(outLookMeetings.get(i).t_end);
                                meeting.setSubject(outLookMeetings.get(i).getSubject());
                                meeting.setSupertype(outLookMeetings.get(i).getSupertype());
                                ArrayList<Invited> invitedArrayList = new ArrayList<>();

                                EmpData host = new EmpData();
                                String[] result = outLookMeetings.get(i).getInvitees().split("; ");
                                host.setName(outLookMeetings.get(i).getName());
                                for (int j = 0; j < result.length; j++) {
                                    if (j == 0) {

                                    } else {
                                        Invited invites = new Invited();
                                        invites.setName(result[j]);
                                        invitedArrayList.add(invites);
                                    }
                                }
                                meeting.setHost(host);
                                meeting.setInvites(invitedArrayList);

                                meetingsArraylist.add(meeting);
                            }
                        }
                        outLookMeetings.clear();
                        meetingsArraylist.addAll(model.getItems());
                        meetingsArraylist = insertionSort(meetingsArraylist);
                        meetings = meetingsArraylist;
                        //
//                        ledColors();
                    }
                }
            }

            @Override
            public void onFailure(Call<Model1> call, Throwable t) {
//                progress.dismiss();
            }
        }, MeetingsRoomMeetings.this, "rmss", sharedPreferences1.getString("comp_id", ""), s_time, e_time, rm_id);
    }

    private void ledColors() {
        p_time = (Calendar.getInstance().getTimeInMillis() / 1000);

        m_timeText.setVisibility(View.GONE);
        m_image.setVisibility(View.GONE);
        txtRuningTimer.setVisibility(View.GONE);


        if (meetings != null && !meetings.isEmpty()) {
            meetinglayout.setVisibility(View.VISIBLE);
            viewTRD.setVisibility(View.VISIBLE);
            linearListTRD.setVisibility(View.VISIBLE);
            Log.e("asdf","1");


            long Mstarttime = meetings.get(0).getStart() + timezone();
            long Mendtime = meetings.get(0).getEnd() + timezone() + 1;
            meetingListAdapter = new MeetingsDateAdapter(MeetingsRoomMeetings.this, meetings);
            meetingsRecyclerView.setAdapter(meetingListAdapter);

            traingingAdapterTrd = new TrainingDateAdapter(MeetingsRoomMeetings.this, meetings);
            meetingsRecyclerViewTrd.setAdapter(traingingAdapterTrd);


            if (p_time >= Mstarttime && p_time < Mendtime) {
                //Red Color
                adbcommand("echo w 0x04 > ./sys/devices/platform/led_con_h/zigbee_reset");
                m_time.setBackgroundColor(getResources().getColor(R.color.inprogress));
                cardTrdStatus.setBackground(getResources().getDrawable(R.drawable.card_inprogress));

                //timer
                txtRuningTimer.setVisibility(View.GONE);
                T1 = "0";
                T2 = "0";

                m_Status.setText("Meeting in progress");
                txtStatusTrd.setText("Training in progress");

                setLeftData();
            } else if (p_time >= Mendtime) {
                meetings.remove(meetings.get(0));
                ledColors();
                //timer
                T1 = "0";
                T2 = "0";
            } else if (p_time < Mstarttime && Mstarttime - p_time <= 600) {

                adbcommand("echo w 0x06 > ./sys/devices/platform/led_con_h/zigbee_reset");
                m_Status.setText("Meeting starts soon");
                txtStatusTrd.setText("Training starts soon");

                //timer
                txtRuningTimer.setVisibility(View.VISIBLE);
                String s_time = millitotime((meetings.get(0).getStart() + timezone()) * 1000, false);
                T1 = "1";
                countDownTimer(s_time);

                m_time.setBackgroundColor(getResources().getColor(R.color.soon));
                cardTrdStatus.setBackground(getResources().getDrawable(R.drawable.card_soon));

                setLeftData();
            } else if (p_time < Mstarttime && Mstarttime - p_time >= 3600) {

                adbcommand("echo w 0x05 > ./sys/devices/platform/led_con_h/zigbee_reset");
                meetinglayout.setVisibility(View.GONE);
                //meetinglayoutTrd.setVisibility(View.GONE);
                m_Status.setText("");
                txtStatusTrd.setText("");
                m_image.setVisibility(View.VISIBLE);
                m_timeText.setVisibility(View.VISIBLE);
                m_timeText.setText("Available");
                m_timeText.setBackground(getResources().getDrawable(R.drawable.card_nextmeet));
                m_timeTextTrd.setText("Available");

                //timer
                txtRuningTimer.setVisibility(View.GONE);
                T1 = "0";
                T2 = "0";

                m_time.setBackgroundColor(getResources().getColor(R.color.nextmeeting));
                cardTrdStatus.setBackground(getResources().getDrawable(R.drawable.card_nextmeet));

            } else {
                //Greeen
                adbcommand("echo w 0x05 > ./sys/devices/platform/led_con_h/zigbee_reset");
                m_Status.setText("Next meeting");
                txtStatusTrd.setText("Next Training");
                m_time.setBackgroundColor(getResources().getColor(R.color.nextmeeting));
                cardTrdStatus.setBackground(getResources().getDrawable(R.drawable.card_nextmeet));

                //timer
                txtRuningTimer.setVisibility(View.GONE);
                T1 = "0";
                T2 = "0";

                setLeftData();
            }
        } else {
            Log.e("asdf","2");

            adbcommand("echo w 0x05 > ./sys/devices/platform/led_con_h/zigbee_reset");
            m_time.setBackgroundColor(getResources().getColor(R.color.nextmeeting));
            cardTrdStatus.setBackground(getResources().getDrawable(R.drawable.card_nextmeet));
            meetinglayout.setVisibility(View.GONE);
            meetinglayoutTrd.setVisibility(View.GONE);
            confidentialData.setVisibility(View.GONE);
            confidentialDataTrd.setVisibility(View.GONE);
            meetingsRecyclerView.setVisibility(View.GONE);
            viewTRD.setVisibility(View.GONE);
            linearListTRD.setVisibility(View.GONE);

            //timer
            txtRuningTimer.setVisibility(View.GONE);
            T1 = "0";
            T2 = "0";

            m_image.setVisibility(View.VISIBLE);
            m_timeText.setVisibility(View.VISIBLE);
            m_timeText.setText("Available");
            m_timeText.setBackground(getResources().getDrawable(R.drawable.card_nextmeet));
            m_timeTextTrd.setText("Available");
            m_Status.setText("");
            txtStatusTrd.setText("");


            if (meetings.isEmpty()){
                //  Toast.makeText(getApplicationContext(),"sdf",Toast.LENGTH_LONG).show();
                cardNoMeetings.setVisibility(View.VISIBLE);
            }else {
                cardNoMeetings.setVisibility(View.GONE);
            }

            meetingListAdapter = new MeetingsDateAdapter(MeetingsRoomMeetings.this, new ArrayList<CompanyData>());
            meetingsRecyclerView.setAdapter(meetingListAdapter);

            traingingAdapterTrd = new TrainingDateAdapter(MeetingsRoomMeetings.this, new ArrayList<CompanyData>());
            meetingsRecyclerViewTrd.setAdapter(traingingAdapterTrd);

        }

//        if (isTrd) {
//            mrd.setVisibility(View.GONE);
//            linearMRD.setVisibility(View.GONE);
//            trd.setVisibility(View.VISIBLE);
//            linearTRD.setVisibility(View.VISIBLE);
//        } else {
//            trd.setVisibility(View.GONE);
//            linearTRD.setVisibility(View.GONE);
//            mrd.setVisibility(View.VISIBLE);
//            linearMRD.setVisibility(View.VISIBLE);
//        }


    }

    private void countDownTimer(String endTime) {

        if (T1.equalsIgnoreCase("1") && T2.equalsIgnoreCase("0")){

            long timestampInMillis = p_time * 1000;
            Date date = new Date(timestampInMillis);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String currentTime = sdf.format(date);

            // Calculate the duration between startTime and endTime in milliseconds
            long timeRemaining = getRemainingTimeInMillis(currentTime, endTime);

            if (timeRemaining > 0) {
                // Use CountDownTimer with a 1-second interval
                CountDownTimer countDownTimer = new CountDownTimer(timeRemaining, 1000) { // 1 second interval
                    @Override
                    public void onTick(long millisUntilFinished) {
                        // Calculate remaining minutes and seconds
                        long minutes = millisUntilFinished / 60000;  // 60000 ms = 1 minute
                        long seconds = (millisUntilFinished % 60000) / 1000; // Remaining seconds

                        T2 = "1";
                        // Format and display the time
                        String timeFormatted = String.format("%02d:%02d", minutes, seconds);

                        // Ensure UI update happens on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtRuningTimer.setText(timeFormatted);
                            }
                        });
                    }

                    @Override
                    public void onFinish() {
                        // When the timer finishes, display "00:00:00"
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtRuningTimer.setText("00:00:00");
                            }
                        });
                    }
                };

                // Start the countdown timer
                countDownTimer.start();
            } else {
                // If the time has already passed, display "00:00:00"
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtRuningTimer.setText("00:00:00");
                    }
                });
            }

        }

    }

    private long getRemainingTimeInMillis(String startTime, String endTime) {
        try {
            // Use SimpleDateFormat to parse the start and end times (12-hour format with AM/PM)
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            // Create Date objects for startTime and endTime
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            // If the start and end time are not null, calculate the remaining time
            if (startDate != null && endDate != null) {
                Calendar calendarStart = Calendar.getInstance();
                Calendar calendarEnd = Calendar.getInstance();

                // Set the start and end times on the Calendar instances
                calendarStart.setTime(startDate);
                calendarEnd.setTime(endDate);

                // Calculate the difference in milliseconds between start and end times
                return calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0; // If there is an issue, return 0
    }


    private void autoShutdown(int day) {

        DataManger dataManager = DataManger.getDataManager();
        dataManager.getworkingdaymrddetails(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {


                final Model model = response.body();
//                                            progressDialog.dismiss();


                if (model != null) {
//                                                approve_btn.setEnabled(true);
                    Integer statuscode = model.getResult();
                    Integer successcode = 200;
                    Integer failurecode = 401;
                    Integer not_verified = 404;
                    Integer invalid_time = 201;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(invalid_time)) {
                        incorrectTimePopup();
                    } else if (statuscode.equals(successcode)) {

                        if (model.getItems() != null && model.getItems().getToday() != null) {
                            if (model.getItems().getToday().getName().equals("THUR")) {
                                model.getItems().getToday().setName("THU");
                            }
                            if (model.getItems().getNextday().getName().equals("THUR")) {
                                model.getItems().getNextday().setName("THU");
                            }
                            SimpleDateFormat day = new SimpleDateFormat("EEE");

                            Calendar calendar = Calendar.getInstance();
                            String presentHour = calendar.get(Calendar.HOUR_OF_DAY) + "";
                            String presentMin = calendar.get(Calendar.MINUTE) + "";


                            if (presentMin.length() == 1) {
                                presentMin = 0 + presentMin;
                            }

                            String presentTimeNow = presentHour + "" + presentMin;
                            String todayEndTime = model.getItems().getToday().getEnd();
                            todayEndTime = todayEndTime.replace(":", "");
                            String todayStartTime = model.getItems().getToday().getStart();

                            todayStartTime = todayStartTime.replace(":", "");

                            int presentTimeInt = Integer.parseInt(presentTimeNow);
                            int todayEndTimeInt = Integer.parseInt(todayEndTime);
                            int todayStartTimeInt = Integer.parseInt(todayStartTime);
                            if (day.format(calendar.getTime()).toUpperCase().equals(model.getItems().getToday().getName())) {
                                if (presentTimeInt >= todayEndTimeInt) {
                                    String autoshutDown = afterEndTime(model.getItems().getNextday().getStart(), model.getItems().getNextday().getName());

                                    adbcommand("/system/xbin/test " + autoshutDown + " enable");
                                } else if (presentTimeInt < todayStartTimeInt) {

                                    String autoshutDown = beforeStartTime(model.getItems().getToday().getStart());
                                    adbcommand("/system/xbin/test " + autoshutDown + " enable");
                                } else {
                                    String autoshutDown = checkNextDay(model.getItems().getToday().getEnd(), model.getItems().getNextday().getStart(), model.getItems().getNextday().getName());
                                    adbcommand("/system/xbin/test " + autoshutDown + " enable");
                                }
                            } else {
                                String autoshutDown = TodayHolidayDeviceOpenned(model.getItems().getToday().getStart(), model.getItems().getToday().getName());
                                adbcommand("/system/xbin/test " + autoshutDown + " enable");
                            }

                        }
                    } else if (statuscode.equals(not_verified)) {
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e("Error", String.valueOf(t));
            }
        }, MeetingsRoomMeetings.this, day + "");


    }

    private void getuserDetails(int Status) {

        DataManger dataManager = DataManger.getDataManager();
        dataManager.getuserDetails(new Callback<Model>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {


                final Model model = response.body();
//                                            progressDialog.dismiss();


                if (model != null) {
//                                                approve_btn.setEnabled(true);
                    Integer statuscode = model.getResult();
                    Integer successcode = 200;
                    Integer failurecode = 401;
                    Integer not_verified = 404;
                    Integer invalid_time = 201;
                    if (statuscode.equals(failurecode)) {
                    } else if (statuscode.equals(invalid_time)) {
                        incorrectTimePopup();
                    } else if (statuscode.equals(successcode)) {
                        CompanyData items = model.getItems();
//                        final boolean b = myDb.(items);
                        Log.d(statuscode + "", items.toString());

                        if (Status == 1) {
                            Popup();
                        }


                        companyAddressArrayList = new ArrayList<>();
                        companyAddressArrayList = items.getAddress();

                        adapter1 = new PeopleAdapter(MeetingsRoomMeetings.this, R.layout.row, R.id.lbl_name, companyAddressArrayList);
                        location.setText(companyAddressArrayList.get(0).getName());
                        if (!items.getPic().isEmpty()) {
                            Glide.with(MeetingsRoomMeetings.this)
                                    .load(DataManger.IMAGE_URL + "/uploads/" + sharedPreferences1.getString("comp_id", null) + "/" + items.getPic().get(0))
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)  // Disable disk cache
                                    .error(R.drawable.logo)
                                    .skipMemoryCache(true)  // Disable memory cache
                                    .into(logo);
                        } else {
                            logo.setImageDrawable(getDrawable(R.drawable.logo));
                        }

                        logo.setVisibility(View.VISIBLE);
                        location.setThreshold(1);//will start working from first character
                        location.setAdapter(adapter1);//setting the adapter data into the AutoCompleteTextView
                        location.setEnabled(true);
                        getmeetingrooms(0 + "");

                    } else if (statuscode.equals(not_verified)) {
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e("Error", String.valueOf(t));
            }
        }, MeetingsRoomMeetings.this, sharedPreferences1.getString("comp_id", ""));


    }

    private void verifyPassword() {

//        passwordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        passwordDialog.setCancelable(false);
        passwordDialog.setContentView(R.layout.authenticationpopup);
        Objects.requireNonNull(passwordDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        TextView close = passwordDialog.findViewById(R.id.close);
        tag = passwordDialog.findViewById(R.id.tag);
        Button login = passwordDialog.findViewById(R.id.login);
        EditText password = passwordDialog.findViewById(R.id.password);
        t5 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30000);
                    while (!isInterrupted()) {
                        runOnUiThread(() -> {
                            if (t5.isAlive()) {
                                t5.interrupt();
                            }
                            passwordDialog.dismiss();
                        });
                    }
                } catch (Exception e) {
                }
            }
        };
        t5.start();


        close.setOnClickListener(v -> {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            if (t5.isAlive()) {
                t5.interrupt();
            }
            passwordDialog.dismiss();

        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                tag.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });
        login.setOnClickListener(v -> {
//
            String email = sharedPreferences1.getString("email", "");
            if (!password.getText().toString().equals("")) {
//
//                } else

                JsonObject gsonObject = new JsonObject();
                JSONObject jsonObj_ = new JSONObject();
                try {
                    jsonObj_.put("val", email);
                    jsonObj_.put("type", "email");
//                        jsonObj_.put("pwd", password.getText().toString().trim());
                    jsonObj_.put("password", aesUtil.encrypt(password.getText().toString().trim(), email));
                    JsonParser jsonParser = new JsonParser();
                    gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Login(gsonObject);
//                    progress.show();
            }
        });
        passwordDialog.show();

    }

    private void inviteessPopup(int index) {
        final Dialog inviteessdialog = new Dialog(MeetingsRoomMeetings.this);
        inviteessdialog.setCancelable(false);
        inviteessdialog.setContentView(R.layout.inviteespopupitem);
        Objects.requireNonNull(inviteessdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RecyclerView recyclerView = inviteessdialog.findViewById(R.id.invitessR);
        TextView close = inviteessdialog.findViewById(R.id.close);
        recyclerView.setLayoutManager(new LinearLayoutManager(MeetingsRoomMeetings.this));
        InvitessAdapter invitessAdapter = new InvitessAdapter(MeetingsRoomMeetings.this, meetings.get(index).getInvites());
        recyclerView.setAdapter(invitessAdapter);
        t4 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30000);
                    while (!isInterrupted()) {
                        runOnUiThread(() -> {
                            if (t4.isAlive()) {
                                t4.interrupt();
                            }
                            inviteessdialog.dismiss();


                        });
                    }
                } catch (Exception e) {
                }
            }
        };
        t4.start();
        close.setOnClickListener(v -> {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            if (t4.isAlive()) {

                t4.interrupt();
            }

            inviteessdialog.dismiss();
        });
        inviteessdialog.show();


    }

    private void incorrectTimePopup() {
        final Dialog invalidTimedialog = new Dialog(MeetingsRoomMeetings.this);
        invalidTimedialog.setCancelable(false);
        invalidTimedialog.setContentView(R.layout.invalidtimepopup);
        Objects.requireNonNull(invalidTimedialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

//        RecyclerView recyclerView = inviteessdialog.findViewById(R.id.invitessR);
        Button close = invalidTimedialog.findViewById(R.id.ok_button);
//        recyclerView.setLayoutManager(new LinearLayoutManager(MeetingsRoomMeetings.this));
//        InvitessAdapter invitessAdapter = new  InvitessAdapter(MeetingsRoomMeetings.this, meetings.get(index).getInvites());
//        recyclerView.setAdapter(invitessAdapter);

        close.setOnClickListener(v -> {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


            invalidTimedialog.dismiss();
        });
        invalidTimedialog.show();


    }

    private void Popup() {

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView close;
        TextView next;
        TextView closeapp;
        ImageButton logout;
        close = dialog.findViewById(R.id.close);
        next = dialog.findViewById(R.id.next);
        closeapp = dialog.findViewById(R.id.closeapp);
        TextView version = dialog.findViewById(R.id.version);
        logout = dialog.findViewById(R.id.logout);
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        meetingEmailPop = "";

        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);

            version.setText("Version " + info.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        TextView logout =  dialog.findViewById()
//        cancel = dialog.findViewById(R.id.cancel);
//        emp_spinner.setInputType(InputType.TYPE_NULL);
//        assign_btn = dialog.findViewById(R.id.assign);

        meeting_room.setOnClickListener(v -> meeting_room.showDropDown());
        meeting_room.setOnItemClickListener((parent, view, position, id) -> {

            Position = position;
            meetingEmailPop = meetingrooms.get(Position).getRm_email();

        });
        location.setOnClickListener(v -> location.showDropDown());
        location.setOnItemClickListener((parent, view, position, id) -> getmeetingrooms(position + ""));
        closeapp.setOnClickListener(v -> {
            dialog.dismiss();

            finishAndRemoveTask();
        });
        next.setOnClickListener(v -> {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            if (!meetingEmailPop.equals("")) {

                if (meeting_room.getText().toString().equals(meetingRoomName)) {
                    dialog.dismiss();
                } else {
                    meetingRoomId = meetingrooms.get(Position).get_id().get$oid();
                    meetingRoomEmail = meetingrooms.get(Position).getRm_email();
                    meetingRoomName = meetingrooms.get(Position).getName();
                    meetingRoom.setText(meetingrooms.get(Position).getName());
                    editor1.putInt("meetingRoomPosition", Position);
                    editor1.putBoolean("isTrd", meetingrooms.get(Position).getTrd_access());
                    editor1.putString("rm_id", meetingRoomId);
                    editor1.putString("rm_email", meetingRoomEmail);
                    editor1.putString("rm_name", meetingrooms.get(Position).getName());
                    editor1.commit();
                    editor1.apply();
                    dialog.dismiss();
                    Calendar cal = Calendar.getInstance();
                    isTrd = meetingrooms.get(Position).getTrd_access();
                    meetingRoom.setText(meetingrooms.get(Position).getName());
                    txtTRDmeetingRoom.setText(meetingrooms.get(Position).getName());
                    if (meetingrooms.get(Position).getTrd_access()) {
                        mrd.setVisibility(View.GONE);
                        trd.setVisibility(View.VISIBLE);
                    } else {
                        trd.setVisibility(View.GONE);
                        mrd.setVisibility(View.VISIBLE);
                    }
                    meetings = new ArrayList<>();
                    getappointments(meetingRoomEmail, meetingRoomId, (cal.getTimeInMillis() / 1000) - timezone(), (getEndOfADay(cal.getTime()).getTimeInMillis() / 1000) - timezone());

                }

            } else {
                builder.setMessage("Select Meeting Room")
                        .setCancelable(false)
                        .setPositiveButton("Ok", (dialog, id) -> dialog.cancel());

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("");
                alert.show();
            }

        });
        logout.setOnClickListener(v -> {
            AnimationSet animation = animation();
            v.startAnimation(animation);
            builder.setMessage("Are you sure you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (alertDialog, id) -> {
                        editor1.clear();
                        editor1.apply();

                        if (t1.isAlive()) {
                            t1.interrupt();
                        }
                        if (t.isAlive()) {
                            t.interrupt();
                        }
                        finish();

                        alertDialog.cancel();
                        dialog.dismiss();
                        Intent intent = new Intent(MeetingsRoomMeetings.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);


                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("");
            alert.show();
        });

        close.setOnClickListener(v -> {
            if (!meetingRoomEmail.equals("")) {

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                dialog.dismiss();
            } else {
                builder.setMessage("Select Meeting Room")
                        .setCancelable(false)
                        .setPositiveButton("Ok", (dialog, id) -> dialog.cancel());

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("");
                alert.show();
            }
        });
        dialog.show();
    }

    public ArrayList<CompanyData> insertionSort(ArrayList<CompanyData> meetingsA) {
        ArrayList<CompanyData> meetingsSort = new ArrayList<>();
        meetingsSort = meetingsA;
        int n = meetingsSort.size();
        int i;
        int j;
        CompanyData key;
        for (i = 1; i < n; i++) {
            key = meetingsSort.get(i);
            j = i - 1;

        /* Move elements of arr[0..i-1], that are
        greater than key, to one position ahead
        of their current position */
            while (j >= 0 && meetingsSort.get(j).getStart() > key.getStart()) {
                meetingsSort.set(j + 1, meetingsSort.get(j));
                j = j - 1;
            }
            meetingsSort.set(j + 1, key);
        }
        return meetingsSort;
    }

    public void showDialog() {
        Objects.requireNonNull(Nodialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Nodialog.setCancelable(false);
        Nodialog.setContentView(R.layout.nointernet_item);
        Nodialog.show();
    }

    public void reloadPage() {
        this.recreate();

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void Login(JsonObject jsonObject) {

        DataManger dataManager = DataManger.getDataManager();
        dataManager.userLogin(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                final Model model = response.body();
//                                            progressDialog.dismiss();

//                progress.dismiss();
                if (model != null) {
//                                                approve_btn.setEnabled(true);
                    Integer statuscode = model.getResult();
                    Integer successcode = 200;
                    Integer failurecode = 201;
                    Integer not_verified = 404;
                    if (statuscode.equals(failurecode)) {
                        tag.setVisibility(View.VISIBLE);

                    } else if (statuscode.equals(not_verified)) {
//                        progressBar.setVisibility(View.GONE);


                    } else if (statuscode.equals(successcode)) {
                        if (t5.isAlive()) {
                            t5.interrupt();
                        }
                        passwordDialog.dismiss();
                        getuserDetails(1);

                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
//                                            progressDialog.dismiss();


            }
        }, MeetingsRoomMeetings.this, jsonObject);

//
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (t1.isAlive()) {
            t1.interrupt();
        }
        if (t.isAlive()) {
            t.interrupt();
        }


    }

    public class TrainingDateAdapter extends RecyclerView.Adapter<TrainingDateAdapter.MyviewHolder> {
        Context context;
        ArrayList<CompanyData> meetigsArrayList;

        public TrainingDateAdapter(Context mContext, ArrayList<CompanyData> meetigsArrayList) {

            this.context = mContext;
            this.meetigsArrayList = meetigsArrayList;
        }

        @Override
        public TrainingDateAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.training_room_item, parent, false);
            return new TrainingDateAdapter.MyviewHolder(itemView);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(TrainingDateAdapter.MyviewHolder holder, @SuppressLint("RecyclerView") final int position) {


            if (meetigsArrayList.isEmpty()) {
                holder.l1.setVisibility(View.GONE);

                holder.empty.setVisibility(View.VISIBLE);
                viewTRD.setVisibility(View.GONE);
                linearListTRD.setVisibility(View.GONE);
                meetinglayoutTrd.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                subjectTrd.setGravity(Gravity.CENTER);
                subjectTrd.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


            } else if (p_time < meetigsArrayList.get(0).getStart() + timezone() && meetigsArrayList.get(0).getStart() + timezone() - p_time >= 3600) {

                viewTRD.setVisibility(View.VISIBLE);
                linearListTRD.setVisibility(View.VISIBLE);

                holder.empty.setVisibility(View.GONE);
                holder.l1.setVisibility(View.VISIBLE);

                holder.confidential_text.setVisibility(View.GONE);

                if (meetigsArrayList.get(0).getCategoryData() != null && meetigsArrayList.get(0).getCategoryData().getCat_type()) {
                    holder.confidential_text.setVisibility(View.VISIBLE);
                    holder.l1.setVisibility(View.GONE);

                } else {
                    holder.itemView.setOnClickListener(v -> inviteessPopup(position));
                }

                String s_time = millitotime((meetigsArrayList.get(position).getStart() + timezone()) * 1000, false);
                String e_time = millitotime((meetigsArrayList.get(position).getEnd() + 1 + timezone()) * 1000, false);
                String UpperCase_s_time = s_time.toUpperCase();
                String UpperCase_e_time = e_time.toUpperCase();
                holder.m_start.setText(UpperCase_s_time);
                holder.m_end.setText(UpperCase_e_time);
                holder.hostName.setText(meetigsArrayList.get(position).getHost().getName());
                holder.subject.setText(meetigsArrayList.get(position).getSubject());
                holder.m_end.setVisibility(View.VISIBLE);

            } else if (meetigsArrayList.size() - 1 == 0) {
                holder.l1.setVisibility(View.GONE);
                holder.empty.setVisibility(View.VISIBLE);
                viewTRD.setVisibility(View.GONE);
                linearListTRD.setVisibility(View.GONE);
                meetinglayoutTrd.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                subjectTrd.setGravity(Gravity.CENTER);
                subjectTrd.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                holder.empty.setVisibility(View.GONE);
                holder.l1.setVisibility(View.VISIBLE);

                holder.confidential_text.setVisibility(View.GONE);

                if (meetigsArrayList.get(position + 1).getCategoryData() != null && meetigsArrayList.get(position + 1).getCategoryData().getCat_type()) {
                    holder.confidential_text.setVisibility(View.VISIBLE);
                    holder.l1.setVisibility(View.GONE);
                } else {
                    holder.itemView.setOnClickListener(v -> inviteessPopup(position + 1));
                }

                String s_time = millitotime((meetigsArrayList.get(position + 1).getStart() + timezone()) * 1000, false);
                String e_time = millitotime((meetigsArrayList.get(position + 1).getEnd() + 1 + timezone()) * 1000, false);
                String UpperCase_s_time = s_time.toUpperCase();
                String UpperCase_e_time = e_time.toUpperCase();
                holder.m_start.setText(UpperCase_s_time);
                holder.m_end.setText(UpperCase_e_time);
                holder.hostName.setText(meetigsArrayList.get(position + 1).getHost().getName());
                holder.subject.setText(meetigsArrayList.get(position + 1).getSubject());

                holder.m_end.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public int getItemCount() {

            if (meetigsArrayList.isEmpty()) {
                return 2;
            } else if (p_time < meetigsArrayList.get(0).getStart() + timezone() && meetigsArrayList.get(0).getStart() + timezone() - p_time >= 3600) {
                return meetigsArrayList.size();
            } else if (meetigsArrayList.size() - 1 == 0) {
                return 2;
            } else {
                return meetigsArrayList.size() - 1;
            }


        }

        public class MyviewHolder extends RecyclerView.ViewHolder {
            TextView m_start;
            TextView m_end;
            TextView hostName;
            TextView subject;

            LinearLayout empty;

            TextView confidential_text;
            LinearLayout l1;

            LinearLayout cardMeetingData;

            public MyviewHolder(View view) {
                super(view);
                m_start = view.findViewById(R.id.m_start);
                m_end = view.findViewById(R.id.m_end);
                confidential_text = view.findViewById(R.id.confidential_text);

                hostName = view.findViewById(R.id.hostName);
                subject = view.findViewById(R.id.subject);

                empty = view.findViewById(R.id.empty);
                l1 = view.findViewById(R.id.l1);

            }
        }
    }

    public class MeetingsDateAdapter extends RecyclerView.Adapter<MeetingsDateAdapter.MyviewHolder> {
        Context context;
        ArrayList<CompanyData> meetigsArrayList;

        public MeetingsDateAdapter(Context mContext, ArrayList<CompanyData> meetigsArrayList) {

            this.context = mContext;
            this.meetigsArrayList = meetigsArrayList;
        }

        @Override
        public MeetingsDateAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.meetings_item, parent, false);
            return new MeetingsDateAdapter.MyviewHolder(itemView);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(MeetingsDateAdapter.MyviewHolder holder, @SuppressLint("RecyclerView") final int position) {
//            holder.Name.setText("Host: " + meetigsArrayList.get(position).getEmployee().getName());


            if (meetigsArrayList.isEmpty()) {
                holder.l1.setVisibility(View.GONE);
                holder.l2.setVisibility(View.GONE);
                holder.empty.setVisibility(View.VISIBLE);
            } else if (p_time < meetigsArrayList.get(0).getStart() + timezone() && meetigsArrayList.get(0).getStart() + timezone() - p_time >= 3600) {

                holder.empty.setVisibility(View.GONE);
                holder.l1.setVisibility(View.VISIBLE);
                holder.l2.setVisibility(View.VISIBLE);
                holder.confidential_text.setVisibility(View.GONE);

                if (meetigsArrayList.get(0).getCategoryData() != null && meetigsArrayList.get(0).getCategoryData().getCat_type()) {
                    holder.confidential_text.setVisibility(View.VISIBLE);
                    holder.l2.setVisibility(View.GONE);

                } else {
                    holder.itemView.setOnClickListener(v -> inviteessPopup(position));
                }

                String s_time = millitotime((meetigsArrayList.get(position).getStart() + timezone()) * 1000, false);
                String e_time = millitotime((meetigsArrayList.get(position).getEnd() + 1 + timezone()) * 1000, false);
                String UpperCase_s_time = s_time.toUpperCase();
                String UpperCase_e_time = e_time.toUpperCase();
                holder.m_start.setText(UpperCase_s_time);
                holder.m_end.setText(UpperCase_e_time);
                holder.hostName.setText(meetigsArrayList.get(position).getHost().getName());
                if (!meetigsArrayList.get(position).getInvites().isEmpty()) {
                    holder.h_designation.setText(meetigsArrayList.get(position).getInvites().get(0).getName());
                    if (meetigsArrayList.get(position).getInvites().get(0).getName().equals("") || meetigsArrayList.get(position).getInvites().get(0).getName().equals("x")) {
                        holder.h_designation.setText(meetigsArrayList.get(position).getInvites().get(0).getEmail());
                        if (meetigsArrayList.get(position).getInvites().get(0).getEmail().equals("")) {
                            holder.h_designation.setText(meetigsArrayList.get(position).getInvites().get(0).getMobile());
                        }
                    }
                    holder.slash.setText(" - ");
                } else {
                    holder.h_designation.setText("");
                    holder.slash.setText("");
                }
                holder.m_end.setVisibility(View.VISIBLE);


            } else if (meetigsArrayList.size() - 1 == 0) {
                holder.l1.setVisibility(View.GONE);
                holder.l2.setVisibility(View.GONE);
                holder.empty.setVisibility(View.VISIBLE);
            } else {
                holder.empty.setVisibility(View.GONE);
                holder.l1.setVisibility(View.VISIBLE);
                holder.l2.setVisibility(View.VISIBLE);
                holder.confidential_text.setVisibility(View.GONE);

                if (meetigsArrayList.get(position + 1).getCategoryData() != null && meetigsArrayList.get(position + 1).getCategoryData().getCat_type()) {
                    holder.confidential_text.setVisibility(View.VISIBLE);
                    holder.l2.setVisibility(View.GONE);
                } else {
                    holder.itemView.setOnClickListener(v -> inviteessPopup(position + 1));

                }

                String s_time = millitotime((meetigsArrayList.get(position + 1).getStart() + timezone()) * 1000, false);
                String e_time = millitotime((meetigsArrayList.get(position + 1).getEnd() + 1 + timezone()) * 1000, false);
                String UpperCase_s_time = s_time.toUpperCase();
                String UpperCase_e_time = e_time.toUpperCase();
                holder.m_start.setText(UpperCase_s_time);
                holder.m_end.setText(UpperCase_e_time);
                holder.hostName.setText(meetigsArrayList.get(position + 1).getHost().getName());
                if (!meetigsArrayList.get(position + 1).getInvites().isEmpty()) {
                    holder.h_designation.setText(meetigsArrayList.get(position + 1).getInvites().get(0).getName());
                    if (meetigsArrayList.get(position + 1).getInvites().get(0).getName().equals("") || meetigsArrayList.get(position + 1).getInvites().get(0).getName().equals("x")) {
                        holder.h_designation.setText(meetigsArrayList.get(position + 1).getInvites().get(0).getEmail());
                        if (meetigsArrayList.get(position + 1).getInvites().get(0).getEmail().equals("")) {
                            holder.h_designation.setText(meetigsArrayList.get(position + 1).getInvites().get(0).getMobile());

                        }
                    }
                    holder.slash.setText(" - ");
                } else {
                    holder.h_designation.setText("");
                    holder.slash.setText("");
                }
                holder.m_end.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public int getItemCount() {

            if (meetigsArrayList.isEmpty()) {
                return 6;
            } else if (p_time < meetigsArrayList.get(0).getStart() + timezone() && meetigsArrayList.get(0).getStart() + timezone() - p_time >= 3600) {
                return meetigsArrayList.size();
            } else if (meetigsArrayList.size() - 1 == 0) {
                return 6;
            } else {
                return meetigsArrayList.size() - 1;
            }


        }

        public class MyviewHolder extends RecyclerView.ViewHolder {
            TextView m_start;
            TextView m_end;
            TextView hostName;
            TextView h_designation;
            TextView empty;
            TextView slash;
            TextView confidential_text;
            LinearLayout l1;
            LinearLayout l2;
            LinearLayout cardMeetingData;

            public MyviewHolder(View view) {
                super(view);
                m_start = view.findViewById(R.id.m_start);
                m_end = view.findViewById(R.id.m_end);
                confidential_text = view.findViewById(R.id.confidential_text);

                hostName = view.findViewById(R.id.hostName);
                h_designation = view.findViewById(R.id.h_designation);
                slash = view.findViewById(R.id.slash);
                empty = view.findViewById(R.id.empty);
                l1 = view.findViewById(R.id.l1);
                l2 = view.findViewById(R.id.l2);
            }
        }
    }

//    public class NetworkChangeReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(final Context context, final Intent intent) {
//            if (checkInternet(context)) {
//
//                if(Nodialog.isShowing()){
//                    Nodialog.dismiss();
//                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    getWindow().getDecorView().setSystemUiVisibility(
//                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
//                                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//                    Calendar cal = Calendar.getInstance();
//                    if(meetingRoomEmail.equals("")){
//                        getuserDetails(1);
//                    }
//                    else{
//                        getuserDetails(0);
//                        if(!meetingRoomEmail.equals("")){
//                            getappointments(meetingRoomEmail,meetingRoomId,(cal.getTimeInMillis()/1000)  - timezone() ,(getEndOfADay(cal.getTime()).getTimeInMillis()/1000)  - timezone() );
//
//                        }
//
//                    }
//
//                }
//            }
//            else{
//
//                String action = intent.getAction();
//
//                if (ACTION.equals(action))
//                {
//                    //your SMS processing code
//                    showDialog();
//                }
//
//            }
//        }
//
//        boolean checkInternet(Context context) {
//            ServiceManager serviceManager = new ServiceManager(context);
//            return serviceManager.isNetworkAvailable();
//        }
//
//    }

    public static class EthernetDataReceiver extends BroadcastReceiver {
        private static final long CHECK_INTERVAL = 1000; // Check every second
        private boolean isChecking = false;
        private Handler handler = new Handler(Looper.getMainLooper());
        private Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //                    if (isConnectedToEthernet(context)){


//                 }
                    if (isEthernetReceivingData()) {
                        Log.d("EthernetDataReceiver", "Ethernet is receiving data");
                        // Ethernet is receiving data
                        // Handle the case when Ethernet is receiving data
                    } else {
                        Log.d("EthernetDataReceiver", "Ethernet is not receiving data");
                        // Ethernet is not receiving data
                        // Handle the case when Ethernet is not receiving data
                    }
                }
                // Schedule the next check
                if (isChecking) {
                    handler.postDelayed(this, CHECK_INTERVAL);
                }
            }
        };

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkEthernetDataStatus(context);
                }
            }
        }


        private boolean isConnectedToEthernet(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                Network network = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    network = connectivityManager.getActiveNetwork();
                }
                if (network != null) {
                    NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                    return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            }
            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private boolean isEthernetReceivingData() {
            long rxBytes = TrafficStats.getTotalRxBytes();
            try {
                Thread.sleep(1000); // Sleep for a second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long newRxBytes = TrafficStats.getTotalRxBytes();

            // If the difference in received bytes is greater than 0, it means data is being received.
            return (newRxBytes - rxBytes) > 0;
        }

        private void checkEthernetDataStatus(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null && isEthernet(connectivityManager)) {
                // Start periodic checking
                startChecking();
            } else {
                // Stop checking if not connected to Ethernet
                stopChecking();
            }
        }

        private boolean isEthernet(ConnectivityManager connectivityManager) {
            return connectivityManager.getActiveNetworkInfo() != null &&
                    connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_ETHERNET;
        }

        private void startChecking() {
            isChecking = true;
            handler.post(checkRunnable);
        }

        private void stopChecking() {
            isChecking = false;
            handler.removeCallbacks(checkRunnable);
        }
    }

    public class InvitessAdapter extends RecyclerView.Adapter<InvitessAdapter.MyviewHolder> {
        Context context;
        ArrayList<Invited> invitedArrayList;

        public InvitessAdapter(Context mContext, ArrayList<Invited> invitedArrayList) {
            this.context = mContext;
            this.invitedArrayList = invitedArrayList;
        }

        @Override
        public InvitessAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.invitee, parent, false);
            return new InvitessAdapter.MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(InvitessAdapter.MyviewHolder holder, final int position) {
            holder.lbl_name.setText((position + 1) + ". " + (invitedArrayList.get(position).getName()));
//            if (invitedArrayList.get(position).getStatus() == 1){
//                holder.status_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
//            }
//            else if (invitedArrayList.get(position).getStatus() == 2){
//                holder.status_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancellation));
//            }
//            else{
//                holder.status_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_84));
//            }
            if (invitedArrayList.get(position).getName().equals("") || invitedArrayList.get(position).getName().equals("x")) {


                holder.lbl_name.setText((position + 1) + ". " + (invitedArrayList.get(position).getEmail()));
                if (invitedArrayList.get(position).getEmail().equals("")) {
                    holder.lbl_name.setText((position + 1) + ". " + (invitedArrayList.get(position).getMobile()));
                }

            }
        }

        @Override
        public int getItemCount() {
            return invitedArrayList.size();
        }

        public class MyviewHolder extends RecyclerView.ViewHolder {
            TextView lbl_name;
            CircleImageView status_img;

            public MyviewHolder(View view) {
                super(view);
                lbl_name = view.findViewById(R.id.lbl_name);
                status_img = view.findViewById(R.id.status_img);

            }
        }
    }

}