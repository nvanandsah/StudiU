package com.lk.hackathon.Activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.lk.hackathon.Adapters.NotesAdapter;
import com.lk.hackathon.Notes;
import com.lk.hackathon.R;
import com.lk.hackathon.Utilities.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Notes>>,
        NotesAdapter.OnDownloadClickListener
      {
    RecyclerView mRecyclerView;
    NotesAdapter adapter;
    String url;
    String subject;
    int classStudent;
    List<Notes> data;
    private int LOADER_ID=101;
    private ProgressBar mProgressBar;

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
        adapter=new NotesAdapter(this,null,this);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(LOADER_ID,null,this);

    }
          @Override
          public void OnDownloadClick(String url,String brandName) {
                new DownloadFile(brandName).execute(url);
          }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<List<Notes>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Notes>>(this) {

            @Override
            protected void onStartLoading() {
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Nullable
            @Override
            public List<Notes> loadInBackground() {
                Log.v("TAG",url);
                data=NetworkUtils.extractNotes(url);
                return data;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Notes>> loader, List<Notes> data) {
        adapter.swapData(data);
        if(data.size()==0){
            Toast.makeText(this,"Nothing found",Toast.LENGTH_SHORT).show();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Notes>> loader) {
        adapter.swapData(null);
    }


          private class DownloadFile extends AsyncTask<String,Void,Void>{
        private String brand;
        private String fileName;

        DownloadFile(String brandName){
            brand=brandName;
        }


       @Override
       protected Void doInBackground(String... strings) {
           String fileUrl=strings[0];
           fileName=brand+"_"+classStudent+"_"+subject+".pdf";
           String externalStorageDirectory= Environment.getExternalStorageDirectory().toString();
           Log.v("TAG","external" +externalStorageDirectory);
           File folder=new File(externalStorageDirectory,"StudiU");
           folder.mkdir();
           File pdfFile=new File(folder,fileName);
           try {
               pdfFile.createNewFile();
           } catch (IOException e) {
               e.printStackTrace();
           }
           NetworkUtils.downloadFile(fileUrl,pdfFile);
           return null;
       }

       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);
           Toast.makeText(getApplicationContext(),"File Downloaded",Toast.LENGTH_SHORT).show();
           viewPdf(fileName);
       }
   }

          private void viewPdf(String fileName) {
            Log.v("TAG",fileName);
              File pdfFile = new File(Environment.getExternalStorageDirectory() + "/StudiU/" + fileName);  // -> filename = maven.pdf
              Uri path = Uri.fromFile(pdfFile);
              Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
              pdfIntent.setDataAndType(path, "application/pdf");
              pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

              try{
                  startActivity(pdfIntent);
              }catch(ActivityNotFoundException e){
                  Toast.makeText(this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
              }
          }
      }
