package org.me.gcu.adekunle_ganiyat_s2110996.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.adekunle_ganiyat_s2110996.R;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Forecast;

import java.util.List;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ForecastViewHolder> {

    private Context mContext;
    private List<Forecast> mForecastList;

    public interface OnItemClickListener {
        void onItemClick(Forecast forecast);
    }

    private OnItemClickListener onItemClickListener;

    public WeatherForecastAdapter(Context context, List<Forecast> forecastList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mForecastList = forecastList;
        this.onItemClickListener = onItemClickListener;
    }

    public void updateForecastList(List<Forecast> newForecastList) {
        this.mForecastList = newForecastList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        if (mForecastList != null && position < mForecastList.size()) {
            Forecast forecast = mForecastList.get(position);
            if (forecast != null) {
                holder.bind(forecast);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mForecastList != null ? mForecastList.size() : 0;
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView dayTextView;
        private TextView dateTextView;
        private TextView weatherTextView;
        private TextView temperatureTextView;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            weatherTextView = itemView.findViewById(R.id.weatherTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Forecast forecast = mForecastList.get(position);
                    onItemClickListener.onItemClick(forecast);
                }
            });
        }

        public void bind(Forecast forecast) {
            dayTextView.setText(forecast.getTime());
            dateTextView.setText(forecast.getDate());
            weatherTextView.setText(forecast.getHumidity());
            temperatureTextView.setText(mContext.getString(R.string.temperature_range, forecast.getMinTemperature(), forecast.getMaxTemperature()));
        }
    }
}
