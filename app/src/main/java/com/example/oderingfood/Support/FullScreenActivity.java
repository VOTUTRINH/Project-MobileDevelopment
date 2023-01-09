package com.example.oderingfood.Support;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.oderingfood.R;
import com.squareup.picasso.Picasso;


public class FullScreenActivity extends Activity
{

    ImageView imgAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        Intent intent = getIntent();
        String res = intent.getStringExtra("id");
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar1);
        Picasso.get().load(res).into(imgAvatar);    }
}
