package com.albendahmetaj.technews;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostiActivity extends AppCompatActivity {

    private ImageButton fotoBtn;
    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri uri = null;
    private EditText textTitulli, textPershkrimi;
    private Button postoBtn;
    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef, mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posti);

        postoBtn = (Button) findViewById(R.id.postoBtn);
        textTitulli = (EditText) findViewById(R.id.txtTitulli);
        textPershkrimi = (EditText) findViewById(R.id.txtPershkrimi);
        fotoBtn = (ImageButton) findViewById(R.id.fotoBtn);

        storage = FirebaseStorage.getInstance().getReference();
        databaseRef = database.getInstance().getReference().child("Tech_News");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Përdoruesit").child(mCurrentUser.getUid());


        // Zgjedhja e fotografisë nga galleria
        fotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });


        // Postimi në Firebase
        postoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(PostiActivity.this, "Ju lutem prisni pak... \n Postimi është duke u publikuar", Toast.LENGTH_LONG).show();
                final String TitulliPostit = textTitulli.getText().toString().trim();
                final String PershkrimiPostit = textPershkrimi.getText().toString().trim();


                if (!TextUtils.isEmpty(TitulliPostit) && !TextUtils.isEmpty(PershkrimiPostit))
                {
                    StorageReference filepath = storage.child("fotografia_postimit").child(uri.getLastPathSegment());
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {

                            @SuppressWarnings("VisibleForTests")
                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(getApplicationContext(), "Postimi u publikua me sukses!", Toast.LENGTH_SHORT).show();
                            final DatabaseReference postiRi = databaseRef.push();

                            mDatabaseUsers.addValueEventListener(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    postiRi.child("Titulli").setValue(TitulliPostit);
                                    postiRi.child("Përshkrimi").setValue(PershkrimiPostit);
                                    postiRi.child("fotografiaUrl").setValue(downloadUrl.toString());
                                    postiRi.child("uid").setValue(mCurrentUser.getUid());
                                    postiRi.child("Përdoruesi").setValue(dataSnapshot.child("Emri").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {

                                            if (task.isSuccessful())
                                            {
                                                Intent intent = new Intent(PostiActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError)
                                {

                                }
                            });

                        }
                    });
                }
            }
        });
    }

    // Rezultati i nxerrjes së fotografisë nga galleria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            uri = data.getData();
            fotoBtn.setImageURI(uri);
        }

    }
}
