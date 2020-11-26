/*
*
* Author: David Ochoa Gtz
* @TheRadikalStyle
* 2020
*
*/

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.List;

public class Tools {
    private static Tools INSTANCE = null;
    private int counter = 0;
    private boolean isEEggAlreadyShow = false;

    private Tools(){}

    public static Tools getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Tools();
        }
        return(INSTANCE);
    }

    public void releaseInstance(){
        INSTANCE = null;
    }


    public void ChangeStatusBarColor(Activity context, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(context.getResources().getColor(color));
        }
    }

    public Bitmap GetBitMapCompresed(Context context, String resIDPath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        return BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(resIDPath), options);
    }

    public boolean isIntentSafe(Context context, Intent intent){
        PackageManager packageManager = context.getPackageManager();
        List activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return activities.size() > 0;
    }

    public boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public Bitmap DecodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public String MonthConverter(int month){
        String Month;
        switch (month){
            case 1: Month = "enero"; break;
            case 2: Month = "febrero"; break;
            case 3: Month = "marzo"; break;
            case 4: Month = "abril"; break;
            case 5: Month = "mayo"; break;
            case 6: Month = "junio"; break;
            case 7: Month = "julio"; break;
            case 8: Month = "agosto"; break;
            case 9: Month = "septiembre"; break;
            case 10: Month = "octubre"; break;
            case 11: Month = "noviembre"; break;
            case 12: Month = "diciembre"; break;
            default: Month = "ERROR";
        }
        return Month;
    }

    public boolean isDeviceOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public String EncryptToMD5(String pwd){
        try{
            //CREATE MD5 HASH
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(pwd.getBytes());
            byte messageDigest[] =  digest.digest();

            //CREATE HEX String
            StringBuilder hexString = new StringBuilder();
            for(byte aMessageDigest : messageDigest){
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        }catch(NoSuchAlgorithmException ex){
            return  pwd;
        }
    }

    public int CheckPlayServices(Context context){
    GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
    return googleApiAvailability.isGooglePlayServicesAvailable(context);

    /*if(tools.CheckPlayServices(this) != ConnectionResult.SUCCESS){
        GoogleApiAvailability.getInstance().getErrorDialog(this, ConnectionResult.SERVICE_MISSING, 0, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        }).show();
    }else{} */
  }

 public void SetEasterEgg(Context ctx, TextView versionTXV){
        //Get App Version
        try {
            PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            String version = pInfo.versionName;
            versionTXV.setText("v."+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versionTXV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                if(counter >= 7){
                    if(!isEEggAlreadyShow){
                        Calendar cal = Calendar.getInstance();
                        int hour = cal.get(Calendar.HOUR);
                        int minute = cal.get(Calendar.MINUTE);
                        if(hour == 3 && minute == 0){
                            Toast.makeText(ctx, "Dev by: David Ochoa", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ctx, "" + ("\ud83d\ude03"), Toast.LENGTH_SHORT).show(); //Show :D emoji
                        }
                        isEEggAlreadyShow = true;
                    }
                    counter = 0;
                }
            }
        });
    }

    public String GetAppVersion(){
      PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
      return pInfo.versionName;
    }

    public void SetSecurity(Activity context){
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    public String GetTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public String GetDeviceName(){
        return Build.MODEL + " ("+ Build.MANUFACTURER +")";
    }
}
