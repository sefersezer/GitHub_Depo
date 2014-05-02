package com.proje.talkymessage;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris);

        menuEkraninaGec();
    }

    private void menuEkraninaGec() {
    	//bir animasyon ve ardýndan menünün gösterimi
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.giris_animasyon);
        ImageView girisLogo = (ImageView) findViewById(R.id.girisLogoImageView);
        anim.reset();
        girisLogo.clearAnimation();
        girisLogo.startAnimation(anim);
        
        anim.setAnimationListener(new AnimationListener() {
        	
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
				//menü gösterilecek.
            }

			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			
        });

    }

}
