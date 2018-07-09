package dzinexperts.com.offers.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import dzinexperts.com.offers.R;
import dzinexperts.com.offers.Utils.PhotoFullPopupWindow;
import dzinexperts.com.offers.apis.DatumImg;

public class ImageOffersAdapter extends RecyclerView.Adapter<ImageOffersAdapter.VHolder> {

    List<DatumImg> datas;
    Context context;
    private String TAG = "ImageOffersAdapter";
    private int lastPosition = -1;
    private View view;

    public ImageOffersAdapter(List<DatumImg> datas, Context context){
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
        view = inflater.inflate(R.layout.imageoffers_cardview,parent,false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(VHolder holder, final int position) {
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;

        Glide.with(context)
                .load(datas.get(position).getOfferImg())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, datas.get(position).getOfferImg(), null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public VHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivimgofferscv);
        }
    }
}
