package com.mttnow.fluttr;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by seanof on 21/11/2016.
 */

public class SearchCriteriaFragment extends Fragment implements
        EditText.OnEditorActionListener, View.OnClickListener {

    private static final String DESTINATION = "destination";
    private static final String DEPART = "depart";
    private static final String RETURN = "return";
    private static final String NUM_TRAVELLERS = "numTravellers";

    private EditText destination;
    private EditText departDate;
    private EditText returnDate;
    private EditText numTravelers;

    private TextView departDateText;
    private TextView returnDateText;

    private LinearLayout numTravelersLayout;
    private RelativeLayout suggestionLayout;
    private LinearLayout selectionLayout;
    private ListView suggestionList;

    final Calendar myCalendar = Calendar.getInstance();

    private long minDate;

    private String paramDestination;
    private String paramDepartDate;
    private String paramReturnDate;
    private int paramNumTravelers = 1;

    private static final String[] SUGGESTIONS = {"Paris", "London", "Dublin",
            "New York", "San Francisco"};

    public SearchCriteriaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presenter, container, false);

        Button clearButton = (Button) view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        destination = (EditText) view.findViewById(R.id.destination);
        departDate = (EditText) view.findViewById(R.id.travel_date_depart);
        returnDate = (EditText) view.findViewById(R.id.travel_date_return);
        numTravelers = (EditText) view.findViewById(R.id.num_travelers);

        departDateText = (TextView) view.findViewById(R.id.travel_date_text);
        returnDateText = (TextView) view.findViewById(R.id.travel_date_text_return);

        TextView flightsSelect = (TextView) view.findViewById(R.id.flights_select);
        TextView hotelsSelect = (TextView) view.findViewById(R.id.hotels_select);
        TextView packagesSelect = (TextView) view.findViewById(R.id.packages_select);
        flightsSelect.setOnClickListener(this);
        hotelsSelect.setOnClickListener(this);
        packagesSelect.setOnClickListener(this);

        numTravelersLayout = (LinearLayout) view.findViewById(R.id.num_travelers_parent);

        suggestionLayout = (RelativeLayout) view.findViewById(R.id.suggestion_layout);
        selectionLayout = (LinearLayout) view.findViewById(R.id.selection_layout);

        suggestionList = (ListView) view.findViewById(R.id.suggestion_list);

        setupSuggestionList();
        promptDestinationInput();

        return view;
    }

    private void setupSuggestionList() {
        suggestionList.setAdapter(new ArrayAdapter<>(getContext(),
                R.layout.suggestion_list_item, SUGGESTIONS));
        suggestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                destination.setText(suggestionList.getItemAtPosition(i).toString());
                promptTravelDate(departDate);
            }
        });
    }

    private void promptDestinationInput() {
        destination.setOnEditorActionListener(this);
        destination.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
          Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(destination, InputMethodManager.SHOW_IMPLICIT);
    }

    private void promptTravelDate(EditText editText) {

        final EditText dateEditText = editText;

        if (dateEditText.getId() == R.id.travel_date_depart) {
            departDateText.setVisibility(View.VISIBLE);
            departDate.setVisibility(View.VISIBLE);
        }
        else {
            returnDateText.setVisibility(View.VISIBLE);
            returnDate.setVisibility(View.VISIBLE);
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (dateEditText.getId() == R.id.travel_date_depart) {
                    updateLabel(R.id.travel_date_depart);
                }
                else {
                    updateLabel(R.id.travel_date_return);
                }
            }
        };

        dateEditText.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focused) {
                        if (focused) {
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                                    date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH));
                            if (dateEditText.getId() == R.id.travel_date_return) {
                                datePickerDialog.getDatePicker().setMinDate(minDate);
                            }
                            datePickerDialog.show();
                        }
                    }
                }
        );

        dateEditText.requestFocus();
    }

    private void updateLabel(int id) {

        String displayDateFormat = "dd-MMM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(displayDateFormat, Locale.US);
        if (id == R.id.travel_date_depart) {
            minDate = myCalendar.getTimeInMillis() + 24 * 60 * 60 * 1000;
            departDate.setText(sdf.format(myCalendar.getTime()));
            departDate.clearFocus();
            promptTravelDate(returnDate);
        }
        else {
            returnDate.setText(sdf.format(myCalendar.getTime()));
            returnDate.clearFocus();
            promptNumTravellers();
        }
    }

    private void promptNumTravellers() {
        numTravelersLayout.setVisibility(View.VISIBLE);
        numTravelers.setOnEditorActionListener(this);
        numTravelers.requestFocus();
    }

    private void finishInput() {
        suggestionLayout.setVisibility(View.GONE);
        selectionLayout.setVisibility(View.VISIBLE);

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void clearAllFields() {
        destination.setText(null);
        departDate.setText(null);
        returnDate.setText(null);
        numTravelers.setText(null);
        paramDestination = null;
        paramDepartDate = null;
        paramReturnDate = null;
        paramNumTravelers = 0;

        departDate.setVisibility(View.INVISIBLE);
        departDateText.setVisibility(View.INVISIBLE);
        returnDate.setVisibility(View.INVISIBLE);
        returnDateText.setVisibility(View.INVISIBLE);
        numTravelersLayout.setVisibility(View.INVISIBLE);

        suggestionLayout.setVisibility(View.VISIBLE);
        selectionLayout.setVisibility(View.GONE);

        promptDestinationInput();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.destination:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    promptTravelDate(departDate);
                    return true;
                }
            case R.id.num_travelers:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    numTravelers.clearFocus();
                    finishInput();
                    return true;
                }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_button:
                clearAllFields();
                break;
            case R.id.flights_select:
                startStream(0);
                break;
            case R.id.hotels_select:
                startStream(1);
                break;
            case R.id.packages_select:
                startStream(2);
                break;
        }
    }

    private void startStream(int stream) {
        if (!destination.getText().toString().equals("")) {
            paramDestination = destination.getText().toString().trim().toLowerCase().replace(" ", "");
            paramDepartDate = departDate.getText().toString().trim();
            paramReturnDate = returnDate.getText().toString().trim();
            paramNumTravelers = !numTravelers.getText().toString().equals("") ? Integer.parseInt(numTravelers.getText().toString()) : 1;

            Intent intent = new Intent();
            intent.putExtra(DESTINATION, paramDestination);
            intent.putExtra(DEPART, paramDepartDate);
            intent.putExtra(RETURN, paramReturnDate);
            intent.putExtra(NUM_TRAVELLERS, paramNumTravelers);

            switch (stream) {
                case 0:
                    intent.setClass(getActivity(), HotelStreamActivity.class);
                    break;
                case 1:
                    intent.setClass(getActivity(), HotelStreamActivity.class);
                    break;
                case 2:
                    intent.setClass(getActivity(), HotelStreamActivity.class);
                    break;
            }

            startActivity(intent);
        }

        else {
            destination.requestFocus();
            destination.setError("Please enter a destination.");
        }
    }
}
