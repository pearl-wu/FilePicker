package com.bais.filepicker;

import android.content.Context;
import android.content.res.Resources;

public final class Customfiletage {
public static int resId_galleryLayout = -1;
	public static int XLS = -1;
	public static int XLSX = -1;
	public static int DOC = -1;
	public static int DOCX = -1;
	public static int PDF = -1;
	public static int APK = -1;
	public static int JPG = -1;
	public static int JPEG = -1;
	public static int GIF = -1;
	public static int PNG = -1;
	public static int RAR = -1;
	public static int TXT = -1;
	public static int PPT = -1;
	public static int PPTX = -1;
	public static int ACC = -1;
	public static int MP3 = -1;
	public static int AVI = -1;
	public static int MP4 = -1;
	public static int RTF = -1;
	public static int ZIP = -1;	
	public static int ODT = -1;
	public static int ODS = -1;
	public static int ODP = -1;

	public static int
		
	public static void loadResourceIds(Context context) {
	    String packageName = context.getPackageName();
	    Resources resources = context.getResources();
	    XLS = resources.getIdentifier("xls", "drawable", packageName);
	    XLSX = resources.getIdentifier("xlsx", "drawable", packageName);
	    DOC = resources.getIdentifier("doc", "drawable", packageName);
	    DOCX = resources.getIdentifier("docx", "drawable", packageName);
	    PDF = resources.getIdentifier("pdf", "drawable", packageName);
	    APK = resources.getIdentifier("apk", "drawable", packageName);
	    JPG = resources.getIdentifier("jpg", "drawable", packageName);
	    JPEG= resources.getIdentifier("jpeg", "drawable", packageName);
	    GIF = resources.getIdentifier("gif", "drawable", packageName);
	    PNG = resources.getIdentifier("png", "drawable", packageName);
	    RAR = resources.getIdentifier("rar", "drawable", packageName);
	    TXT = resources.getIdentifier("txt", "drawable", packageName);
	    PPT = resources.getIdentifier("ppt", "drawable", packageName);
	    PPTX = resources.getIdentifier("pptx", "drawable", packageName);
	    ACC = resources.getIdentifier("acc", "drawable", packageName);
	    MP3 = resources.getIdentifier("mp3", "drawable", packageName);
	    AVI = resources.getIdentifier("avi", "drawable", packageName);
	    MP4 = resources.getIdentifier("mp4", "drawable", packageName);
	    RTF = resources.getIdentifier("rtf", "drawable", packageName);
	    ZIP = resources.getIdentifier("zip", "drawable", packageName);
	    ODT = resources.getIdentifier("odt", "drawable", packageName);
	    ODS = resources.getIdentifier("ods", "drawable", packageName);
	    ODP = resources.getIdentifier("odp", "drawable", packageName);
		//resId_btnGalleryPickMul = resources.getIdentifier("btnGalleryPickMul", "layout", packageName);
		//resId_btnGalleryPickMul = resources.getIdentifier("btnGalleryPickMul", "drawable", packageName);
		//resId_btnGalleryPickMul = resources.getIdentifier("btnGalleryPickMul", "drawable", packageName);
		//resId_btnGalleryPickMul = resources.getIdentifier("btnGalleryPickMul", "drawable", packageName);
	}
	
}
