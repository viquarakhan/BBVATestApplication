package viquar.com.bbvatestapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import viquar.com.bbvatestapplication.R;
import viquar.com.bbvatestapplication.fragments.ListFragment.OnListFragmentInteractionListener;
import viquar.com.bbvatestapplication.model.BankLocation;

/**
 * {@link RecyclerView.Adapter} that can display a list and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    View view;
    private final ArrayList<BankLocation> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CustomAdapter(ArrayList<BankLocation> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mNameVew.setText(mValues.get(position).getName());
        holder.mLatLngView.setText("Latitude: "+mValues.get(position).getLatitude()+" Longitude: "
                +mValues.get(position).getLongitude());
        holder.mAddressView.setText("Address: "+mValues.get(position).getAddress());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                   // mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameVew;
        public final TextView mLatLngView;
        public final TextView mAddressView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameVew = (TextView) view.findViewById(R.id.tv_list_name);
            mLatLngView = (TextView) view.findViewById(R.id.tv_list_latlng);
            mAddressView = (TextView) view.findViewById(R.id.tv_list_address);
        }

    }
}
