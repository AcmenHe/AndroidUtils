package com.acmenhe.mylibrary.utils;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;

/**
 * Author：hepeng
 * Creation time：2020-03-05
 * Email：280922465@qq.com
 * Description：语音朗读
 */
public class ChineseToSpeech {
    private static final String TAG="ChineseToSpeech";
    private static ChineseToSpeech manager;
    private static TextToSpeech textToSpeech;
    private static String speakText="";

    private ChineseToSpeech(Activity activity) {
        try {
            this.textToSpeech = new TextToSpeech(activity.getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = textToSpeech.setLanguage(Locale.CHINA);
                        switch (result) {
                            case TextToSpeech.LANG_MISSING_DATA:
                            case TextToSpeech.LANG_NOT_SUPPORTED:
                                //                        ToastUtils.showShort("不支持朗读功能");
                                Log.e(TAG,"不支持朗读功能");
                                break;
                            case TextToSpeech.LANG_AVAILABLE:
                                //初始化成功之后才可以播放文字
                                //否则会提示“speak failed: not bound to tts engine
                                //TextToSpeech.QUEUE_ADD会将加入队列的待播报文字按顺序播放
                                //TextToSpeech.QUEUE_FLUSH会替换原有文字
                                textToSpeech.speak(speakText, TextToSpeech.QUEUE_ADD,null);
                                break;
                            default:
                                result = textToSpeech.setLanguage(Locale.US);
                                Log.e(TAG, "result:   " + result);
                                Log.e(TAG,"不支持朗读功能");
                                int speak = textToSpeech.speak(speakText, TextToSpeech.QUEUE_ADD, null);
                                Log.e(TAG,"speak="+speak);
                                break;
                        }
                    }else {
                        Log.e(TAG,"初始化失败");
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    //单例
    public static synchronized ChineseToSpeech getInstance(Activity context) {
        if (manager == null) {
            manager = new ChineseToSpeech(context);
        }
        return manager;
    }

    public static void speech(Activity context, String text) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null,"ChineseToSpeech");
//        }else{
//           int result =  textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//           Log.e(TAG,"speech result:"+result);
//        }
        speakText = text;
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        manager = new ChineseToSpeech(context);
    }

    public void destroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
