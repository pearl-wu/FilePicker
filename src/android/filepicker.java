package com.bais.filepicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.orleonsoft.android.simplefilechooser.Constants;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

public class filepicker extends CordovaPlugin {
	
	private static final int REQ_CODE = 0;
    private CallbackContext callbackContext;
    private JSONObject params; 
    private ArrayList<String> exts;
    private static String PTAG = "MultiImageSelector";
    private static final String FTAG = "MFileChooser";

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
       this.callbackContext = callbackContext;
       if (action.equals("get")) {
         cordova.getThreadPool().execute(new Runnable(){
	          public void run() {
	           try {
	        	    params = args.getJSONObject(0);
		            String fileUrl=params.getString("url");
		            Boolean overwrite=params.getBoolean("overwrite");
		            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		            String dirName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
		            downloadUrl(fileUrl, dirName, fileName, overwrite, callbackContext);
	           } catch (JSONException e) {
	        	   e.printStackTrace();
	        	   //Log.e("PhoneGapLog", "Downloader Plugin: Error: " + PluginResult.Status.JSON_EXCEPTION);
	        	   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
	           } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          }
         });
         return true;
        }
       
       if(action.equals("find")){
    	   this.params = args.getJSONObject(0);
    	   String fileUrl = params.getString("url");
    	   fileUrl = fileUrl.replaceAll("\\s*|\t|\r|\n", "");
    	   String dirName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
    	   File filed = new File(dirName+fileUrl);
    	   if(filed.exists()){
    		   //showToast(filed.getAbsolutePath(),"short");
    		   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK,dirName+fileUrl));
    	   }  
    	   return true;
       }
       
       if(action.equals("openweb")){
    	   this.params = args.getJSONObject(0);
    	   String webUrl = params.getString("url");
    	   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
    	   Uri uri = Uri.parse(webUrl);
    	   Intent intent = new Intent(Intent.ACTION_VIEW,uri);
    	   this.cordova.getActivity().startActivity(intent);
    	   return true;
       } 
       
       if(action.equals("openfile")){
    	   this.params = args.getJSONObject(0);
    	   String fileUrl = params.getString("url");
           try{
               openFile(fileUrl);
               callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
               return true;
           }catch(IOException e){
               return false;
           }
       } 
       
       if(action.equals("choosepicture")){
               this.params = args.getJSONObject(0);
               Intent intent = new Intent(cordova.getActivity(), CustomGalleryActivity.class);
               int max = 15;
               String cancelButtonText = "Cancel";
               String okButtonText = "Done";
               String titleText = "Gallery";
               String errorMessageText = "Max limit reached!";
               String type = "multiple";
               int requestCode=0;

               LOG.d(PTAG, "params: " + params);
               if (this.params.has("limit")) {
                   max = this.params.getInt("limit");
               }
               if (this.params.has("cancelButtonText")) {
                   cancelButtonText = this.params.getString("cancelButtonText");
               }
               if (this.params.has("okButtonText")) {
                   okButtonText = this.params.getString("okButtonText");
               }
               if (this.params.has("titleText")) {
                   titleText = this.params.getString("titleText");
               }
               if (this.params.has("errorMessageText")) {
                   errorMessageText = this.params.getString("errorMessageText");
               }
               if (this.params.has("type")) {
                   type = this.params.getString("type");
               }
               intent.putExtra("limit", max);
               intent.putExtra("cancelButtonText", cancelButtonText);
               intent.putExtra("okButtonText", okButtonText);
               intent.putExtra("titleText", titleText);
               intent.putExtra("errorMessageText", errorMessageText);
               if (type.equalsIgnoreCase("multiple")) {
                   intent.putExtra("action", "luminous.ACTION_MULTIPLE_PICK");
                   requestCode=200;
               } else {
                   intent.putExtra("action", "luminous.ACTION_PICK");
                   requestCode=100;
               }
               if (this.cordova != null) {
                   Utility.loadResourceIds(cordova.getActivity());
                   this.cordova.startActivityForResult((CordovaPlugin) this, intent, requestCode);
                  ///cordova.getActivity().startActivityForResult(intent, requestCode);
               }
    	   return true;
       }   
     
       if(action.equals("chooesfiile")){
    	   exts = new ArrayList<String>();
    	   int count = args.length();
    	   
    	   Toast.makeText(cordova.getActivity(), count, Toast.LENGTH_LONG).show();
    	   for(int i = 0;i<count;i++){
           	   exts.add(args.getString(i).toLowerCase());
           }
	        chooseFile(callbackContext,exts);
	        return true;
       }
        return false;
    }
    
public void chooseFile(CallbackContext callbackContext, ArrayList<String> ext) {
   Context context=this.cordova.getActivity().getApplicationContext();
   Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
   intent.setClass(context,CustomfileActivity.class);
   if(ext.size()>0){
     intent.putStringArrayListExtra(Constants.KEY_FILTER_FILES_EXTENSIONS, ext);
   }
   cordova.startActivityForResult(this, intent, 1);
   PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
   pluginResult.setKeepCallback(true);
   callbackContext.sendPluginResult(pluginResult);
}

   
private Boolean downloadUrl(String fileUrl, String dirName, String fileName, Boolean overwrite, CallbackContext callbackContext)
	throws InterruptedException, JSONException {
		  try {
			   File dir = new File(dirName);
			   if (!dir.exists()) {
				   dir.mkdirs();
			   }
			   File file = new File(dirName, fileName);
			   if (overwrite == true || !file.exists()) {
				    Intent intent = new Intent ();
				    intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
				    PendingIntent pend = PendingIntent.getActivity(cordova.getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				    NotificationManager mNotifyManager = (NotificationManager) cordova.getActivity().getSystemService(Activity.NOTIFICATION_SERVICE);
				    Notification.Builder mBuilder =
				         new Notification.Builder(cordova.getActivity())
				         //.setContentTitle(cordova.getActivity().getString(R.string.app_name))
				         .setContentText("File: " + fileName + " - 0%");
				    int mNotificationId = new Random().nextInt(10000);
				    URL url = new URL(fileUrl);
				    HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
				    ucon.setRequestMethod("GET");
				    ucon.connect();
				    InputStream is = ucon.getInputStream();
				    byte[] buffer = new byte[1024];
				    int readed = 0, progress = 0, totalReaded = 0, fileSize = ucon.getContentLength();
				    FileOutputStream fos = new FileOutputStream(file);
				    showToast("Download started.","short");
				    int step = 0;
				    while ((readed = is.read(buffer)) > 0) {
				     fos.write(buffer, 0, readed);
				     totalReaded += readed;
				     int newProgress = (int) (totalReaded*100/fileSize);
				     if (newProgress != progress & newProgress > step) {
				      mBuilder.setProgress(100, newProgress, false);
				      mBuilder.setContentText("File: " + fileName + " - " + step + "%");
				      mBuilder.setContentIntent(pend);
				      mNotifyManager.notify(mNotificationId, mBuilder.build());
				      step = step + 1;
				     }
			    }
			    fos.flush();
			    fos.close();
			    is.close();
			    ucon.disconnect();
			    mBuilder.setContentText("Download of \"" + fileName + "\" complete").setProgress(0,0,false);
			             mNotifyManager.notify(mNotificationId, mBuilder.build());
			             try {
			                    Thread.sleep(1000);
			                } catch (InterruptedException e) {
			                 Log.d("PhoneGapLog", "Downloader Plugin: Thread sleep error: " + e);
			                }
			             mNotifyManager.cancel(mNotificationId);
			    showToast("Download finished.","short");
			   } else if (overwrite == false) {
			    showToast("File is already downloaded.","short");
			   }
			   if(!file.exists()) {
			    showToast("Download went wrong, please try again or contact the developer.","long");
			    Log.e("PhoneGapLog", "Downloader Plugin: Error: Download went wrong.");
			   }
			   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
			   return true;
		  } catch (FileNotFoundException e) {
			   showToast("File does not exists or cannot connect to webserver, please try again or contact the developer.","long");
			   Log.e("PhoneGapLog", "Downloader Plugin: Error: " + PluginResult.Status.ERROR);
			   e.printStackTrace();
			   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
			   return false;
		  } catch (IOException e) {
			   showToast("Error downloading file, please try again or contact the developer.","long");
			   Log.e("PhoneGapLog", "Downloader Plugin: Error: " + PluginResult.Status.ERROR);
			   e.printStackTrace();
			   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
			   return false;
		  }

       /*try {
    	   URL url = new URL(fileUrl);
    	   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    	   InputStream inputStream = conn.getInputStream();
    	   byte[] getData = readInputStream(inputStream);
    	   
    	   File saveDir = new File(dirName);  
           if (!saveDir.exists()) {  
               saveDir.mkdir();  
           }  
           File file = new File(saveDir + File.separator + fileName);  
           FileOutputStream fos = new FileOutputStream(file);  
           fos.write(getData);  
           if (fos != null) {  
               fos.close();  
           }  
           if (inputStream != null) {  
               inputStream.close();  
           } 
           Toast.makeText(cordova.getActivity(), "info:" + url + " download success", Toast.LENGTH_LONG).show();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  */

}
   
public static byte[] readInputStream(InputStream inputStream) throws IOException {  
    byte[] buffer = new byte[1024];  
    int len = 0;  
    ByteArrayOutputStream bos = new ByteArrayOutputStream();  
       while ((len = inputStream.read(buffer)) != -1) {  
           bos.write(buffer, 0, len);  
       }  
    bos.close();  
    return bos.toByteArray();  
}     
   
   
@SuppressWarnings("unused")
private void openFile(String url) throws IOException {
       // Create URI
       Uri uri = Uri.parse(url);
       Intent intent = null;

       if (url.contains(".doc") || url.contains(".docx")) {
           // Word document
           intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "application/msword");
       } else if(url.contains(".pdf")) {
           // PDF file
           intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "application/pdf");
       } else if(url.contains(".ppt") || url.contains(".pptx")) {
           // Powerpoint file
           intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
       } else if(url.contains(".xls") || url.contains(".xlsx")) {
           // Excel file
           intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "application/vnd.ms-excel");
       } else if(url.contains(".rtf")) {
           // RTF file
           intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "application/rtf");
       } else if(url.contains(".wav")) {
           // WAV audio file
           intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "audio/x-wav");
       } else if(url.contains(".gif")) {
           // GIF file
           intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "image/gif");
       } else if(url.contains(".jpg") || url.contains(".jpeg")) {
           // JPG file
           intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "image/jpeg");
       } else if(url.contains(".png")) {
           // PNG file
           intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "image/png");
       } else if(url.contains(".txt")) {
           // Text file
           intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "text/plain");
       } else if(url.contains(".mpg") || url.contains(".mpeg") || url.contains(".mpe") || url.contains(".mp4") || url.contains(".avi")) {
           // Video files
           intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "video/*");
       }
       else {            
       	intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "*/*");
       }
       this.cordova.getActivity().startActivity(intent);
   } 

public void onActivityResult(int requestCode, int resultCode, Intent data) {
   try {
        if (resultCode == Activity.RESULT_CANCELED) {
            JSONObject res = new JSONObject();
            res.put("cancelled", true);
            //showToast("Activity.RESULT_CANCELED","");
            this.callbackContext.success(res);
        } else if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            String single_path = data.getStringExtra("single_path");
            JSONObject res = new JSONObject();
            res.put("cancelled", false);
            res.put("path", single_path);
            //showToast("Activity.RESULT_OK","");
            this.callbackContext.success(res);
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] fileNames = data.getStringArrayExtra("all_path");
            JSONArray p = new JSONArray();
            for(int i = 0;i < fileNames.length; i++) {
                p.put(fileNames[i]);
            }
            JSONObject res = new JSONObject();
            res.put("cancelled", false);
            res.put("paths", p);
            //showToast("Activity.RESULT_OK","");
            this.callbackContext.success(res);
        } else {
        	this.callbackContext.error("run error"+resultCode);
        }
    } catch (JSONException e) {}
}
   
 
 private void showToast(final String message, final String duration) {
  cordova.getActivity().runOnUiThread(new Runnable() {
   public void run() {
    Toast toast;
    if(duration.equals("long")) {
     toast = Toast.makeText(cordova.getActivity(), message, Toast.LENGTH_LONG);
    } else {
     toast = Toast.makeText(cordova.getActivity(), message, Toast.LENGTH_SHORT);
    }
    toast.show();
   }
  });
 }  
 
}
