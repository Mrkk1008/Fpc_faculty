package com.fpc_faculty;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog pd=null;
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!haveNetworkConnection())
                {
                    alertView();
                }
                myWebView = findViewById(R.id.web1);
                myWebView.getSettings().setAppCacheEnabled(true);
                myWebView.getSettings().setJavaScriptEnabled(true);
                myWebView.setVerticalScrollBarEnabled(true);
                myWebView.setHorizontalScrollBarEnabled(true);
                myWebView.getSettings().setBuiltInZoomControls(true);
                myWebView.getSettings().setSupportZoom(true);
                myWebView.getSettings().setDisplayZoomControls(false);
                myWebView.setScrollContainer(true);
                myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/admin1.php");
                myWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        pd = new ProgressDialog(MainActivity.this);
                        pd.setTitle("Please Wait...");
                        pd.setMessage("Page is Loading...");
                        pd.show();
                        super.onPageStarted(view, url, favicon);
                    }
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        pd.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        myWebView.loadUrl("error.html");
                    }

                });
                myWebView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (myWebView.getScrollY() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                });
            }

        });
        if(!haveNetworkConnection())
        {
            alertView();
        }
        myWebView = findViewById(R.id.web1);
        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setVerticalScrollBarEnabled(true);
        myWebView.setHorizontalScrollBarEnabled(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setDisplayZoomControls(false);
        myWebView.setScrollContainer(true);
        myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/admin1.php");
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pd = new ProgressDialog(MainActivity.this);
                pd.setTitle("Please Wait...");
                pd.setMessage("Page is Loading...");
                pd.show();
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                myWebView.loadUrl("error.html");
            }
        });
        //Navigation Coding Start
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelected(menuItem);
                return false;
            }
        });

    }

    private boolean haveNetworkConnection(){
        ConnectivityManager cm=(ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void alertView()
    {
        AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Attention");
        alertDialog.setMessage("You are not connected to Internet!");
        alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //android.os.Process.killProcess(android.os.Process.myPid());
                // System.exit(1);
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if(myWebView.canGoBack())
        {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void UserMenuSelected(MenuItem menuItem){

        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(!haveNetworkConnection())
                        {
                            alertView();
                        }
                        myWebView = findViewById(R.id.web1);
                        myWebView.getSettings().setAppCacheEnabled(true);
                        myWebView.getSettings().setJavaScriptEnabled(true);
                        myWebView.setVerticalScrollBarEnabled(true);
                        myWebView.setHorizontalScrollBarEnabled(true);
                        myWebView.getSettings().setBuiltInZoomControls(true);
                        myWebView.getSettings().setSupportZoom(true);
                        myWebView.getSettings().setDisplayZoomControls(false);
                        myWebView.setScrollContainer(true);
                        myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/admin1.php");
                        myWebView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                pd = new ProgressDialog(MainActivity.this);
                                pd.setTitle("Please Wait...");
                                pd.setMessage("Page is Loading...");
                                pd.show();
                                super.onPageStarted(view, url, favicon);
                            }
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                pd.dismiss();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            @Override
                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                myWebView.loadUrl("error.html");
                            }

                        });
                        myWebView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                            @Override
                            public void onScrollChanged() {
                                if (myWebView.getScrollY() == 0) {
                                    swipeRefreshLayout.setEnabled(true);
                                } else {
                                    swipeRefreshLayout.setEnabled(false);
                                }
                            }
                        });
                    }

                });
                if(haveNetworkConnection())
                {
                    myWebView.getSettings().setAppCacheEnabled(true);
                    myWebView.getSettings().setJavaScriptEnabled(true);
                    myWebView.setVerticalScrollBarEnabled(true);
                    myWebView.getSettings().setBuiltInZoomControls(true);
                    myWebView.getSettings().setSupportZoom(true);
                    myWebView.getSettings().setDisplayZoomControls(false);
                    myWebView.setScrollContainer(true);
                    myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/admin1.php");
                    myWebView.setWebViewClient(new WebViewClient() {
                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                            myWebView.loadUrl("error.html");
                        }

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {

                            pd = new ProgressDialog(MainActivity.this);
                            pd.setTitle("Please Wait...");
                            pd.setMessage("Page is Loading...");
                            pd.show();
                            super.onPageStarted(view, url, favicon);
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            pd.dismiss();
                            super.onPageFinished(view, url);
                        }
                    });
                    drawerLayout.closeDrawer(navigationView);
                }
                else {
                    alertView();
                }
                break;

            case R.id.nav_time_table:
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(!haveNetworkConnection())
                        {
                            alertView();
                        }
                        myWebView = findViewById(R.id.web1);
                        myWebView.getSettings().setAppCacheEnabled(true);
                        myWebView.getSettings().setJavaScriptEnabled(true);
                        myWebView.setVerticalScrollBarEnabled(true);
                        myWebView.setHorizontalScrollBarEnabled(true);
                        myWebView.getSettings().setBuiltInZoomControls(true);
                        myWebView.getSettings().setSupportZoom(true);
                        myWebView.getSettings().setDisplayZoomControls(false);
                        myWebView.setScrollContainer(true);
                        myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/timetable.php");
                        myWebView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                pd = new ProgressDialog(MainActivity.this);
                                pd.setTitle("Please Wait...");
                                pd.setMessage("Page is Loading...");
                                pd.show();
                                super.onPageStarted(view, url, favicon);
                            }
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                pd.dismiss();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            @Override
                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                myWebView.loadUrl("error.html");
                            }

                        });
                        myWebView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                            @Override
                            public void onScrollChanged() {
                                if (myWebView.getScrollY() == 0) {
                                    swipeRefreshLayout.setEnabled(true);
                                } else {
                                    swipeRefreshLayout.setEnabled(false);
                                }
                            }
                        });
                    }

                });
                if(haveNetworkConnection())
                {
                    myWebView.getSettings().setAppCacheEnabled(true);
                    myWebView.getSettings().setJavaScriptEnabled(true);
                    myWebView.setVerticalScrollBarEnabled(true);
                    myWebView.getSettings().setBuiltInZoomControls(true);
                    myWebView.getSettings().setSupportZoom(true);
                    myWebView.getSettings().setDisplayZoomControls(false);
                    myWebView.setScrollContainer(true);
                    myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/timetable.php");
                    myWebView.setWebViewClient(new WebViewClient());myWebView.setWebViewClient(new WebViewClient()
                {
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        myWebView.loadUrl("error.html");
                    }
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {

                        pd=new ProgressDialog(MainActivity.this);
                        pd.setTitle("Please Wait...");
                        pd.setMessage("Page is Loading...");
                        pd.show();
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        pd.dismiss();
                        super.onPageFinished(view, url);
                    }
                });
                    drawerLayout.closeDrawer(navigationView);
                }
                else
                {
                    alertView();
                }
                break;

            case R.id.nav_attendance:
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(!haveNetworkConnection())
                        {
                            alertView();
                        }
                        myWebView = findViewById(R.id.web1);
                        myWebView.getSettings().setAppCacheEnabled(true);
                        myWebView.getSettings().setJavaScriptEnabled(true);
                        myWebView.setVerticalScrollBarEnabled(true);
                        myWebView.setHorizontalScrollBarEnabled(true);
                        myWebView.getSettings().setBuiltInZoomControls(true);
                        myWebView.getSettings().setSupportZoom(true);
                        myWebView.getSettings().setDisplayZoomControls(false);
                        myWebView.setScrollContainer(true);
                        myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/attendance.php");
                        myWebView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                pd = new ProgressDialog(MainActivity.this);
                                pd.setTitle("Please Wait...");
                                pd.setMessage("Page is Loading...");
                                pd.show();
                                super.onPageStarted(view, url, favicon);
                            }
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                pd.dismiss();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            @Override
                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                myWebView.loadUrl("error.html");
                            }

                        });
                        myWebView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                            @Override
                            public void onScrollChanged() {
                                if (myWebView.getScrollY() == 0) {
                                    swipeRefreshLayout.setEnabled(true);
                                } else {
                                    swipeRefreshLayout.setEnabled(false);
                                }
                            }
                        });
                    }

                });
                if(haveNetworkConnection())
                {
                    myWebView.getSettings().setAppCacheEnabled(true);
                    myWebView.getSettings().setJavaScriptEnabled(true);
                    myWebView.setVerticalScrollBarEnabled(true);
                    myWebView.getSettings().setBuiltInZoomControls(true);
                    myWebView.getSettings().setSupportZoom(true);
                    myWebView.getSettings().setDisplayZoomControls(false);
                    myWebView.setScrollContainer(true);
                    myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/attendance.php");
                    myWebView.setWebViewClient(new WebViewClient()
                    {
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {

                            pd=new ProgressDialog(MainActivity.this);
                            pd.setTitle("Please Wait...");
                            pd.setMessage("Page is Loading...");
                            pd.show();
                            super.onPageStarted(view, url, favicon);
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            pd.dismiss();
                            super.onPageFinished(view, url);
                        }
                    });
                    drawerLayout.closeDrawer(navigationView);
                }
                else
                {
                    alertView();
                }

                break;

            case R.id.nav_global:
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(!haveNetworkConnection())
                        {
                            alertView();
                        }
                        myWebView = findViewById(R.id.web1);
                        myWebView.getSettings().setAppCacheEnabled(true);
                        myWebView.getSettings().setJavaScriptEnabled(true);
                        myWebView.setVerticalScrollBarEnabled(true);
                        myWebView.setHorizontalScrollBarEnabled(true);
                        myWebView.getSettings().setBuiltInZoomControls(true);
                        myWebView.getSettings().setSupportZoom(true);
                        myWebView.getSettings().setDisplayZoomControls(false);
                        myWebView.setScrollContainer(true);
                        myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/globle.php");
                        myWebView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                pd = new ProgressDialog(MainActivity.this);
                                pd.setTitle("Please Wait...");
                                pd.setMessage("Page is Loading...");
                                pd.show();
                                super.onPageStarted(view, url, favicon);
                            }
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                pd.dismiss();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            @Override
                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                myWebView.loadUrl("error.html");
                            }

                        });
                        myWebView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                            @Override
                            public void onScrollChanged() {
                                if (myWebView.getScrollY() == 0) {
                                    swipeRefreshLayout.setEnabled(true);
                                } else {
                                    swipeRefreshLayout.setEnabled(false);
                                }
                            }
                        });
                    }

                });
                if(haveNetworkConnection())
                {
                    myWebView.getSettings().setAppCacheEnabled(true);
                    myWebView.getSettings().setJavaScriptEnabled(true);
                    myWebView.setVerticalScrollBarEnabled(true);
                    myWebView.getSettings().setBuiltInZoomControls(true);
                    myWebView.getSettings().setSupportZoom(true);
                    myWebView.getSettings().setDisplayZoomControls(false);
                    myWebView.setScrollContainer(true);
                    myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/globle.php");
                    myWebView.setWebViewClient(new WebViewClient()
                    {
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {

                            pd=new ProgressDialog(MainActivity.this);
                            pd.setTitle("Please Wait...");
                            pd.setMessage("Page is Loading...");
                            pd.show();
                            super.onPageStarted(view, url, favicon);
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            pd.dismiss();
                            super.onPageFinished(view, url);
                        }
                    });
                    drawerLayout.closeDrawer(navigationView);
                }
                else
                {
                    alertView();
                }

                break;

            case R.id.nav_change_pwd:
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(!haveNetworkConnection())
                        {
                            alertView();
                        }
                        myWebView = findViewById(R.id.web1);
                        myWebView.getSettings().setAppCacheEnabled(true);
                        myWebView.getSettings().setJavaScriptEnabled(true);
                        myWebView.setVerticalScrollBarEnabled(true);
                        myWebView.setHorizontalScrollBarEnabled(true);
                        myWebView.getSettings().setBuiltInZoomControls(true);
                        myWebView.getSettings().setSupportZoom(true);
                        myWebView.getSettings().setDisplayZoomControls(false);
                        myWebView.setScrollContainer(true);
                        myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/changepass.php");
                        myWebView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                pd = new ProgressDialog(MainActivity.this);
                                pd.setTitle("Please Wait...");
                                pd.setMessage("Page is Loading...");
                                pd.show();
                                super.onPageStarted(view, url, favicon);
                            }
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                pd.dismiss();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            @Override
                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                myWebView.loadUrl("error.html");
                            }

                        });
                        myWebView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                            @Override
                            public void onScrollChanged() {
                                if (myWebView.getScrollY() == 0) {
                                    swipeRefreshLayout.setEnabled(true);
                                } else {
                                    swipeRefreshLayout.setEnabled(false);
                                }
                            }
                        });
                    }

                });
                if(haveNetworkConnection())
                {
                    myWebView.getSettings().setAppCacheEnabled(true);
                    myWebView.getSettings().setJavaScriptEnabled(true);
                    myWebView.setVerticalScrollBarEnabled(true);
                    myWebView.getSettings().setBuiltInZoomControls(true);
                    myWebView.getSettings().setSupportZoom(true);
                    myWebView.getSettings().setDisplayZoomControls(false);
                    myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/changepass.php");
                    myWebView.setWebViewClient(new WebViewClient()
                    {
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {

                            pd=new ProgressDialog(MainActivity.this);
                            pd.setTitle("Please Wait...");
                            pd.setMessage("Page is Loading...");
                            pd.show();
                            super.onPageStarted(view, url, favicon);
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            pd.dismiss();
                            super.onPageFinished(view, url);
                        }
                    });
                    drawerLayout.closeDrawer(navigationView);
                }
                else
                {
                    alertView();
                }
                break;

            case R.id.nav_logout:

                if(haveNetworkConnection())
                {
                    myWebView.getSettings().setAppCacheEnabled(true);
                    myWebView.getSettings().setJavaScriptEnabled(true);
                    myWebView.setVerticalScrollBarEnabled(true);
                    myWebView.getSettings().setBuiltInZoomControls(true);
                    myWebView.getSettings().setSupportZoom(true);
                    myWebView.getSettings().setDisplayZoomControls(false);
                    myWebView.loadUrl("https://faculty-parent-connect.000webhostapp.com/faculty/logout.php");
                    myWebView.setWebViewClient(new WebViewClient());
                    drawerLayout.closeDrawer(navigationView);
                }
                else {
                    alertView();
                }
                break;
        }
    }
}
