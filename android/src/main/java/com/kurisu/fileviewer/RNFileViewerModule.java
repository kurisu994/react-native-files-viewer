package com.kurisu.fileviewer;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RNFileViewerModule extends ReactContextBaseJavaModule {
    public static final int ERROR_NO_HANDLER_FOR_DATA_TYPE = 53;
    public static final int ERROR_FILE_NOT_FOUND = 2;
    public static final int ERROR_UNKNOWN_ERROR = 1;
    private final ReactApplicationContext reactContext;

    public RNFileViewerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNFileViewerModule";
    }

    @ReactMethod
    public void openDoc(ReadableMap args, Callback callback) {
        try {
            if (args.getString("url") != null && args.getString("fileName") != null && args.getString("fileType") != null) {
                // parameter parsing
                final String url = args.getString("url");
                final String fileName = args.getString("fileName");
                final String fileType = args.getString("fileType");
                final Boolean cache = args.getBoolean("cache");
                final byte[] bytesData = new byte[0];
                // Begin the Download Task
                new FileDownloaderAsyncTask(callback, url, cache, fileName, fileType, bytesData).execute();
            } else {
                callback.invoke("url/fileName/fileType can not be empty");
            }
        } catch (Exception e) {
            callback.invoke(e.getMessage());
        }
    }


    @ReactMethod
    public void openDocb64(ReadableMap args, Callback callback) {
        try {
            if (args.getString("base64") != null && args.getString("fileName") != null && args.getString("fileType") != null) {
                // parameter parsing
                final String base64 = args.getString("base64");
                final String fileName = args.getString("fileName");
                final String fileType = args.getString("fileType");
                final Boolean cache = args.getBoolean("cache");
                //Bytes
                final byte[] bytesData = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);
                // Begin the Download Task
                new FileDownloaderAsyncTask(callback, "", cache, fileName, fileType, bytesData).execute();
            } else {
                callback.invoke("base64/fileName/fileType can not be empty");
            }
        } catch (Exception e) {
            callback.invoke(e.getMessage());
        }


    }

    @ReactMethod
    public void openDocBinaryinUrl(ReadableMap args, Callback callback) {
        try {
            if (args.getString("url") != null && args.getString("fileName") != null && args.getString("fileType") != null) {
                // parameter parsing
                final String url = args.getString("url");
                final String fileName = args.getString("fileName");
                final String fileType = args.getString("fileType");
                final Boolean cache = args.getBoolean("cache");
                final byte[] bytesData = new byte[0];
                // Begin the Download Task
                new FileDownloaderAsyncTask(callback, url, cache, fileName, fileType, bytesData).execute();
            } else {
                callback.invoke("url/fileName/fileType can not be empty");
            }
        } catch (Exception e) {
            callback.invoke(e.getMessage());
        }
    }

    // used for all downloaded files, so we can find and delete them again.
    private final static String FILE_TYPE_PREFIX = "PP_";

    /**
     * downloads the file from the given url to external storage.
     *
     * @param url
     * @return
     */
    private File downloadFile(String url, String fileName, Boolean cache, String fileType, byte[] bytesData, Callback callback) {

        try {
            Context context = getReactApplicationContext().getBaseContext();
            File outputDir = context.getCacheDir();
            if (bytesData.length > 0) {
                // use cache
                File f = cache != null && cache ? new File(outputDir, fileName) : File.createTempFile(FILE_TYPE_PREFIX, "." + fileType,
                        outputDir);
                System.out.println("Bytes will be creating a file");
                final FileOutputStream fileOutputStream;
                try {
                    fileOutputStream = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }

                final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                        fileOutputStream);
                try {
                    bufferedOutputStream.write(bytesData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return f;
            } else {
                String extension = MimeTypeMap.getFileExtensionFromUrl(url);
                System.out.println("Extensions DownloadFile " + extension);
                if (extension.equals("") && fileType.equals("")) {
                    extension = "pdf";
                    System.out.println("extension (default): " + extension);
                }

                if (!fileType.equals("") && extension.equals("")) {
                    extension = fileType;
                    System.out.println("extension (default): " + extension);
                }

                // check has extension
                if (!fileName.contains("\\.")) {
                    fileName = fileName + '.' + extension;
                }
                // if use cache, check exist
                if (cache != null && cache) {
                    File existFile = new File(outputDir, fileName);
                    if (existFile.exists()) {
                        return existFile;
                    }
                }

                // get an instance of a cookie manager since it has access to our
                // auth cookie
                CookieManager cookieManager = CookieManager.getInstance();

                // get the cookie string for the site.
                String auth = null;
                if (cookieManager.getCookie(url) != null) {
                    auth = cookieManager.getCookie(url).toString();
                }

                URL url2 = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
                //GET Connection Content length
                int fileLength = conn.getContentLength();
                if (auth != null) {
                    conn.setRequestProperty("Cookie", auth);
                }

                InputStream reader = conn.getInputStream();

                // use cache
                File f = cache != null && cache ? new File(outputDir, fileName) : File.createTempFile(FILE_TYPE_PREFIX, "." + extension,
                        outputDir);

                // make sure the receiving app can read this file
                f.setReadable(true, false);
                System.out.println(f.getPath());
                FileOutputStream outStream = new FileOutputStream(f);

                byte[] buffer = new byte[4096];
                long total = 0;
                int readBytes = reader.read(buffer);
                while (readBytes > 0) {
                    total += readBytes;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        System.out.println((int) (total * 100 / fileLength));
                    outStream.write(buffer, 0, readBytes);
                    readBytes = reader.read(buffer);
                }
                reader.close();
                outStream.close();
                if (f.exists()) {
                    System.out.println("File exists");
                } else {
                    System.out.println("File doesn't exist");
                }
                return f;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            callback.invoke(ERROR_FILE_NOT_FOUND);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            callback.invoke(ERROR_UNKNOWN_ERROR);
            return null;
        }
    }

    /**
     * Returns the MIME Type of the file by looking at file name extension in
     * the URL.
     *
     * @param url
     * @return
     */
    private static String getMimeType(String url) {
        String mimeType = null;
        System.out.println("Url: " + url);
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

        if (mimeType == null) {
            mimeType = "application/pdf";
            System.out.println("Mime Type (default): " + mimeType);
        }

        return mimeType;
    }

    private class FileDownloaderAsyncTask extends AsyncTask<Void, Void, File> {
        private final Callback callback;
        private final String url;
        private final String fileName;
        private final Boolean cache;
        private final String fileType;
        private final byte[] bytesData;

        public FileDownloaderAsyncTask(Callback callback,
                                       String url, Boolean cache, String fileName, String fileType, byte[] bytesData) {
            super();
            this.callback = callback;
            this.url = url;
            this.fileName = fileName;
            this.cache = cache;
            this.fileType = fileType;
            this.bytesData = bytesData;
        }

        @Override
        protected File doInBackground(Void... arg0) {
            if (!url.startsWith("file://")) {
                if (url.startsWith("content://")) {
                    Uri uri = Uri.parse(url);
                    return new File(FileUtil.getPathFromUri(getReactApplicationContext(), uri));
                }
                System.out.println("Url to download " + url);
                return downloadFile(url, fileName, cache, fileType, bytesData, callback);
            } else {
                return new File(url.replace("file://", ""));
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                // Has already been handled
                return;
            }

            Context context = getCurrentActivity();
            String mimeType;
            // mime type of file data
            if (fileName != null && fileType != null) {
                mimeType = getMimeType(fileName + "." + fileType);
            } else {
                mimeType = getMimeType(url);
            }
            if (context == null) {
                return;
            }
            try {

                Uri contentUri = FileProvider.getUriForFile(context, reactContext.getPackageName() + ".fileViewer_provider", result);
                System.out.println("ContentUri");
                System.out.println(contentUri);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(contentUri, mimeType);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                    // Thread-safe.
                    callback.invoke(null, fileName);
                } else {
                    activityNotFoundMessage("Activity not found to handle: " + contentUri.toString() + " (" + mimeType + ")");
                }
            } catch (ActivityNotFoundException e) {
                activityNotFoundMessage(e.getMessage());
            }

        }

        private void activityNotFoundMessage(String message) {
            System.out.println("ERROR");
            System.out.println(message);
            callback.invoke(message);
            //e.printStackTrace();
        }
    }
}
