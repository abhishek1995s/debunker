package com.hackthefake2.oui.hackthefake2;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
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
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;
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
import java.util.Random;

import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

import static android.view.View.*;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;

    Button btnCpy,btnCheck,btnClear;
    ImageButton Iclr;
    String news=" ",url1,val1,res2,pd,pres=" ",wot,cdat,loc,case1[]={"","Not Available","Not Available","Not Available"},n2[];
    ImageView tick,cross,excla;
    EditText etNews;
    CharSequence textToPaste;
    NotificationManager manager;
    int flag=0;
    Notification myNotication;
    boolean urlFlag=false;
    ProgressBar mProgressBar;
    CardView c1,c2;
    TextView infotext;
     Animation fad,fado;
    int tap=0;
    InputMethodManager mgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-9748470020568912~9679446806");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        fad = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fado = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
      /*  getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Debunker");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setLogo(R.drawable.icon1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);*/




        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        c1=(CardView)findViewById(R.id.card_view);
        c2=(CardView)findViewById(R.id.card_view2);
        Iclr=(ImageButton)findViewById(R.id.btnClr) ;
        tick=(ImageView)findViewById(R.id.imgTick);
        cross=(ImageView)findViewById(R.id.imgCross);
        excla=(ImageView)findViewById(R.id.imgExcla);
        etNews=(EditText)findViewById(R.id.txtnews);
        btnCheck=(Button)findViewById(R.id.btnCheck);
        btnCpy=(Button)findViewById(R.id.btnCpy);
        btnClear=(Button)findViewById(R.id.btnClear);
        infotext=(TextView)findViewById(R.id.info_text);



       // c1.startAnimation(fado);
        c1.setVisibility(GONE);


       etNews.setScroller(new Scroller(this));
        etNews.setMaxLines(2);
        etNews.setVerticalScrollBarEnabled(true);
        etNews.setMovementMethod(new ScrollingMovementMethod());

        etNews.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String t=etNews.getText().toString();
                if(!t.equals("") && !t.equals(" ") ) {
                    ClipboardManager cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("debunker_text", t);
                    cb.setPrimaryClip(clip);
                    Toast.makeText(getBaseContext(), "text copied to clipboard", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });



        infotext.setTextIsSelectable(true);
        infotext.setScroller(new Scroller(this));
        infotext.setMaxLines(6);
        infotext.setVerticalScrollBarEnabled(true);
        infotext.setMovementMethod(new ScrollingMovementMethod());


        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        mProgressBar.setIndeterminateDrawable(new CircularProgressDrawable
                .Builder(this)
                .colors(getResources().getIntArray(R.array.rainbow))
                .sweepSpeed(1f)
                .strokeWidth(8f)
                .style(CircularProgressDrawable.Style.ROUNDED)
                .build()
        );
        mProgressBar.setVisibility(GONE);
        excla.setVisibility(GONE);
        tick.setVisibility(GONE);
        cross.setVisibility(GONE);
        Iclr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etNews.setText("");
                tick.setVisibility(GONE);
                cross.setVisibility(GONE);
                excla.setVisibility(GONE);
                flag=0;
                infotext.setText("");
                if(c1.getVisibility()==VISIBLE) {
                    c1.startAnimation(fado);
                    c1.setVisibility(GONE);
                }
                if(c2.getVisibility()==VISIBLE) {
                    c2.startAnimation(fado);
                    c2.setVisibility(GONE);
                }

            }
        });
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener( new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                String a = clipboard.getText().toString();
                //a=a.replace("http://","").replace("http:// www.","").replace("www.","");


                etNews.setText("");
                tick.setVisibility(GONE);
                cross.setVisibility(GONE);
                excla.setVisibility(GONE);
                flag=0;
                infotext.setText("");
                //c1.startAnimation(fado);
                c1.setVisibility(GONE);


                etNews.setText(a);
                news=a;
               // goCheck(a);
                //networkCheck();
                //Toast.makeText(getBaseContext(),"Copy:\n"+a,Toast.LENGTH_LONG).show();

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
                builder.setSmallIcon(R.drawable.icon_small);
                builder.setContentIntent(pendingIntent);
                builder.setOngoing(false);
                builder.setAutoCancel(true);
                builder.setSubText("Validate with Debunker..");   //API level 16
                builder.setNumber(100);
                builder.build();

                builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.FLAG_AUTO_CANCEL);

              //  Toast.makeText(getBaseContext(),"Copy:\n"+a+URLUtil.isValidUrl(a),Toast.LENGTH_LONG).show();
                myNotication = builder.getNotification();
               //if(URLUtil.isValidUrl(a))
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
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
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

                mgr.hideSoftInputFromWindow(etNews.getWindowToken(), 0);
                if(news.equals("")){
                    Toast.makeText(getBaseContext(),"Enter text or URL",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(c2.getVisibility()!=GONE)
                    {
                        c2.startAnimation(fado);
                    c2.setVisibility(GONE);
                    }
                    etNews.clearFocus();
                    goCheck(news);
                }
            }
        });

      /*  final RippleView rippleView = (RippleView) findViewById(R.id.more);
        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: onRippleViewClick
                news=etNews.getText().toString();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(etNews.getWindowToken(), 0);
                if(news.equals("")){
                    Toast.makeText(getBaseContext(),"Enter text or URL",Toast.LENGTH_SHORT).show();
                }
                else{
                    goCheck(news);
                }
            }
        });*/
        if(!etNews.getText().toString().equals(""))
        {
            goCheck(news);
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       /* if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_guide) {
            mgr.hideSoftInputFromWindow(etNews.getWindowToken(), 0);
            c2.setVisibility(VISIBLE);
            c2.startAnimation(fad);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        tap++;
        if(tap>1)
        {
        /* android.os.Process.killProcess(android.os.Process.myPid());
           // System.exit(1);
            super.onDestroy();*/
            moveTaskToBack(true);
        }
        else
            Toast.makeText(getApplicationContext(), "Tap back again to exit",Toast.LENGTH_SHORT).show();
    }
public void goCheck(String txt)
{
    if(URLUtil.isValidUrl(txt) || txt.contains("www.")|| txt.contains("http") || txt.contains("https") || txt.contains(".in") || txt.contains(".com") || txt.contains(".org")|| txt.contains(".net") ){
        txt=txt.replace("http://","").replace("https://","").replace("http:// www.","").replace("www.","");
        news=txt;

        n2=news.split("/");
        news=n2[0];
       // Toast.makeText(getApplicationContext(), news,Toast.LENGTH_SHORT).show();

        urlFlag=true;
        networkCheck();
    }
    else{
        news=txt;
        urlFlag=false;
        networkCheck();
    }

}


    public class task extends AsyncTask<String, String, Void> {
        InputStream is1 = null;
        String paid_result = "";
        String qod_result = "";


        protected void onPreExecute() {
            mProgressBar.setVisibility(VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {

            //String paid_url_select = "http://gokera.phacsin.com/signup_temp.php?news="+news+"&uname="+name+"&uemail="+mailid+"&upswd="+pswd+"&country="+country;//"&phno="+phno
            //String paid_url_select = "http://sctjobfair.in:8000/url?name="+news;

            String paid_url_select;
            if (urlFlag) {
                news = news.replace("http://", "").replace("http://www.", "").replace("www.", "");
               // paid_url_select = "https://infinite-fortress-24638.herokuapp.com/web/" + news;
                paid_url_select = "https://stormy-thicket-40639.herokuapp.com/web/" + news;
                try {
                    paid_result = getJSONfromURL(paid_url_select);
                    pd = paid_result;
                    case1=paid_result.split(":::");
                    wot=case1[1];
                    cdat=case1[2];
                    loc=case1[3];
              /*  if (paid_result != null)
                    progressDialog.setProgress(100);*/
                    if (paid_result == null || paid_result.equals(":::::::::")) {
                       flag=8;
                    }

                    // Toast.makeText(getApplicationContext(), paid_result,Toast.LENGTH_LONG).show();
                    /*if (paid_result.equals("T")) {
                        flag = 4;
                    } else if (paid_result.equals("F")) {
                        flag = 5;
                    }*/
                    if(paid_result.equals("F:::::::::"))
                    {
                        flag = 6;
                    }
                    else if(paid_result.equals("T:::::::::"))
                    {
                        flag = 7;
                    }
                    if (case1[0].equals("T")) {
                        flag = 4;
                    } else if (case1[0].equals("F")) {
                        flag = 5;
                    }

                } catch (Exception e1) {
                   // Toast.makeText(getApplicationContext(), "Sorry, No results available ", Toast.LENGTH_LONG).show();
                   // e1.printStackTrace();
                    flag=8;
                }
            } else {
                news = news.replace(" ", "%20");
                news = news.replace("\n", "%20");
                paid_url_select = "https://shrouded-dawn-53400.herokuapp.com/clickbaits/" + news;

                try {
                    paid_result = getJSONfromURL(paid_url_select);
                    pd = paid_result;
              /*  if (paid_result != null)
                    progressDialog.setProgress(100);*/
                    if (paid_result == null) {
                        //Toast.makeText(getApplicationContext(), "Result is null", Toast.LENGTH_LONG).show();
                    }

                    // Toast.makeText(getApplicationContext(), paid_result,Toast.LENGTH_LONG).show();
                    float per = Float.parseFloat(paid_result);
                    if (per > 0.70) {
                        flag =3;
                    } else {
                        news = news.replace(" ", "%20");
                        news = news.replace("\n", "%20");
                        paid_url_select = "https://radiant-tor-32290.herokuapp.com/text/" + news;
                        try {
                            paid_result = getJSONfromURL(paid_url_select);
                            String[] parts = paid_result.split(":::");
                            //pres=parts[0];
                            if (parts[0].equals("T")) {
                                flag = 1;
                            } else if (parts[0].equals("F")) {
                                flag = 2;
                                pres = parts[1];
                            }
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }
                    }

                } catch (Exception e1) {

                    e1.printStackTrace();
                }
            }

            // dbQod.close();
            return null;


        }


        protected void onPostExecute(Void v) {
            mProgressBar.setVisibility(GONE);
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
           //Toast.makeText(getApplicationContext(), paid_result, Toast.LENGTH_LONG).show();
           // Toast.makeText(getApplicationContext(), "prss: " + pres, Toast.LENGTH_LONG).show();
            // etNews.setText(url1);
            //Toast.makeText(getApplicationContext(), "Here is the Result!", Toast.LENGTH_SHORT).show();
            if (flag == 1) {
                Cappear();
                tick.setVisibility(VISIBLE);
              pres="Potentially safe to believe.";
                infotext.setText(pres);
                if (cross.getVisibility() != GONE)
                    cross.setVisibility(GONE);
                if (excla.getVisibility() != GONE)
                    excla.setVisibility(GONE);
            } else if (flag == 2) {
                Cappear();
                cross.setVisibility(VISIBLE);
                String t = "\tThis has been reported fake in the following site:\n";
                infotext.setText(t + pres);
                Linkify.addLinks(infotext, Linkify.WEB_URLS);
                if (tick.getVisibility() != GONE)
                    tick.setVisibility(GONE);
                if (excla.getVisibility() != GONE)
                    excla.setVisibility(GONE);
            }
            else if(flag==3){
                Cappear();
                cross.setVisibility(VISIBLE);
                pres = "This is a Clickbait- a site with fake info to attract users.\n It may have malicious content";
                infotext.setText(pres);
                if (tick.getVisibility() != GONE)
                    tick.setVisibility(GONE);
                if (excla.getVisibility() != GONE)
                    excla.setVisibility(GONE);
            }
            else if(flag==4){
                Cappear();
                tick.setVisibility(VISIBLE);

                pres = "Trustworthiness :   "+wot+"\n Creation date :   "+cdat+"\n Location :   "+loc;
                infotext.setText(pres);

                if (cross.getVisibility() != GONE)
                    cross.setVisibility(GONE);
                if (excla.getVisibility() != GONE)
                    excla.setVisibility(GONE);
            }
            else if(flag==5){
                Cappear();
                cross.setVisibility(VISIBLE);
               // pres = "This is a potentially unsafe site.";
                pres = "Trustworthiness :   "+wot+"\n Creation date :   "+cdat+"\n Location :   "+loc;
                infotext.setText(pres);
                if (tick.getVisibility() != GONE)
                    tick.setVisibility(GONE);
                if (excla.getVisibility() != GONE)
                    excla.setVisibility(GONE);
            }

            else if(flag==6){
                Cappear();
                cross.setVisibility(VISIBLE);
                pres = "This is a potentially unsafe site.";
               // pres = "Trustworthiness :   "+"Not Available"+"\n Creation date :   "+"Not Available"+"\n Location :   "+"Not Available";
                infotext.setText(pres);
                if (tick.getVisibility() != GONE)
                    tick.setVisibility(GONE);
                if (excla.getVisibility() != GONE)
                    excla.setVisibility(GONE);
            }
            else if(flag==7){
                Cappear();
                tick.setVisibility(VISIBLE);
                pres = "This is a potentially safe site.";
             //   pres = "Trustworthiness :   "+"Not Available"+"\n Creation date :   "+"Not Available"+"\n Location :   "+"Not Available";
                infotext.setText(pres);

                if (cross.getVisibility() != GONE)
                    cross.setVisibility(GONE);
                if (excla.getVisibility() != GONE)
                    excla.setVisibility(GONE);
            }
            else if(flag==8){
                Cappear();
                excla.setVisibility(VISIBLE);
                pres = "Oops! This looks like a hard one.\nWe are working hard to solve it, at the moment. \nPlease wait for the update :)";
                //   pres = "Trustworthiness :   "+"Not Available"+"\n Creation date :   "+"Not Available"+"\n Location :   "+"Not Available";
                infotext.setText(pres);

                if (cross.getVisibility() != GONE)
                    cross.setVisibility(GONE);
                if (tick.getVisibility() != GONE)
                    tick.setVisibility(GONE);
            }
            //flag=0;
        }
public void Cappear(){
    mgr.hideSoftInputFromWindow(etNews.getWindowToken(), 0);
    c1.setVisibility(VISIBLE);
    c1.startAnimation(fad);
}

        public String getJSONfromURL(String URL) {
            String res = "";

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
