package com.smartnote_demo.notepad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
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
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import com.example.smartnote_demo.R;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphObject;
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
    private UiLifecycleHelper uiHelper;
    
    // Your Facebook APP ID
    final static private String APPL_ID = "242229975950290"; // Replace your App ID here
 
    // Instance of Facebook Class
    private Facebook facebook;
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
		Log.v("facebook","still alive");
		// We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);
    	
        checkAppKeySetup();
        Log.v("facebook","still alive");
        //facebook = new Facebook
        //mAsyncRunner = new AsyncFacebookRunner(facebook);
        uiHelper = new UiLifecycleHelper(this,null);
    	Log.v("facebook","still alive");
       // uiHelper.onCreate(savedInstanceState);
		Log.v("facebook","still alive");
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mRightDrawer = (LinearLayout)findViewById(R.id.right_drawer);
        
        Log.v("facebook","still alive");
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
           uiHelper.onResume();
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
		uiHelper.onDestroy();
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
			List<Bitmap> list = new ArrayList<Bitmap>();
			list.add((Bitmap) mSCanvas.getCanvasBitmap(false));
			shareOnFacebook(list);
			//export to pdf
						
		}
		else if(nBtnID == mNewSiteBtn.getId()) {
			//add new site in editor
			
		}
	}
};


private void publishPhoto(String imageURL) {
    Log.d("FACEBOOK", "Post to Facebook!");

    try {

        JSONObject attachment = new JSONObject();
        attachment.put("message","text");
        attachment.put("name", "MyGreatAndroidAppTest");
        attachment.put("href", "http://stackoverflow.com/users/909317/sunny");
        attachment.put("description","Test Test TEst");

        JSONObject media = new JSONObject();
        media.put("type", "image");
        media.put("src",  imageURL);
        media.put("href",imageURL);
        attachment.put("media", new JSONArray().put(media));

        JSONObject properties = new JSONObject();

        JSONObject prop1 = new JSONObject();
        prop1.put("text", "Text or captionText to Post");
        prop1.put("href", imageURL);
        properties.put("text", prop1);

        // u can make any number of prop object and put on "properties" for    ex:    //prop2,prop3

        attachment.put("properties", properties);

        Log.d("FACEBOOK", attachment.toString());

        Bundle params = new Bundle();
        params.putString("attachment", attachment.toString());
        facebook.dialog(CanvasActivity.this, "stream.publish", params, new DialogListener() {

            @Override
            public void onFacebookError(FacebookError e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(DialogError e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onComplete(Bundle values) {
                final String postId = values.getString("post_id");
                if (postId != null) {
                    Log.d("FACEBOOK", "Dialog Success! post_id=" + postId);
                    Toast.makeText(CanvasActivity.this, "Successfully shared on Facebook!", Toast.LENGTH_LONG).show();

                } else {
                    Log.d("FACEBOOK", "No wall post made");
                }

            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub

            }
        });      

    } catch (JSONException e) {
        Log.e("FACEBOOK", e.getLocalizedMessage(), e);
    }
}


/*FACEBOOK share function*/
private boolean shareOnFacebook(List<Bitmap> bitmaps) {
	Log.v("facebook","share activated");
	 Session session = Session.getActiveSession();
	 Log.v("facebook","share activated");
	 if (session.isOpened()) {
		 Log.v("facebook","share activated");
	        OpenGraphAction action = GraphObject.Factory.create(OpenGraphAction.class);
	        //action.setProperty("meal", "https://example.com/cooking-app/meal/Lamb-Vindaloo.html");
	        Log.v("facebook","share activated");
	        action.setType("feed");
	  
	        Log.v("facebook","share activated");
	        FacebookDialog shareDialog = new FacebookDialog.OpenGraphActionDialogBuilder(this, action, "meal")
	        .setImageAttachmentsForAction(bitmaps,true)
	        .build();
	        Log.v("facebook","share activated");
	        uiHelper.trackPendingDialogCall(shareDialog.present());
	     
	  
	        }

return true;
}




public void loginToFacebook() {
    mPrefs = getPreferences(MODE_PRIVATE);
    String access_token = mPrefs.getString("access_token", null);
    long expires = mPrefs.getLong("access_expires", 0);
 
    if (access_token != null) {
        facebook.setAccessToken(access_token);
    }
 
    if (expires != 0) {
        facebook.setAccessExpires(expires);
    }
 
    if (!facebook.isSessionValid()) {
        facebook.authorize(
        		this,
                new String[] { "email", "publish_stream" },
                new DialogListener() {
 
                    @Override
                    public void onCancel() {
                        // Function to handle cancel event
                    }
 
                    @Override
                    public void onComplete(Bundle values) {
                        // Function to handle complete event
                        // Edit Preferences and update facebook acess_token
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("access_token",
                                facebook.getAccessToken());
                        editor.putLong("access_expires",
                                facebook.getAccessExpires());
                        editor.commit();
                    }
 
                    @Override
                    public void onError(DialogError error) {
                        // Function to handle error
 
                    }
 
                    @Override
                    public void onFacebookError(FacebookError fberror) {
                        // Function to handle Facebook errors
 
                    }
 
                });
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