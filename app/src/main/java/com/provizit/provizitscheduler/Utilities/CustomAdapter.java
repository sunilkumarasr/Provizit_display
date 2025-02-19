package com.provizit.provizitscheduler.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import com.provizit.provizitscheduler.CircleImageView;
import com.provizit.provizitscheduler.R;
import com.provizit.provizitscheduler.Services.CompanyData;

import java.util.ArrayList;
import java.util.List;



public class CustomAdapter extends ArrayAdapter<CompanyData> {

    Context context;
    int resource;
    int textViewResourceId;
    List<CompanyData> items;
    List<CompanyData> tempItems;
    List<CompanyData> suggestions;
    int status;
    String comp_id;


    public CustomAdapter(Context context, int resource, int textViewResourceId, List<CompanyData> items, int status, String Comp_id) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.status = status;
        this.resource = resource;
        this.comp_id = Comp_id;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<CompanyData>(items); // this makes the difference.
        suggestions = new ArrayList<CompanyData>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row, parent, false);
        }
        CompanyData people = items.get(position);
        if (people != null) {
            TextView lblName = view.findViewById(R.id.lbl_name);
            CircleImageView emp_img = view.findViewById(R.id.emp_img);
            if (lblName != null) {
                lblName.setText(people.getName());
                if (status == 1) {
                    emp_img.setVisibility(View.VISIBLE);
                    emp_img.setImageResource(R.drawable.ic_user);

                    lblName.setText(people.getName() + "(" + people.getEmail() + ")");
                }

            }

        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((CompanyData) resultValue).getName();
            if (status == 1){
                return "";
            }
            else{
                return str;
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CompanyData people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase()) || people.getRm_email().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                suggestions.clear();
                suggestions.addAll(tempItems);
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<CompanyData> filterList = (ArrayList<CompanyData>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CompanyData people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}