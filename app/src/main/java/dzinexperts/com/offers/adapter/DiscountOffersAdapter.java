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
import dzinexperts.com.offers.Utils.PhotoFullPopupWindow;
import dzinexperts.com.offers.apis.Datum;

public class DiscountOffersAdapter extends RecyclerView.Adapter<DiscountOffersAdapter.VHolder> {

    //Variables
    List<Datum> datas;
    Context context;
    private String TAG = "DiscountOffersAdapter";
    private int lastPosition = -1;
    private View view;


    public DiscountOffersAdapter(List<Datum> datas, Context context){
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
        view = inflater.inflate(R.layout.discountoffers_cardview,parent,false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
        setFromtoDate(holder,0,position);
        setFromtoDate(holder,1,position);

        holder.textTitle.setText(datas.get(position).getDiscItem());
        holder.textView.setText(datas.get(position).getDescription());
        holder.rates.setText("Price PKR. "+datas.get(position).getRate()+"/-");
        holder.disc_rates.setText("Deal PKR. "+datas.get(position).getDiscRate()+"/-");
        holder.rates.setPaintFlags(holder.rates.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Glide.with(context)
                .load(datas.get(position).getDiscImg())
                .into(holder.imageView);
    }

    private void setFromtoDate(VHolder holder, int i, int position) {
        String tempDate = "",prefix="";

        if(i==0){
            tempDate = datas.get(position).getDiscFdate().substring(0,datas.get(position).getDiscFdate().indexOf(" "));
            prefix = "Valid from:";
        } else {
            tempDate = datas.get(position).getDiscTdate().substring(0,datas.get(position).getDiscTdate().indexOf(" "));
            prefix = "Deal Expire: ";
        }

        SimpleDateFormat input = new SimpleDateFormat("yyyy-dd-MM");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM, yyyy");
        try {
            Date oneWayTripDate = input.parse(tempDate);                 // parse input
            if(i==0)
                Log.d(TAG,prefix+output.format(oneWayTripDate));
//                holder.textDatefrom.setText(prefix+output.format(oneWayTripDate));
            else
                holder.textDateto.setText(prefix+output.format(oneWayTripDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{
        TextView textView,textTitle,textDatefrom,textDateto,rates,disc_rates;
        ImageView imageView;
        public VHolder(View itemView) {
            super(itemView);
//            textDatefrom = (TextView) itemView.findViewById(R.id.validfrom);
            rates = (TextView) itemView.findViewById(R.id.ratess);
            disc_rates = (TextView) itemView.findViewById(R.id.disc_ratess);
            textDateto = (TextView) itemView.findViewById(R.id.validto);
            textTitle = (TextView) itemView.findViewById(R.id.tvtitle);
            textView = (TextView) itemView.findViewById(R.id.tv1cv);
            imageView = (ImageView) itemView.findViewById(R.id.iv1cv);
        }
    }
}
