package com.example.jolenam.nytimessearch;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jolenam.nytimessearch.Activities.SearchFilters;

/**
 * Created by jolenam on 6/24/16.
 */
public class FilterDialogFragment extends DialogFragment implements View.OnClickListener{
    Button btnDatePicker;
    Spinner spSort;
    CheckBox cbArts, cbFashionandStyle, cbSports;
    Button btnFilter;

    private SearchFilters mFilters;

    public FilterDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    //public interface

    public static FilterDialogFragment newInstance(SearchFilters filters) {
        FilterDialogFragment frag = new FilterDialogFragment();
        // Store this filters object inside a bundle to be accessed later
        Bundle args = new Bundle();
        args.putSerializable("filters", filters);
        frag.setArguments(args);
        return frag;
    }

    // 1. Defines the listener interface with a method
    //    passing back filters as result to activity.
    public interface OnFilterSearchListener {
        void onUpdateFilters(SearchFilters filters);
    }
    // This gets triggered once the view is created
    // Good time to look up our views and setup click handler
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Store the filters to a member variable
        mFilters = (SearchFilters) getArguments().getSerializable("filters");
        // ... any other view lookups here...
        // Get access to the button
        //btnDatePicker = (Button) view.findViewById(R.id.btnDatePicker);
        spSort = (Spinner) view.findViewById(R.id.spSort);
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashionandStyle = (CheckBox) view.findViewById(R.id.cbFashionAndStyle);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
        btnFilter = (Button) view.findViewById(R.id.btnFilter);

        // 2. Attach a callback when the button is pressed
        btnFilter.setOnClickListener(this);
    }

    // This is fired when the button in the filer activity is clicked
    // This is the time to apply the filters by sending back to the `SearchActivity`
    @Override
    public void onClick(View v) {
        // Update the mFilters attribute values based on the input views
        if (mFilters != null) {
            mFilters.setSortType(spSort.getSelectedItem().toString());
        }


        Toast.makeText(v.getContext(), mFilters.getSortType(), Toast.LENGTH_LONG).show();

        // Return filters back to activity through the implemented listener
        OnFilterSearchListener listener = (OnFilterSearchListener) getActivity();
        listener.onUpdateFilters(mFilters);

        // Close the dialog to return back to the parent activity
        dismiss();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container);

    }


}
