package com.android.giveandtake.Center;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.giveandtake.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CenterFragment extends Fragment {



    private View root;
    private RecyclerView _RecyclerView;
    private tradeAdaper _Adapter;
    private RecyclerView.LayoutManager _LayoutManager;
    private FirebaseDatabase firebaseDatabase;
    static private ArrayList<Trade> TradeList;
    private DatabaseReference RootRef;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String currentUserID;



    private CenterViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(CenterViewModel.class);
        root = inflater.inflate(R.layout.fragment_center, container, false);


        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Trades");
        RootRef = firebaseDatabase.getInstance().getReference();
        firebaseAuth = firebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        createToShowTrades();

        return root;
    }

    public void createToShowTrades(){

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TradeList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String current_post_id = ds.child("current_post_id").getValue(String.class);
                    String current_trade_id = ds.child("current_Trade_id").getValue(String.class);
                    String user_post_id = ds.child("user_post_id").getValue(String.class);
                    String user_post_name = ds.child("user_post_name").getValue(String.class);
                    String current_user_name = ds.child("current_user_name").getValue(String.class);
                    String post_give = ds.child("post_give").getValue(String.class);
                    String post_take = ds.child("post_take").getValue(String.class);
                    String current_user_phone = ds.child("current_user_phone").getValue(String.class);
                    String current_user_city = ds.child("current_user_city").getValue(String.class);
                    String Textfree = ds.child("textFree").getValue(String.class);

                    Log.e(": TAG12=",current_post_id+" "+current_trade_id+" "+user_post_id+" "+user_post_name+" "+current_user_name+" "+current_user_phone+" "+post_give+" "+post_take);
                    if(user_post_id.equals(currentUserID)){
                        Trade t = new Trade(R.drawable.black2people,current_post_id,current_trade_id,user_post_id,user_post_name,current_user_name,post_give,post_take,current_user_phone,current_user_city,Textfree);
                        if(!TradeList.contains(t)){
                            TradeList.add(t);
                        }
                        // check if trade exsit

//                        for(Trade i : TradeList){
//                            if(i.getCurrent_Trade_id().ecurrent_trade_id)
//                        }
                    }

                }

                updateView();

                _Adapter.setOnTradeClickListener(new tradeAdaper.OnTradeClickListener(){
                    @Override
                    public void onTradeClick(final int position) {
                        TradeList.get(position);

                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                        mBuilder.setTitle("Post ID: "+TradeList.get(position).getCurrent_post_id());
                        mBuilder.setMessage(TradeList.get(position).getTextFree()+"\n");


                        mBuilder.setPositiveButton("Accpet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteTrade(TradeList.get(position).getCurrent_Trade_id());
                                //DeletePost(TradeList.get(position).getCurrent_post_id());
                                updateView();

                            }

                        });
                        mBuilder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteTrade(TradeList.get(position).getCurrent_Trade_id());
                                updateView();

                                /// delete the Trade only
                            }
                        });

                        mBuilder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();
                    }


                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
            myRef.addListenerForSingleValueEvent(eventListener);
    }

    public void DeleteTrade(String uid) {
        myRef.child(uid).orderByKey().equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();
                dataSnapshot.getRef().removeValue();
                updateView();
                createToShowTrades();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

//    public void DeletePost(String uid) {
//        myRef = firebaseDatabase.getReference("Posts");
//        myRef.child(uid).orderByKey().equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                String key = dataSnapshot.getKey();
//                dataSnapshot.getRef().removeValue();
//                updateView();
//                createToShowTrades();
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });
//    }

        public void updateView(){
            _RecyclerView = root.findViewById(R.id.recyclerviewCenter);
            _RecyclerView.setHasFixedSize(true);
            _LayoutManager = new LinearLayoutManager(getContext());
            _Adapter = new tradeAdaper(TradeList);
            _RecyclerView.setLayoutManager(_LayoutManager);
            _RecyclerView.setAdapter(_Adapter);
        }

}