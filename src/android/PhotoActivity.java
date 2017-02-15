package com.bais.filepicker;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.net.Uri;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class PhotoActivity extends Activity {
	
	private PhotoViewAttacher mAttacher;
	private ImageView photo;
	private int warning;
	private int place;
	private int wid;
	private int heg;
	private String imageUrl;
	private ImageButton closeBtn;
	private ImageButton shareBtn;
	private ProgressBar progress;
	private JSONObject options;
	private int shareBtnVisibility;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		setContentView(getApplication().getResources().getIdentifier("activity_photo", "layout", getApplication().getPackageName()));
		// Load the Views
		findViews();

		try {
			options = new JSONObject(this.getIntent().getStringExtra("options"));
			shareBtnVisibility = options.getBoolean("share") ? View.VISIBLE : View.INVISIBLE;
		} catch(JSONException exception) {
			shareBtnVisibility = View.VISIBLE;
		}
		shareBtn.setVisibility(shareBtnVisibility);

		// Change the Activity Title
		final String actTitle = this.getIntent().getStringExtra("title");
		imageUrl = this.getIntent().getStringExtra("url");
		
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap bitmap = BitmapFactory.decodeStream(input);
	        //Toast.makeText(getApplicationContext(), bitmap.getWidth()+"*"+bitmap.getHeight(), Toast.LENGTH_SHORT).show();	
	        wid = bitmap.getWidth();
	        heg = bitmap.getHeight();
	        
	       /* if(bitmap.getWidth()<bitmap.getHeight()){
	        	image_parames = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	        }else{
	        	image_parames = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	        }	        
	        photo.setLayoutParams(image_parames);*/
	        
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Set Button Listeners
		closeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		shareBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HttpDownloader httpDownloader = new HttpDownloader();
				try {
					httpDownloader.downFile(imageUrl, "Download/", actTitle);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});

		try {		
			loadImage();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Find and Connect Views
	 *
	 */
	private void findViews() {
		// Buttons first
		closeBtn = (ImageButton) findViewById( getApplication().getResources().getIdentifier("closeBtn", "id", getApplication().getPackageName()) );
		shareBtn = (ImageButton) findViewById( getApplication().getResources().getIdentifier("shareBtn", "id", getApplication().getPackageName()) );
		// Photo Container
		photo = (ImageView) findViewById( getApplication().getResources().getIdentifier("photoView", "id", getApplication().getPackageName()) );
		warning = this.getResources().getIdentifier("warning", "drawable",  getApplication().getPackageName());
		place = this.getResources().getIdentifier("no_media", "drawable",  getApplication().getPackageName());
		mAttacher = new PhotoViewAttacher(photo);
		// Title TextView
		//titleTxt = (TextView) findViewById( getApplication().getResources().getIdentifier("titleTxt", "id", getApplication().getPackageName()) );
		progress = (ProgressBar) findViewById( getApplication().getResources().getIdentifier("progressBar", "id", getApplication().getPackageName()));
	}


	private void hideLoadingAndUpdate() {
		photo.setVisibility(View.VISIBLE);
        shareBtn.setVisibility(shareBtnVisibility);
		mAttacher.update();
	}

	@SuppressLint("InlinedApi") private void loadImage() throws MalformedURLException{
		//Toast.makeText(getApplicationContext(), imageUrl, Toast.LENGTH_SHORT).show();	
		
		if( imageUrl.startsWith("http") ) {

			Picasso.with(this)
				.load(StringProcessCode.toBrowserCode(imageUrl))
				.resize(1024, 0)
				.onlyScaleDown()
				.into(photo, new com.squareup.picasso.Callback() {
					@Override
					public void onSuccess() {
						hideLoadingAndUpdate();
					}

					@Override
					public void onError() {
						Toast.makeText(getApplicationContext(), "Error loading image.", Toast.LENGTH_LONG).show();
						finish();
					}
			});
			
			progress.setVisibility(View.INVISIBLE);
			
		} else if ( imageUrl.startsWith("data:image")){
	            String base64String = imageUrl.substring(imageUrl.indexOf(",")+1);
	            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
	            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
	            photo.setImageBitmap(decodedByte);
	            hideLoadingAndUpdate();
	    } else {
	            photo.setImageURI(Uri.parse(imageUrl));
	            hideLoadingAndUpdate();
	    }
	}	

	public class HttpDownloader {
		private URL url = null;

		
	     public void downFile(final String urlStr, final String path, final String fileName) throws Exception {
	    	 
	         try {
	             final FileUtils fileUtils = new FileUtils();
	             if (fileUtils.isFileExist(path + fileName)) {
	            	 Toastshow(1);
	             } else {
	            	 
	            	 AsyncHttpClient client = new AsyncHttpClient();
	            	 RequestParams params = new RequestParams();
	            	 client.get(urlStr, params, new AsyncHttpResponseHandler(){

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							Toastshow(-1);
						}

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							InputStream inputStream = null;
							progress.setProgress(0); 
							Log.i("binaryData:", "下載..........：" + arg2.length);  
							
							try {
								inputStream = getInputStreamFromUrl(urlStr);
								
								ByteArrayOutputStream bao = new ByteArrayOutputStream();
								String text;
								int c = 0;
								byte[] buffer = new byte[8 * 1024];
								while ((c = inputStream.read(buffer)) != -1) {
									bao.write(buffer, 0, c);
								}
								inputStream.close();
								bao.close();								
								File resultFile = fileUtils.write2SDFromInput(path,fileName, new ByteArrayInputStream(bao.toByteArray()));
				                 if (resultFile == null) {
				                	 Toastshow(-1);
				                     return ;
				                 }				                 
				                 Toastshow(0);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally {
					             try {
					                 inputStream.close();
					             } catch (Exception e) {
					                 e.printStackTrace();
					             }
					         }
						}						
									 @Override
									 public void onStart() {
						                 super.onStart();
						                 System.out.println("--------onStart------");
						                 //Toast.makeText(getApplicationContext(), "--------onStart------", Toast.LENGTH_SHORT).show();
						             }
						 
						             @Override
						             public void onFinish() {
						                 super.onFinish();
						                 System.out.println("--------onFinish------");
						                 //Toast.makeText(getApplicationContext(), "--------onFinish------", Toast.LENGTH_SHORT).show();
						             }
						 
						             @Override
						             public void onRetry(int retryNo) {
						                 super.onRetry(retryNo);
						                 System.out.println("--------onRetry------");
						                 //Toast.makeText(getApplicationContext(), "--------onRetry------", Toast.LENGTH_SHORT).show();
						             }
						 
						             @Override
						             public void onCancel() {
						                 super.onCancel();
						                 System.out.println("--------onCancel------");
						                 //Toast.makeText(getApplicationContext(), "--------onCancel------", Toast.LENGTH_SHORT).show();
						             }
						 
						             public void onProgress(int bytesWritten, int totalSize) {
						                 super.onProgress(bytesWritten, totalSize);
						                 System.out.println("--------onProgress------");
				            	            super.onProgress(bytesWritten, totalSize);  
				            	            int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);  
				            	            // 進度顯示
				            	            progress.setProgress(count);  
				            	            Log.i("下載 Progress>>>>>", bytesWritten + " / " + totalSize); 
						             }
	            		 
	            	 });
	             }
	         } catch (Exception e) {
	             e.printStackTrace();
	             Toastshow(-1);
	         } 
	     }
	     
	     public InputStream getInputStreamFromUrl(String urlStr) throws MalformedURLException, IOException{
	    	Toast.makeText(getApplicationContext(), "下載中...", Toast.LENGTH_SHORT).show();
	    	urlStr = StringProcessCode.ecodeUrlWithUTf8(urlStr);
		    // 建立一個URL物件 
		   	url = new URL(urlStr.trim());
		   	// 建立一個Http連接
		   	HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		   	urlConn.setRequestMethod("POST");
		   	urlConn.setConnectTimeout(6*1000);
		   	urlConn.setReadTimeout(6*1000);
		   	//得到輸入流
		   	InputStream inputStream = urlConn.getInputStream();
		   	return inputStream;
	     }
	     
	     
	     
	     public void Toastshow(int mt){
	    	 String st = null;
	    	 if(mt==-1){
	    		 st = "儲存錯誤";
			 }else if(mt==0){
				 st = "儲存成功";
			 }else if(mt==1){
				 st = "已儲存";
			 }
	    	 Toast.makeText(getApplicationContext(), st, Toast.LENGTH_SHORT).show();
	     }
	}     
}
