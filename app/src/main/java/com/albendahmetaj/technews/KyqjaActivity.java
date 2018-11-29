package com.albendahmetaj.technews;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KyqjaActivity extends AppCompatActivity {

    private EditText kyqjaAdresa, kyqjaFjalekalimi;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button kyqjaBtn;
    private TextView regjistrohuTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyqja);

        regjistrohuTextView = (TextView) findViewById(R.id.regjistrohuTxtView);
        kyqjaBtn = (Button) findViewById(R.id.kyquBtn);
        kyqjaAdresa = (EditText) findViewById(R.id.kyqu_adresa);
        kyqjaFjalekalimi = (EditText) findViewById(R.id.kyqu_fjalekalimi);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Përdoruesit");

        regjistrohuTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(KyqjaActivity.this, RegjistrimiActivity.class));
                finish();
            }
        });

        kyqjaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(KyqjaActivity.this, "Ju lutem prisni pak...", Toast.LENGTH_LONG).show();
                String adresa = kyqjaAdresa.getText().toString().trim();
                String fjalekalimi = kyqjaFjalekalimi.getText().toString().trim();
                finish();



                if (!TextUtils.isEmpty(adresa) && !TextUtils.isEmpty(fjalekalimi))
                {
                    mAuth.signInWithEmailAndPassword(adresa, fjalekalimi).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                checkUserExistence();
                            }
                            else 
                            {
                                Toast.makeText(KyqjaActivity.this, "Nuk mund të kyqesh... \n Adresa ose fjalëkalimi i pasaktë!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {

                    Toast.makeText(KyqjaActivity.this, "Ju lutem mbushni të gjitha fushat...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void checkUserExistence()
    {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id))
                {
                    startActivity(new Intent(KyqjaActivity.this, MainActivity.class));
                }
                else 
                {

                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
