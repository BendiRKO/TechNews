package com.albendahmetaj.technews;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));



        recyclerView = (RecyclerView) findViewById(R.id.rcView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tech_News");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser()==null)
                {
                    Intent kyqjaIntent = new Intent(MainActivity.this, KyqjaActivity.class);
                    kyqjaIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(kyqjaIntent);
                }
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<TechNews, TechNewsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TechNews, TechNewsViewHolder>(
                TechNews.class,
                R.layout.card_items,
                TechNewsViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(TechNewsViewHolder viewHolder, TechNews model, int position)
            {
                final String post_key = getRef(position).getKey().toString();
                viewHolder.setTitulli(model.getTitulli());
                viewHolder.setPershkrimi(model.getPershkrimi());
                viewHolder.setFotoUrl(getApplicationContext(), model.getFotoUrl());
                viewHolder.setEmri(model.getEmri());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent postihapurActivity = new Intent(MainActivity.this, PostiHapurActivity.class);
                        postihapurActivity.putExtra("PostiID", post_key);
                        startActivity(postihapurActivity);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }



    public static class TechNewsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public TechNewsViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }
        public void setTitulli(String titulli)
        {
            TextView titulli_postit = mView.findViewById(R.id.posti_titulli_txtview);
            titulli_postit.setText(titulli);
        }
        public void setPershkrimi(String pershkrimi)
        {
            TextView pershkrimi_postit = mView.findViewById(R.id.posti_pershkrimi_txtview);
            pershkrimi_postit.setText(pershkrimi);
        }
        public void setFotoUrl (Context ctx, String fotoUrl)
        {
            ImageView foto_posti = mView.findViewById(R.id.posti_fotoja);
            Picasso.with(ctx).load(fotoUrl).into(foto_posti);
        }
        public void setEmri (String emri)
        {
            TextView emri_posti = mView.findViewById(R.id.posti_emri);
            emri_posti.setText(emri);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_shto)
        {
            startActivity(new Intent(MainActivity.this, PostiActivity.class));
        }
        else if (id == R.id.dil)
        {
            mAuth.signOut();
            Intent dilIntent = new Intent(MainActivity.this, RegjistrimiActivity.class);
            dilIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(dilIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
