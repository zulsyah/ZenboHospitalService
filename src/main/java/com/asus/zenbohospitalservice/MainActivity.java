package com.asus.zenbohospitalservice;

/*

ROOM INDEX KEYWORD
0. Ward
1. Emergency
2. Lobby

*/

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;
import com.asus.robotframework.API.RobotFace;
import com.asus.robotframework.API.RobotUtil;
import com.asus.robotframework.API.SpeakConfig;
import com.asus.robotframework.API.results.RoomInfo;
import com.robot.asus.robotactivity.RobotActivity;

import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends RobotActivity {
    public final static String TAG = "ZenboHospitalService";
    public final static String DOMAIN = "D83A78E49B4348A892AF4E88C4E04B14";

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static boolean isRobotApiInitialed = false;

    private static ConstraintLayout mMainMenuLayout, mVisitorLayout, mPatientLayout, mAboutUsLayout, mRunningLayout;
    private static ImageButton mVisitorButton, mPatientButton, mAboutUsButton, mEmergencyButton, mPharmacyButton, mCheckupButton, mWardButton, mLobbyButton, mShopButton, mHomeButton, mArriveButton;
    private static ImageView mGoToView;
    private static TextView mGoToText;

    private static String mapRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Layout
        mRunningLayout = findViewById(R.id.RunningLayout);
        mMainMenuLayout = findViewById(R.id.MainMenuLayout);
        mVisitorLayout = findViewById(R.id.VisitorLayout);
        mPatientLayout = findViewById(R.id.PatientLayout);
        mAboutUsLayout = findViewById(R.id.AboutUsLayout);

        //ImageView
        mGoToView = findViewById(R.id.image_goto);

        //TextView
        mGoToText = findViewById(R.id.locationText);


        mHomeButton = findViewById(R.id.btn_home);
        mHomeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open MainMenuLayout
                setMainMenuLayout();

            }
        });

        mHomeButton = findViewById(R.id.btn_home2);
        mHomeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open MainMenuLayout
                setMainMenuLayout();

            }
        });

        mHomeButton = findViewById(R.id.btn_home3);
        mHomeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open MainMenuLayout
                setMainMenuLayout();

            }
        });

        mHomeButton = findViewById(R.id.btn_home4);
        mHomeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open MainMenuLayout
                setMainMenuLayout();

            }
        });

        mPatientButton = findViewById(R.id.btn_patient);
        mPatientButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                robotAPI.robot.stopSpeakAndListen();
                // open PatientLayout
                setPatientLayout();

            }
        });

        mVisitorButton = findViewById(R.id.btn_visitor);
        mVisitorButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                robotAPI.robot.stopSpeakAndListen();
                // open VisitorLayout
                setVisitorLayout();

            }
        });

        mAboutUsButton = findViewById(R.id.btn_aboutus);
        mAboutUsButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                robotAPI.robot.stopSpeakAndListen();
                // open AboutUsLayout
                setAboutUsLayout();

            }
        });

        mPharmacyButton = findViewById(R.id.btn_pharmacy);
        mPharmacyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                robotAPI.robot.stopSpeakAndListen();
                setGoToPharmacy();

            }
        });

        mEmergencyButton = findViewById(R.id.btn_emergency);
        mEmergencyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                robotAPI.robot.stopSpeakAndListen();
                setGoToEmergency();

            }
        });

        mCheckupButton = findViewById(R.id.btn_checkup);
        mCheckupButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                robotAPI.robot.stopSpeakAndListen();
                setGoToCheckUp();

            }
        });

        mLobbyButton = findViewById(R.id.btn_lobby);
        mLobbyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                robotAPI.robot.stopSpeakAndListen();
                setGoToLobby();

            }
        });

        mWardButton = findViewById(R.id.btn_ward);
        mWardButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                robotAPI.robot.stopSpeakAndListen();
                setGoToWard();

            }
        });

        mShopButton = findViewById(R.id.btn_shop);
        mShopButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                robotAPI.robot.stopSpeakAndListen();
                setGoToShop();

            }
        });

        mArriveButton = findViewById(R.id.btn_arrived);
        mArriveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                robotAPI.robot.stopSpeakAndListen();
                //setGoToLobby();

                robotAPI.robot.speak("Okey, thank you for using me as your guidance, if you want to find me, just go back to lobby");
                ArrayList<RoomInfo> arrayListRooms = robotAPI.contacts.room.getAllRoomInfo();
                mapRoom = arrayListRooms.get(2).keyword; //lobby

                if(!mapRoom.equals("")) {

                    if(isRobotApiInitialed) {
                        // use robotAPI to go to the position "keyword":
                        robotAPI.motion.goTo(mapRoom);
                    }

                }

                setMainMenuLayout();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        setMainMenuLayout();
        //setRunningLayout();

        // check permission READ_CONTACTS is granted or not
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted by user yet
            Log.d("ZenboGoToLocation", "READ_CONTACTS permission is not granted by user yet");
        }
        else{
            // permission is granted by user
            Log.d("ZenboGoToLocation", "READ_CONTACTS permission is granted");
        }

        // close facial
        robotAPI.robot.setExpression(RobotFace.HIDEFACE);

        robotAPI.robot.setVoiceTrigger(false);

        // jump dialog domain
        robotAPI.robot.jumpToPlan(DOMAIN, "launchHospitalApp");

        // listen user utterance
        robotAPI.robot.speakAndListen("Welcome to Hospital Annur, Im Zenbo. Im the assistant robot for this app.\n" +
                "        Before we start, you must tell me whether you are patient or visitor.\n" +
                "        Then we can proceed....   \n" +
                "        Are you patient or visitor? You may also touch the screen.", new SpeakConfig().timeout(20));

        requestPermission();

    }

    private void requestPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                this.checkSelfPermission(Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED) {
            // Android version is lesser than 6.0 or the permission is already granted.
            return;
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            //showMessageOKCancel("You need to allow access to Contacts",
            //        new DialogInterface.OnClickListener() {
            //            @Override
            //            public void onClick(DialogInterface dialog, int which) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            //            }
            //        });
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        //stop listen user utterance
        robotAPI.robot.stopSpeakAndListen();
    }

    @Override
    public void onBackPressed() {

        android.os.Process.killProcess(android.os.Process.myPid());
        // This above line close correctly
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static RobotCallback robotCallback = new RobotCallback() {
        @Override
        public void onResult(int cmd, int serial, RobotErrorCode err_code, Bundle result) {
            super.onResult(cmd, serial, err_code, result);
        }

        @Override
        public void onStateChange(int cmd, int serial, RobotErrorCode err_code, RobotCmdState state) {
            super.onStateChange(cmd, serial, err_code, state);
        }

        @Override
        public void initComplete() {
            super.initComplete();

            isRobotApiInitialed = true;
        }

        @Override
        public void onDetectFaceResult(java.util.List resultList){
            super.onDetectFaceResult(resultList);
        }

    };


    public static RobotCallback.Listen robotListenCallback = new RobotCallback.Listen() {


        @Override
        public void onFinishRegister() {

        }

        @Override
        public void onVoiceDetect(JSONObject jsonObject) {

        }

        @Override
        public void onSpeakComplete(String s, String s1) {

        }

        @Override
        public void onEventUserUtterance(JSONObject jsonObject) {
            String text;
            text = "onEventUserUtterance: " + jsonObject.toString();
            Log.d(TAG, text);
        }

        @Override
        public void onResult(JSONObject jsonObject) {
            String text;
            text = "onResult: " + jsonObject.toString();
            Log.d(TAG, text);


            String sIntentionID = RobotUtil.queryListenResultJson(jsonObject, "IntentionId");
            Log.d(TAG, "Intention Id = " + sIntentionID);

            if(sIntentionID.equals("hospitalApp")) {

                String SLUvisitor = RobotUtil.queryListenResultJson(jsonObject, "myVisitor", null);
                String SLUpatient = RobotUtil.queryListenResultJson(jsonObject, "myPatient", null);
                String SLUcommand = RobotUtil.queryListenResultJson(jsonObject, "myCommand", null);

                Log.d(TAG, "Result Visitor = " + SLUvisitor);
                Log.d(TAG, "Result Patient = " + SLUpatient);
                Log.d(TAG, "Result Command = " + SLUcommand);


                if( ( SLUvisitor).equals("null")) { }
                else {

                    robotAPI.robot.stopSpeakAndListen();

                    if (SLUvisitor.equals("visitor")){

                        setVisitorLayout();
                    }

                    if (SLUvisitor.equals("lobby")){

                        setGoToLobby();
                    }

                    if (SLUvisitor.equals("ward")){

                        setGoToWard();
                    }

                    if (SLUvisitor.equals("shop")){

                        setGoToShop();
                    }


                }

                if ( SLUpatient.equals("null")){ }
                else {

                    robotAPI.robot.setExpression(RobotFace.HIDEFACE);

                    if (SLUpatient.equals("patient") ){

                        setPatientLayout();
                    }

                    if (SLUpatient.equals("pharmacy")){

                        setGoToPharmacy();
                    }

                    if (SLUpatient.equals("emergency")){

                        setGoToEmergency();
                    }

                    if (SLUpatient.equals("checkup")){

                        setGoToCheckUp();
                    }

                }

                if ( SLUcommand.equals("null")){ }
                else {

                    if (SLUcommand.equals("about")) {

                        setAboutUsLayout();
                    }

                }

            }
        }

        @Override
        public void onRetry(JSONObject jsonObject) {

        }
    };

    public MainActivity() {
        super(robotCallback, robotListenCallback);
    }

    public void setMainMenuLayout() {

        mRunningLayout.setVisibility(View.INVISIBLE);
        mMainMenuLayout.setVisibility(View.VISIBLE);
        mVisitorLayout.setVisibility(View.INVISIBLE);
        mPatientLayout.setVisibility(View.INVISIBLE);
        mAboutUsLayout.setVisibility(View.INVISIBLE);

    }

    public static void setVisitorLayout() {

        mMainMenuLayout.setVisibility(View.INVISIBLE);
        mVisitorLayout.setVisibility(View.VISIBLE);
        mPatientLayout.setVisibility(View.INVISIBLE);
        mAboutUsLayout.setVisibility(View.INVISIBLE);

        robotAPI.robot.speakAndListen("Hi visitor, there are 3 places you can go, which is, lobby, ward and shop. Which one you want to go?", new SpeakConfig().timeout(5));

    }

    public static void setPatientLayout() {

        mMainMenuLayout.setVisibility(View.INVISIBLE);
        mVisitorLayout.setVisibility(View.INVISIBLE);
        mPatientLayout.setVisibility(View.VISIBLE);
        mAboutUsLayout.setVisibility(View.INVISIBLE);

        robotAPI.robot.speakAndListen("Hi patient, there are 3 places you can go, which is, pharmacy, emergency and check up. Which one you want to go?", new SpeakConfig().timeout(5));
    }

    public static void setAboutUsLayout() {

        mMainMenuLayout.setVisibility(View.INVISIBLE);
        mVisitorLayout.setVisibility(View.INVISIBLE);
        mPatientLayout.setVisibility(View.INVISIBLE);
        mAboutUsLayout.setVisibility(View.VISIBLE);

        robotAPI.robot.speak("An-Nur Specialist Hospital (ANSH) is a tertiary multidisciplinary hospital, fully owned by Medic IG Holdings Sdn Bhd. From its humble beginnings in September 2005, ANSH grew to become a Shari’ah Compliant private hospital in Malaysia.\n" +
                "\n" +
                "This hospital is well-equipped with modern medical facilities that include 24-hour Accident & Emergency Unit, Intensive Care Units, Operating Theatres, Labour Rooms, Clinical Laboratory as well as Rehabilitation and Health Screening Centre.");

    }

    public static void setRunningLayout() {

        mMainMenuLayout.setVisibility(View.INVISIBLE);
        mVisitorLayout.setVisibility(View.INVISIBLE);
        mPatientLayout.setVisibility(View.INVISIBLE);
        mAboutUsLayout.setVisibility(View.INVISIBLE);
        mRunningLayout.setVisibility(View.VISIBLE);

        robotAPI.robot.speak("After you arrived, you can pressed im arrived button, then i will go back to the lobby, if you want to do something, then just pressed home button");

    }

    public static void setGoToPharmacy() {

        robotAPI.robot.speak("Lets go to the pharmacy");
        ArrayList<RoomInfo> arrayListRooms = robotAPI.contacts.room.getAllRoomInfo();
        mapRoom = arrayListRooms.get(0).keyword; //ward

        if(!mapRoom.equals("")) {

            if(isRobotApiInitialed) {
                // use robotAPI to go to the position "keyword":
                robotAPI.motion.goTo(mapRoom);
            }

        }

        setRunningLayout();
        mGoToText.setText("PHARMACY");
        mGoToView.setImageResource(R.drawable.pharmacyi);

    }

    public static void setGoToEmergency() {

        robotAPI.robot.speak("Lets go to the emergency room");
        ArrayList<RoomInfo> arrayListRooms = robotAPI.contacts.room.getAllRoomInfo();
        mapRoom = arrayListRooms.get(1).keyword; //emergency

        if(!mapRoom.equals("")) {

            if(isRobotApiInitialed) {
                // use robotAPI to go to the position "keyword":
                robotAPI.motion.goTo(mapRoom);
            }

        }

        setRunningLayout();
        mGoToText.setText("EMERGENCY");
        mGoToView.setImageResource(R.drawable.emergency);

    }

    public static void setGoToCheckUp() {

        robotAPI.robot.speak("Lets go to the checkup room");
        ArrayList<RoomInfo> arrayListRooms = robotAPI.contacts.room.getAllRoomInfo();
        mapRoom = arrayListRooms.get(0).keyword; //ward

        if(!mapRoom.equals("")) {

            if(isRobotApiInitialed) {
                // use robotAPI to go to the position "keyword":
                robotAPI.motion.goTo(mapRoom);
            }

        }

        setRunningLayout();
        mGoToText.setText("CHECK-UP");
        mGoToView.setImageResource(R.drawable.checkup);

    }

    public static void setGoToLobby() {

        robotAPI.robot.speak("Okey, lets go to the lobby so you can make your registration there");
        ArrayList<RoomInfo> arrayListRooms = robotAPI.contacts.room.getAllRoomInfo();
        mapRoom = arrayListRooms.get(2).keyword; //lobby

        if(!mapRoom.equals("")) {

            if(isRobotApiInitialed) {
                // use robotAPI to go to the position "keyword":
                robotAPI.motion.goTo(mapRoom);
            }

        }

        setRunningLayout();
        mGoToText.setText("LOBBY");
        mGoToView.setImageResource(R.drawable.lobby);

    }

    public static void setGoToWard() {

        robotAPI.robot.speak("Okey, lets go to the ward!");
        ArrayList<RoomInfo> arrayListRooms = robotAPI.contacts.room.getAllRoomInfo();
        mapRoom = arrayListRooms.get(0).keyword; //ward

        if(!mapRoom.equals("")) {

            if(isRobotApiInitialed) {
                // use robotAPI to go to the position "keyword":
                robotAPI.motion.goTo(mapRoom);
            }

        }

        setRunningLayout();
        mGoToText.setText("WARD");
        mGoToView.setImageResource(R.drawable.ward);

    }

    public static void setGoToShop() {

        robotAPI.robot.speak("Okey, lets go to the shop. You can shopping anything you want.");
        ArrayList<RoomInfo> arrayListRooms = robotAPI.contacts.room.getAllRoomInfo();
        mapRoom = arrayListRooms.get(1).keyword; //emergency

        if(!mapRoom.equals("")) {

            if(isRobotApiInitialed) {
                // use robotAPI to go to the position "keyword":
                robotAPI.motion.goTo(mapRoom);
            }

        }

        setRunningLayout();
        mGoToText.setText("SHOP");
        mGoToView.setImageResource(R.drawable.shop);
    }

}

