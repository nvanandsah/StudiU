package com.lk.hackathon.Activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lk.hackathon.R;

public class SubjectsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView ScienceTextView;
    TextView SocialTextView;
    TextView MathTextView;
    TextView EnglishTextView;

    TextView CBSETextView;
    TextView JEETextView;
    TextView NEETTextView;

    TextView PhysicsTextView;
    TextView ChemTextView;
    TextView MathHigherTextView;
    TextView BioTextView;

    private int flag;
    private String URL_1="http://54.88.235.156/inspect?class=9&subject=sci";
    private String url_initial="http://54.88.235.156/inspect?";
    String url_initial_chapter="http://54.88.235.156/gettopics?";

    private String classS="class=";
    private String subject="subject=";
    private String And="&";
    private PopupWindow mPopupWindow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        if(intent==null)return;

        flag=intent.getIntExtra("flag_class",9);
        loadContentView(flag);

    }

    private void loadContentView(int flag) {
        if(flag==9 || flag==10){
            setContentView(R.layout.activity_subjects_nine_tenth);
            ScienceTextView=findViewById(R.id.sci);
            ScienceTextView.setOnClickListener(this);
            SocialTextView=findViewById(R.id.soc);
            SocialTextView.setOnClickListener(this);
            MathTextView=findViewById(R.id.mth);
            MathTextView.setOnClickListener(this);
            EnglishTextView=findViewById(R.id.eng);
            EnglishTextView.setOnClickListener(this);

        }
        else if(flag==11 || flag==12){
            setContentView(R.layout.activity_higher_options);
            CBSETextView=findViewById(R.id.cbse);
            CBSETextView.setOnClickListener(this);
            JEETextView=findViewById(R.id.jee);
            JEETextView.setOnClickListener(this);
            NEETTextView=findViewById(R.id.neet);
            NEETTextView.setOnClickListener(this);
        }
        else {
            setContentView(R.layout.activity_subjects_higher);
            PhysicsTextView=findViewById(R.id.phy);
            PhysicsTextView.setOnClickListener(this);
            ChemTextView=findViewById(R.id.chem);
            ChemTextView.setOnClickListener(this);
            MathHigherTextView=findViewById(R.id.mth_high);
            MathHigherTextView.setOnClickListener(this);
            BioTextView=findViewById(R.id.bio);
            BioTextView.setOnClickListener(this);

            if(flag==101)BioTextView.setVisibility(View.GONE);
            if(flag==102) MathHigherTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        Intent intent=null;
        switch (id){
            case R.id.cbse:{
                intent=new Intent(this,SubjectsActivity.class);
                intent.putExtra("flag_class",100);
                intent.putExtra("class",flag);
                intent.putExtra("flag_cat","");
                startActivity(intent);
            }
            break;
            case R.id.jee:{
                intent=new Intent(this,SubjectsActivity.class);
                intent.putExtra("flag_class",101);
                intent.putExtra("class",flag);
                intent.putExtra("flag_cat","jee");
                startActivity(intent);
            }
            break;
            case R.id.neet:{
                intent=new Intent(this,SubjectsActivity.class);
                intent.putExtra("flag_class",102);
                intent.putExtra("class",flag);
                intent.putExtra("flag_cat","neet");
                startActivity(intent);
            }
            break;
//            case R.id.eng:{
//                intent=new Intent(this,NotesActivity.class);
//                intent.putExtra("sub","English");
//                intent.putExtra("class",flag);
//                String url=url_initial+classS;
//                intent.putExtra("url",URL_1);
//                startActivity(intent);
//            }
//            break;
            default:popUpChoice(String.valueOf(v.getTag()));

        }

    }

    private void popUpChoice(final String tag) {
        final LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate the quote view
        View screen = null;
        if (inflater != null) {
            screen = inflater.inflate(R.layout.popup_1,null);
        }
        // Initialize a new instance of popup window
        RelativeLayout mRelLayout=findViewById(R.id.rel_layout);
        mPopupWindow = new PopupWindow(
                screen,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

     //   mPopupWindow.setAnimationStyle(R.style.popup_window_animation_choice);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPopupWindow.setElevation(0.9f);
        }
        // Closes the popup window when touch outside.
        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setFocusable(true);
        // Removes default background.
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mPopupWindow.showAtLocation(mRelLayout, Gravity.CENTER,0,0);

        TextView allTextView=screen.findViewById(R.id.complete);
        TextView chapterWiseTextView=screen.findViewById(R.id.chapter_wise);
        allTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="";
                if(flag==9 || flag==10) {
                    url = url_initial + classS + flag + And + subject + tag;
                    Log.v("TAG", "URL: " + url);
                }
                else {
                    String prefix=getIntent().getStringExtra("flag_cat");
                    int cl=getIntent().getIntExtra("class",11);
                    url=url_initial+classS+cl+And+subject+tag+prefix;

                }
                    Intent intent=new Intent(getApplicationContext(),NotesActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("class",flag);
                    intent.putExtra("sub",tag);
                    mPopupWindow.dismiss();
                    startActivity(intent);
                }

        });

        chapterWiseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="";
                if(flag==9 || flag==10) {
                    url = url_initial_chapter + classS + flag + And + subject + tag;
                    Log.v("TAG", "URL: " + url);
                }
                else {
                    String prefix=getIntent().getStringExtra("flag_cat");
                    int cl=getIntent().getIntExtra("class",11);
                    url=url_initial+classS+cl+And+subject+tag+prefix;

                }
                Intent intent=new Intent(getApplicationContext(),ChaptersActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("class",flag);
                intent.putExtra("sub",tag);
                mPopupWindow.dismiss();
                startActivity(intent);
            }

        });


    }
}
