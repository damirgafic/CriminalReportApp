package android.bignerdranch.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubTitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_crime_list,
                container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(
                R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity())
        );

        if(savedInstanceState != null)
            mSubTitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);

        updateUI();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubTitleVisible);
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    // Override the appropriate callback
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle); // get a reference to the item
        if(mSubTitleVisible)  // if the state is true, then we set the title one way
            subtitleItem.setTitle(R.string.hide_subtitle);
        else  // otherwise, we set it the other way
            subtitleItem.setTitle(R.string.show_subtitle);
    }

    // Override the callback to handle the user's menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId())
        {
            case R.id.new_crime:            // this is the id of the menu option selected
                Crime crime = new Crime();  // create the crime
                CrimeLab.get(getActivity()).addCrime(crime);    // Add the crime to CrimeLab
                // Create the new intent to pass the needed info
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);  // start the activity, passing it the intent it needs
                return true; // return true tells the OS we're done here, no further work needed
            case R.id.show_subtitle:
                mSubTitleVisible = !mSubTitleVisible;  // toggle the truth value
                getActivity().invalidateOptionsMenu(); // Tells Android to redraw the menu
                updateSubtitle();
                return true;
            default:  // If the selected option isn't found, defer to the superclass
                return super.onOptionsItemSelected(item);
        }
    }

    // Method to update the subtitle bar with the number of crimes
    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());  // get the list of crimes
        int crimeCount = crimeLab.getCrimes().size();     // ask how many there are
        // build the string to display
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        if(!mSubTitleVisible) // get rid of the string object if we don't need it.
            subtitle = null;
        // get a reference to the host activity
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle); // update the subtitle bar
    }




    // defined as an inner class in class CrimeListFragment
    private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mSolvedImageView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView)itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView)itemView.findViewById(R.id.crime_solved);
        }

        @Override
        public void onClick(View view){
            Intent intent = CrimePagerActivity.newIntent(
                    getActivity(), mCrime.getId());
            startActivity(intent);
        }

        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }
    }

    // the adapter, also an inner class in class CrimeListFragment
    // requires 3 overrides
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position){
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount(){
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) { mCrimes = crimes; }
    }
}

