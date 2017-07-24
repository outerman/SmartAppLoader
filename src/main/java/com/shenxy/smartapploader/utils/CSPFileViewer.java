package com.shenxy.smartapploader.utils;

/**
 * Created by David on 14-2-12.
 */
public class CSPFileViewer {

//    public static final String TAG = "CSPFileViewer";
//
//    String serviceString = Context.DOWNLOAD_SERVICE;
//    private static final String HMAC_SHA1 = "HmacSHA1";
//
//    private  final int LINUX_MAX_FILE_NAME = 255;
//    private final int MAX_FILE_NAME_HAS_EXTENSION = LINUX_MAX_FILE_NAME -32 -1;
//    private DownloadInfoListener listener;
//
//    long thisDownloadReference = -1;
//    AsyncTask dl;
//
//
//    private CSPFileViewer() {
//    }
//
//    static class SingletonHolder {
//        static CSPFileViewer instance = new CSPFileViewer();
//    }
//
//    public static CSPFileViewer getInstance() {
//        return SingletonHolder.instance;
//    }
//
//    public interface DownloadInfoListener {
//        public void OnDownloadComplete();
//
//        public void OnDownloadProgressChanged(long totalSize, long currentSize);
//
//        public void OnDownloadFaild();
//    }
//
//    public void setDownloadInfoListener(DownloadInfoListener listener) {
//        this.listener = listener;
//    }
//
//    public static String getSignature(String data, String key) throws Exception {
//        byte[] keyBytes = key.getBytes("UTF-8");
//        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
//        Mac mac = Mac.getInstance(HMAC_SHA1);
//        mac.init(signingKey);
//        byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
//        StringBuilder sb = new StringBuilder();
//        for (byte b : rawHmac) {
//            sb.append(byteToHexString(b));
//        }
//        return sb.toString();
//    }
//
//    private static String byteToHexString(byte ib) {
//        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//        char[] ob = new char[2];
//        ob[0] = Digit[(ib >>> 4) & 0X0f];
//        ob[1] = Digit[ib & 0X0F];
//        String s = new String(ob);
//        return s;
//    }
//
    public static String getFileSizeForDisplay(long size) {
        Float sizef = Float.valueOf(size);

        int count = 0;
        while (sizef > 1024) {
            sizef = sizef / 1024;
            count++;
        }

        switch (count) {
            case 0: {
                return "" + size + "B";
            }
            case 1: {
                return "" + String.format("%.2f", sizef.floatValue()) + "KB";
            }
            case 2: {
                return "" + String.format("%.2f", sizef.floatValue()) + "MB";
            }
            case 3: {
                return "" + String.format("%.2f", sizef.floatValue()) + "GB";
            }
        }
        return "";
    }
//
//    public Uri buildUri(final TopicQuery.File dto) {
//
//        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
//        String message = dto.uri.trim() + AppConstants.APPID_WORKCIRCLE + date + "6dw2de";
//        String signature = null;
//        try {
////            signature = Encodes.encodeHex(CSPFileViewer.getSignature(message, "6dw2de").getBytes("UTF-8"));
//            signature = HexUtil.toHexString(CSPFileViewer.getSignature(message, "6dw2de").getBytes("UTF-8"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String queryStr =
//                "appkey=" + AppConstants.APPID_WORKCIRCLE + "&date=" + date + "&signature=" + signature;
//
//        String urlStr = dto.uri;
//        Uri u = Uri.parse(urlStr);
//        //注释掉文件浏览加后缀参数 by wk
////        if (urlStr.toLowerCase(Locale.CHINA).startsWith("https://")) {  //把https，HTTPS等，替换成http
////            urlStr = "http://" + urlStr.substring("https://".length());
////        }
////        if (urlStr.contains("?")) {
////
////            u = Uri.parse(urlStr + "&" + queryStr);
////        }
////        else {
////           u = Uri.parse(urlStr + "?" + queryStr);
////        }
//        return u;
//
//    }
//
//    private String parseUriToDir(TopicQuery.File dto) {
//
//        String uri = dto.uri;
//        uri = uri.substring(uri.indexOf("/", 8) + 1, uri.lastIndexOf("/") + 1);
//        Log.d(TAG, uri);
//
//        return uri;
//    }
//
//
//    public Intent buildIntent(final TopicQuery.File dto) {
//
//        Uri u = buildUri(dto);
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, u);
//        String mimeType =
//                MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(dto.uri));
//
//        intent.setType(mimeType);
//
//        return intent;
//    }
//
//    public boolean canOpenLocally(Intent intent, Context _ctx) {
//
//        List list = _ctx.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//        if (list == null || list.size() == 0) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public void downloadAndPreview(TopicQuery.File dto, Context _ctx) {
//
//        Bundle bundle = new Bundle();
//
//        bundle.putSerializable("images", new String[]{buildUri(dto).toString()});
////        bundle.putBoolean("imageLocal", false);
//
//        bundle.putInt("imageIndex", 0);
//        Intent imageIntent = new Intent();
//        imageIntent.putExtras(bundle);
//        imageIntent.setClass(_ctx, ViewPagerActivity.class);
//        imageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        _ctx.startActivity(imageIntent);
//    }
//
//    public void downloadAndPreviewRange(String fileName, int pageCount, String type, Context _ctx) {
//
//        Bundle bundle = new Bundle();
//        ArrayList<String> urls = new ArrayList<String>();
//
//        for (int j = 0; j <= pageCount; j++) {
//            urls.add(fileName + "-" + j + "." + type);
//        }
//
//        String[] urlArray = urls.toArray(new String[urls.size()]);
//
//        bundle.putSerializable("images", urlArray);
////        bundle.putBoolean("imageLocal", false);
//
//        bundle.putInt("imageIndex", 0);
//        Intent imageIntent = new Intent();
//        imageIntent.putExtras(bundle);
//        imageIntent.setClass(_ctx, ViewPagerActivity.class);
//        imageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
////        imageIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        _ctx.startActivity(imageIntent);
//    }
//
//
//    public void downloadAndDone(final TopicQuery.File dto, Context _ctx, final Activity base) {
//
//        final String path = parseUriToDir(dto);
//
//        File file = new File(
//                Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/chanjet/doc/" +
//                        path + getFileName(dto));//dto.name);
//        if (file.exists()) {
//            if(FileUtils.openFile(file,base)){//通过系统默认工具打开,若打开失败 则提示以下信息
//                base.finish();
//               return;
//            }
//            Uri uri = Uri.fromFile(file);
//            Toast.makeText(_ctx, "文件已经下载过，并存放在" + uri.toString(), Toast.LENGTH_LONG).show();
//            base.finish();
//        } else {
//            File SDFile = Environment.getExternalStorageDirectory();
//            if (!SDFile.exists()) {
//                Toast.makeText(_ctx, "未安装SD卡，不能下载", Toast.LENGTH_SHORT).show();
//                base.finish();
//                return;
//            }
//
//            ConnectivityManager connectivityManager =
//                    (ConnectivityManager) _ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//            if (null != networkInfo && networkInfo.isConnected()) {
//                File dir = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS +
//                        "/chanjet/doc/" + path);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//
//                if (!dir.exists()) {
//                    dir.mkdir();
//                    if (!dir.exists()) {
//                        Toast.makeText(_ctx, "不能创建下载目录", Toast.LENGTH_SHORT).show();
//                        base.finish();
//                        return;
//                    }
//
//                    Toast.makeText(_ctx, "未安装SD卡，不能下载", Toast.LENGTH_SHORT).show();
//                    base.finish();
//                    return;
//                }
//
//                dl = new DownloadFileFromURL(base, _ctx, false,dto.type).execute(dto.uri, path, getFileName(dto));//dto.name);
//            } else {
//                Toast.makeText(_ctx, "请联网重试", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }
//
//    //为避免重复文件名的附件，导致打开错误，在文件名加上md5值
//    private String getFileName(TopicQuery.File dto){
////        return dto.name;
//
//        String md5 = MD5Util.md5Hex(dto.uri);
//        int dotPosition = dto.name.lastIndexOf(".");
//
//        if (dotPosition >= 0){
//                //Toast.makeText(_ctx, "文件名过长，已被截断", Toast.LENGTH_SHORT).show();
//            String fileExtention = dto.name.substring(dotPosition, dto.name.length());
//            String fileActName = dto.name.substring(0,dotPosition);
//            int cuttedIndex = getFileNameCuttedIndex(fileActName, dotPosition, fileExtention.length());
//            return fileActName.substring(0, cuttedIndex) + "_" + md5 + fileExtention;
//        }
//        else {
//            int cuttedIndex = getFileNameCuttedIndex(dto.name, dto.name.length(), 0);
//            return dto.name.substring(0, cuttedIndex) + "_" + md5;
//        }
//    }
//
//    @Nullable
//    private int getFileNameCuttedIndex(String fileActName, int dotPosition, int fileExtensionLength) {
//        for (int index = 0, len = dotPosition, bytes = 0; index < len; ++index) {
//            bytes += String.valueOf(fileActName.charAt(index)).getBytes(Charset.forName("UTF-8")).length;
//
//            if (bytes > (MAX_FILE_NAME_HAS_EXTENSION - fileExtensionLength)) {
//                return index;
//            }
//        }
//        return dotPosition;
//    }
//
////    public void downloadConvertImageAndPreview(final TopicQuery.File dto, Context _ctx) {
////        Uri u = buildUri(dto);
////        String ServiceUrl = "http://test.gongzuoquan.com/upload/document/status?url=" + u.toString();
////
////    }
//
////    public void queryDownloadProgress() {
////        DownloadManager.Query query = new DownloadManager.Query();
////        if (thisDownloadReference <= 0) {
////            return;
////        }
////        query.setFilterById(thisDownloadReference);
////        if(query != null) {
////            Cursor c = downloadManager.query(query);
////            if(c != null) {
////                if (c.moveToFirst()) {
////                    int sizeIndex = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
////                    int downloadedIndex = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
////                    long size = c.getInt(sizeIndex);
////                    long downloaded = c.getInt(downloadedIndex);
////
////                    if(this.listener != null) {
////                        listener.OnDownloadProgressChanged(size, downloaded);
////                    }
////
////                    double progress = 0.0;
////                    if (size != -1)
////                        progress = downloaded*100.0/size;
////                    // At this point you have the progress as a percentage.
////                }
////            }
////            c.close();
////        }
////
////
////
////    }
//
//    public void cancelDownload() {
////        if (thisDownloadReference <= 0) {
////            return;
////        }
////        downloadManager.remove(thisDownloadReference);
//
//        if (dl != null) {
//            dl.cancel(true);
//        }
//    }
//
//
//    public void downloadAndOpen(TopicQuery.File dto, Context _ctx, final Activity base) {
//
//        final String path = parseUriToDir(dto);
//        File SDFile = Environment.getExternalStorageDirectory();
//        if (!SDFile.exists()) {
//            Toast.makeText(_ctx, "未安装SD卡，不能下载", Toast.LENGTH_SHORT).show();
//            base.finish();
//            return;
//        }
//
//        File file = new File(
//                Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/chanjet/doc/" +
//                        path + getFileName(dto));//dto.name);
//        if (file.exists()) {
//
//            Uri uri = Uri.fromFile(file);
//
//            base.finish();
//
//            if ("png".equalsIgnoreCase(dto.type) ||
//                    "jpg".equalsIgnoreCase(dto.type) ||
//                    "gif".equalsIgnoreCase(dto.type)) {
//
//                Bundle bundle = new Bundle();
//
//                bundle.putSerializable("images", new String[]{file.getPath()});
////                bundle.putBoolean("imageLocal", true);
//
//                bundle.putInt("imageIndex", 0);
//                Intent imageIntent = new Intent();
//                imageIntent.putExtras(bundle);
//                imageIntent.setClass(_ctx, ViewPagerActivity.class);
//                imageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                _ctx.startActivity(imageIntent);
//                return;
//            }
//
//
//
//            Intent i = new Intent("android.intent.action.VIEW");
//            i.addCategory("android.intent.category.DEFAULT");
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            String mimeType =
//                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(dto.uri));
//
//            i.setDataAndType(uri, mimeType);
//
//            ComponentName cn = i.resolveActivity(_ctx.getPackageManager());
//            if (cn != null) {
//                _ctx.startActivity(i);
//            }
//            else {
//                Toast.makeText(_ctx, "没有可以打开该种类型文件的应用！", Toast.LENGTH_SHORT).show();
//            }
////            Toast.makeText(_ctx, "文件已经下载过!", Toast.LENGTH_SHORT).show();
//        } else {
//
//            ConnectivityManager connectivityManager =
//                    (ConnectivityManager) _ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//            if (null != networkInfo && networkInfo.isConnected()) {
////                downloadManager = (DownloadManager)_ctx.getSystemService(serviceString);
////
////                Uri u = buildUri(dto);
////                DownloadManager.Request request = new DownloadManager.Request(u);
////
////                request.setTitle("工作圈附件");
////                request.setDestinationInExternalFilesDir(_ctx, Environment.DIRECTORY_DOWNLOADS, dto.getName());
//                File dir = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS +
//                        "/chanjet/doc/" + path);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//
//                if (!dir.exists()) {
//                    dir.mkdir();
//                    if (!dir.exists()) {
//                        Toast.makeText(_ctx, "不能创建下载目录", Toast.LENGTH_SHORT).show();
//                        base.finish();
//                        return;
//                    }
//
//                    Toast.makeText(_ctx, "未安装SD卡，不能下载", Toast.LENGTH_SHORT).show();
//                    base.finish();
//                    return;
//                }
//
//                dl = new DownloadFileFromURL(base, _ctx, true,dto.type).execute(dto.uri, path, getFileName(dto));//dto.name);
//            } else {
//                Toast.makeText(_ctx, "No network connection available", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//    /**
//     * Background Async Task to download file
//     */
//    class DownloadFileFromURL extends AsyncTask<String, String, String> {
//
//        Activity _base;
//        Context _ctx;
//        boolean openAfterDone = false;
//        String dest_file;
//        String uri;
//        String type;
//
//        public DownloadFileFromURL(Activity activity, Context ctx, boolean open,String type) {
//            super();
//            _base = activity;
//            _ctx = ctx;
//            openAfterDone = open;
//            this.type = type;
//        }
//
//
//        /**
//         * Before starting background thread Show Progress Bar Dialog
//         */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        /**
//         * Downloading file in background thread
//         */
//        @Override
//        protected String doInBackground(String... f_url) {
//            int count;
//            OutputStream output = null;
//            try {
//                URL url = new URL(f_url[0]);
//                URLConnection conection = url.openConnection();
//                conection.connect();
//
//                // this will be useful so that you can show a tipical 0-100%
//                // progress bar
//                int lenghtOfFile = conection.getContentLength();
//
//                // download the file
//                InputStream input = new BufferedInputStream(url.openStream(), 102400);
//
//                // Output stream
////                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/2011.kml");
//                uri = f_url[0];
//                File dir = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS +
//                        "/chanjet/doc/" + f_url[1]);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                    if (!dir.exists()) {
//                        Toast.makeText(_ctx, "不能创建下载目录", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                dest_file = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS +
//                        "/chanjet/doc/" + f_url[1] + f_url[2];
//                output = new FileOutputStream(dest_file);
//
//                byte data[] = new byte[102400];
//
//                long total = 0;
//
//                while ((count = input.read(data)) != -1) {
//                    total += count;
//                    // publishing the progress....
//                    // After this onProgressUpdate will be called
//                    publishProgress(Long.valueOf(lenghtOfFile).toString(), Long.valueOf(total).toString());
//
//                    // writing data to file
//                    output.write(data, 0, count);
//                }
//
//                // flushing output
//                output.flush();
//
//                // closing streams
//                output.close();
//                input.close();
//
//            } catch (Exception e) {
//                if (dest_file != null) {
//                    File f = new File(dest_file);
//                    if (f.exists()) {
//                        f.delete();
//                    }
//                }
//                if (e.getMessage() != null) {
//                    Log.e("Error: ", e.getMessage());
//                } else {
//                    Log.e("Error:", e.toString());
//                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//        }
//
//        /**
//         * Updating progress bar
//         */
//        protected void onProgressUpdate(String... progress) {
//            // setting progress percentage
////            pDialog.setProgress(Integer.parseInt(progress[0]));
//            if (CSPFileViewer.this.listener != null) {
//                listener.OnDownloadProgressChanged(Long.parseLong(progress[0]), Long.parseLong(progress[1]));
//            }
//
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * *
//         */
//        @Override
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog after the file was downloaded
////            dismissDialog(progress_bar_type);
//
//            if (dest_file == null || StringUtils.isBlank(dest_file)) {
//                Toast.makeText(_ctx, "文件下载失败", Toast.LENGTH_SHORT).show();
//                listener.OnDownloadFaild();
//                return;
//            }
//
//            File destFile = new File(dest_file);
//            if (destFile.exists()) {
//
//                Toast.makeText(_ctx, "文件下载完成，并存放在" + dest_file, Toast.LENGTH_LONG).show();
//
//
//                if (CSPFileViewer.this.listener != null) {
//                    CSPFileViewer.this.listener.OnDownloadComplete();
//                }
//
//                _base.finish();
//
////                Intent intent = new Intent();
////                intent.setAction(Intent.ACTION_GET_CONTENT);
////                intent.setData(Uri.fromFile(new File(dest_file)));
////                intent.setType("file/*");
////                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                _ctx.startActivity(intent);
//
//                try {
//                    if (dest_file != null && dest_file.endsWith(".apk")) {
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setDataAndType(Uri.fromFile(new File(dest_file)), "application/vnd.android.package-archive");
//                        _ctx.startActivity(intent);
//                        return;
//                    }
//
//
//                    if (type.equalsIgnoreCase("png")||
//                            type.equalsIgnoreCase("jpg") ||
//                            type.equalsIgnoreCase("gif")) {
//
//                        Bundle bundle = new Bundle();
//
//                        bundle.putSerializable("images", new String[]{dest_file});
////                        bundle.putBoolean("imageLocal", true);
//
//                        bundle.putInt("imageIndex", 0);
//                        Intent imageIntent = new Intent();
//                        imageIntent.putExtras(bundle);
//                        imageIntent.setClass(_ctx, ViewPagerActivity.class);
//                        imageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        _ctx.startActivity(imageIntent);
//                        return;
//                    }
//
//
//                    if (openAfterDone) {
//                        Intent i = new Intent("android.intent.action.VIEW");
//                        i.addCategory("android.intent.category.DEFAULT");
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        String mimeType =
//                                MimeTypeMap.getSingleton()
//                                        .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri));
//
//                        Uri u = Uri.fromFile(destFile);
//
//                        i.setDataAndType(u, mimeType);
//                        _ctx.startActivity(i);
//                    }
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(_ctx, "文件下载失败", Toast.LENGTH_SHORT).show();
//                listener.OnDownloadFaild();
//             }
//        }
//
//    }


}