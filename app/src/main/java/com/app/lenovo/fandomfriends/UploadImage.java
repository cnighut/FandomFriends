package com.app.lenovo.fandomfriends;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by qawbecrdteyf on 3/5/18.
 */

class UploadImage extends AsyncTask<String,Void,String> {

    Context ctx;

    UploadImage(Context ctx){

        this.ctx = ctx;
    }

    //ProgressDialog loading;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //loading = ProgressDialog.show(this,"Please wait...","uploading",false,false);
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            super.onPostExecute(s);
        }catch (NullPointerException e)
        {
            Log.e("Oops","Null here");
        }
        //loading.dismiss();
        Toast.makeText(ctx,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... params) {
        /*RequestHandler rh = new RequestHandler();
        HashMap<String,String> param = new HashMap<String,String>();
        param.put(KEY_TEXT,house);
        param.put(KEY_TEXT1,build);
        param.put(KEY_IMAGE,image);
        param.put(KEY_IMAGE1,image1);
        String result = rh.sendPostRequest(UPLOAD_URL, param);*/


        try {
                URL url;
                String reg_url = "http://almat.almafiesta.com/Kryptex5.0/upload.php";
                url = new URL(reg_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String imgstring = params[0];
                String name=params[1];
                Log.d("Huhu2",imgstring);
            String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                    URLEncoder.encode("imgstring", "UTF-8") + "=" + URLEncoder.encode(imgstring, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                httpURLConnection.disconnect();
                return "ImgUpload done!";
            } catch (MalformedURLException e) {
            Log.d("error", "error is good");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

}
