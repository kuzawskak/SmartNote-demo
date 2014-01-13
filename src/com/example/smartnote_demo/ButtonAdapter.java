package com.example.smartnote_demo;

import com.smartnote_demo.notepad.*;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
 
    NotepadItem[] items;
 
    
    
    public void AddItem(NotepadItem item){
    	items[getCount()]=item;
    }
    
    // Constructor
    public ButtonAdapter(Context c,NotepadItem[] notepad_items){
        mContext = c;
        items = notepad_items;
    }
 
    @Override
    public int getCount() {
        return items.length;
    }
 
    @Override
    public Object getItem(int position) {
        return items[position];
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;

        return imageView;
    }
 
}