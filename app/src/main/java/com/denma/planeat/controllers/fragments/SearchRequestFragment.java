package com.denma.planeat.controllers.fragments;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.denma.planeat.R;
import com.denma.planeat.arch.repositories.ResponseRepository;
import com.denma.planeat.arch.viewmodels.MenuViewModel;
import com.denma.planeat.arch.viewmodels.ResponseViewModel;
import com.denma.planeat.controllers.BaseFragment;
import com.denma.planeat.controllers.activities.SearchActivity;
import com.denma.planeat.utils.TimeAndDateUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;


public class SearchRequestFragment extends BaseFragment {

    // FOR DESIGN
    @BindView(R.id.search_request_edit_text)
    EditText keyWord;
    @BindView(R.id.radio_group_diet)
    RadioGroup dietGroup;
    @BindView(R.id.radio_group_health)
    RadioGroup healthGroup;
    @BindView(R.id.check_box_sugar_conscious)
    CheckBox sugarConscious;
    @BindView(R.id.check_box_alcohol_free)
    CheckBox alcoholFree;
    @BindView(R.id.check_box_peanut_free)
    CheckBox peanutFree;
    @BindView(R.id.check_box_tree_nut_free)
    CheckBox treeNutFree;

    // FOR DATA
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ResponseViewModel responseViewModel;

    public OnSearchClickListener callback;
    public void setOnSearchClickListener(SearchActivity activity) {
        callback = activity;
    }
    public interface OnSearchClickListener {
        void onSearchClick();
    }

    // --------------------
    // CONSTRUCTORS
    // --------------------

    public SearchRequestFragment() {
        // Required empty public constructor
    }

    // --------------------
    // ON CREATE VIEW
    // --------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Configuration
        this.configureDagger();
        this.configureViewModel();

        return view;
    }

    // --------------------
    // GETTERS
    // --------------------

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_search_request;
    }

    // --------------------
    // CONFIGURATIONS
    // --------------------

    // - Configure Dagger2
    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel(){
        responseViewModel = ViewModelProviders.of(this, viewModelFactory).get(ResponseViewModel.class);
    }


    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.search_button)
    public void doTheSearch(){
        String query = queryAttribution();
        // error you need to change/add a query
        if(query.equals("0")){
            Toast.makeText(this.getActivity(), getResources().getText(R.string.error_text_empty), Toast.LENGTH_SHORT).show();
            return;
        } else if (query.equals("-1")){
            Toast.makeText(this.getActivity(), getResources().getText(R.string.error_text_too_short), Toast.LENGTH_SHORT).show();
            return;
        }
        String diet = dietAttribution();
        String health = healthAttribution();
        responseViewModel.updateResponseFromAPI(query, diet, health);
        callback.onSearchClick();
    }

    private String queryAttribution(){
        if(this.keyWord.getText().length() >= 3){
            return this.keyWord.getText().toString();
        } else if(this.keyWord.length() == 0){
            // Error, the text is empty
            return "0";
        } else {
            // Error, the text is too short
            return "-1";
        }
    }

    private String dietAttribution(){
        int radioButtonId = dietGroup.getCheckedRadioButtonId();
        String dietChoice;
        switch(radioButtonId){
            case R.id.radio_balanced:
                dietChoice = "balanced";
                break;
            case R.id.radio_high_protein:
                dietChoice = "high-protein";
                break;
            case R.id.radio_low_fat:
                dietChoice = "low-fat";
                break;
            case R.id.radio_low_carb:
                dietChoice = "low-carb";
                break;
            default:
                // no selection has be done
                dietChoice = "";
                break;
        }
        return dietChoice;
    }

    private String healthAttribution(){
        int radioButtonId = healthGroup.getCheckedRadioButtonId();
        String healthChoice;

        // - 1 init from radio group
        switch(radioButtonId){
            case R.id.radio_vegan:
                healthChoice = "vegan";
                break;
            case R.id.radio_vegetarian:
                healthChoice = "vegetarian";
                break;
            default:
                // no selection has be done
                healthChoice = "";
                break;
        }

        // - 2 add more from checkboxes
        if(this.sugarConscious.isChecked()){
            if(healthChoice.equals("")){
                healthChoice = "sugar-conscious";
            } else {
                healthChoice += "&health=sugar-conscious";
            }
        }
        if(this.alcoholFree.isChecked()){
            if(healthChoice.equals("")){
                healthChoice = "alcohol-free";
            } else {
                healthChoice += "&health=alcohol-free";
            }
        }
        if(this.peanutFree.isChecked()){
            if(healthChoice.equals("")){
                healthChoice = "peanut-free";
            } else {
                healthChoice += "&health=peanut-free";
            }
        }
        if(this.treeNutFree.isChecked()){
            if(healthChoice.equals("")){
                healthChoice = "tree-nut-free";
            } else {
                healthChoice += "&health=tree-nut-free";
            }
        }

        return healthChoice;
    }

}
