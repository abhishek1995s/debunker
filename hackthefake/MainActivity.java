package com.hackthefake.oui.hackthefake;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity {
Button btnCpy,btnCheck,btnClear;
    String news=" ",url1,val1,res2,pd;
    ImageView tick,cross;
    EditText etNews;
    CharSequence textToPaste;
    NotificationManager manager;
    int flag=0;
    Notification myNotication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        tick=(ImageView)findViewById(R.id.imgTick);
        cross=(ImageView)findViewById(R.id.imgCross);
        etNews=(EditText)findViewById(R.id.txtnews);
        btnCheck=(Button)findViewById(R.id.btnCheck);
        btnCpy=(Button)findViewById(R.id.btnCpy);
        btnClear=(Button)findViewById(R.id.btnClear);

        tick.setVisibility(GONE);
        cross.setVisibility(GONE);

        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etNews.setText("");
                tick.setVisibility(GONE);
                cross.setVisibility(GONE);
                flag=0;

            }
        });

        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener( new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                String a = clipboard.getText().toString();
                etNews.setText(a);
                news=a;
                //networkCheck();
                Toast.makeText(getBaseContext(),"Copy:\n"+a,Toast.LENGTH_LONG).show();

                //Intent intent = new Intent("com.rj.notitfications.SECACTIVITY");


                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClass(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

                Notification.Builder builder = new Notification.Builder(MainActivity.this);

                builder.setAutoCancel(false);
                builder.setTicker("this is ticker text");
                builder.setContentTitle("Debunker");
                builder.setContentText("You have copied something");
                builder.setSmallIcon(R.drawable.icon1);
                builder.setContentIntent(pendingIntent);
                builder.setOngoing(true);
                builder.setSubText("This is subtext...");   //API level 16
                builder.setNumber(100);
                builder.build();

                builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);


                myNotication = builder.getNotification();
               if(URLUtil.isValidUrl(a))
                    manager.notify(11, myNotication);





                /*try{
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new Notification(R.drawable.city, "Notification Test", System.currentTimeMillis());
                    Context context = getApplicationContext();
                    CharSequence contentTitle = "asdf TITLE asdf";
                    CharSequence contentText = "blah blah";
                    Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);

                    notification.flags |= Notification.FLAG_SHOW_LIGHTS;

                    //auto cancel after select
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent, 0);
                    notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
                    mNotificationManager.notify(1, notification);
                    //flag = false; //reset you flag
                }catch(Exception e){}*/

            }
        });

        btnCpy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    try {
                        textToPaste = clipboard.getText();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    if (clipboard.getPrimaryClip() != null) {
                        android.content.ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                        textToPaste = item.getText();
                    }
                }

// insert the text at the cursor position, or if there is a selection it
// replaces the selection with the text to paste
                if (textToPaste != null) {
                    etNews.setText(textToPaste.toString());

                   /* int start = Math.max(etNews.getSelectionStart(), 0);
                    int end = Math.max(etNews.getSelectionEnd(), 0);
                    etNews.getText().replace(Math.min(start, end), Math.max(start, end),
                            textToPaste, 0, textToPaste.length());*/
                }
            }
        });

        btnCheck.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                news=etNews.getText().toString();
                news=news.replace("http://","").replace("http:// www.","").replace("www.","");
                networkCheck();

            }
        });

    }






    public class task extends AsyncTask<String, String, Void> {
        InputStream is1 = null;
        String paid_result = "";
        String qod_result = "";


        protected void onPreExecute() {
          /*  progressDialog = new ProgressDialog(ProfileEditActivity.this);
            progressDialog.setTitle("This is a One Time Process");
            progressDialog.setMessage("Fetching Data From Server...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(1800);
            progressDialog.show();
            progressDialog.setCancelable(false);*/

        }

        @Override
        protected Void doInBackground(String... params) {

            //String paid_url_select = "http://gokera.phacsin.com/signup_temp.php?news="+news+"&uname="+name+"&uemail="+mailid+"&upswd="+pswd+"&country="+country;//"&phno="+phno
            //String paid_url_select = "http://sctjobfair.in:8000/url?name="+news;
            String paid_url_select="https://infinite-fortress-24638.herokuapp.com/web/"+news;
            try {
                paid_result = getJSONfromURL(paid_url_select);
                pd=paid_result;
              /*  if (paid_result != null)
                    progressDialog.setProgress(100);*/
                if (paid_result == null) {
                    //Toast.makeText(getApplicationContext(), "Result is null", Toast.LENGTH_LONG).show();
                }
                try {

                    JSONArray jr = new JSONArray(paid_result);
                    //for (int i = 0; i < jr.length(); i++) {
                        JSONObject obj = jr.getJSONObject(Integer.parseInt(Integer.toString(0)));
                        url1 = obj.getString("url");
                        val1=obj.getString("value");
                    //}
                }
            catch (Exception e){}
               // Toast.makeText(getApplicationContext(), paid_result,Toast.LENGTH_LONG).show();
                if(paid_result.equals("T"))
                {
                    flag=1;
                }
                else if(paid_result.equals("F")){
                    flag=2;
                }
                    //result=true;

               /* JSONArray array1 = new JSONArray(paid_result);

                for (int i = 0; i < array1.length(); i++) {
                    JSONObject obj = array1.getJSONObject(Integer.parseInt(Integer.toString(i)));
                   // result=obj.toString();
                }

*/
                //progressDialog.setProgress(1800);

          /*  } catch (JSONException e1) {
                Log.d("log_tag", e1.toString());

                e1.printStackTrace();*/
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            // dbQod.close();
            return null;


        }


        protected void onPostExecute(Void v) {
          /*  if (progressDialog != null)
                progressDialog.dismiss();*/
            //  Toast.makeText(getApplicationContext(), paid_result,Toast.LENGTH_LONG).show();
           // if(result)

               /* {Toast.makeText(getApplicationContext(),"Signed up successfully\n Welcome to BusQuiz..!",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Please login..",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
               // overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);}*/
           // Toast.makeText(getApplicationContext(),"paid result="+pd,Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"res="+res2,Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), paid_result,Toast.LENGTH_LONG).show();
           // Toast.makeText(getApplicationContext(),"flag="+flag+"\nvalue="+val1,Toast.LENGTH_LONG).show();
            etNews.setText(url1);
            if(flag==1) {
                tick.setVisibility(VISIBLE);
                if (cross.getVisibility()!=GONE)
                cross.setVisibility(GONE);
            }
            else if (flag==2) {
                cross.setVisibility(VISIBLE);
                if (tick.getVisibility()!=GONE)
                tick.setVisibility(GONE);
            }

            //flag=0;
        }


        public String getJSONfromURL(String URL) {
            String res = "";
            //String paid_url_select = "http://sctjobfair.in:8000/url?name="+news;
            /*HttpClient httpClient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000);
            HttpPost httpPost = new HttpPost(URL);
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();


                //read content
                is1 = httpEntity.getContent();
            } catch (Exception e) {

                Log.e("log_tag", "Error in http connection " + e.toString());
            }
*/

           // String POST_PARAMS = "param1=" + params[0] + "&param2=" + params[1];
            URL obj = null;
            HttpURLConnection con = null;
            try {
                obj = new URL(URL);
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");

                // For POST only - BEGIN
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(URL.getBytes());
                os.flush();
                os.close();
                // For POST only - END




              /*  con.setRequestProperty("Content-length", "0");
                con.setUseCaches(false);
                con.setAllowUserInteraction(false);
               con.setConnectTimeout(100000);
                con.setReadTimeout(100000);

                con.connect();*/




                int responseCode = con.getResponseCode();
                //Log.i(TAG, "POST Response Code :: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    res=response.toString();
                    in.close();

                    // print result
                    Log.i("TAG", response.toString());
                  //  Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                } else {
                    Log.i("TAG", "POST request did not work.");
                   // Toast.makeText(getApplicationContext(),"POST request did not work.",Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is1));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is1.close();
                //res = sb.toString();


            } catch (IOException e) {
                Log.d("log_tag", "IOException " + e.toString());
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error converting result " + e.toString());
            }
            is1 = null;

            res2=res;
            return res;
        }
    }


    public void networkCheck()
    {
        if(false){//!Network_Available()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("No Network Connectivity");
            alertDialog.setMessage("Turn ON Wifi or Mobile Data");
            alertDialog.show();
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()

                    {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }

            ).show().setCancelable(false);
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()

                    {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    }

            ).show().setCancelable(false);

        }
        else {
            new NetCheck().execute();
        }
    }

    private boolean Network_Available() {
        try {
            ConnectivityManager nInfo = (ConnectivityManager) getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            nInfo.getActiveNetworkInfo().isConnectedOrConnecting();
            Log.d("log_tag", "Net avail:"
                    + nInfo.getActiveNetworkInfo().isConnectedOrConnecting());
            // Toast.makeText(getApplicationContext(), String.valueOf(nInfo.getActiveNetworkInfo().isConnectedOrConnecting()), Toast.LENGTH_SHORT).show();
            ConnectivityManager cm = (ConnectivityManager) getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                Log.d("log_tag", "Network available:true");
                //Toast.makeText(getApplicationContext(), "Network available", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Log.d("log_tag", "Network available:false");
               // Toast.makeText(getApplicationContext(), "Network available", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public class NetCheck extends AsyncTask<String,String,Boolean>
    {
        ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            /*nDialog = new ProgressDialog(MainActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();*/
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
         **/
        @Override
        protected Boolean doInBackground(String... args){



            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");//("http://www.entreprenia15.com/quiz/select_paid.php");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    //Toast.makeText(getApplicationContext(),urlc.getResponseCode() , Toast.LENGTH_SHORT).show();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){


            if(th == true){
                //nDialog.dismiss();
                new task().execute();
            }
            else{
               // nDialog.dismiss();
                new task().execute();
              /*Toast toast = Toast.makeText(getApplicationContext(),
                        "Error in Network Connection", Toast.LENGTH_SHORT);
                toast.show();*/

                /*  toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 700);
                toast.show();*/
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }*/
    }





}
