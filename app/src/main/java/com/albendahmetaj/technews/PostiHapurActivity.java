package com.albendahmetaj.technews;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PostiHapurActivity extends AppCompatActivity {

    private ImageView postiFotoja;
    private TextView postiTitulli, postiPershkrimi;
    String post_key = null;
    private DatabaseReference mDatabase;
    private Button shlyejBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posti_hapur);

        postiFotoja = (ImageView) findViewById(R.id.postiHapur_fotoja);
        postiTitulli = (TextView) findViewById(R.id.postiHapur_titulli);
        postiPershkrimi = (TextView) findViewById(R.id.postiHapur_pershkrimi);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tech_News");
        post_key = getIntent().getExtras().getString("PostiID");
        mAuth = FirebaseAuth.getInstance();

        shlyejBtn = (Button) findViewById(R.id.shlyejBtn);
        shlyejBtn.setVisibility(View.INVISIBLE);
        shlyejBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mDatabase.child(post_key).removeValue();

                Intent mainIntent = new Intent(PostiHapurActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String titulli_postit = (String) dataSnapshot.child("Titulli").getValue();
                String pershkrimi_postit = (String) dataSnapshot.child("Përshkrimi").getValue();
                String fotografia_postit = (String) dataSnapshot.child("fotografiaUrl").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                postiTitulli.setText(titulli_postit);
                postiPershkrimi.setText(pershkrimi_postit);
                Picasso.with(PostiHapurActivity.this).load(fotografia_postit).into(postiFotoja);
                if (mAuth.getCurrentUser().getUid().equals(post_uid))
                {
                    shlyejBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
