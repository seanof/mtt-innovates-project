package com.mttnow.fluttr;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SearchCriteriaFragment extends Fragment implements EditText.OnEditorActionListener {

    private EditText destination;
    private EditText departDate;
    private EditText returnDate;
    private EditText numTravelers;

    private TextView departDateText;
    private TextView returnDateText;

    private LinearLayout numTravelersLayout;
    private ListView suggestionList;

    final Calendar myCalendar = Calendar.getInstance();

    private String paramDestination;
    private String paramDate;
    private int paramNumTravelers;

    private static final String[] SUGGESTIONS = {"Paris", "London", "Dublin", "New York", "San Francisco"};

    public SearchCriteriaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presenter, container, false);

        destination = (EditText) view.findViewById(R.id.destination);
        departDate = (EditText) view.findViewById(R.id.travel_date_depart);
        returnDate = (EditText) view.findViewById(R.id.travel_date_return);
        numTravelers = (EditText) view.findViewById(R.id.num_travelers);

        departDateText = (TextView) view.findViewById(R.id.travel_date_text);
        returnDateText = (TextView) view.findViewById(R.id.travel_date_text_return);
        numTravelersLayout = (LinearLayout) view.findViewById(R.id.num_travelers_parent);

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
        destination.requestFocus();
        destination.setOnEditorActionListener(this);
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
                            new DatePickerDialog(getContext(), date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        numTravelers.requestFocus();
    }

    private void saveParams() {
        paramDestination = destination.getText().toString().trim().toLowerCase();
        paramDate = departDate.getText().toString().trim();
        paramNumTravelers = Integer.parseInt(departDate.getText().toString());
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
                    saveParams();
                    return true;
                }
        }
        return false;
    }
}
