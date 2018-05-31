package com.ezhealthtrack.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.fragments.LabOrdersFragment;
import com.ezhealthtrack.model.LabOrder;

import java.util.List;

public class LabOrdersAdapter extends EzListFragmentAdaptor {

    /* private view holder class */
    private static class ViewHolder {
        TextView txtDetails;
        TextView txtIndex;
    }

    private final Context context;

    // private static ArrayList<Appointment> objects = new
    // ArrayList<Appointment>();
    // private ArrayList<Appointment> historyList = new
    // ArrayList<Appointment>();

    public LabOrdersAdapter(List<Object> items, LabOrdersFragment fragment) {
        super(items, fragment);
        context = fragment.getActivity();
    }

    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        super.getView(position);

        final LabOrder rowItem = (LabOrder) mDataList.get(position);
        final ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            final LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_lab_order, parent, false);
            mHolder.txtDetails = (TextView) convertView.findViewById(R.id.txt_details);
            mHolder.txtIndex = (TextView) convertView.findViewById(R.id.txt_index);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.txtIndex.setText("" + (position + 1) + ".");
        mHolder.txtDetails.setText(Html.fromHtml("<b>Order:</b> "
                + rowItem.getDisplayId() + ", <b>Patient:</b> "
                + rowItem.getPatientDetail() + ", <b>Lab:</b> "
                + rowItem.getLabName() + ", <b>Date:</b> "
                + rowItem.getOrderDate() + ", <br><b>Order Status:</b> "
                + rowItem.getOrderStatus() + ", <b>Technician:</b> "
                + rowItem.getAssignTo() + ", <b>Approval Status:</b> "
                + rowItem.getApprovalStatus()));
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}