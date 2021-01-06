package maniananana.chm.locationlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import maniananana.chm.R;
import maniananana.chm.locationPoint.LocationPointRepository;
import maniananana.chm.locationPoint.Storage;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<String> names;
    private ArrayList<String> ids;
    private String userID;
    private Context mContext;
    private LocationPointRepository lpr = Storage.getLocationPointRepository();
    private FirebaseFirestore fStore;

    public RecyclerViewAdapter(ArrayList<String> names, ArrayList<String> ids, String userID, Context mContext) {
        this.names = names;
        this.ids = ids;
        this.userID = userID;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_location_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        fStore = FirebaseFirestore.getInstance();
        lpr.loadDataFromFirebase(mContext);
        holder.textView.setText(names.get(position));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference df = fStore.collection("Users").document(userID);
                df.update("Locations", FieldValue.arrayRemove(lpr.find(ids.get(position)).toString()));
                lpr.delete(ids.get(position));
                lpr.saveDataToFirebase(mContext);
                names.remove(names.get(position));
                ids.remove(ids.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        Button button;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            button = itemView.findViewById(R.id.button);
            relativeLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
