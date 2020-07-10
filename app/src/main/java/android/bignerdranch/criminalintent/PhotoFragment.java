package android.bignerdranch.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class PhotoFragment extends DialogFragment {

    private final static String ARG_PHOTO = "path";

    public static PhotoFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO, path);

        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String path = getArguments().getString(ARG_PHOTO);
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_photo, null);

        Bitmap bitmap = PictureUtils
                .getScaledBitmap(path, getActivity());


        ImageView photoImageView = (ImageView)v
                .findViewById(R.id.crime_photo_zoomed_in);

        photoImageView.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Enlarged Photo")
                .create();
    }
}