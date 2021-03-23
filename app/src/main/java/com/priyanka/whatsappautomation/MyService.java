package com.priyanka.whatsappautomation;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class MyService extends AccessibilityService {

    String [] convs = new String[]{"hey",";P",":)"};

    static String targetName="default";

    static final String TAG="MyService";
    static final String nameRefId="com.whatsapp:id/conversation_contact_name";
    static final String sendButtonRefId="com.whatsapp:id/send";
    static final String chatBoxRefId="com.whatsapp:id/entry";
    static int convIndex = 0;
    static int takeAction = 0;
    final int chatTimeLapseInMs=5000;
    final int stdTimeLApse = 600;

    public MyService() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event==null){
            return;
        }
        AccessibilityNodeInfo rootNode = event.getSource();
        if (rootNode==null){
            return;
        }

        try {
            String name = getName(rootNode);
            if (name == null){
                return;
            }
            if (!name.equalsIgnoreCase(targetName)){
                Log.e(TAG, "onAccessibilityEvent: no target name" );
                return;
            }else {
                AccessibilityNodeInfo textBox = getNode(rootNode, chatBoxRefId);
                Bundle arguments = new Bundle();
                if (convIndex == convs.length -1) {
                    arguments.putString(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "<3");

                } else {
                    arguments.putString(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, convs[convIndex % (convs.length - 1)]);
                    convIndex ++;
                }
                textBox.performAction(AccessibilityNodeInfoCompat.ACTION_SET_TEXT, arguments);
                AccessibilityNodeInfo sendButton = getNode(rootNode, sendButtonRefId);
                sendButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Thread.sleep(2500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private AccessibilityNodeInfo getNode(AccessibilityNodeInfo rootNode, String refId) {
        AccessibilityNodeInfo textBoxNode = null;
        List<AccessibilityNodeInfo> urlNodeInfo = rootNode.findAccessibilityNodeInfosByViewId(refId);
        if (urlNodeInfo != null && !urlNodeInfo.isEmpty()){
            textBoxNode =urlNodeInfo.get(0);
            return textBoxNode;
        }
        return textBoxNode;
    }

    private String getName(AccessibilityNodeInfo rootNode) {
        List<AccessibilityNodeInfo> urlNodeInfo = rootNode.findAccessibilityNodeInfosByViewId(nameRefId);

        if (urlNodeInfo != null && !urlNodeInfo.isEmpty()){
            AccessibilityNodeInfo  urlNode = urlNodeInfo.get(0);
            CharSequence charArray = urlNode.getText();
            if (charArray != null && charArray.length()>0){
                Log.e(TAG, "getName: "+charArray.toString());
                return charArray.toString();
            }
        }
        Log.e(TAG, "getName: Name not found");
        return null;
    }

    @Override
    public void onInterrupt() {

    }

}