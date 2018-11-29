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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegjistrimiActivity extends AppCompatActivity {

    private Button regjistrohuBtn;
    private EditText emriRegjistrohu, adresaRegjistrohu, fjalekalimiRegjistrohu;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView kyqjaTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regjistrimi);

        kyqjaTxtView = (TextView) findViewById(R.id.kyquTxtView);
        regjistrohuBtn = (Button) findViewById(R.id.regjistrohuBtn);
        emriRegjistrohu = (EditText) findViewById(R.id.regjistrohu_emri);
        adresaRegjistrohu = (EditText) findViewById(R.id.regjistrohu_adresa);
        fjalekalimiRegjistrohu = (EditText) findViewById(R.id.regjistrohu_fjalekalimi);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Përdoruesit");

        kyqjaTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(RegjistrimiActivity.this, KyqjaActivity.class));
                finish();
            }
        });

        regjistrohuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(RegjistrimiActivity.this, "Ju lutem prisni pak...", Toast.LENGTH_LONG).show();
                final String emri = emriRegjistrohu.getText().toString().trim();
                final String adresa = adresaRegjistrohu.getText().toString().trim();
                final String fjalekalimi = fjalekalimiRegjistrohu.getText().toString().trim();

                if (!TextUtils.isEmpty(adresa) && !TextUtils.isEmpty(fjalekalimi) && !TextUtils.isEmpty(emri))
                {
                    mAuth.createUserWithEmailAndPassword(adresa, fjalekalimi).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = mDatabase.child(user_id);
                            current_user_db.child("Emri").setValue(emri);
                            current_user_db.child("Fotografia").setValue("Default");
                            Toast.makeText(RegjistrimiActivity.this, "Regjistrimi u bë me sukses!", Toast.LENGTH_SHORT).show();
                            Intent regjIntent = new Intent(RegjistrimiActivity.this, MainActivity.class);
                            regjIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(regjIntent);
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegjistrimiActivity.this, "Ju lutem mbushni të gjitha fushat...", Toast.LENGTH_SHORT).show();

                }
                finish();
            }
        });

    }
}
