package com.omninos.petrowagon.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omninos.petrowagon.Activities.TrackOrderActivity;
import com.omninos.petrowagon.Adapters.AdapterCurrentOrders;
import com.omninos.petrowagon.ModelClass.CurrentOrderModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.ViewModel.OrderViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentOrdersFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private AdapterCurrentOrders adapterCurrentOrders;
    List<CurrentOrderModel> list;
    private OrderViewModel viewModel;
    private Activity activity;
    private TextView CurrentOrderText;


    public CurrentOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_current_orders, container, false);
        initIds(v);
        viewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);
        getCurrentOrderList();
        return v;
    }

    private void initIds(View v) {
        activity = getActivity();

        recyclerView = v.findViewById(R.id.recycler_current_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CurrentOrderText = v.findViewById(R.id.CurrentOrderText);


    }

    private void getCurrentOrderList() {
        String userId = App.getAppPreference().getLoginDetail().getDetails().getId();
        viewModel.currentOrder(activity, userId).observe(getActivity(), new Observer<CurrentOrderModel>() {
            @Override
            public void onChanged(final CurrentOrderModel currentOrderModel) {
                if (currentOrderModel.getSuccess().equalsIgnoreCase("1")) {
                    CurrentOrderText.setVisibility(View.GONE);
                    adapterCurrentOrders = new AdapterCurrentOrders(currentOrderModel.getDetails(), getActivity(), new AdapterCurrentOrders.Select() {
                        @Override
                        public void Choose(int position) {
                            App.getSinltonPojo().setTrackOrderStatus("0");
                            App.getSinltonPojo().setOrderId(currentOrderModel.getDetails().get(position).getId());
                            App.getSinltonPojo().setProductLocation(currentOrderModel.getDetails().get(position).getLocation());
                            App.getSinltonPojo().setProductQuantity(currentOrderModel.getDetails().get(position).getQuantity());
                            App.getSinltonPojo().setProductPrice(currentOrderModel.getDetails().get(position).getPricePerLitre());
                            App.getSinltonPojo().setProductTotalPrice(currentOrderModel.getDetails().get(position).getTotalPrice());
                            App.getSinltonPojo().setProductDate(currentOrderModel.getDetails().get(position).getDate());

//                            App.getSinltonPojo().setCurrentLat(currentOrderModel.getDetails().get(position).getLatitude());
//                            App.getSinltonPojo().setCurrentLat(currentOrderModel.getDetails().get(position).getLatitude());
                            startActivity(new Intent(getActivity(), TrackOrderActivity.class));
                        }
                    });
                    recyclerView.setAdapter(adapterCurrentOrders);

                }
                else {
                    CurrentOrderText.setVisibility(View.VISIBLE);
                }
            }
        });

    }




}
