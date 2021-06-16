package com.omninos.petrowagon.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.omninos.petrowagon.Adapters.AdapterHome;
import com.omninos.petrowagon.Adapters.AdapterHomeDescription;
import com.omninos.petrowagon.ModelClass.CheckVersionModel;
import com.omninos.petrowagon.ModelClass.DescriptionModel;
import com.omninos.petrowagon.ModelClass.MinimumValueModel;
import com.omninos.petrowagon.ModelClass.ProductListModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.RejectedOrderActivity;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.ViewModel.DescriptionViewModel;
import com.omninos.petrowagon.ViewModel.OrderViewModel;
import com.omninos.petrowagon.ViewModel.ProductViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_drawer;
    private RecyclerView recyclerView, dialogRecyclerView;
    private AdapterHome adapterHome;
    private List<ProductListModel.Detail> list = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private TextView UserName, homePageText, underMaintainenceText;
    private AdvanceDrawerLayout drawerLayout;
    private TextView tv_profile, tv_my_orders, tv_my_payments, tv_track_orders, tv_settings, tv_faq, tv_our_partners, tv_about_us, drawer_logout, drawer_reject_orders;
    private ProductViewModel viewModel;
    private Activity activity;
    private CircleImageView userImg;
    private boolean doubleBackToExitPressedOnce = false;
    private DescriptionViewModel descriptionViewModel;
    private ProgressBar drawerProgress;
    private SwipeRefreshLayout swipeRefresh;
    private int version;
    private Button btn_update_version;
    private String minPriceText, minQuantityText;
    private OrderViewModel minViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        viewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        descriptionViewModel = ViewModelProviders.of(this).get(DescriptionViewModel.class);
        minViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        initIds();
        getData();
//        RecyclerItems();
//        productList();


        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode;
            System.out.println("VersionCode : " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        viewModel.checkVersion(activity).observe(HomePageActivity.this, new Observer<CheckVersionModel>() {
            @Override
            public void onChanged(CheckVersionModel checkVersionModel) {
                if (checkVersionModel.getSuccess().equalsIgnoreCase("1")) {
                    if (checkVersionModel.getDetails().getVersionStatus().equalsIgnoreCase(String.valueOf(version))) {
                        getdata();
                    } else {
                        UpdateVersionDialog();
                    }
                } else {
                    UnderMaintainence(checkVersionModel.getMessage());
                }
            }
        });
    }

    private void UpdateVersionDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.update_version_dialog, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(HomePageActivity.this).create();
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setCancelable(false);
        dailogbox.setView(confirmdailog);

        btn_update_version = confirmdailog.findViewById(R.id.btn_update_version);
        btn_update_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                dailogbox.dismiss();
            }
        });

        dailogbox.show();

    }


    private void UnderMaintainence(String message) {

        final View confirmdailog = layoutInflater.inflate(R.layout.under_maintainence, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(HomePageActivity.this).create();
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setCancelable(false);
        dailogbox.setView(confirmdailog);

        underMaintainenceText = confirmdailog.findViewById(R.id.underMaintainenceText);
        underMaintainenceText.setText(message);

        dailogbox.show();


    }

    private void getdata() {
        performActions();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productList();
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent, R.color.blue);

        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
                productList();
            }
        });
    }


    private void initIds() {
        activity = HomePageActivity.this;
        recyclerView = findViewById(R.id.recycler_home);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        homePageText = findViewById(R.id.homePageText);
        drawerLayout = findViewById(R.id.drawer_main);
        tv_profile = findViewById(R.id.drawer_profile);
        tv_my_orders = findViewById(R.id.drawer_my_orders);
        tv_my_payments = findViewById(R.id.drawer_my_payments);
        tv_track_orders = findViewById(R.id.drawer_track_orders);
        tv_settings = findViewById(R.id.drawer_settings);
        tv_faq = findViewById(R.id.drawer_faq_help);
        tv_our_partners = findViewById(R.id.drawer_our_partners);
        tv_about_us = findViewById(R.id.drawer_about_us);
        drawer_logout = findViewById(R.id.drawer_logout);
        img_drawer = findViewById(R.id.img_drawer_opener);
        userImg = findViewById(R.id.userImg);
        UserName = findViewById(R.id.UserName);
        drawerProgress = findViewById(R.id.drawerProgress);
        drawer_reject_orders = findViewById(R.id.drawer_reject_orders);

        swipeRefresh = findViewById(R.id.swipeRefresh);

        img_drawer.setOnClickListener(this);
        tv_profile.setOnClickListener(this);
        tv_my_orders.setOnClickListener(this);
        tv_my_payments.setOnClickListener(this);
        tv_track_orders.setOnClickListener(this);
        tv_settings.setOnClickListener(this);
        tv_faq.setOnClickListener(this);
        tv_our_partners.setOnClickListener(this);
        tv_about_us.setOnClickListener(this);
        drawer_logout.setOnClickListener(this);
        homePageText.setOnClickListener(this);
        drawer_reject_orders.setOnClickListener(this);

        UserName.setText(App.getAppPreference().getLoginDetail().getDetails().getName());


        if (App.getAppPreference().getLoginDetail().getDetails().getImage().contains("https:")) {
            drawerProgress.setVisibility(View.GONE);
            Glide.with(this).load(App.getAppPreference().getLoginDetail().getDetails().getImage());
        } else {
            if (App.getAppPreference().getLoginDetail().getDetails().getImage().equals("")) {
                drawerProgress.setVisibility(View.GONE);
                userImg.setImageResource(R.drawable.dummy_img);
            } else {
//            Picasso.get().load("http://petrowagon.com/petrowagonApplication/"+App.getAppPreference().getLoginDetail().getDetails().getImage()).into(img_profile);
                Glide.with(this).load("http://petrowagon.com/petrowagonApplication/" + App.getAppPreference().getLoginDetail().getDetails().getImage()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        drawerProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        drawerProgress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(userImg);
            }
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }


    private void productList() {
        viewModel.productList(activity).observe(HomePageActivity.this, new Observer<ProductListModel>() {
            @Override
            public void onChanged(final ProductListModel productListModel) {
                if (productListModel.getSuccess().equalsIgnoreCase("1")) {
                    homePageText.setText(productListModel.getHomepageText());

                    adapterHome = new AdapterHome(productListModel.getDetails(), HomePageActivity.this, new AdapterHome.Select() {
                        @Override
                        public void Choose(int position) {
                            Intent intent = new Intent(HomePageActivity.this, PlaceOrderActivity.class);
                            App.getSinltonPojo().setProductName(productListModel.getDetails().get(position).getTitle());
                            App.getSinltonPojo().setProductPrice(productListModel.getDetails().get(position).getPrice());
                            App.getSinltonPojo().setProductId(productListModel.getDetails().get(position).getId());
                            startActivity(intent);
                        }
                    });
                    swipeRefresh.setRefreshing(false);
                    recyclerView.setAdapter(adapterHome);
                } else if (productListModel.getSuccess().equalsIgnoreCase("0")) {
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(activity, productListModel.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(activity, productListModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void performActions() {
        drawerLayout.useCustomBehavior(Gravity.START);
        drawerLayout.setRadius(Gravity.START, 35);//set end container's corner radius (dimension)
        drawerLayout.setViewScale(Gravity.START, 0.9f);
        drawerLayout.setViewElevation(Gravity.START, 20);

    }

    private void ShowDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_home, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(HomePageActivity.this).create();
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setCancelable(true);
        dailogbox.setView(confirmdailog);

        dialogRecyclerView = confirmdailog.findViewById(R.id.dialogRecyclerView);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(HomePageActivity.this, LinearLayoutManager.VERTICAL, false));


        descriptionViewModel.description(activity).observe(HomePageActivity.this, new Observer<DescriptionModel>() {
            @Override
            public void onChanged(DescriptionModel descriptionModel) {
                if (descriptionModel.getSuccess().equalsIgnoreCase("1")) {
                    AdapterHomeDescription adapterHomeDescription = new AdapterHomeDescription(descriptionModel.getDetails(), HomePageActivity.this);
                    dialogRecyclerView.setAdapter(adapterHomeDescription);

                }
            }
        });

//        confirmdailog.findViewById(R.id.btn_done_dialog_otp).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(HomePageActivity.this, HomePageActivity.class);
//                startActivity(intent);
//                finishAffinity();
//                Toast.makeText(HomePageActivity.this, "Logged in Successfully !", Toast.LENGTH_LONG).show();
//
//            }
//        });
        dailogbox.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homePageText:
                ShowDialog();
                break;
            case R.id.img_drawer_opener:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.drawer_profile:
                startActivity(new Intent(HomePageActivity.this, ProfileActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.drawer_my_orders:
                startActivity(new Intent(HomePageActivity.this, MyOrdersActivity.class));
//                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.drawer_my_payments:
                startActivity(new Intent(HomePageActivity.this, MyPaymentsActivity.class));
//                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.drawer_track_orders:
                App.getSinltonPojo().setTrackOrderStatus("1");
                startActivity(new Intent(HomePageActivity.this, TrackOrderActivity.class));
//                drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.drawer_settings:
                startActivity(new Intent(HomePageActivity.this, SettingsActivity.class));
//                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.drawer_faq_help:
                startActivity(new Intent(HomePageActivity.this, FAQHELPActivity.class));
//                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.drawer_our_partners:
                startActivity(new Intent(HomePageActivity.this, OurPartnersActivity.class));
//                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.drawer_about_us:
                App.getSinltonPojo().setWebViewStatus("3");
                startActivity(new Intent(HomePageActivity.this, AboutUsActivity.class));
//                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.drawer_logout:
                App.getAppPreference().clearAppPreferences();
                startActivity(new Intent(this, MainActivity.class));
                finishAffinity();
                break;

            case R.id.drawer_reject_orders:
                startActivity(new Intent(this, RejectedOrderActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar snackbar = Snackbar.make(drawerLayout, "Please click BACK again to exit", Snackbar.LENGTH_LONG);
        snackbar.setAction("EXIT", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomePageActivity.this.finish();
                finishAffinity();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.yellow));
        snackbar.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                doubleBackToExitPressedOnce = false;

            }
        }, 2000);
    }

    private void handlerDrawerOnClick(final TextView textView) {
        textView.setBackgroundColor(getResources().getColor(R.color.muchgrey));

        new Handler().postDelayed(new Runnable() {
            public void run() {

                textView.setBackgroundColor(getResources().getColor(R.color.white));

            }
        }, 200);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (App.getAppPreference().getLoginDetail().getDetails().getImage().contains("https:")) {
            drawerProgress.setVisibility(View.GONE);
            Glide.with(this).load(App.getAppPreference().getLoginDetail().getDetails().getImage());
        } else {
            if (App.getAppPreference().getLoginDetail().getDetails().getImage().equals("")) {
                drawerProgress.setVisibility(View.GONE);
                userImg.setImageResource(R.drawable.dummy_img);
            } else {
                Glide.with(this).load("http://petrowagon.com/petrowagonApplication/" + App.getAppPreference().getLoginDetail().getDetails().getImage()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        drawerProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        drawerProgress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(userImg);
            }
        }
    }

    private void getData() {
        minViewModel.minimum(activity).observe(HomePageActivity.this, new Observer<MinimumValueModel>() {
            @Override
            public void onChanged(MinimumValueModel minimumValueModel) {
                if (minimumValueModel.getSuccess().equalsIgnoreCase("1")) {
                    for (int i = 0; i < minimumValueModel.getDetails().size(); i++) {
                        minPriceText = minimumValueModel.getDetails().get(i).getPriceText();
                        minQuantityText = minimumValueModel.getDetails().get(i).getQuantityText();
                        App.getSinltonPojo().setMinPriceText(minPriceText);
                        App.getSinltonPojo().setMinQuantityText(minQuantityText);

                    }
                } else {
                    Toast.makeText(activity, minimumValueModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}

