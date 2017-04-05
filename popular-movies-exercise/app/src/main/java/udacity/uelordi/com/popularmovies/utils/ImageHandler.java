package udacity.uelordi.com.popularmovies.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by uelordi on 05/04/2017.
 */

public class ImageHandler {
    private static String DEFAULT_FOLDER = "Picasso";
    private static ImageHandler m_instance;

    public ImageHandler() {
    }

    public static ImageHandler getInstance() {
        if (m_instance == null) {
            m_instance = new ImageHandler();
        }
        return m_instance;
    }


    public boolean saveFile(Context context, ImageView iv, String filename)  {
        OutputStream fOut = null;
        Uri outputFileUri;

        File root = new File(Environment.getExternalStorageDirectory() + File.separator + DEFAULT_FOLDER);
        root.mkdirs();
        File sdImageMainDirectory = new File(root, filename);
        try {
            fOut = new FileOutputStream(sdImageMainDirectory);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        iv.setDrawingCacheEnabled(true);
        iv.buildDrawingCache(true);
        Bitmap bitmap = iv.getDrawingCache();
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getImagePath(Context context,String filename){
        return Environment.getExternalStorageDirectory() + File.separator + DEFAULT_FOLDER+filename;

    }
}
