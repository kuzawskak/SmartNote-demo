package com.smartnote_demo.share;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.os.Bundle;

public class FacebookLogin extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {
					// make request to;2 the /me API
					Request.executeMeRequestAsync(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response with user
								// object
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {

									}
								}

							});
				}
			}
		});
	}
}
