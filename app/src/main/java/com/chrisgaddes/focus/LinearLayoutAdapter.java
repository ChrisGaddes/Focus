package com.chrisgaddes.focus;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Nikunj Popat on 16-12-2015.
 */

public class LinearLayoutAdapter extends CustomRecyclerViewAdapter {
    private Activity activity;
    private ArrayList<String> images;
    private int screenWidth;
    private String str_probCurrent_file_name;

    public LinearLayoutAdapter(Activity activity, ArrayList<String> images) {
        this.activity = activity;
        this.images = images;

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public CustomRecyclerViewAdapter.CustomRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.demo_images, parent, false);
        Holder dataObjectHolder = new Holder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final CustomRecyclerViewAdapter.CustomRecycleViewHolder holder, final int position) {
        final Holder myHolder = (Holder) holder;


//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(images.get(position), opts);
//        opts.inJustDecodeBounds = false;


//        images.get(position)

        str_probCurrent_file_name = images.get(position);
//        getResources().getIdentifier(str_partCurrent_file_name, "drawable", getPackageName()

        getDrawableResourceID(activity, str_probCurrent_file_name);


        Glide.with(activity)
                .load(getDrawableResourceID(activity, str_probCurrent_file_name))
                .error(R.drawable.card_blank)
                .placeholder(R.drawable.card_blank)
                .centerCrop()
                .into((myHolder.images));


        Glide.with(activity)
                .load(getDrawableResourceID(activity, str_probCurrent_file_name))
                .error(R.drawable.card_blank)
                .placeholder(R.drawable.card_blank)
                .centerCrop()
                .into((myHolder.imagesright));



//        Picasso.with(activity)
//                .load(activity.getResources().getIdentifier("clubs_10", "drawable", activity.getPackageName()))
//                .error(R.drawable.ic_empty)
//                .placeholder(R.drawable.ic_launcher)
//                .resize(500, 500)
//                .centerCrop()
//                .into((myHolder.images));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class Holder extends CustomRecyclerViewAdapter.CustomRecycleViewHolder {
        private ImageView images;
        private ImageView imagesright;

        public Holder(View itemView) {
            super(itemView);
            images = (ImageView) itemView.findViewById(R.id.ivItemGridImage);
            imagesright = (ImageView) itemView.findViewById(R.id.ivItemGridImageRight);
        }
    }

    public static int getRawResourceID(Context context, String rawResourceName) {
        return context.getResources().getIdentifier(rawResourceName, "raw", context.getPackageName());
    }
    public static int getDrawableResourceID(Context context, String drawableResourceName) {
        return context.getResources().getIdentifier(drawableResourceName, "drawable", context.getPackageName());
    }

    private class MyCache {

        private String DiretoryName;

        private void OpenOrCreateCache(Context _context, String _directoryName) {
            File file = new File(_context.getCacheDir().getAbsolutePath() + "/" + _directoryName);
            if (!file.exists()) {
                file.mkdir();
            }
            DiretoryName = file.getAbsolutePath();
        }

        private void SaveBitmap(String fileName, Bitmap bmp) {
            try {
                File file = new File(DiretoryName + "/" + fileName);
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream Filestream = new FileOutputStream(DiretoryName + "/" + fileName);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Filestream.write(byteArray);
                Filestream.close();
                bmp = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Bitmap OpenBitmap(String name) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                File file = new File(DiretoryName + "/" + name);
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(DiretoryName + "/" + name, options);
                    return bitmap;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
