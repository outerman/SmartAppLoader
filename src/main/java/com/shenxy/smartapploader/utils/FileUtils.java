package com.shenxy.smartapploader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


public class FileUtils {
	/**
	 * 写入文本到文件
	 * 
	 * @param path
	 *            文件目录
	 * @param content 文本内容
	 * @return
	 */
	public static boolean write(String path, String content) {
		File saveFile = new File(path);
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(saveFile);
			outStream.write(content.getBytes("UTF-8"));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 读取文本文件的内容
	 *
	 * @param path 文件目录
	 * @return
	 */
	public static String read(String path) {
		try {
			return readInStream(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据图片Url加载成Bitmap
	 * 
	 * @param url
	 * @return 出错返回NULL
	 */
//	public static String loadImageFromUrl(final String url) {
//		if(!Utils2.hasSDcardMounted()){
//			return null;
//		}
//		if(url==null||"".equals(url))
//			return null;
//		final String filename = MD5.md5(url);
//		Log.d("TAG", "-----------要保存的文件名称---------------" +"MD5=="  + filename + "源文件名==" + url);
//		File f = new File(Utils2.getFilePath(filename));
//		try {
//			if (f.exists()) {
//				return Utils2.getFilePath(filename);
//			}
//
////			final Bitmap bitmap = BitmapFactory.decodeStream(stream);//如果采用这种解码方式在低版本的API上会出现解码问题
//			new Thread() {
//				public void run() {
//					InputStream stream;
//					try {
//						stream = Request.getResource(url);
//						byte[] bt=getBytes(stream); //注释部分换用另外一种方式解码
//						Bitmap bitmap = BitmapFactory.decodeByteArray(bt,0,bt.length);
//						if(bitmap != null)
//							saveBitmap(bitmap, filename);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//			}.start();
//			return url;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}
	
/*	*//**
	 * 保存Bitmap
	 *
	 *//*
	public static boolean saveBitmap(Bitmap bitmap, String imageurl) {
		String filename = Utils2.getFileName(imageurl);
		if (Utils2.sdcard() != null) {
			File file = new File(Utils2.getCachePath() + filename + "_tmp");
			try {
				FileOutputStream out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
				return file.renameTo(new File(Utils2.getCachePath() + filename));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}*/

	public static String readInStream(FileInputStream is) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}
			outStream.close();
			is.close();
			return outStream.toString("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 已经确定从网络获取的数据流没有出现问题，而是在图片解码时出现错误。上网查找了不少资料，也没有得出确切的原因，不过有几条意见值得关注。
	 * 一种说法是在android 较低版本的api中会有不少内部的错误，我的代码运行时选择2.1API Level 7和2.2API Level 8都会出现这个问题，
	 * 而选择2.3 API Level 9后能够正常解码图片。
	 * 我的另外一种做法是换用别的解码方式对图片解码，见代码中被注释的那俩行，使用decodeByteArray（）方法在低版本的API上也能够正常解码，解决了这个问题。
	 * 其中getBytes(InputStream is)是将InputStream对象转换为Byte[]的方法，具体代码如下：
	 * @param is
	 * @return
	 * @throws IOException
	 */
	static byte[] getBytes(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len ;

		while ((len = is.read(b, 0, 1024)) != -1) {
			baos.write(b, 0, len);
			baos.flush();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}
	
	/**
	 * Drawable → Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	private long mTimeDiff =  5 * 60 * 60 * 1000L;
	
	/**
	 * Bitmap → byte[] 
	 * @param bm
	 * @return
	 */
	private static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// compress 是用于压缩图片的api接口，可以将图片压缩成jpg,png等格式。
		// 第一个参数是压缩成图片的格式
		// 第二个参数是压缩图片的质量，对于png格式，此参数可以忽略
		// 第三个参数是图片压缩成的输出流
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	/**
	 * byte[] → Bitmap
	 * @param b
	 * @return
	 */
	private Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
	
	/**
	 * 获取汉字的首字母(例如：黑玉龙-->hyl)
	 * @param characters
	 * @return
	 */
	public static String getSpells(String characters) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < characters.length(); i++) {
			char ch = characters.charAt(i);
			if ((ch >> 7) == 0) {
				// 判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
			} else {
				char spell = getFirstLetter(ch);
				buffer.append(String.valueOf(spell));
			}
		}
		return buffer.toString();
	}
	
	// 获取一个汉字的首字母
	public static Character getFirstLetter(char ch) {
		byte[] uniCode;
		try {
			uniCode = String.valueOf(ch).getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
			return null;
		} else {
			return convert(uniCode);
		}
	}
	
	static final int GB_SP_DIFF = 160;
	// 存放国标一级汉字不同读音的起始区位码
	static final int[] secPosValueList = { 1601, 1637, 1833, 2078, 2274, 2302,
			2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
			4086, 4390, 4558, 4684, 4925, 5249, 5600 };
	// 存放国标一级汉字不同读音的起始区位码对应读音
	static final char[] firstLetter = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x',
			'y', 'z' };
	private static int FREE_SD_SPACE_NEEDED_TO_CACHE = 0;

	/**
	 * 
	 * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
	 * 
	 * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
	 * 
	 * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
	 */
	static char convert(byte[] bytes) {
		char result = '-';
		int secPosValue;
		int i;
		for (i = 0; i < bytes.length; i++) {
			bytes[i] -= GB_SP_DIFF;
		}
		secPosValue = bytes[0] * 100 + bytes[1];
		for (i = 0; i < 23; i++) {
			if (secPosValue >= secPosValueList[i]
					&& secPosValue < secPosValueList[i + 1]) {
				result = firstLetter[i];
				break;
			}
		}
		return result;

	}
	
	/** 
	 * 计算sdcard上的剩余空间 
	 * @return 
	 */ 
	private int freeSpaceOnSd() { 
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory() .getPath()); 
	    double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / 1024; 
	    return (int) sdFreeMB; 
	} 
	
	/** 
	 * 修改文件的最后修改时间 
	 * @param dir 
	 * @param fileName 
	 */ 
	private void updateFileTime(String dir,String fileName) { 
	    File file = new File(dir,fileName);        
	    long newModifiedTime =System.currentTimeMillis(); 
	    file.setLastModified(newModifiedTime); 
	} 
	
	/** 
	 *计算存储目录下的文件大小，当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定 
	 * 那么删除40%最近没有被使用的文件 
	 * @param dirPath
	 */ 
	private void removeCache(String dirPath) { 
	    File dir = new File(dirPath); 
	    File[] files = dir.listFiles(); 
	    if (files == null) { 
	        return; 
	    } 
	    int dirSize = 0; 
		for (int i = 0; i < files.length; i++) {
			dirSize += files[i].length();
		} 
	    int CACHE_SIZE = 256;
		if (dirSize > CACHE_SIZE  * 1024 ||FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) { 
	        int removeFactor = (int) ((0.4 *files.length) + 1); 
	        Log.i("TAG", "Clear some expiredcache files "); 
			for (int i = 0; i < removeFactor; i++) {
				files[i].delete();
			}
	 
	    } 
	 
	} 
	/** 
	 * 删除过期文件 
	 * @param dirPath 
	 * @param filename 
	 */ 
	private void removeExpiredCache(String dirPath, String filename) { 
	 
	    File file = new File(dirPath,filename); 
	 
	    if (System.currentTimeMillis()-file.lastModified() > mTimeDiff) { 
	 
	        Log.i("TAG", "Clear some expiredcache files "); 
	 
	        file.delete(); 
	 
	    } 
	 
	}



    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread ;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
//                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

	public static void copyFile(InputStream oldFileStream, String newPath) {
		try {
			int bytesum = 0;
			int byteread ;
			if (oldFileStream != null && oldFileStream.available() > 0){
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ( (byteread = oldFileStream.read(buffer)) != -1) {
					bytesum += byteread; //字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
				fs.close();
				oldFileStream.close();
			}
		}
		catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

    public static int copy(String fromFile, String toFile)
    {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if(!root.exists())
        {
            return -1;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();

        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if(!targetDir.exists())
        {
            targetDir.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for(int i= 0;i<currentFiles.length;i++)
        {
            if(currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

            }else//如果当前项为文件则进行文件拷贝
            {
                CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
        }
        return 0;
    }

    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public static int CopySdcardFile(String fromFile, String toFile)
    {

        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex)
        {
            return -1;
        }
    }


	/**
	 * 在SD卡上创建文件
	 *
	 * @param fileName
	 * @return
	 */
	public File createSDFile(String fileName) {
//        File file=new File(SDPath+fileName);
		File file = new File(fileName);
		try {
			//目标目录
			if (fileName.contains("/")) {
				String dir = fileName.substring(0, fileName.lastIndexOf("/"));
				File targetDir = new File(dir);
				//创建目录
				if (!targetDir.exists()) {
					targetDir.mkdirs();
				}
			}

			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 上报下载进度的接口
	 *
	 * @author shenxy
	 */
	public static interface DownloadProgressListener {
		void transferred(long num);
	}

	/**
	 * 将一个inputStream里面的数据写到SD卡中
	 *
	 * @param path
	 * @param fileName
	 * @param inputStream
	 * @return
	 */
	public File writeToSDfromInput(String path, String fileName, InputStream inputStream, DownloadProgressListener processListener) {
		//createSDDir(path);
		File file = createSDFile(path + fileName);
		OutputStream outStream = null;
		long totalDownloadSize = 0;

		try {
			outStream = new FileOutputStream(file);
//            byte[] buffer=new byte[4*1024];
			byte[] buffer = new byte[1024]; //shenxy 减小buffer
			int temp;
			int num=0;
			while ((temp = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, temp);
				//shenxy 上报进度
				totalDownloadSize = totalDownloadSize + temp;
				if (num%10==0){//10次 报告一次进度
					processListener.transferred(totalDownloadSize);
				}
				num++;
			}
			outStream.flush();
		} catch (Exception e) {
			e.printStackTrace();

			return null;//发生异常  返回null 表示下载失败

		} finally {

			try {
				if (outStream != null) {
					outStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static int getImageSize(String imageSourcePath) {
		if (TextUtils.isEmpty(imageSourcePath)) {
			return 0;
		}

		try {
			File imageFile = new File(imageSourcePath);
			if (imageFile.exists()) {
				FileInputStream fis = null;
				fis = new FileInputStream(imageFile);
				int fileSize = fis.available();
				fis.close();

				return fileSize;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
