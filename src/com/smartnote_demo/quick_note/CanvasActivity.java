package com.smartnote_demo.quick_note;

import org.apache.commons.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import com.example.smartnote_demo.R;
import com.example.smartnote_demo.R.layout;
import com.samsung.samm.common.SOptionSAMM;
import com.samsung.samm.common.SOptionSCanvas;
import com.samsung.spenemulatorlibrary.ActivityWithSPenLayer;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;
import com.samsung.spensdk.applistener.HistoryUpdateListener;
import com.samsung.spensdk.applistener.SCanvasInitializeListener;
import com.smartnote_demo.quick_note.QuickNoteActivity.PlanetFragment;
import com.smartnote_demo.spen_tools.SPenSDKUtils;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CanvasActivity extends ActivityWithSPenLayer {

	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mPlanetTitles;
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
	private File mFolder = null;
	private float mZoomValue = 1f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_basic_ui);
		
		mContext = this;
		
		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        
        mDrawerLayout.setDrawerShadow(R.drawable.notepad, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
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
        // update the main content by replacing fragments
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
      //  setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
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
		.setMessage("Exit this program")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {				
				// finish dialog
				dialog.dismiss();
				setResult(RESULT_OK, getIntent());
				finish();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

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

			if(
			saveSAMMFile()==false)
				Log.e("smart","failed to save samm file");
		
			//temporary zoom testing			
			//thats works! but we want probably canvas to fill the whole place
		//	mSCanvas.setCanvasZoomScale(mZoomValue += 0.2, false);
		//	mSCanvas.setCanvasZoomScale(mZoomValue, true);			
		}
	}
};





private boolean saveSAMMFile() 
{    
	//we get the whole bitmap - not only the foreground (arg0 ==false)
	Bitmap bmCanvas = mSCanvas.getCanvasBitmap(false);
	  return saveImageToInternalStorage(bmCanvas);

	
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
	if(bmForeground==null) Log.e("smart","no bitmap");
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

public boolean saveImageToInternalStorage(Bitmap image) {

    try {
    // Use the compress method on the Bitmap object to write image to
    // the OutputStream
    FileOutputStream fos = openFileOutput("desiredFilename.png", Context.MODE_PRIVATE);

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
}
