package com.acmenhe.mylibrary.utils;

import android.app.Activity;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-05-28.
 */

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
		try {
			if(activities!=null) {
				activities.add(activity);
			}
		} catch (Exception e) {
			Log.e("addActivity",e.getMessage());
		}
	}

    public static void removeActivity(Activity activity) {
		try {
			if(activities!=null) {
				activities.remove(activity);
			}
		} catch (Exception e) {
			Log.e("removeActivity",e.getMessage());
		}
	}

    public static void finishAll() {
		try {
			if(activities!=null) {
				for (Activity activity : activities) {
					if (!activity.isFinishing()) {
						activity.finish();
					}
				}
			}
		} catch (Exception e) {
			Log.e("finishAll",e.getMessage());
		}
	}
}
