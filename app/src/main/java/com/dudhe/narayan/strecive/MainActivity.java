package com.dudhe.narayan.strecive;

import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextToSpeech t1;
    TextView textView;
    ListView ls;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    busi bussend;
    private String uid,x;
    int y=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ls=(ListView)findViewById(R.id.card_listView);
        textView=(TextView)findViewById(R.id.aura);
        bussend=new busi();

         firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Bus");
        list=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,R.layout.list_bus,R.id.line1,list);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    bussend = ds.getValue(busi.class);
                    String res = bussend.getbNo() + "          " + bussend.getbFrom() + "         " + bussend.getbTo();
                    list.add(res);
                    String toSpeak = "Bus Number " + bussend.getbNo() + " From  " + bussend.getbFrom() + " to " + bussend.getbTo() + " is arriving in five minutes.";
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
                ls.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"hello error",Toast.LENGTH_SHORT).show();

            }

        });



        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

    }
    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}
