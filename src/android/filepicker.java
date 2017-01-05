package com.bais.filepicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.Manifest;
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
    	   String fileUrl = params.getString("url");
    	   PluginResult.Status status = PluginResult.Status.OK;
           try{
               openFile(fileUrl);
               callbackContext.sendPluginResult(new PluginResult(status));
               return true;
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
   
   private Boolean downloadUrl(String fileUrl, String dirName, String fileName, Boolean overwrite, CallbackContext callbackContext){

       try {
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
	}  

	 return false ;
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
