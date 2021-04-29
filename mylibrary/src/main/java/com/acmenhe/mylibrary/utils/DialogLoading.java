package com.acmenhe.mylibrary.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.acmenhe.mylibrary.R;


/**
 * Created by Administrator on 2016-08-18.
 */
public class DialogLoading {
    private TextView loadingLabel;
    private AlertDialog alertDialog;
    private Context mContext;

    public DialogLoading(Context context) {
        if(context!=null) {
            this.mContext = context;
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading_layout, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            loadingLabel = (TextView) view.findViewById(R.id.loading_text);
            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
        }
    }

    public void show(){
        if (alertDialog!=null){
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //控制宽度
            WindowManager manager=(WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display defaultDisplay = manager.getDefaultDisplay();
			Point point = new Point();
			defaultDisplay.getSize(point);
			int width = point.x;
            WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
            attributes.width=(int) (width * 0.5);
            alertDialog.getWindow().setAttributes(attributes);
        }
    }

    public void dismiss(){
        if (alertDialog!=null){
            alertDialog.dismiss();
        }
    }

    public void setDialogLabel(String label) {
        loadingLabel.setText(label);
    }
}
