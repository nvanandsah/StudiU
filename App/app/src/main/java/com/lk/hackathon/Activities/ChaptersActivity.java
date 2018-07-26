package com.lk.hackathon.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lk.hackathon.Adapters.ChaptersAdapter;
import com.lk.hackathon.Adapters.NotesAdapter;
import com.lk.hackathon.Chapter;
import com.lk.hackathon.Notes;
import com.lk.hackathon.R;
import com.lk.hackathon.Utilities.NetworkUtils;

import java.util.List;

public class ChaptersActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Chapter>>,ChaptersAdapter.OnChapterClickListener {
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private ChaptersAdapter adapter;
    private List<Chapter> data;
    private String url_initial="http://54.88.235.156/getresources?";

    private int LOADER_ID=102;
    String url;
    String subject;
    int classStudent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        mProgressBar=findViewById(R.id.progress_bar);
        Intent intent=getIntent();
        if(intent!=null){
            url=intent.getStringExtra("url");
            subject=intent.getStringExtra("sub");
            classStudent=intent.getIntExtra("class",10);
            //  Log.v("TAG",url+classStudent+subject);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_notes);
        adapter=new ChaptersAdapter(this,null,this);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(LOADER_ID,null,this);
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<List<Chapter>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Chapter>>(this) {

            @Override
            protected void onStartLoading() {
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Nullable
            @Override
            public List<Chapter> loadInBackground() {
                adapter.swapData(null);
                data= NetworkUtils.extractChapters(url);
                return data;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Chapter>> loader, List<Chapter> data) {
        adapter.swapData(data);
        if(data==null||data.size()==0){
            Toast.makeText(this,"Nothing found",Toast.LENGTH_SHORT).show();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Chapter>> loader) {
        adapter.swapData(null);
    }

    @Override
    public void OnChapterClick(String id) {
        String url=url_initial+"id="+id;
        Intent intent=new Intent(getApplicationContext(),NotesActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("class",classStudent);
        intent.putExtra("sub",subject);
        startActivity(intent);
    }
}
