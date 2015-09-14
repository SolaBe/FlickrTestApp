package android.flickrtestapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Sola2Be on 14.09.2015.
 */
public class AdapterGrid extends ArrayAdapter<Bitmap> {

    private List<Bitmap> bitmapList;
    private LayoutInflater inflater;
    private VHolder holder;
    public AdapterGrid(Context context, int resource, List<Bitmap> objects) {
        super(context, resource, objects);
        bitmapList = objects;
        inflater = LayoutInflater.from(context);
    }

    class VHolder{
        ImageView image;
    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public int getPosition(Bitmap item) {
        return bitmapList.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(view == null) {
            holder = new VHolder();
            view = inflater.inflate(R.layout.grid_item, null, false);
            holder.image = (ImageView) view.findViewById(R.id.imageView);
            view.setTag(holder);
        }
        else
            holder = (VHolder) view.getTag();
            holder.image.setImageBitmap(bitmapList.get(position));
        return view;
    }
}
