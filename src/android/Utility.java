package com.bais.filepicker;

import android.content.Context;
import android.content.res.Resources;

public final class Utility {
    public static int resId_galleryLayout = -1;
    public static int resId_gridGallery = -1;
    public static int resId_bottomContainer = -1;
    public static int resId_imgNoMedia = -1;
    public static int resId_btnGalleryOk = -1;
    public static int resId_btnGalleryCancel = -1;
    public static int resId_tvTitleText = -1;
    public static int resId_gallery_itemLayout = -1;
    public static int resId_imgQueue = -1;
    public static int resId_imgQueueMultiSelected = -1;
    public static int resId_no_media = -1;
    public static int resId_mainLayout = -1;
    public static int resId_viewSwitcher = -1;
    public static int resId_imgSinglePick = -1;
    public static int resId_btnGalleryPick = -1;
    public static int resId_btnGalleryPickMul = -1;
    
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
	public static int main = -1;
	public static int main_item = -1;
	public static int lists = -1;
	public static int icong = -1;
	public static int sline = -1;
	public static int fline = -1;

	public static void loadResourceIds(Context context) {
		String packageName = context.getPackageName();
		Resources resources = context.getResources();
	    resId_galleryLayout = resources.getIdentifier("gallery", "layout", packageName);
	    resId_gridGallery = resources.getIdentifier("gridGallery", "id", packageName);
	    resId_bottomContainer = resources.getIdentifier("llBottomContainer", "id", packageName);
	    resId_imgNoMedia = resources.getIdentifier("imgNoMedia", "id", packageName);
	    resId_btnGalleryOk = resources.getIdentifier("btnGalleryOk", "id", packageName);
	    resId_btnGalleryCancel = resources.getIdentifier("btnGalleryCancel", "id", packageName);
	    resId_tvTitleText = resources.getIdentifier("tvTitleText", "id", packageName);
	    resId_gallery_itemLayout = resources.getIdentifier("gallery_item", "layout", packageName);
	    resId_imgQueue = resources.getIdentifier("imgQueue", "id", packageName);
	    resId_imgQueueMultiSelected = resources.getIdentifier("imgQueueMultiSelected", "id", packageName);
	    resId_no_media = resources.getIdentifier("no_media", "drawable", packageName);
	    resId_mainLayout = resources.getIdentifier("main", "layout", packageName);
	    resId_viewSwitcher = resources.getIdentifier("viewSwitcher", "id", packageName);
	    resId_imgSinglePick = resources.getIdentifier("imgSinglePick", "id", packageName);
	    resId_btnGalleryPick = resources.getIdentifier("btnGalleryPick", "id", packageName);
	    resId_btnGalleryPickMul = resources.getIdentifier("btnGalleryPickMul", "id", packageName);
	    
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
	    
	    main = resources.getIdentifier("multiselectorfile", "layout", packageName);
	    main_item = resources.getIdentifier("multiselectorfile_item", "layout", packageName);
	    lists = resources.getIdentifier("list", "id", packageName);
	    icong = resources.getIdentifier("icon", "id", packageName);
	    sline = resources.getIdentifier("secondLine", "id", packageName);
	    fline = resources.getIdentifier("firstLine", "id", packageName);
	}
}
