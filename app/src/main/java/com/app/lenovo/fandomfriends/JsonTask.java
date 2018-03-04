package com.app.lenovo.fandomfriends;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by qawbecrdteyf on 3/3/18.
 */


public class JsonTask extends AsyncTask<String, String, String> {
    Context ctx;

    JsonTask(Context ctx){

        this.ctx = ctx;
    }
    protected void onPreExecute() {
        super.onPreExecute();

        /*pd = new ProgressDialog(this);
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();*/
    }

    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Responsehopeful: ", "> " + line);   //here u ll get whole response...... :-)

            }

            return buffer.toString();


        } catch (MalformedURLException e) {
            Log.e("MalformedURL", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e("IOException2", e.getMessage());
            }
        }
        return null;

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        /*if (pd.isShowing()){
            pd.dismiss();
        }*/
        //txtJson.setText(result);

        String filename = "profiles.json";

        //File file = new File(this.getDir(), filename);



        //String fileContents = "Hello world!";
        FileOutputStream outputStream;

        try {
            //JsonTask ctx = this;
            outputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(result.getBytes());
            outputStream.close();
            Log.d("Hopefully", "JSON!!");
//            String yourFilePath = ctx.getFilesDir() + "/" + "profiles.json";

            FileInputStream fis = ctx.openFileInput("profiles.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            Log.d("please", String.valueOf(sb));

        } catch (Exception e) {

            Log.e("Error here",e.getMessage());
        }

    }
}
