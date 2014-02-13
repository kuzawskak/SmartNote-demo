package com.smartnote_demo.notepad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

import android.net.Uri;
import android.os.Bundle;

import android.os.StrictMode;
import android.provider.MediaStore;

import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.RelativeLayout;
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


import com.samsung.samm.common.SObjectImage;
import com.samsung.samm.common.SOptionSCanvas;
import com.samsung.spenemulatorlibrary.ActivityWithSPenLayer;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;
import com.samsung.spensdk.applistener.HistoryUpdateListener;
import com.samsung.spensdk.applistener.SCanvasInitializeListener;

import com.smartnote_demo.database.Notepad;
import com.smartnote_demo.database.NotepadDatabaseHandler;
import com.smartnote_demo.database.Site;
import com.smartnote_demo.database.SiteDatabaseHandler;
import com.smartnote_demo.images.GalleryActivity;
import com.smartnote_demo.spen_tools.SPenSDKUtils;
import com.smartnote_demo.share.*;

public class CanvasActivity extends ActivityWithSPenLayer  {

	//===============================
	// App settings for share in Dropbox 
	//===============================
	
	//for DROPBOX share	
    private final String PHOTO_DIR = "/SmartNote-demo/";
    final static private String APP_KEY = "18zpadpciv1g63b";
    final static private String APP_SECRET = "gppsfv7gb0944xt";
    
	DropboxAPI<AndroidAuthSession> mApi;
    private boolean mLoggedIn;
    private String share_filename;
    
    // If you'd like to change the access type to the full Dropbox instead of
   // an app folder, change this value.
   final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;


   //  don't need to change these, leave them alone
   final static private String ACCOUNT_PREFS_NAME = "prefs";
   final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
   final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    private static final boolean USE_OAUTH1 = false;
	
	//================================
    //UI controls
    //================================
	private DrawerLayout mDrawerLayout;
	private FrameLayout	mLayoutContainer;
	private RelativeLayout	mCanvasContainer;
	private SCanvasView		mSCanvas;
	private ImageView		mPenBtn;
	private ImageView		mEraserBtn;
	private ImageView		mUndoBtn;
	private ImageView		mRedoBtn;	
	private ImageView		mTextBtn;
	private ImageView		mAddSiteBtn;
	private ImageView		mStampBtn;
	//right drawer buttons
	private ImageView		mDropboxBtn;
	private ImageView		mSaveBtn;
	private ImageView		mEventBtn;
	//bottom buttons and textview with site number
	private ImageView 		mNextSiteBtn;
	private ImageView		mPrevSiteBtn;
	private TextView 		mSiteNumberTextview;

	//==============================
	// Variables
	//==============================
	Context mContext = null;
	public static String notepad_name;
	private int current_site_number;
	private int sites_count = 1;
	//needed for new site creation
	private int site_template_id;
	private Site current_site;
	
	private Bitmap 	mBGBitmap;
	private Rect	mSrcImageRect = null;

	protected static final int DATE_DIALOG_ID = 0;
	protected static final int REQUEST_CODE_INSERT_STAMP_OBJECT = 100;

	
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
        
  
        checkAppKeySetup();
       
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
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

		mSaveBtn = (ImageView) findViewById(R.id.save_notepad_button);
		mSaveBtn.setOnClickListener(mBtnClickListener);
		
		mNextSiteBtn = (ImageView) findViewById(R.id.next_site_button);
		mNextSiteBtn.setOnClickListener(mBtnClickListener);
		
		mPrevSiteBtn = (ImageView) findViewById(R.id.prev_site_button);
		mPrevSiteBtn.setOnClickListener(mBtnClickListener);
		
		mDropboxBtn = (ImageView) findViewById(R.id.dropbox_button);
		mDropboxBtn.setOnClickListener(mBtnClickListener);
		
		mEventBtn = (ImageView) findViewById(R.id.event_button);
		mEventBtn.setOnClickListener(mBtnClickListener);
				
		mAddSiteBtn = (ImageView) findViewById(R.id.addSiteBtn);
		mAddSiteBtn.setOnClickListener(mBtnClickListener);
		
		mStampBtn = (ImageView) findViewById(R.id.stampBtn);
		mStampBtn.setOnClickListener(mBtnClickListener);
		
		mSiteNumberTextview = (TextView) findViewById(R.id.site_textview);

		if (savedInstanceState != null) {
			notepad_name = savedInstanceState.getString("name");
		}
		NotepadDatabaseHandler notepad_handler = new NotepadDatabaseHandler(this);		
		Notepad current_notepad = notepad_handler.getNotepad(notepad_name);
		site_template_id = current_notepad.getSiteID();
		notepad_handler.close();
		SiteDatabaseHandler site_handler = new SiteDatabaseHandler(this);
		//List<Site> sites = site_handler.getAllSitesFromNotepad(notepad_id);
		//add new site when notepad is opened
		
			String notepad_name = current_notepad.getFileName();
			current_site = new Site(notepad_name,notepad_name+String.format("%d",sites_count),sites_count);
			site_handler.addSite(current_site);
			List<Site> sites = site_handler.getAllSitesFromNotepad(notepad_name);


			if (savedInstanceState != null) {
				Log.v("notepad","setting counters");
				sites_count = savedInstanceState.getInt("sites_counter");
				current_site_number =  savedInstanceState.getInt("current_site_no");
			} else {
				sites_count = sites.size();
				current_site_number = sites.size();
			}
			mSiteNumberTextview.setText(String.format("%d",sites_count)+ " / " + String.format("%d",sites_count,sites_count));
			
			site_handler.close();
		
		//------------------------------------
		// Create SCanvasView
		//------------------------------------
		mLayoutContainer = (FrameLayout) findViewById(R.id.layout_container);
		mCanvasContainer = (RelativeLayout) findViewById(R.id.canvas_container);

		mSCanvas = new SCanvasView(mContext);    
		mCanvasContainer.addView(mSCanvas);

		
		if (savedInstanceState != null) {
			Log.v("notepad","getting bitmap");
			mBGBitmap = savedInstanceState.getParcelable("bitmap");			
		} else {		
				mBGBitmap = BitmapFactory.decodeResource(getResources(), site_template_id);
		}
		if(mBGBitmap != null){
			mSrcImageRect = new Rect(0,0,mBGBitmap.getWidth(), mBGBitmap.getHeight());
		}
			
		setSCanvasViewLayout();
		// Set Background of layout container
		//	mLayoutContainer.setBackgroundResource(site_template_id);
		updatePrevNextState();
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
		//change it and ask if the user wants to SAVE the file
		AlertDialog.Builder ad = new AlertDialog.Builder(mContext);		
		ad.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));	// Android Resource
		ad.setTitle(getResources().getString(R.string.app_name))
		.setMessage("Save the note?")
		.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {				
				//save note				
				savePNGFile(notepad_name+current_site_number);
				// finish dialog
				dialog.dismiss();
				setResult(RESULT_OK, getIntent());
				
				//Close previous activities and go to MainActivity
				Intent intent = new Intent(mContext,MainActivity.class);
			    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    mContext.startActivity(intent);
				finish();
			}
		})
		.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				setResult(RESULT_OK, getIntent());
				
				//Close previous activities and go to MainActivity
				Intent intent = new Intent(mContext,MainActivity.class);
			    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    mContext.startActivity(intent);
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
		else if(nBtnID == mAddSiteBtn.getId()) {
			AddNewSite();
			updatePrevNextState();
			//add new site in editor			
		}
		//bottom menu
		else if(nBtnID == mPrevSiteBtn.getId()){
			//switch to previous site if possible
			Log.v("add_new_site","clicked");
			switchToPrevSite();
		}
		else if(nBtnID == mStampBtn.getId()) {
			Intent intent = new Intent(CanvasActivity.this, GalleryActivity.class);
			GalleryActivity.only_preview = false;
			startActivityForResult(intent, REQUEST_CODE_INSERT_STAMP_OBJECT);
		}
		else if(nBtnID == mNextSiteBtn.getId()){
			//switch to next site if possible
			switchToNextSite();
		}
	
		/**********RIGHT DRAWER**********/
	
		else if(nBtnID == mSaveBtn.getId()){
			//save current state of notepad
			if(savePNGFile(notepad_name+current_site_number)==false) {
				Log.e("smart","failed to save png file");
			}
			mDrawerLayout.closeDrawers();
		
		}
		else if(nBtnID == mDropboxBtn.getId()) {
			//login and share on dropbox
			saveAndSharePngFile();						
		}
		else if(nBtnID == mEventBtn.getId()) {
			Log.v("event","event button clicked");
			showDialog(DATE_DIALOG_ID);
			//add an event associated with notepad
		}
	}
};


public static void setReminder(Context caller, long time, String message)
{ 
	Intent calIntent = new Intent(Intent.ACTION_EDIT); 
	calIntent.setType("vnd.android.cursor.item/event"); 
	calIntent.putExtra("beginTime", time); 
	calIntent.putExtra("allDay", true);
	calIntent.putExtra("title", message); 
	caller.startActivity(calIntent); 
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
    mDrawerLayout.closeDrawers();
	upload.execute();
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

private void showToast(String msg) {
    Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
    error.show();
}

private void loadAuth(AndroidAuthSession session) {
	// TODO Auto-generated method stub
	
}

/**
 * Save current site
 */
private boolean savePNGFile(String filename) 
{    
	//we get the whole bitmap - not only the foreground (arg0 ==false)
	Bitmap bmCanvas = mSCanvas.getCanvasBitmap(false);
	return saveImageToInternalStorage(bmCanvas,filename);
}   

private boolean saveAndSharePngFile() 
{    
	//we get the whole bitmap - not only the foreground (arg0 ==false)
	Bitmap bmCanvas = mSCanvas.getCanvasBitmap(false);
	String current_date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
	share_filename = current_date+".png";
	  if(saveImageToInternalStorage(bmCanvas,current_date))
	  {		
		  //we don't add the file to database, 
		  //bcs it's temporary file for sharing
	        shareOnDropbox(share_filename);
	        return true;
	  }
	  return false;
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



public void deleteCurrentSite() {
	if(sites_count>0) {
		SiteDatabaseHandler db = new SiteDatabaseHandler(this);
	--sites_count;
	
	switchToNextSite();
	}
	
}


public boolean saveCurrentSite() {
	savePNGFile(notepad_name+current_site_number);
	return true;
}

public void switchToNextSite() {
	if(saveCurrentSite()&&current_site_number!=sites_count) {
		++current_site_number;
		//the filename is : notepad_name+current_site_number
		loadCanvasImage(notepad_name+String.format("%d",current_site_number)+".png",true);
		//load bitmap from saved file
		//mSCanvas.setClearImageBitmap(mBGBitmap);
		refreshSiteNumberTextView();
	}
	else {
		Log.d("notepad","failed to save current site");
	}
}

public void switchToPrevSite() {
	Log.v("add_new_site","0");
	if(saveCurrentSite()&&current_site_number!=0) {
		Log.v("add_new_site","1");
		--current_site_number;
		//the filename is : notepad_name+current_site_number
		loadCanvasImage(notepad_name+String.format("%d",current_site_number)+".png",true);
		refreshSiteNumberTextView();
		updatePrevNextState();
	}
	else {
		Log.d("notepad","failed to save current site");
	}

}

public void AddNewSite() {
	if(saveCurrentSite()) {
		sites_count++;
		current_site_number++;
		SiteDatabaseHandler db = new SiteDatabaseHandler(this);
		Site new_site = new Site(notepad_name,notepad_name+String.format("%d",sites_count),sites_count);	
		db.addSite(new_site);
		db.close();
		mSCanvas.setClearImageBitmap(mBGBitmap);
		refreshSiteNumberTextView();
		updatePrevNextState();
	}
	else {
		Log.d("notepad","failed to save current site");
	}
}

public void refreshSiteNumberTextView() {
	mSiteNumberTextview.setText
			(String.format("%d",current_site_number)
			+" / "
			+String.format("%d", sites_count));
}

private DatePickerDialog.OnDateSetListener datePickerListener 
= new DatePickerDialog.OnDateSetListener() {

// when dialog box is closed, below method will be called.
public void onDateSet(DatePicker view, int selectedYear,
int selectedMonth, int selectedDay) {
	int year = selectedYear;
	int month = selectedMonth;
	int day = selectedDay;
	
	Calendar beginTime = Calendar.getInstance();
	beginTime.set(year, month, day, 8, 0);
	long startMillis = beginTime.getTimeInMillis();
	setReminder(mContext, startMillis, notepad_name);
	
	}
};


protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	    outState.putParcelable("bitmap", mSCanvas.getCanvasBitmap(false));
	    outState.putInt("current_site_no", current_site_number);
	    outState.putInt("sites_counter", sites_count);
	    outState.putString("name", notepad_name);
	    
};

@Override
protected Dialog onCreateDialog(int id) {
	switch (id) {
	case DATE_DIALOG_ID:
	   // set date picker as current date
	   return new DatePickerDialog(this, datePickerListener,2014,1,1);
	   
	}
	return null;
}

void updatePrevNextState() {
		mNextSiteBtn.setEnabled(sites_count<=1||current_site_number==sites_count?
				false:true);
		mPrevSiteBtn.setEnabled(sites_count<=1||current_site_number<=1?
				false:true);
}



@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);

	// Check result error
	if(resultCode!=RESULT_OK)
		return;		
	if(data == null)
		return;

	if(requestCode == REQUEST_CODE_INSERT_STAMP_OBJECT) {    			
		//String 
		Uri strStampPath = data.getParcelableExtra("stamp_path");//();//UrExtra("stamp_path");
		
		Bitmap bm = null;
		try {
			 bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), strStampPath);
			//bm = BitmapFactory.decodeStream(mContext.getAssets().open(strStampPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(bm == null)
			return;

		// canvas option setting
		SOptionSCanvas canvasOption = mSCanvas.getOption();					
		if(canvasOption == null)
			return;

		//if(canvasOption.mSAMMOption == null)
		//	return;

		//canvasOption.mSAMMOption.setContentsQuality(PreferencesOfSAMMOption.getPreferenceSaveImageQuality(mContext));
		// option setting
		//mSCanvas.setOption(canvasOption);			

		RectF rectF = getDefaultRect(bm);
		int nContentsQualityOption = canvasOption.mSAMMOption.getContentsQuality();
		SObjectImage sImageObject = new SObjectImage(nContentsQualityOption);
		sImageObject.setRect(rectF);
		try {
			bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), strStampPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//sImageObject.setImageUri(strStampPath);//Path(strStampPath);
		sImageObject.setImageBitmap(bm);

		if(mSCanvas.insertSAMMImage(sImageObject, true)){
			Toast.makeText(this, "Insert image file("+ strStampPath +") Success!", Toast.LENGTH_SHORT).show();	
		}
		else{
			Toast.makeText(this, "Insert image file("+ strStampPath +") Fail!", Toast.LENGTH_LONG).show();    				
		}
	}
}

RectF getDefaultRect(Bitmap bm){
	// Rect Region : Consider image real size		
	int nImageWidth = bm.getWidth();
	int nImageHeight = bm.getHeight();
	int nScreenWidth = mSCanvas.getWidth();
	int nScreenHeight = mSCanvas.getHeight();    			
	int nBoxRadius = (nScreenWidth>nScreenHeight) ? nScreenHeight/8 : nScreenWidth/8;
	int nCenterX = nScreenWidth/2;
	int nCenterY = nScreenHeight/2;
	if(nImageWidth > nImageHeight)
		return new RectF(nCenterX-nBoxRadius,nCenterY-(nBoxRadius*nImageHeight/nImageWidth),nCenterX+nBoxRadius,nCenterY+(nBoxRadius*nImageHeight/nImageWidth));
	else
		return new RectF(nCenterX-(nBoxRadius*nImageWidth/nImageHeight),nCenterY-nBoxRadius,nCenterX+(nBoxRadius*nImageWidth/nImageHeight),nCenterY+nBoxRadius);
}

}