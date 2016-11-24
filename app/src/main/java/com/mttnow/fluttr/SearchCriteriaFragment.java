package com.mttnow.fluttr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import util.EditTextDatePicker;

public class SearchCriteriaFragment extends Fragment implements EditText.OnEditorActionListener {

    private EditText destination;
    private EditText travelDate;
    private EditText numTravelers;

    private TextView travelDateText;

    private LinearLayout numTravelersLayout;

    public SearchCriteriaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presenter, container, false);

        destination = (EditText) view.findViewById(R.id.destination);
        travelDate = (EditText) view.findViewById(R.id.travel_date_depart);
        numTravelers = (EditText) view.findViewById(R.id.num_travelers);

        travelDateText = (TextView) view.findViewById(R.id.travel_date_text);
        numTravelersLayout = (LinearLayout) view.findViewById(R.id.num_travelers_parent);

        promptDestinationInput();

        return view;
    }

    private void promptDestinationInput() {
        destination.requestFocus();
        destination.setOnEditorActionListener(this);
    }

    private void promptTravelDate() {
        travelDateText.setVisibility(View.VISIBLE);
        travelDate.setVisibility(View.VISIBLE);
        new EditTextDatePicker(travelDate, getActivity());
        travelDate.requestFocus();
        travelDate.setOnEditorActionListener(this);
    }

    private void promptNumTravellers() {
        numTravelersLayout.setVisibility(View.VISIBLE);
        numTravelers.requestFocus();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.destination:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    destination.performClick();
                    promptTravelDate();
                    return true;
                }
            case R.id.travel_date_depart:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    travelDate.performClick();
                    promptNumTravellers();
                    return true;
                }
            case R.id.num_travelers:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    return true;
                }
        }
        return false;
    }
}
