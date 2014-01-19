package com.example.smartnote_demo;

import android.app.Application;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ExitButtonListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		final Animation animAlpha = AnimationUtils.loadAnimation(v.getContext(), R.anim.anim_alpha);
		
		animAlpha.setAnimationListener(new Animation.AnimationListener(){
		    @Override
		    public void onAnimationStart(Animation arg0) {
		    }           
		    @Override
		    public void onAnimationRepeat(Animation arg0) {
		    }           
		    @Override
		    public void onAnimationEnd(Animation arg0) {
		    	System.exit(0);
		    }
		});
		 v.startAnimation(animAlpha);
		//System.exit(0);
		//maybe add methods showing dialogs to save notepads or sth like that
	}

		

}
