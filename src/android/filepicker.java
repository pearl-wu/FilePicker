package com.bais.filepicker;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

public class filepicker extends CordovaPlugin {

   //public static final int PERMISSION_DENIED_ERROR = 20;

    public static final String WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String READ = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final int REQ_CODE = 0;
    protected JSONArray args;
    protected CallbackContext callbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("start")) {
            this.args = args;
            this.callbackContext = callbackContext;

            if (cordova.hasPermission(READ) && cordova.hasPermission(WRITE)) {
                this.launchActivity();
            } else {
                this.getPermission();
            }
            return true;
        }
        return false;
    }

    protected void getPermission() {
        cordova.requestPermissions(this, REQ_CODE, new String[]{WRITE, READ});
    }

    protected void launchActivity() throws JSONException {
       // Intent i = new Intent(this.cordova.getActivity(), com.sarriaroman.PhotoViewer.PhotoActivity.class);
       
        //i.putExtra("options", this.args.optJSONObject(0).toString());

       // this.cordova.getActivity().startActivity(i);
        this.callbackContext.success("");
    }
}
