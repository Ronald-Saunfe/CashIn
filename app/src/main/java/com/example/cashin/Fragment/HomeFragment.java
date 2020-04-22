package com.example.cashin.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cashin.Activity.Entrance_Page;
import com.example.cashin.Activity.InvestEarn;
import com.example.cashin.Activity.Messages;
import com.example.cashin.Activity.Withdraw;
import com.example.cashin.Activity.myInvestments;
import com.example.cashin.Activity.myReferee;
import com.example.cashin.Activity.profilePage;
import com.example.cashin.Activity.purchaseInvestment;

import com.example.cashin.Activity.termsCondition;
import com.example.cashin.GlobalVariable;
import com.example.cashin.R;
import com.google.android.material.card.MaterialCardView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    //menu button
    private MaterialCardView cdMenuIE, cdMenuPI,cdMenuMI,cdMenuWC;
    private LinearLayout lnNav1,lnNav, lnEntrnce;
    private int lnavInit1Top,leftVal, initLeftVal;
    private EditText editSearchN;
    private boolean mainL=true;
    private SlidingPaneLayout rootDL;
    private TextView txtUserDL, txtemailDL;
    private Toolbar toolbLogin;
    private PrimaryDrawerItem itemM;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lnavInit1Top = lnNav1.getTop();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        lnNav1 = view.findViewById(R.id.lnNav1);
        editSearchN = view.findViewById(R.id.editSearchN);
        lnEntrnce= view.findViewById(R.id.lnEntrnce);
        leftVal = lnEntrnce.getLeft();
        initLeftVal = editSearchN.getLeft();

        //menu buttons
        cdMenuIE = view.findViewById(R.id.cdMenuIE);
        cdMenuPI = view.findViewById(R.id.cdMenuPI);
        cdMenuMI= view.findViewById(R.id.cdMenuMI);
        cdMenuWC = view.findViewById(R.id.cdMenuWC);

        cdMenuIE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), InvestEarn.class);
                startActivity(i);
            }
        });
        cdMenuPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), purchaseInvestment.class);
                startActivity(i);
            }
        });
        cdMenuMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), myInvestments.class);
                startActivity(i);
            }
        });
        cdMenuWC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Withdraw.class);
                startActivity(i);
            }
        });

        toolbLogin= view.findViewById(R.id.toolbLogin);
        itemM = new PrimaryDrawerItem().withName("Message").withBadge("0").withIcon(R.drawable.ic_message_black_24dp)
                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.gray)).withTag(2);
        //display the navigation drawer
        //if you want to update the items at a later time it is recommended to keep it in a variable

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(getActivity())
                .withAlternativeProfileHeaderSwitching(false)
                .withHeaderBackground(R.drawable.menubtn)
                .addProfiles(
                        new ProfileDrawerItem().withName(GlobalVariable.GlobUsername)
                                .withEmail(GlobalVariable.GlobEmail)
                                .withIcon(getResources().getDrawable(R.drawable.ic_people_black_24dp))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        System.out.println("=================="+profile.getEmail().toString());
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        final Drawer result = new DrawerBuilder()
                .withActivity(getActivity())
                .withToolbar(toolbLogin)
                .withGenerateMiniDrawer(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Profile").withTag(0).withIcon(R.drawable.ic_person_black_24dp),
                        new PrimaryDrawerItem().withName("My referee").withTag(1).withIcon(R.drawable.ic_people_black_24dp),
                        itemM,
                        new PrimaryDrawerItem().withName("Terms & Conditions").withTag(3).withIcon(R.drawable.ic_assignment_black_24dp),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Settings").withTag(4).withIcon(R.drawable.ic_settings_black_24dp),
                        new PrimaryDrawerItem().withName("Log out").withTag(5).withIcon(R.drawable.ic_exit_to_app_black_24dp)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        System.out.println("=================="+drawerItem.getTag());
                        switch ((int)drawerItem.getTag()){
                            case 0:
                                Intent i = new Intent(getContext(), profilePage.class);
                                startActivity(i);
                                break;
                            case 1:
                                Intent i1 = new Intent(getContext(), myReferee.class);
                                startActivity(i1);
                                break;
                            case 2:
                                Intent i3 = new Intent(getContext(), Messages.class);
                                startActivity(i3);
                                break;
                            case 3:
                                Intent i4 = new Intent(getContext(), termsCondition.class);
                                startActivity(i4);
                                break;
                            case 4:
                                Intent i5 = new Intent(getContext(), profilePage.class);
                                startActivity(i5);
                                break;
                        }
                        return true;
                    }
                }
                ).build();

        // Inflate the header view at runtime
        //View headerLayout = MenuSlider.inflateHeaderView(R.layout.headerdl);

        //navigation view header views
        //txtUserDL= headerLayout.findViewById(R.id.txtUserDL);
        //txtemailDL= headerLayout.findViewById(R.id.txtemailDL);

        //txtUserDL.setText(GlobalVariable.GlobUsername);
        //txtemailDL.setText(GlobalVariable.GlobEmail);

        LayoutTransition itemLayoutTransition = new LayoutTransition();
        itemLayoutTransition.setInterpolator(LayoutTransition.DISAPPEARING, new AnticipateOvershootInterpolator());
        itemLayoutTransition.setInterpolator(LayoutTransition.CHANGE_DISAPPEARING, new OvershootInterpolator());
        itemLayoutTransition.setInterpolator(LayoutTransition.CHANGE_APPEARING, new OvershootInterpolator());

        ViewGroup av = view.findViewById(R.id.ltoolbmain);
        av.setLayoutTransition(itemLayoutTransition);

        //set layout fade animation
        LayoutTransition itemLayoutTransition1 = new LayoutTransition();
        itemLayoutTransition1.setInterpolator(LayoutTransition.DISAPPEARING, new AccelerateInterpolator());
        itemLayoutTransition1.setDuration(125); //3/4*time of transition

        ViewGroup av1 = view.findViewById(R.id.constEntrance);
        av1.setLayoutTransition(itemLayoutTransition1);

        editSearchN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println(count);
                if (count==0){
                    unShowSearchHelp();
                }
                else {
                    showSearchHelp();
                    System.out.println("shown");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //set Navigation drawer

        return view;
    }

    //hide help
    private void unShowSearchHelp(){
        lnEntrnce.setVisibility(View.VISIBLE);

    }

    //show help
    private void showSearchHelp(){
        lnEntrnce.setVisibility(View.GONE);

    }

    private void unShowMoreOption(){
        mainL=true;

    }

    private void showMoreOption(){
        mainL=false;
        lnNav1.setVisibility(View.VISIBLE);
        lnNav.setVisibility(View.GONE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(lnavInit1Top+160,lnavInit1Top);
        valueAnimator.setInterpolator(new OvershootInterpolator()); // increase the speed first and then decrease
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                lnNav1.setTop(val);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
        valueAnimator.start();
    }

    public void showDialog() {
        new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setIcon(R.drawable.signout)
                .setCancelable(false)
                .setTitle("Log out!")
                .setMessage("Are you sure you want to logout?\n")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //SignOut();
                        startActivity(new Intent(getContext(), Entrance_Page.class));
                    }
                })
                .create().show();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
