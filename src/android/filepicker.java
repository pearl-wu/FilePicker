package com.bais.filepicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class filepicker extends CordovaPlugin {
   //public static final int PERMISSION_DENIED_ERROR = 20;
    public static final String WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String READ = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final int REQ_CODE = 0;
    protected JSONArray args;
    protected CallbackContext callbackContext;

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
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
       
       if (action.equals("get")) {
         cordova.getThreadPool().execute(new Runnable() {
	          public void run() {
	           try {
		            JSONObject params = args.getJSONObject(0);
		            String fileUrl=params.getString("url");
		            Boolean overwrite=params.getBoolean("overwrite");
		            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		            String dirName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
		            downloadUrl(fileUrl, dirName, fileName, overwrite, callbackContext);
	           } catch (JSONException e) {
	        	   e.printStackTrace();
	        	   //Log.e("PhoneGapLog", "Downloader Plugin: Error: " + PluginResult.Status.JSON_EXCEPTION);
	        	   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
	           } catch (InterruptedException e) {
	        	   e.printStackTrace();
	        	   //Log.e("PhoneGapLog", "Downloader Plugin: Error: " + PluginResult.Status.ERROR);
	        	   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
	           }
	          }
         });
         return true;
        }
       
       if(action.equals("find")){
    	   JSONObject params = args.getJSONObject(0);
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
    	   JSONObject params = args.getJSONObject(0);
    	   String webUrl = params.getString("url");
    	   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
    	   Uri uri = Uri.parse(webUrl);
    	   Intent intent = new Intent(Intent.ACTION_VIEW,uri);
    	   this.cordova.getActivity().startActivity(intent);
    	  return true;
       } 
       
       if(action.equals("openfile")){
    	   JSONObject params = args.getJSONObject(0);
    	   String webUrl = params.getString("url");
    	   
    	   PluginResult.Status status = PluginResult.Status.OK;
           String result = "";

           try{
               if(action.equals("openFile")) {
                   openFile(args.getString(0));
               }else{
                   status = PluginResult.Status.INVALID_ACTION;
               }
               return true;
           }catch(JSONException e){
               return false;
           }catch(IOException e){
               return false;
           }
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
			    Notification.Builder mBuilder = new Notification.Builder(cordova.getActivity())
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
			    //showToast("開始下載...","short");
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
					      showToast("開始下載... " + fileName + " - " + step + "%","short");
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
			                 //Log.d("PhoneGapLog", "Downloader Plugin: Thread sleep error: " + e);
			             }
			    mNotifyManager.cancel(mNotificationId);
			    showToast("下載完成,檔案路徑:/Download/"+fileName,"short");
		   } else if (overwrite == false) {
			   showToast("File is already downloaded.","short");
		   }
		   if(!file.exists()) {
			   showToast("Download went wrong, please try again or contact the developer.","long");
			   //Log.e("PhoneGapLog", "Downloader Plugin: Error: Download went wrong.");
		   }
		   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
		   return true;
	  } catch (FileNotFoundException e) {
		   showToast("File does not exists or cannot connect to webserver, please try again or contact the developer.","long");
		   //Log.e("PhoneGapLog", "Downloader Plugin: Error: " + PluginResult.Status.ERROR);
		   e.printStackTrace();
		   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
		   return false;
	  } catch (IOException e) {
		   showToast("Error downloading file, please try again or contact the developer.","long");
		   //Log.e("PhoneGapLog", "Downloader Plugin: Error: " + PluginResult.Status.ERROR);
		   e.printStackTrace();
		   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
		   return false;
	  }
 }
   
   @SuppressWarnings("unused")
private void openFile(String url) throws IOException {
       // Create URI
       Uri uri = Uri.parse(url);
       Intent intent = null;
       // Check what kind of file you are trying to open, by comparing the url with extensions.
       // When the if condition is matched, plugin sets the correct intent (mime) type,
       // so Android knew what application to use to open the file

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

       //if you want you can also define the intent type for any other file

       //additionally use else clause below, to manage other unknown extensions
       //in this case, Android will show all applications installed on the device
       //so you can choose which application to use


       else {            
       	intent = new Intent(Intent.ACTION_VIEW);
           intent.setDataAndType(uri, "*/*");
       }

       this.cordova.getActivity().startActivity(intent);
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
