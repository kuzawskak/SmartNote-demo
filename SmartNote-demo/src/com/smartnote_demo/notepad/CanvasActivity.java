package com.smartnote_demo.notepad;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.drm.DrmStore.RightsStatus;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import com.example.smartnote_demo.MainActivity;
import com.example.smartnote_demo.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.StatusCallback;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.model.OpenGraphAction;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.PendingCall;
import com.samsung.spenemulatorlibrary.ActivityWithSPenLayer;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;
import com.samsung.spensdk.applistener.HistoryUpdateListener;
import com.samsung.spensdk.applistener.SCanvasInitializeListener;
import com.smartnote_demo.database.Memo;
import com.smartnote_demo.database.MemoDatabaseHandler;
import com.smartnote_demo.spen_tools.SPenSDKUtils;
import com.smartnote_demo.share.*;

import android.widget.CursorAdapter;
public class CanvasActivity extends ActivityWithSPenLayer implements API_Listener {

	//for DROPBOX share	
    private final String PHOTO_DIR = "/SmartNote-demo/";
    final static private String APP_KEY = "18zpadpciv1g63b";
    final static private String APP_SECRET = "gppsfv7gb0944xt";
    
	DropboxAPI<AndroidAuthSession> mApi;
    private boolean mLoggedIn;

    
    //  Facebook APP ID
    final static private String APPL_ID = "242229975950290"; // Replace your App ID here
    // Instance of Facebook Class
    private Facebook fb;
    
    private SharedPreferences preferences_for_fb;
    
    
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    
    

    
    // If you'd like to change the access type to the full Dropbox instead of
   // an app folder, change this value.
   final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

   // You don't need to change these, leave them alone.
   final static private String ACCOUNT_PREFS_NAME = "prefs";
   final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
   final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    private static final boolean USE_OAUTH1 = false;
	
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mRightDrawer;

	//==============================
	// Variables
	//==============================
	Context mContext = null;

	private Bitmap 	mBGBitmap;
	private Rect	mSrcImageRect = null;

	private final static int    CANVAS_HEIGHT_MARGIN = 160; // Top,Bottom margin  
	private final static int    CANVAS_WIDTH_MARGIN = 50; // Left,Right margin

	private FrameLayout	mLayoutContainer;
	private RelativeLayout	mCanvasContainer;
	private SCanvasView		mSCanvas;
	private ImageView		mPenBtn;
	private ImageView		mEraserBtn;
	private ImageView		mUndoBtn;
	private ImageView		mRedoBtn;	
	private ImageView		mTextBtn;
	private ImageView		mSaveBtn;	
	
	
	private ImageView		mShareBtn;	
	private ImageView		mExportBtn;
	private ImageView		mNewSiteBtn;

		
	private File mFolder = null;
	private float mZoomValue = 1f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_extended_ui);
		
		mContext = this;
		// We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);
    	//temporary solution
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 
        
        fb = new Facebook(APPL_ID);
        preferences_for_fb = getPreferences(MODE_PRIVATE);
        String access_token = preferences_for_fb.getString("access_token",null);
        long expires = preferences_for_fb.getLong("acces_expires", 0);
        
        if(access_token!=null) {
        	fb.setAccessToken(access_token);
        }
        if(expires!=0) {
        	fb.setAccessExpires(expires);
        }
        
//     // start Facebook Login
//        Session.openActiveSession(this, true, new Session.StatusCallback() {
//
//			@Override
//			public void call(Session session, SessionState state,
//					Exception exception) {
//				if (session.isOpened()) {
//		              // make request to;2 the /me API
//		                 Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
//
//							@Override
//							public void onCompleted(GraphUser user, Response response) {
//								// TODO Auto-generated method stub
//								
//							}
//		               });
//		              }
//				
//			}
//        	
//
//        });
        
        
        
        
        
        
        
        checkAppKeySetup();
       
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mRightDrawer = (LinearLayout)findViewById(R.id.right_drawer);
        
        mDrawerLayout.setDrawerShadow(R.drawable.notepad, GravityCompat.START);
        MemoDatabaseHandler handler = new MemoDatabaseHandler(this);
        String[] arrayColumns = new String[]{"date"};
        //arrayViewID contains the id of textViews
        // you can add more Views as per Requirement
        // textViewSMSSender is connected to "address" of arrayColumns
        // textViewMessageBody is connected to "body"of arrayColumns
        int[] arrayViewIDs = new int[]{com.example.smartnote_demo.R.id.text1};
        Cursor cursor;
        cursor = handler.GetCursor();
       //cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.drawer_list_item, cursor,arrayColumns,arrayViewIDs,CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
       // SimpleCursorAdapter a = new Sim
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(adapter);
        		//new ArrayAdapter<String>(this,
               // R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		//------------------------------------
		// UI Setting
		//------------------------------------
		mPenBtn = (ImageView) findViewById(R.id.penBtn);
		mPenBtn.setOnClickListener(mBtnClickListener);
		
		mEraserBtn = (ImageView) findViewById(R.id.eraseBtn);
		mEraserBtn.setOnClickListener(mBtnClickListener);

		mTextBtn = (ImageView) findViewById(R.id.textBtn);
		mTextBtn.setOnClickListener(mBtnClickListener);
		
		mUndoBtn = (ImageView) findViewById(R.id.undoBtn);
		mUndoBtn.setOnClickListener(undoNredoBtnClickListener);
		
		mRedoBtn = (ImageView) findViewById(R.id.redoBtn);
		mRedoBtn.setOnClickListener(undoNredoBtnClickListener);

		mSaveBtn = (ImageView) findViewById(R.id.saveBtn);
		mSaveBtn.setOnClickListener(mBtnClickListener);
		
		mShareBtn = (ImageView) findViewById(R.id.share_button);
		mShareBtn.setOnClickListener(mBtnClickListener);
		
		mExportBtn = (ImageView) findViewById(R.id.export_button);
		mExportBtn.setOnClickListener(mBtnClickListener);
		
		mNewSiteBtn = (ImageView) findViewById(R.id.new_site);
		mNewSiteBtn.setOnClickListener(mBtnClickListener);
		//------------------------------------
		// Create SCanvasView
		//------------------------------------
		mLayoutContainer = (FrameLayout) findViewById(R.id.layout_container);
		mCanvasContainer = (RelativeLayout) findViewById(R.id.canvas_container);

		mSCanvas = new SCanvasView(mContext);        
		mCanvasContainer.addView(mSCanvas);

		mBGBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.page4);
		if(mBGBitmap != null){
			mSrcImageRect = new Rect(0,0,mBGBitmap.getWidth(), mBGBitmap.getHeight());
		}

		setSCanvasViewLayout();
		// Set Background of layout container
		//mLayoutContainer.setBackgroundResource(R.drawable.page4);

		
		//------------------------------------
		// SettingView Setting
		//------------------------------------
		// Resource Map for Layout & Locale
		HashMap<String,Integer> settingResourceMapInt = SPenSDKUtils.getSettingLayoutLocaleResourceMap(true, true, false, false);
		// Talk & Description Setting by Locale
		SPenSDKUtils.addTalkbackAndDescriptionStringResourceMap(settingResourceMapInt);
		// Resource Map for Custom font path
		HashMap<String,String> settingResourceMapString = SPenSDKUtils.getSettingLayoutStringResourceMap(true, true, false, false);
		// Create Setting View
		mSCanvas.createSettingView(mLayoutContainer, settingResourceMapInt, settingResourceMapString);

		
		
	
		//====================================================================================
		//
		// Set Callback Listener(Interface)
		//
		//====================================================================================
		//------------------------------------------------
		// SCanvas Listener
		//------------------------------------------------
		SCanvasInitializeListener mSCanvasInitializeListener = new SCanvasInitializeListener() {
			@Override
			public void onInitialized() { 
				//--------------------------------------------
				// Start SCanvasView/CanvasView Task Here

				// Set Title
				if(!mSCanvas.setTitle("SPen-SDK Test"))
					Toast.makeText(mContext, "Fail to set Title.", Toast.LENGTH_LONG).show();

				// Set Initial Setting View Size
				mSCanvas.setSettingViewSizeOption(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_EXT);

				// Set Pen Only Mode with Finger Control - the finger won't be used for drawing
				//mSCanvas.setFingerControlPenDrawing(true);

				// Update button state
				updateModeState();

				// Set Background Image
				if(!mSCanvas.setBGImage(mBGBitmap)){
					Toast.makeText(mContext, "Fail to set Background Image Bitmap.", Toast.LENGTH_LONG).show();
				}
			}
		};

		//------------------------------------------------
		// History Change
		//------------------------------------------------
		HistoryUpdateListener mHistoryUpdateListener = new HistoryUpdateListener() {
			@Override
			public void onHistoryChanged(boolean undoable, boolean redoable) {
				mUndoBtn.setEnabled(undoable);
				mRedoBtn.setEnabled(redoable);
			}
		};

		// Register Application Listener
		mSCanvas.setSCanvasInitializeListener(mSCanvasInitializeListener);
		mSCanvas.setHistoryUpdateListener(mHistoryUpdateListener);

		mUndoBtn.setEnabled(false);
		mRedoBtn.setEnabled(false);
		mPenBtn.setSelected(true);

		// Caution:
		// Do NOT load file or start animation here because we don't know canvas size here.
		// Start such SCanvasView Task at onInitialized() of SCanvasInitializeListener
        }

	/* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }	
	
    private void selectItem(int position) {
    	//set new bitmap
        android.database.sqlite.SQLiteCursor c =(SQLiteCursor) mDrawerList.getItemAtPosition(position);
    	Log.v("loading",c.getString(2));
    	String filename = c.getString(2);
    	loadCanvasImage(filename,true);
    	
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    
    @Override
    protected void onResume()
    {
           super.onResume();
           AndroidAuthSession session = mApi.getSession();

           // The next part must be inserted in the onResume() method of the
           // activity from which session.startAuthentication() was called, so
           // that Dropbox authentication completes properly.
           if (session.authenticationSuccessful())
           {
                  try {
                        // Mandatory call to complete the auth
                        session.finishAuthentication();

                        // Store it locally in our app for later use
                        TokenPair tokens = session.getAccessTokenPair();
                        if(tokens!=null)
                        {
                        storeKeys(tokens.key, tokens.secret);
                        setLoggedIn(true);
                        }
                  } catch (IllegalStateException e) {
                        showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                       
                  }
           }
    }
    
    public class UiAsyncTask extends AsyncTask<Void, Void, Void> {

        public void onPreExecute() {
            // On first execute
        }

        public Void doInBackground(Void... unused) {
            // Background Work
             Log.d("Tests", "Testing graph API wall post");
             try {
                    //String response = fb.request("me");
                    Bundle parameters = new Bundle();
                    parameters.putString("message", "This test message for wall post");
                    parameters.putString("description", "test test test");
                    String response = fb.request("feed", parameters, "POST");
                    Log.d("Tests", "got response: " + response);
                    if (response == null || response.equals("") || response.equals("false")) {
                       Log.v("Error", "Blank response");
                    }
             } catch(Exception e) {
                 e.printStackTrace();
             }
            return null;
        }

        public void onPostExecute(Void unused) {
             // Result
        }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		exitActivity();	
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Release SCanvasView resources
		if(!mSCanvas.closeSCanvasView())
			Log.e("smart", "Fail to close SCanvasView");
	}	

	
	private void exitActivity(){
		// TODO Auto-generated method stub		
		//change it and ask if the user wants to SAVE the file
		AlertDialog.Builder ad = new AlertDialog.Builder(mContext);		
		ad.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));	// Android Resource
		ad.setTitle(getResources().getString(R.string.app_name))
		.setMessage("Save the note?")
		.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {				
				//save note				
				saveSAMMFile();
				// finish dialog
				dialog.dismiss();
				setResult(RESULT_OK, getIntent());
				finish();
			}
		})
		.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				setResult(RESULT_OK, getIntent());
				finish();
			}
		})
		.show();
		ad = null;	
	}

@Override
public void onConfigurationChanged(Configuration newConfig) {
	setSCanvasViewLayout();
	super.onConfigurationChanged(newConfig);
}


void setSCanvasViewLayout(){
	//TODO: consider changing it back to original and try to get rid of invisible margins (now we draw also on margin with background set)!
	Rect rectCanvas = getMaximumCanvasRect(mSrcImageRect,0,0);// CANVAS_WIDTH_MARGIN, CANVAS_HEIGHT_MARGIN);
	int nCurWidth = rectCanvas.right-rectCanvas.left;
	int nCurHeight = rectCanvas.bottom-rectCanvas.top;
	// Place SCanvasView In the Center
	FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)mCanvasContainer.getLayoutParams();		
	layoutParams.width = nCurWidth;
	layoutParams.height= nCurHeight;
	layoutParams.gravity = Gravity.CENTER; 
	mCanvasContainer.setLayoutParams(layoutParams);
}

private OnClickListener undoNredoBtnClickListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
		if (v.equals(mUndoBtn)) {
			mSCanvas.undo();
		} else if (v.equals(mRedoBtn)) {
			mSCanvas.redo();
		}
		mUndoBtn.setEnabled(mSCanvas.isUndoable());
		mRedoBtn.setEnabled(mSCanvas.isRedoable());
	}
};



OnClickListener mBtnClickListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
		int nBtnID = v.getId();
		// If the mode is not changed, open the setting view. If the mode is same, close the setting view. 
		if(nBtnID == mPenBtn.getId()){				
			if(mSCanvas.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_PEN){
				mSCanvas.setSettingViewSizeOption(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_EXT);
				mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN);
			}
			else{
				mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
				mSCanvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);					
				updateModeState();
			}
		}
		else if(nBtnID == mEraserBtn.getId()){
			if(mSCanvas.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER){
				mSCanvas.setSettingViewSizeOption(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_NORMAL);
				mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER);
			}

			else {
				mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
				mSCanvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
				updateModeState();
			}			
		}
		else if(nBtnID == mTextBtn.getId()){
			if(mSCanvas.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_TEXT){
				mSCanvas.setSettingViewSizeOption(SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT, SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_NORMAL);
				mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT);
			}
			else{
				mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_TEXT);
				mSCanvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT, false);										
				updateModeState();
				Toast.makeText(mContext, "Tap Canvas to insert Text", Toast.LENGTH_SHORT).show();
			}
		}
		else if(nBtnID == mSaveBtn.getId()){

			if(saveSAMMFile()==false)
				Log.e("smart","failed to save samm file");
		
			//temporary zoom testing			
			//thats works! but we want probably canvas to fill the whole place
		//	mSCanvas.setCanvasZoomScale(mZoomValue += 0.2, false);
		//	mSCanvas.setCanvasZoomScale(mZoomValue, true);			
		}
		else if(nBtnID == mShareBtn.getId()) {
			saveAndSharePngFile();						
		}
		else if(nBtnID == mExportBtn.getId()) {	
				try {
					shareOnFacebook();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
		}
		else if(nBtnID == mNewSiteBtn.getId()) {
			//add new site in editor			
		}
	}
};


/*FACEBOOK share function*/
private boolean shareOnFacebook() throws FileNotFoundException, MalformedURLException, IOException, JSONException {


if(fb.isSessionValid()) {

	//postToWall();
	FacebookLogin fb_login_async_task = new FacebookLogin(mSCanvas, CanvasActivity.this, fb,preferences_for_fb);
	fb_login_async_task.execute();
}else 

//{
//	FacebookLogin fb_login_async_task = new FacebookLogin(mSCanvas, CanvasActivity.this, fb,preferences_for_fb);
//	fb_login_async_task.execute();
//}

{
	//login to facebook
	String[] perm = {"publish_stream","photo_upload"};
	fb.authorize(CanvasActivity.this,perm, new DialogListener() {
		
		@Override
		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub
			Toast.makeText(CanvasActivity.this,"onFacebookError", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onError(DialogError e) {
			// TODO Auto-generated method stub
			Toast.makeText(CanvasActivity.this,"onError", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
			Editor  editor = preferences_for_fb.edit();
			editor.putString("access_token",fb.getAccessToken());
			editor.putLong("acccess_expires", fb.getAccessExpires());
			editor.commit();
			Toast.makeText(CanvasActivity.this,"onComplete", Toast.LENGTH_SHORT).show();

					FacebookLogin fb_login_async_task = new FacebookLogin(mSCanvas, CanvasActivity.this, fb,preferences_for_fb);
					fb_login_async_task.execute();
					
		}
		
		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			Toast.makeText(CanvasActivity.this,"onCancel", Toast.LENGTH_SHORT).show();
		}
	});
}

return true;
}

void postToWall() throws FileNotFoundException, MalformedURLException, IOException, JSONException {
	Bitmap bmCanvas = mSCanvas.getCanvasBitmap(false);
	  
	     String albumId = "me";	    	 
//works!!!!!!!!!!!!!!!!!!!
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    bmCanvas.compress(Bitmap.CompressFormat.JPEG,100,bos);
	    byte[] photoBytes = bos.toByteArray();
	     
	    Bundle params = new Bundle();
	    params.putByteArray("picture", photoBytes);
	     
	    try {
	        String resp = fb.request(albumId + "/photos", params, "POST");
	        JSONObject json = Util.parseJson(resp);
	
	    } catch ( IOException e ) {
	    	Log.v("facebook","IOException");
	    } catch ( FacebookError e ) {
	    	Log.v("facebook","FacebookError");
	    } catch ( JSONException e ) {
	    	Log.v("facebook","JSONException");
	    }

}
	


/*
 * DROPBOX share function
 */

private boolean shareOnDropbox(String fileName ) {
	if (USE_OAUTH1) {
        mApi.getSession().startAuthentication(CanvasActivity.this);
    } else {
        mApi.getSession().startOAuth2Authentication(CanvasActivity.this);
    }
	
	File filePath = getFileStreamPath(fileName);
	
	DropboxUploadPicture upload = new DropboxUploadPicture(this, mApi,PHOTO_DIR,filePath);
    upload.execute();
	//Upload upload = new Upload(1,this,mApi, PHOTO_DIR,filePath);
	//upload.execute();
	return true;
}

private void checkAppKeySetup() {
    // Check to make sure that we have a valid app key
    if (APP_KEY.startsWith("CHANGE") ||
            APP_SECRET.startsWith("CHANGE")) {
        showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
        finish();
        return;
    }
}

/*private AndroidAuthSession buildSession() {
    AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);

    AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
    loadAuth(session);
    return session;
}*/

private void showToast(String msg) {
    Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
    error.show();
}

private void loadAuth(AndroidAuthSession session) {
	// TODO Auto-generated method stub
	
}



private boolean saveSAMMFile() 
{    
	//we get the whole bitmap - not only the foreground (arg0 ==false)
	Bitmap bmCanvas = mSCanvas.getCanvasBitmap(false);
	 String current_date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
	 String filename = current_date+".png";
	  if(saveImageToInternalStorage(bmCanvas,current_date))
	  {
		  	MemoDatabaseHandler db = new MemoDatabaseHandler(this);
		  	Log.d("Insert: ", "Inserting ...");	 
	        db.addMemo(new Memo(filename,current_date));
	        return true;
	  }
	  return false;
		  //add to database

	
}       
private boolean saveAndSharePngFile() 
{    
	//we get the whole bitmap - not only the foreground (arg0 ==false)
	Bitmap bmCanvas = mSCanvas.getCanvasBitmap(false);
	 String current_date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
	 String filename = current_date+".png";
	  if(saveImageToInternalStorage(bmCanvas,current_date))
	  {
		  	MemoDatabaseHandler db = new MemoDatabaseHandler(this);
		  	Log.d("Insert: ", "Inserting ...");	 
	        db.addMemo(new Memo(filename,current_date));
	        Log.d("Insert",filename + "inserted ");
	        shareOnDropbox(filename);
	        return true;
	  }
	  
	  return false;
		  //add to database

	
}   

//it will be used rather in Directories menu when we will be loading saved notepad
private boolean loadCanvasImage(String fileName, boolean loadAsForegroundImage) {
{
	// Look for file only in internal storage!
	Bitmap bmForeground = null;;
	try {
	File filePath = getFileStreamPath(fileName);
	FileInputStream fi = new FileInputStream(filePath);
	bmForeground =  BitmapFactory.decodeStream(fi);
	if(bmForeground==null) Log.e("loadCanvasImage","no bitmap");
	} catch (Exception ex) {
	Log.e("getThumbnail() on internal storage", ex.getMessage());
	}
	
		int nWidth = mSCanvas.getWidth();
		int nHeight = mSCanvas.getHeight();
		Log.e("smart","load bm");
		bmForeground=Bitmap.createScaledBitmap(bmForeground, nWidth, nHeight, true);		
		return mSCanvas.setClearImageBitmap(bmForeground);
	} 
	
}

public boolean saveImageToInternalStorage(Bitmap image,String filename) {

    try {
    // Use the compress method on the Bitmap object to write image to
    // the OutputStream
    FileOutputStream fos = openFileOutput(filename+".png", Context.MODE_PRIVATE);

    // Writing the bitmap to the output stream
    image.compress(Bitmap.CompressFormat.PNG, 100, fos);
    fos.close();
	  
       
    return true;
    } catch (Exception e) {
    Log.e("saveToInternalStorage()", e.getMessage());
    return false;
    }
    }
 
// Update tool button
private void updateModeState(){
	int nCurMode = mSCanvas.getCanvasMode();
	mPenBtn.setSelected(nCurMode==SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
	mEraserBtn.setSelected(nCurMode==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
}


// Get the minimum image scaled rect which is fit to current screen 
Rect getMaximumCanvasRect(Rect rectImage, int nMarginWidth, int nMarginHeight){
	DisplayMetrics displayMetrics = new DisplayMetrics();
	WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
	wm.getDefaultDisplay().getMetrics(displayMetrics);
	int nScreenWidth =  displayMetrics.widthPixels - nMarginWidth;
	int nScreenHeight = displayMetrics.heightPixels - nMarginHeight;

	int nImageWidth = rectImage.right - rectImage.left;
	int nImageHeight = rectImage.bottom - rectImage.top;			

	float fResizeWidth = (float) nScreenWidth / nImageWidth;
	float fResizeHeight = (float) nScreenHeight / nImageHeight;
	float fResizeRatio;

	// Fit to Height
	if(fResizeWidth>fResizeHeight){
		fResizeRatio = fResizeHeight;
	}
	// Fit to Width
	else {	
		fResizeRatio = fResizeWidth;
	}
	//return new Rect(0,0, (int)(nImageWidth*fResizeRatio), (int)(nImageHeight*fResizeRatio));
	// Adjust more detail
	int nResizeWidth = (int)(nImageWidth*fResizeRatio);
	int nResizeHeight = (int)(0.5 + (nResizeWidth * nImageHeight)/(float)nImageWidth);		
	return new Rect(0,0, nResizeWidth, nResizeHeight);
}



// This is what gets called on finishing a media piece to import


private void logOut()
{
       // Remove credentials from the session
       mApi.getSession().unlink();

       // Clear our stored keys
       clearKeys();
       // Change UI state to display logged out version
       setLoggedIn(false);
}

/**
 * Convenience function to change UI state based on being logged in
 */
private void setLoggedIn(boolean loggedIn)
{

       mLoggedIn = loggedIn;     
    
}

/**
* Shows keeping the access keys returned from Trusted Authenticator in a local
* store, rather than storing user name & password, and re-authenticating each
* time (which is not to be done, ever).
*
* @return Array of [access_key, access_secret], or null if none stored
*/
private String[] getKeys() {
      SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
      String key = prefs.getString(ACCESS_KEY_NAME, null);
      String secret = prefs.getString(ACCESS_SECRET_NAME, null);
      if (key != null && secret != null) {
             String[] ret = new String[2];
             ret[0] = key;
             ret[1] = secret;
             return ret;
      } else {
             return null;
      }
}



/**
 * Shows keeping the access keys returned from Trusted Authenticator in a local
 * store, rather than storing user name & password, and re-authenticating each
 * time (which is not to be done, ever).
 */
private void storeKeys(String key, String secret) {
       // Save the access key for later
       SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
       Editor edit = prefs.edit();
       edit.putString(ACCESS_KEY_NAME, key);
       edit.putString(ACCESS_SECRET_NAME, secret);
       edit.commit();
}

private void clearKeys() {
       SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
       Editor edit = prefs.edit();
       edit.clear();
       edit.commit();
}

private AndroidAuthSession buildSession() {
       AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
       AndroidAuthSession session;

       String[] stored = getKeys();
       if (stored != null) {
              AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
              session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
       } else {
              session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
       }

       return session;
}      

@Override
public void onSuccess(int requestnumber, Object obj) {
	// TODO Auto-generated method stub
	 try
     {
            if(requestnumber == Constants.UploadPhotos_Code)
            {
                  boolean sucess=(Boolean) obj;
                  if(sucess)
                  {
                         Toast.makeText(CanvasActivity.this, "Photos uploaded successfully", Toast.LENGTH_LONG).show();
                         Intent i=new Intent(CanvasActivity.this,CanvasActivity.class);
                         
                         startActivity(i);
                         finish();
                  }
            }
     }
     catch (Exception e)
     {
            e.printStackTrace();
     }
     
}


@Override
public void onFail(String errormessage) {
	// TODO Auto-generated method stub
	
}	
}