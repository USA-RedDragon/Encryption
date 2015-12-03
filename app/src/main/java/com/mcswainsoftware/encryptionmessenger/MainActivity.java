package com.mcswainsoftware.encryptionmessenger;

import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;
import android.widget.*;

public class MainActivity extends ListActivity {

    private EditText mNewMessage;
    private ImageButton mNewMessageSend;
    private ViewHolderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = ListHelper.buildViewHolderAdapter(this,
                R.layout.list_item);
        setListAdapter(mAdapter);
        
        mNewMessage = (EditText) findViewById(R.id.newmsg);
        mNewMessageSend = (ImageButton) findViewById(R.id.newmsgsend);
        if (mNewMessageSend!=null){
            mNewMessageSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addItem();
                }
            });
        }
    }

    private void addItem() {

        MyObj obj = new MyObj("Gabriele",mNewMessage.getText().toString());
        mAdapter.add(obj);

        mAdapter.notifyDataSetChanged();
    }



}
