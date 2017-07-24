package com.shenxy.smartapploader.utils;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.shenxy.smartapploader.AppConstants;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils2 {
    static Dialog dialog;
    private static Bitmap b;
    private static byte[] bt;


    /**
     * 要更加准确的匹配手机号码只匹配11位数字是不够的，比如说就没有以144开始的号码段，
     * <p/>
     * 　　 * 故先要整清楚现在已经开放了多少个号码段，国家号码段分配如下：
     * <p/>
     * 　　 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * <p/>
     * 　　 * 联通：130、131、132、152、155、156、185、186
     * <p/>
     * 　　 * 电信：133、153、180、189、（1349卫通）
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        if (mobiles != null && !"".equals(mobiles.trim())){
//        if (MStrOperate.hasValue(mobiles)) {
            Pattern p = Pattern.compile(AppConstants.REGEX_MOBILE_FULL);//"^(1)\\d{10}$");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        }
        return false;
    }

    /**
     * 验证字符串是否是email
     *
     * @param str
     * @return
     */

    public static boolean isEmail(String str) {
        Pattern pattern = Pattern.compile(AppConstants.REGEX_EMAIL, Pattern.CASE_INSENSITIVE);//"^([a-zA-Z0-9_\\-\\.]+)@(([a-zA-Z0-9_\\-]+\\.)+)([a-zA-Z]{2,4})(\\]?)$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(str);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
/*
    *//**
     * 创建进度条对话框
     *
     * @param context
     * @param resId    文本Id
     * @param isCancel 是否能取消
     * @return
     *//*
    public static Dialog createProgressDialog(Context context, int resId, boolean isCancel) {
        Dialog dialog = new Dialog(context, R.style.TransparentDialog);
        dialog.setContentView(R.layout.progressing);
        TextView tvContentText = (TextView) dialog.findViewById(R.id.tv_progress_text);
        if (resId != R.string.loading) {
            tvContentText.setText(resId);
            tvContentText.setVisibility(View.VISIBLE);
        }
        else {
            tvContentText.setVisibility(View.GONE);
        }
        dialog.setCancelable(isCancel);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();

        wl.gravity = Gravity.CENTER;
//        wl.flags = wl.flags;
        window.setAttributes(wl);

        return dialog;
    }

    public static Dialog createProgressbarAndTipsProgressDialog(Context context, int resId, boolean isCancel) {
        Dialog dialog = new Dialog(context, R.style.TransparentDialog);
        dialog.setContentView(R.layout.progressing_bar_and_tips);
        TextView tvContentText = (TextView) dialog.findViewById(R.id.tv_progress_text);
        tvContentText.setText(resId);
        dialog.setCancelable(isCancel);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();

        wl.gravity = Gravity.CENTER;
//        wl.flags = wl.flags;
        window.setAttributes(wl);

        return dialog;
    }
//*/


    /**
     * 获取网络文件 URL 中的文件名称
     *
     * @param urlpath
     * @return
     */
    public static String getFileName(String urlpath) {
        if (TextUtils.isEmpty(urlpath)) {
            return "";
        }

        //获取 URL 中最后一个 “/” 的位置
        int index1 = urlpath.lastIndexOf("/");

        //获取 URL 中 “？” 的位置
        int index2 = urlpath.indexOf("?");

        String fileName;

        if (index2 >= 0 && index2 > index1) {
            fileName = urlpath.substring(index1 + 1, index2);
        } else {
            fileName = urlpath.substring(index1 + 1);
        }

        return fileName;
    }

    public static String getFileType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }

        //获取最后一个 “.” 的位置
        int indexDot = fileName.lastIndexOf(".");

        String fileType = "";

        if (indexDot != -1) {
            fileType = fileName.substring(indexDot + 1);
        }

        return fileType;
    }

    public static String getFilePureName(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }

        //获取最后一个 “.” 的位置
        int indexDot = fileName.lastIndexOf(".");

        String filePureName = "";

        if (indexDot != -1) {
            filePureName = fileName.substring(0,indexDot);
        }

        return filePureName;
    }


    public static String getUrlSuffix(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return "";
        }

        return (imageUrl.lastIndexOf(".") >= 0 ? imageUrl.substring(imageUrl.lastIndexOf(".") + 1) : "jpg");
    }

//
//
//    /**
//     * 获取网络图片的本地保存路径
//     *
//     * @param urlpath 网络URL
//     * @return 返回本地保存路径
//     */
//    public static String getFilePath(String urlpath) {
//
//        if (Utils2.getSdcard() != null) {
//            return Utils2.getSdcard() + PICTUREDIR + Utils2.getFileName(urlpath);
//        } else {
//            return null;
//        }
//    }

    public static boolean copy(InputStream is, OutputStream os) {
        if (is == null) {
            return false;
        }
        try {
            byte[] b = new byte[524288];
            while (true) {
                int i = is.read(b);
                if (i == -1) {
                    break;
                }
                os.write(b, 0, i);
                os.flush();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * SP 转为 PX
     *
     * @param context
     * @param sp
     * @return
     */
    public static float SpToPixels(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;

        return sp * scaledDensity;
    }


//    /**
//     * 计算单行text文字的宽
//     *
//     * @param text
//     * @param typeface
//     * @param textSize
//     * @return int
//     */
//    public static int measureSingleTextWidth(CharSequence text, float textSize, Typeface typeface) {
//        TextPaint mTextPaint = new TextPaint();
//        mTextPaint.setAntiAlias(true);
//        mTextPaint.setSubpixelText(true);
//        mTextPaint.setTextSize(textSize);
//        mTextPaint.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
//        return (int) FloatMath.ceil(mTextPaint.measureText(text, 0, text.length()));
//    }
//
//    /**
//     * 计算单行text文字的高
//     *
//     * @param typeface
//     * @param textSize
//     * @return int
//     */
//    public static int measureSingleTextHeight(float textSize, Typeface typeface) {
//        TextPaint mTextPaint = new TextPaint();
//        mTextPaint.setAntiAlias(true);
//        mTextPaint.setSubpixelText(true);
//        mTextPaint.setTextSize(textSize);
//        mTextPaint.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
//
//        Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();
//        mTextPaint.getFontMetrics(mFontMetrics);
//        return (int) FloatMath.ceil(0 - mFontMetrics.ascent);
//    }

    /**
     * 保存图片文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static void saveImageFile(Bitmap bm, String filePath, String fileName, Bitmap.CompressFormat imageSavedType) throws IOException {
        File dirFile = new File(filePath);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }


        File imageFile = new File(filePath + "/" + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imageFile));
        bm.compress(imageSavedType, 100, bos);
        bos.flush();
        bos.close();
    }

    /**
     * 保存图片文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static void saveJPEGFile(Bitmap bm, String filePath, String fileName) throws IOException {
        File dirFile = new File(filePath);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File jpegFile = new File(filePath + "/" + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(jpegFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }
//
//    public static int StringToInt(String string, int defaultValue) {
//        return Utils2.StringToInt(string, 10, defaultValue);
//    }
//
//    public static int StringToInt(String string, int radix, int defaultValue) {
//        int result = defaultValue;
//
//        try {
//            result = Integer.parseInt(string, radix);
//        } catch (NumberFormatException ex) {
//
//        }
//        return result;
//    }


    public static String getChannelCode(Context context) {
        String code = getMetaData(context, "UMENG_CHANNEL");
        if (code != null) {
            return code;
        }
        return "";
    }

    private static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            //
        }
        return null;
    }

    /**
     * 格式化日期时间（用于格式化 圈子、帖子、回复 的创建时间）
     *
     * @param millis
     * @return
     */
    public static String formatTime(long millis) {
        Calendar cur_calendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        String format;
        if (calendar.get(Calendar.YEAR) != cur_calendar.get(Calendar.YEAR)) {
            format = "yyyy-MM-dd HH:mm";
        } else if (calendar.get(Calendar.MONTH) != cur_calendar.get(Calendar.MONTH)) {
            format = "MM-dd HH:mm";
        } else if (calendar.get(Calendar.DATE) != cur_calendar.get(Calendar.DATE)) {
            format = "MM-dd HH:mm";
        } else {
            format = "HH:mm";
        }
        return formatDateByFormat(calendar.getTime(), format);
    }
    /**
     * 格式化日期时间 by wk
     *
     * @param millisString
     * @return
     */
    public static String formatStringTime(String millisString) {
        if(StringUtils.isBlank(millisString)){
            return "";
        }
        long millis=0;
        try{
         millis=Long.parseLong(millisString);
        }catch (Exception e){
            return  "";
        }
        if(millis==0){
            return "";
        }
        return formatTime(millis);
    }
    /**
     * 获得指定格式化日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 调用手机的短信程序发送邀请短信
     *
     * @param context
     * @param mobileNumber
     * @param smsBody
     *//*
    public static void sendInviteSMSByDevice(final Context context, String mobileNumber, String smsBody) {
        if ((context != null) &&
                (mobileNumber != null && mobileNumber.length() > 0 && Utils2.isMobileNO(mobileNumber)) &&
                (smsBody != null && smsBody.length() > 0)) {

            try {
                //判断设备能否发送短信
                boolean deviceCanSendSMS = false;

                PackageManager packageManager = context.getPackageManager();

                if (packageManager != null) {
                    deviceCanSendSMS = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
                }

                if (deviceCanSendSMS) {
                    final String finalMobileNumber = mobileNumber;

                    final String finalSmsBody = smsBody;

                    final ChanjetDialog dialog = new ChanjetDialog(context);

                    dialog.setCancelable(true);
                    dialog.setDialogText("本次添加会使用您的手机短信，由此产生的短信费用，由您个人承担，是否同意？");
                    dialog.setButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    }, "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                //发送短信
                                Uri smsUri = Uri.parse("smsto:" + finalMobileNumber);
                                Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
                                intent.putExtra("sms_body", finalSmsBody);

                                context.startActivity(intent);

                                dialog.dismiss();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });

                    dialog.setButton1TextColor(context.getResources().getColor(R.color.dialog_botton1_text));
                    dialog.setButton2TextColor(context.getResources().getColor(R.color.dialog_botton2_text));
                    dialog.setTitleVisible(false);
                    dialog.show();

                } else //不能发送短信
                {

                }
            } catch (Exception ex) {

            }
        }
    }*/
/*

    */
/**
     * 弹未安装对话框
     * @return
     *//*

    public static  void showNoAppDialog(Context context,String text){
        final ChanjetDialog dialog = new ChanjetDialog(context);

        dialog.setCancelable(false);
        dialog.setTitleVisible(false);

        dialog.setDialogText(text==null?"":text);

        dialog.setButton("我知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
*/



    /**
     * 获取用于分享到微信的图片的 Bytes (微信分享的图片最大支持32K)
     *
     * @param bitmap
     * @return
     */
    public static byte[] getBitmapBytesForWeixin(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        } else {
            int quality = 100;

            Bitmap output = bitmap;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (bitmap.getHeight() > 160 || bitmap.getWidth() > 160) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//
//            options.inJustDecodeBounds = true;
//
//            int widthScale = (int) bitmap.getWidth() / 160;
//
//            int heightScale = (int) bitmap.getHeight() / 160;
//
//            int scale = Math.max(widthScale, heightScale);
//
//            options.inSampleSize = scale;
//
//            bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), null, options);

                double smallRatio = Math.min((double) 160 / (double) bitmap.getWidth(), (double) 160 / (double) bitmap.getHeight());
                Matrix m = new Matrix();
                m.postScale((float) smallRatio, (float) smallRatio);
                output = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                output.compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream);

//                bitmap2.recycle();
//                bitmap2 = null;
            } else {

                output.compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream);
            }

            while (byteArrayOutputStream.toByteArray().length / 1024 > 32) {

                byteArrayOutputStream.reset();

                quality -= 10;

                if (quality <= 0) {
                    quality = 1;
                }

                output.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            }

            byte[] bitmapByte = byteArrayOutputStream.toByteArray();

            if (output != bitmap){
                output.recycle();
                output = null;
            }

            return bitmapByte;
        }
    }




    /**
     * 获取超出了多少个字符
     * 1.超过了目标数 返回超出了多少个字符 正数
     * 2.没达到目标数 返回还差多少个汉字字符  负数  如果还差4个目标数 则返回的汉字字符数是 4/2 = 2
     *
     * @param str        字符串
     * @param byteLength 字节长度  一个汉字的字节长度按2计算  比如需求要求最多50个汉字 那么byteLength就是50*2
     * @return 超出了多少个字符
     */
    public static int getOverflowCharNum(String str, int byteLength) {
        try {
            int targetValue = byteLength;// 目标值 如果目标数为100，对应的汉字数是50
            int curValue = 0;// 当前值
            int curcharNum = 0;// 截止达到临界值统计的字符数
            for (int i = 0; i < str.length(); i++) {
                if ((str.charAt(i) + "").getBytes("UTF-8").length > 1) {// 字节数>1 认为是汉字
                    curValue = curValue + 2;
                } else {// 字节数=1 认为是字母
                    curValue++;
                }
                curcharNum++;
                if (curValue >= targetValue) {
                    break;
                }
            }
            //如果没有超过目标值 则返回负数 这个负数代表 还差多少个汉字字符
            if (curValue < targetValue) {
                return (curValue - targetValue)/2;
            }
            return str.length() - curcharNum;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0;
        }

    }


    /**
     * 字符串转成正则表达式的形式
     * @param str
     * @return
     */
    public static String getRegStr(String str){
//        String reg = "\\d+";
        String reg = "\\\\\\\\";
        String reg1 = "\\\\:";
        String reg2 = "\\\\+";
        String reg3 = "\\\\(";
        String reg4 = "\\\\)";
        String reg5 = "\\\\[";
        String reg6 = "\\\\]";
        String reg7 = "\\\\\\$";//不知道为什么这里需要多加两个“\\”
        String reg8 = "\\\\*";
        String reg9 = "\\\\^";
        String reg10 = "\\\\{";
        String reg11 = "\\\\}";
        String reg12 = "\\\\.";
        String tmp = str.replaceAll("\\\\",reg)
                        .replaceAll("\\+", reg2)
                        .replaceAll("\\:", reg1)
                        .replaceAll("\\(", reg3)
                        .replaceAll("\\)", reg4)
                        .replaceAll("\\[", reg5)
                        .replaceAll("\\]", reg6)
                        .replaceAll("\\$", reg7)
                        .replaceAll("\\*", reg8)
                        .replaceAll("\\^", reg9)
                        .replaceAll("\\{", reg10)
                        .replaceAll("\\}", reg11)
                        .replaceAll("\\.", reg12);
        return tmp;
    }

    public static void copyToClipboard(Context context,String text){
        if (text == null) {
            text = "";
        }

        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        // Creates a new text clip to put on the clipboard
        ClipData clip = ClipData.newPlainText("simple text", text);
        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip);
    }


    /**
     * Try to return the absolute file path from the given Uri 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getRealFilePathFromUri( final Context context, Uri uri ) {
//        public static String getPath(final Context context, final Uri uri) {

            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            boolean isDocumentUri=false;
            if(isKitKat){
                try {
                    isDocumentUri=DocumentsContract.isDocumentUri(context, uri);
                }catch (Exception e){
                    e.fillInStackTrace();
                }
            }
        // DocumentProvider
            if (isKitKat && isDocumentUri) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };


                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context The context.
         * @param uri The Uri to query.
         * @param selection (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        public static String getDataColumn(Context context, Uri uri, String selection,
                String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {
                    column
            };

            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        public static boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri.getAuthority());
        }
/*

    *//**
     * 保存图片到本地
     * @param mContext
     * @param imageUrl
     * @param imageFileSavePath
     * @param imageFileName
     * @throws IOException
     *//*
    public static void downloadImage(final Context mContext,String imageUrl, final String imageFileSavePath, final String imageFileName) throws IOException {

        if (imageFileSavePath.length() == 0) {
            Toast.makeText(mContext, "目录为空，无法保存图片！", Toast.LENGTH_SHORT).show();
            return;
        }

        final String imageFileFullName = imageFileSavePath + imageFileName;


        ImageLoader.getInstance().loadImage(imageUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                Toast.makeText(mContext, "保存失败，请重试！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if(bitmap==null){
                    Toast.makeText(mContext, "保存失败，请重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Utils2.saveJPEGFile(bitmap, imageFileSavePath, imageFileName);
                    Toast.makeText(mContext, "保存路径： " + imageFileFullName, Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "保存失败，请重试！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    *//**
     * 保存图片到本地
     * @param mContext
     * @param imageUrl
     * @param imageFileSavePath
     * @param imageFileName
     * @throws IOException
     *//*
    public static void downloadAndSaveImage(final Context mContext, String imageUrl, final String imageFileSavePath, final String imageFileName, Bitmap.CompressFormat imageSavedType) throws IOException {

        if (imageFileSavePath.length() == 0) {
            Toast.makeText(mContext, "目录为空，无法保存图片！", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageFilePureName = Utils2.getFilePureName(imageFileName);
        String imageFileType = "";

        if (imageSavedType == Bitmap.CompressFormat.PNG){
            imageFileType = ".png";
        }else if(imageSavedType == Bitmap.CompressFormat.JPEG){
            imageFileType = ".jpg";
        }

        final String imageFileFullName = Utils2.getFilePureName(imageFileSavePath + imageFileName) + imageFileType;

        String imageFileNameWithChangedType = imageFilePureName + imageFileType;

        ImageLoader.getInstance().loadImage(imageUrl, getImageLoadingListener(mContext, imageFileSavePath, imageFileNameWithChangedType, imageFileFullName, imageSavedType));
    }

    @NonNull
    private static ImageLoadingListener getImageLoadingListener(final Context mContext, final String imageFileSavePath, final String imageFileName, final String imageFileFullName,Bitmap.CompressFormat imageSavedType) {
        return new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                Toast.makeText(mContext, "保存失败，请重试！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if(bitmap==null){
                    Toast.makeText(mContext, "保存失败，请重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Utils2.saveImageFile(bitmap, imageFileSavePath, imageFileName,imageSavedType);
                    Toast.makeText(mContext, "保存路径： " + imageFileFullName, Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "保存失败，请重试！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        };
    }*/

}
