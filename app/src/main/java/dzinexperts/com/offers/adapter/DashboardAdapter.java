package dzinexperts.com.offers.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dzinexperts.com.offers.R;
import dzinexperts.com.offers.apis.DashboardData;
import dzinexperts.com.offers.apis.Datum;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.VHolder> {

    //Variables
    List<DashboardData> datas;
    Context context;
    private String TAG = "DashboardAdapter";
    private int lastPosition = -1;

    public DashboardAdapter(List<DashboardData> datas, Context context){
        this.datas = datas;
        this.context = context;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dashboard_cardview,parent,false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
        if (position == 0){
            holder.textCount.setText(datas.get(position).getTotalOffers().toString());
            holder.textTitle.setText("Total No. of Uploads");
        } else if (position == 1){
            holder.textCount.setText(datas.get(position).getBrochures().toString());
            holder.textTitle.setText("Total Uploaded Brochures");
        } else if (position == 2){
            holder.textCount.setText(datas.get(position).getDeals().toString());
            holder.textTitle.setText("Total Uploaded Deals");
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{
        TextView textCount,textTitle;
        ImageView imageView;
        public VHolder(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.cv2title);
            textCount = (TextView) itemView.findViewById(R.id.brochures);
        }
    }
}
