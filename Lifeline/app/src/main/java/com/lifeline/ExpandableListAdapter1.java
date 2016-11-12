package com.lifeline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
   Adapter for an expandable list.
 */
public class ExpandableListAdapter1 extends BaseExpandableListAdapter {
    private Activity _context;
    private List<String> _listDataHeader;
    private HashMap<String,List<String>> _listDataChild;
    int[] _drawerImageIds;
    int[] _prodImageIds;
    int[] _accountImageIds;
    private Typeface custom_font,custom_font1;

    public ExpandableListAdapter1(Activity context, List<String> listDataHeader, int[] drawerImageIds, Typeface custom, Typeface custom2) {
        this._context=context;
        this._listDataHeader=listDataHeader;
        this._drawerImageIds=drawerImageIds;
        this.custom_font=custom;
        this.custom_font1=custom2;
    }
    public ExpandableListAdapter1(Activity context, List<String> listDataHeader, int[] drawerImageIds, int[] prodImageIds, int[] accountImageIds,
                                  HashMap<String, List<String>> listChildData, Typeface custom, Typeface custom2) {
        this._context=context;
        this._listDataHeader=listDataHeader;
        this._drawerImageIds=drawerImageIds;
        this._listDataChild=listChildData;
        this._prodImageIds=prodImageIds;
        this._accountImageIds=accountImageIds;
        this.custom_font=custom;
        this.custom_font1=custom2;
    }
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }



    @Override
    public int getChildrenCount(int groupPosition) {
         return 0;
         // return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                //.get(childPosition);
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exp_group_item, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.listTitle);
        ImageView lblListDrawable=(ImageView)convertView.findViewById(R.id.listIcon);
        lblListHeader.setTypeface(custom_font);
        lblListHeader.setText(_listDataHeader.get(groupPosition));
        lblListDrawable.setImageResource(_drawerImageIds[groupPosition]);

        return convertView;
    }
    public  int getImageId(int groupPosition,int childPosition)
{
    if(groupPosition==1)
        return _prodImageIds[childPosition];
    else
        return _accountImageIds[childPosition];

}
    /*
     each child item's view (appearance)
   */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

//        final String childText = (String)getChild(groupPosition, childPosition);
//        if (convertView == null) {
//            LayoutInflater infalInflater = (LayoutInflater) this._context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = infalInflater.inflate(R.layout.exp_child_item, null);
//        }
//        TextView checkListChild = (TextView)convertView
//                .findViewById(R.id.expandedListItem);
//        ImageView imageView=(ImageView)convertView.findViewById(R.id.expandedListIcon);
//        checkListChild.setText(childText);
//        checkListChild.setTypeface(custom_font1);
//        imageView.setImageResource(getImageId(groupPosition,childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
