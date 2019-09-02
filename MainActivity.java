package com.ritrageproductions.app.chatbot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {



    EditText username;
    MediaRecorder recorder;
    String name;
    String saveUrl = "/sdcard/VoiceBot/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        final TextView displayText = findViewById(R.id.displayText);

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {


            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                Toast.makeText(MainActivity.this, "blahblah", Toast.LENGTH_SHORT).show();
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    displayText.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        boolean exists = (new File(saveUrl)).exists();
        if(!exists) {
            new File(saveUrl).mkdirs();
        }
        findViewById(R.id.confirmUsername).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = findViewById(R.id.username);
                name = username.getText().toString() + ".mp3";
            }
        });
//        recorder = new MediaRecorder();
//        recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        recorder.setAudioEncodingBitRate(160 * 1024);
//        recorder.setOutputFile(saveUrl + name);
        findViewById(R.id.onRecord).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        recorder.stop();
                        recorder.reset();
                        recorder.release();
                        recorder = null;
                        mSpeechRecognizer.stopListening();
                    }
                    break;
                    case MotionEvent.ACTION_DOWN: {
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        recorder = new MediaRecorder();
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                        recorder.setAudioEncodingBitRate(160 * 1024);
                        recorder.setOutputFile(saveUrl + name);
                        try {
                            recorder.prepare();
                            //Toast.makeText(MainActivity.this, "nahi kata", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            //Toast.makeText(MainActivity.this, "kat gaya", Toast.LENGTH_SHORT).show();
                        }
                        recorder.start();
                        displayText.setText("");
                        displayText.setHint("Listening...");
                    }
                    break;
                }
                return false;
            }
        });
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
}
