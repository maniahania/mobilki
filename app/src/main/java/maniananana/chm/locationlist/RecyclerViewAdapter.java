package maniananana.chm.locationlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import maniananana.chm.R;
import maniananana.chm.UserLocation;
import maniananana.chm.locationPoint.LocationPointRepository;
import maniananana.chm.locationPoint.Storage;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List<UserLocation> userLocationList;
    private final String userID;
    private final LocationPointRepository lpr = Storage.getLocationPointRepository();
    private FirebaseFirestore fStore;

    public RecyclerViewAdapter(List<UserLocation> userLocationList, String userID) {
        this.userLocationList = userLocationList;
        this.userID = userID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_location_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        fStore = FirebaseFirestore.getInstance();
        lpr.loadDataFromFirebase();
        holder.textView.setText(userLocationList.get(position).getName());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference df = fStore.collection("Users").document(userID);
                df.update("Locations", FieldValue.arrayRemove(userLocationList.get(position)));
                lpr.delete(userLocationList.get(position).getId());
                lpr.saveDataToFirebase();
                userLocationList.remove(userLocationList.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userLocationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageButton button;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            button = itemView.findViewById(R.id.button);
            relativeLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
