package com.shenxy.smartapploader.utils.download;

import android.content.Context;
import android.util.Log;

import com.shenxy.smartapploader.greendao.DBHelper;
import com.shenxy.smartapploader.greendao.dao.db.TWDownloadFilesEntityDao;
import com.shenxy.smartapploader.greendao.dao.download.TWDownloadFilesEntity;
import com.shenxy.smartapploader.utils.OnDownloadStatusChangedListener;
import com.shenxy.smartapploader.utils.StorageUtil;
import com.shenxy.smartapploader.workTmp.BaseApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

//import com.chanjet.greendao.DBHelper;
//import com.chanjet.workcircle.application.BaseApplication;
//import com.chanjet.workcircle.dao.db.TWDownloadFilesEntityDao;
//import com.chanjet.workcircle.dao.download.TWDownloadFilesEntity;

/**
 * Created by David on 14-7-31.
 */
public class DownloadHelper {


    /*
    USAGE:

    DownloadHelper.start("http://10.10.71.153/f/public/ios.zip", new OnDownloadStatusChangedListener() {
            @Override
            public void OnProgressChanged(long total, long current) {
                Log.d("OnDownloadStatusChangedListener-->", current +" / "+total);
            }

            @Override
            public void OnError(Exception ex) {
                Log.d("OnDownloadStatusChangedListener-->", "ERROR");
            }

            @Override
            public void OnDownloadComplete(String fullPath) {
                Log.d("OnDownloadStatusChangedListener-->", "OnDownloadComplete" + fullPath);
            }

            @Override
            public void OnZipFileUnCompressed(String path) {
                Log.d("OnDownloadStatusChangedListener-->", "OnZipFileUnCompressed" + path);
            }
        });
     */

    public static void start(String uri, final OnDownloadStatusChangedListener l) {

        final TWDownloadFilesEntity dlEntity = new TWDownloadFilesEntity();
        dlEntity.setUri(uri);
        dlEntity.setMethod("GET");
        dlEntity.setAlt("");
        DBHelper.getInstance().getTWDownloadFilesEntityDao().insert(dlEntity);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doDownload(dlEntity, l);
            }
        }
        ).start();
    }

    public static String getPathNameAfterUnZip(String uri) {
        if (getFileExtNameFromUri(uri).equalsIgnoreCase("zip")) {
            List<TWDownloadFilesEntity> dls = DBHelper.getInstance().getTWDownloadFilesEntityDao().queryBuilder().where(
                    TWDownloadFilesEntityDao.Properties.Uri.eq(uri),
                    TWDownloadFilesEntityDao.Properties.File_status.eq(TWDownloadFilesEntity.FILE_STATUS.FILE_STATUS_DOWNLOADED.value())
            ).orderDesc(TWDownloadFilesEntityDao.Properties.Update_time).list();

            if (dls != null && dls.size() > 0) {
                return dls.get(0).getFile_uncompressed_path();
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public static String findDownloadedByUri(String uri) {

        List<TWDownloadFilesEntity> dls = DBHelper.getInstance().getTWDownloadFilesEntityDao().queryBuilder().where(
                TWDownloadFilesEntityDao.Properties.Uri.eq(uri),
                TWDownloadFilesEntityDao.Properties.File_status.eq(TWDownloadFilesEntity.FILE_STATUS.FILE_STATUS_DOWNLOADED.value())
        ).orderDesc(TWDownloadFilesEntityDao.Properties.Update_time).list();

        if (dls != null && dls.size() > 0) {
            return dls.get(0).getFile_full_name();
        }
        else {
            return null;
        }
    }

    public static void deleteDownloadedByUri(String uri) {
        List<TWDownloadFilesEntity> dls = DBHelper.getInstance().getTWDownloadFilesEntityDao().queryBuilder().where(
                TWDownloadFilesEntityDao.Properties.Uri.eq(uri)
        ).orderDesc(TWDownloadFilesEntityDao.Properties.Update_time).list();

        if (dls != null && dls.size() > 0) {
            for (TWDownloadFilesEntity entity : dls) {
                File f = new File(entity.getFile_full_name());
                f.delete();
                if (entity.getFile_ext_name().equalsIgnoreCase("zip")) {
                    File z = new File(entity.getFile_uncompressed_path());
                    z.delete();
                }

                DBHelper.getInstance().getTWDownloadFilesEntityDao().delete(entity);
            }
        }

    }



    /*
//        tWDownloadFilesEntity.addLongProperty("id").primaryKey().autoincrement();
//        tWDownloadFilesEntity.addStringProperty("uri");
//        tWDownloadFilesEntity.addStringProperty("alt");
//        tWDownloadFilesEntity.addStringProperty("method");
        tWDownloadFilesEntity.addStringProperty("mime_type");

//        tWDownloadFilesEntity.addIntProperty("http_status");
        tWDownloadFilesEntity.addIntProperty("file_status"); //FILE_STATUS

//        tWDownloadFilesEntity.addLongProperty("start_time");
//        tWDownloadFilesEntity.addLongProperty("update_time");

//        tWDownloadFilesEntity.addLongProperty("length");
//        tWDownloadFilesEntity.addLongProperty("current_length");
//        tWDownloadFilesEntity.addStringProperty("file_md5");

        tWDownloadFilesEntity.addStringProperty("path");

        tWDownloadFilesEntity.addStringProperty("file_full_name");
//        tWDownloadFilesEntity.addStringProperty("file_name");
//        tWDownloadFilesEntity.addStringProperty("file_ext_name");
//        tWDownloadFilesEntity.addStringProperty("file_uncompressed_path");

        tWDownloadFilesEntity.addStringProperty("info");
     */



    /**
     * Background Async Task to download file
     */
    private static void doDownload(TWDownloadFilesEntity dlEntity, OnDownloadStatusChangedListener l) {

        int count;
        OutputStream output = null;
        Context _ctx = BaseApplication.getContext();
        String dest_file = null;

        if (l == null) {
            return;
        }

        try {
            URL url = new URL(dlEntity.getUri());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.setConnectTimeout(5000); //连接超时5s
            connection.connect();

            long lenghtOfFile = connection.getContentLength();
            String contentType = connection.getContentType();
            int code = connection.getResponseCode();

            InputStream input = new BufferedInputStream(url.openStream(), 102400);

            File dir = new File(StorageUtil.GetStorePath(StorageUtil.Type.download));
            if (!dir.exists()) {
                dir.mkdirs();
                if (!dir.exists()) {
                    l.OnError(new FileNotFoundException());
                }
            }

            dlEntity.setLength(lenghtOfFile);
            dlEntity.setMime_type(contentType);

            dlEntity.setHttp_status(code);

            dlEntity.setFile_ext_name(getFileExtNameFromUri(dlEntity.getUri()));
            dlEntity.setFile_name(getFileNameFromUri(dlEntity.getUri()));

            dlEntity.setFile_full_name(UUID.randomUUID().toString()+"."+dlEntity.getFile_ext_name());
            dlEntity.setStart_time(System.currentTimeMillis());

            dest_file = StorageUtil.GetStorePath(StorageUtil.Type.download) + dlEntity.getFile_full_name();
            output = new FileOutputStream(dest_file);
            DBHelper.getInstance().getTWDownloadFilesEntityDao().update(dlEntity);


            byte data[] = new byte[102400];
            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);

                dlEntity.setCurrent_length(total);
                dlEntity.setUpdate_time(System.currentTimeMillis());
                DBHelper.getInstance().getTWDownloadFilesEntityDao().update(dlEntity);

                l.OnProgressChanged(lenghtOfFile, total);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            if (dest_file != null) {
                File f = new File(dest_file);
                if (f.exists()) {
                    f.delete();
                }
            }
            if (e.getMessage() != null) {
                Log.e("Error: ", e.getMessage());
            } else {
                Log.e("Error:", e.toString());
            }

            l.OnError(e);
            return;
        }


        if (dest_file == null) {
            l.OnError(new FileNotFoundException());
            return;
        }

        File destFile = new File(dest_file);
        if (destFile.exists()) {

            String md5 = StorageUtil.getFileMD5(destFile);
            dlEntity.setFile_md5(md5);
            File tmp = new File(StorageUtil.GetStorePath(StorageUtil.Type.download) + md5 + "." + dlEntity.getFile_ext_name());
            boolean isRenamed = destFile.renameTo(tmp);

            if (isRenamed) {
                DBHelper.getInstance().getTWDownloadFilesEntityDao().update(dlEntity);
            }

            dlEntity.setFile_status(TWDownloadFilesEntity.FILE_STATUS.FILE_STATUS_DOWNLOADED.value());
            dlEntity.setFile_full_name(StorageUtil.GetStorePath(StorageUtil.Type.download) + md5 + "." + dlEntity.getFile_ext_name());
            DBHelper.getInstance().getTWDownloadFilesEntityDao().update(dlEntity);

            l.OnDownloadComplete(StorageUtil.GetStorePath(StorageUtil.Type.download) + md5 + "." + dlEntity.getFile_ext_name());

            if (dlEntity.getFile_ext_name().equalsIgnoreCase("zip")) {
                try {
                    File unzip = new File(StorageUtil.GetStorePath(StorageUtil.Type.download) + md5);
                    if (!unzip.exists()) {
                        unzip.mkdirs();
                        if (!unzip.exists()) {
                            l.OnError(new FileNotFoundException());
                        }
                    }
                    upZipFile(tmp, StorageUtil.GetStorePath(StorageUtil.Type.download) + md5);

                    dlEntity.setFile_status(TWDownloadFilesEntity.FILE_STATUS.FILE_STATUS_UNCOMPRESSED.value());
                    dlEntity.setFile_uncompressed_path(StorageUtil.GetStorePath(StorageUtil.Type.download) + md5);

                    DBHelper.getInstance().getTWDownloadFilesEntityDao().update(dlEntity);

                    l.OnZipFileUnCompressed(StorageUtil.GetStorePath(StorageUtil.Type.download) + md5);

                } catch (IOException e) {
                    e.printStackTrace();
                    l.OnError(e);
                }
            }
        } else {
            l.OnError(new FileNotFoundException());
        }

    }


    public static String getFileExtNameFromUri(String uri) {
        String []parts = uri.split("/");
        if (parts.length > 1) {
            String name = parts[parts.length - 1];
            if (name.contains(".") && !name.startsWith(".") && !name.endsWith(".") && name.indexOf(".") == name.lastIndexOf(".")) {
                String ext = name.substring(name.indexOf(".")+1);
                if (ext.contains("?")) {
                    ext = ext.substring(0, ext.indexOf("?"));
                }
                return ext;
            }

        }
        return "";
    }

    private static String getFileNameFromUri(String uri) {
        String parts[] = uri.split("/");
        if (parts.length > 1) {
            String name = parts[parts.length - 1];
            if (name.contains(".") && !name.startsWith(".") && !name.endsWith(".") && name.indexOf(".") == name.lastIndexOf(".")) {
                String prefix = name.substring(0, name.indexOf("."));
                return prefix;
            }

        }
        return "";
    }

    /**
     * 解压缩功能.
     * 将zipFile文件解压到folderPath目录下.
     * @throws Exception
     */
    public static int upZipFile(File zipFile, String folderPath) throws ZipException,IOException {
        //public static void upZipFile() throws Exception{
        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList=zfile.entries();
        ZipEntry ze=null;
        byte[] buf=new byte[1024];
        while(zList.hasMoreElements()){
            ze=(ZipEntry)zList.nextElement();
            if(ze.isDirectory()){
//                Log.d("upZipFile", "ze.getName() = "+ze.getName());
                String dirstr = folderPath +"/"+ ze.getName();
                //dirstr.trim();
                dirstr = new String(dirstr.getBytes("utf-8"), "utf-8");
//                Log.d("upZipFile", "str = "+dirstr);
                File f=new File(dirstr);
                f.mkdir();
                continue;
            }
//            Log.d("upZipFile", "ze.getName() = "+ze.getName());
            OutputStream os=new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            InputStream is=new BufferedInputStream(zfile.getInputStream(ze));
            int readLen=0;
            while ((readLen=is.read(buf, 0, 1024))!=-1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();



        return 0;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     * @param baseDir 指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    private static File getRealFileName(String baseDir, String absFileName){
        String[] dirs = absFileName.split("/");
        File ret=new File(baseDir);
        String substr = null;
        if(dirs.length>1){
            for (int i = 0; i < dirs.length-1;i++) {
                substr = dirs[i];
                try {
                    //substr.trim();
                    substr = new String(substr.getBytes("utf-8"), "utf-8");

                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ret=new File(ret, substr);

            }
//            Log.d("upZipFile", "1ret = "+ret);
            if(!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length-1];
            try {
                //substr.trim();
                substr = new String(substr.getBytes("utf-8"), "utf-8");
//                Log.d("upZipFile", "substr = "+substr);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ret=new File(ret, substr);
//            Log.d("upZipFile", "2ret = "+ret);
            return ret;
        }
        else if (dirs.length == 1){
            ret = new File(baseDir + "/" + dirs[0]);
        }
        return ret;
    }

}
