package de.dennisguse.opentracks.ui.aggregatedStatistics;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.ActivityType;
import de.dennisguse.opentracks.data.models.DistanceFormatter;
import de.dennisguse.opentracks.data.models.SpeedFormatter;
import de.dennisguse.opentracks.settings.PreferencesUtils;
import de.dennisguse.opentracks.settings.UnitSystem;
import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.util.StringUtils;
import static de.dennisguse.opentracks.settings.PreferencesUtils.shouldElevationGain;
import static de.dennisguse.opentracks.settings.PreferencesUtils.shouldDistance;
import static de.dennisguse.opentracks.settings.PreferencesUtils.shouldMaxSpeed;
import static de.dennisguse.opentracks.settings.PreferencesUtils.shouldMovingSpeed;
import static de.dennisguse.opentracks.settings.PreferencesUtils.shouldMovingTime;

public class AggregatedStatisticsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AggregatedStatistics aggregatedStatistics;
    private final Context context;

    public AggregatedStatisticsAdapter(Context context, AggregatedStatistics aggregatedStatistics) {
        this.context = context;
        this.aggregatedStatistics = aggregatedStatistics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aggregated_stats_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        AggregatedStatistics.AggregatedStatistic aggregatedStatistic = aggregatedStatistics.getItem(position);

        String type = aggregatedStatistic.getActivityTypeLocalized();
        if (ActivityType.findByLocalizedString(context, type).isShowSpeedPreferred()) {
            viewHolder.setSpeed(aggregatedStatistic);
            if(shouldElevationGain()) {
                viewHolder.setElevation(aggregatedStatistic);
            }
            if(shouldDistance()) {
                Log.d("myLogs", "ShouldDistance is called");
                viewHolder.setDistance(aggregatedStatistic, true);
            }
            else {
                Log.d("myLogs", "Inside Else");
                viewHolder.setDistance(aggregatedStatistic, false);
            }
            if(shouldMaxSpeed()) {
                viewHolder.setMaxSpeed(aggregatedStatistic, true);
            }
            else {
                viewHolder.setMaxSpeed(aggregatedStatistic, false);
            }
            if(shouldMovingSpeed()){
                viewHolder.setMovingSpeed(aggregatedStatistic, true);
            }
            else {
                viewHolder.setMovingSpeed(aggregatedStatistic, false);
            }
            if(shouldMovingTime()){
                viewHolder.setMovingTime(aggregatedStatistic, true);
            }
            else {
                viewHolder.setMovingTime(aggregatedStatistic, false);
            }
        } else {
            viewHolder.setPace(aggregatedStatistic);
        }
    }

    @Override
    public int getItemCount() {
        if (aggregatedStatistics == null) {
            return 0;
        }
        return aggregatedStatistics.getCount();
    }

    public void swapData(AggregatedStatistics aggregatedStatistics) {
        this.aggregatedStatistics = aggregatedStatistics;
        this.notifyDataSetChanged();
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < aggregatedStatistics.getCount(); i++) {
            categories.add(aggregatedStatistics.getItem(i).getActivityTypeLocalized());
        }
        return categories;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView sportIcon;
        private final TextView typeLabel;
        private final TextView numTracks;
        private final TextView distance;
        private final TextView distanceUnit;
        private final TextView time;

        private final TextView avgSpeed;
        private final TextView avgSpeedUnit;
        private final TextView avgSpeedLabel;
        private final TextView maxSpeed;
        private final TextView maxSpeedUnit;
        private final TextView maxSpeedLabel;

        private TextView elevationGain = null;
        private TextView elevationLabel = null;
        private TextView elevationUnit = null;

        private UnitSystem unitSystem = UnitSystem.defaultUnitSystem();
        private boolean reportSpeed;

        public ViewHolder(View view) {
            super(view);
            sportIcon = view.findViewById(R.id.aggregated_stats_sport_icon);
            typeLabel = view.findViewById(R.id.aggregated_stats_type_label);
            numTracks = view.findViewById(R.id.aggregated_stats_num_tracks);
            distance = view.findViewById(R.id.aggregated_stats_distance);
            distanceUnit = view.findViewById(R.id.aggregated_stats_distance_unit);
            time = view.findViewById(R.id.aggregated_stats_time);
            avgSpeed = view.findViewById(R.id.aggregated_stats_avg_rate);
            avgSpeedUnit = view.findViewById(R.id.aggregated_stats_avg_rate_unit);
            avgSpeedLabel = view.findViewById(R.id.aggregated_stats_avg_rate_label);
            maxSpeed = view.findViewById(R.id.aggregated_stats_max_rate);
            maxSpeedUnit = view.findViewById(R.id.aggregated_stats_max_rate_unit);
            maxSpeedLabel = view.findViewById(R.id.aggregated_stats_max_rate_label);
            if(shouldElevationGain()) {
                elevationGain = view.findViewById(R.id.aggregated_stats_elevation_gain);
                elevationLabel = view.findViewById(R.id.aggregated_stats_elevation_gain_label);
                elevationUnit = view.findViewById(R.id.aggregated_stats_elevation_gain_unit);

                elevationLabel.setText("Elevation Gain");
                elevationUnit.setText("ft");
            }
        }
        public void setElevation(AggregatedStatistics.AggregatedStatistic aggregatedStatistic) {
            if(aggregatedStatistic.getTrackStatistics().getTotalAltitudeGain()==null)
                elevationGain.setText("0.0");
            else
                elevationGain.setText(aggregatedStatistic.getTrackStatistics().getTotalAltitudeGain().toString());
            System.out.println();
        }

        public void setDistance(AggregatedStatistics.AggregatedStatistic aggregatedStatistic, Boolean visibility) {
            if(visibility) {
                if (aggregatedStatistic.getTrackStatistics().getTotalDistance() == null) {
                    Log.d("myLogs", "aggregate value is null");
                    distance.setText("0.0");
                } else {
                    Log.d("myLogs", "Visibility true, setting value in else");
                    Pair<String, String> parts = DistanceFormatter.Builder()
                            .setUnit(unitSystem)
                            .build(context).getDistanceParts(aggregatedStatistic.getTrackStatistics().getTotalDistance());
                    distance.setText(parts.first);
                    distanceUnit.setText(parts.second);
                }
            }
            else{
                Log.d("myLogs", "Visibility is false");
                distance.setText("");
                distanceUnit.setText("");
            }
        }

        public void setMaxSpeed(AggregatedStatistics.AggregatedStatistic aggregatedStatistic, Boolean visibility) {
            SpeedFormatter formatter = SpeedFormatter.Builder().setUnit(unitSystem).setReportSpeedOrPace(reportSpeed).build(context);
            if(visibility){
                if(aggregatedStatistic.getTrackStatistics().getMaxSpeed() ==null)
                    maxSpeed.setText("0.0");

                else
                {
                    Pair<String, String> parts = formatter.getSpeedParts(aggregatedStatistic.getTrackStatistics().getMaxSpeed());
                    maxSpeed.setText(parts.first);
                    maxSpeedUnit.setText(parts.second);
                    maxSpeedLabel.setText(context.getString(R.string.stats_max_speed));
                }
            }
            else{
                maxSpeed.setText("");
                maxSpeedUnit.setText("");
                maxSpeedLabel.setText("");
            }

        }

        public void setMovingSpeed(AggregatedStatistics.AggregatedStatistic aggregatedStatistic, Boolean visibility) {
            SpeedFormatter formatter = SpeedFormatter.Builder().setUnit(unitSystem).setReportSpeedOrPace(reportSpeed).build(context);
            if(visibility){
                if(aggregatedStatistic.getTrackStatistics().getMaxSpeed() ==null)
                    avgSpeed.setText("0.0");
                else
                {
                    Pair<String, String> parts = formatter.getSpeedParts(aggregatedStatistic.getTrackStatistics().getMaxSpeed());
                    avgSpeed.setText(parts.first);
                    avgSpeedUnit.setText(parts.second);
                    avgSpeedLabel.setText(context.getString(R.string.stats_max_speed));
                }
            }
            else{
                avgSpeed.setText("");
                avgSpeedUnit.setText("");
                avgSpeedLabel.setText("");
            }

        }
        public void setMovingTime(AggregatedStatistics.AggregatedStatistic aggregatedStatistic, Boolean visibility) {
            SpeedFormatter formatter = SpeedFormatter.Builder().setUnit(unitSystem).setReportSpeedOrPace(reportSpeed).build(context);
            if(visibility){
                if(aggregatedStatistic.getTrackStatistics().getMaxSpeed() ==null)
                    time.setText("0.0");
                else
                {
                    time.setText(StringUtils.formatElapsedTime(aggregatedStatistic.getTrackStatistics().getMovingTime()));
                }
            }
            else{
                time.setText("");
            }

        }
        public void setSpeed(AggregatedStatistics.AggregatedStatistic aggregatedStatistic) {
            setCommonValues(aggregatedStatistic);

            SpeedFormatter formatter = SpeedFormatter.Builder().setUnit(unitSystem).setReportSpeedOrPace(reportSpeed).build(context);
            {
                Pair<String, String> parts = formatter.getSpeedParts(aggregatedStatistic.getTrackStatistics().getAverageMovingSpeed());
                avgSpeed.setText(parts.first);
                avgSpeedUnit.setText(parts.second);
                avgSpeedLabel.setText(context.getString(R.string.stats_average_moving_speed));
            }

            {
                Pair<String, String> parts = formatter.getSpeedParts(aggregatedStatistic.getTrackStatistics().getMaxSpeed());
                maxSpeed.setText(parts.first);
                maxSpeedUnit.setText(parts.second);
                maxSpeedLabel.setText(context.getString(R.string.stats_max_speed));
            }
        }

        public void setPace(AggregatedStatistics.AggregatedStatistic aggregatedStatistic) {
            setCommonValues(aggregatedStatistic);

            SpeedFormatter formatter = SpeedFormatter.Builder().setUnit(unitSystem).setReportSpeedOrPace(reportSpeed).build(context);
            {
                Pair<String, String> parts = formatter.getSpeedParts(aggregatedStatistic.getTrackStatistics().getAverageMovingSpeed());
                avgSpeed.setText(parts.first);
                avgSpeedUnit.setText(parts.second);
                avgSpeedLabel.setText(context.getString(R.string.stats_average_moving_pace));
            }

            {
                Pair<String, String> parts = formatter.getSpeedParts(aggregatedStatistic.getTrackStatistics().getMaxSpeed());
                maxSpeed.setText(parts.first);
                maxSpeedUnit.setText(parts.second);
                maxSpeedLabel.setText(R.string.stats_fastest_pace);
            }
        }

        //TODO Check preference handling.
        private void setCommonValues(AggregatedStatistics.AggregatedStatistic aggregatedStatistic) {
            String activityType = aggregatedStatistic.getActivityTypeLocalized();

            reportSpeed = PreferencesUtils.isReportSpeed(activityType);
            unitSystem = PreferencesUtils.getUnitSystem();

            sportIcon.setImageResource(getIcon(aggregatedStatistic));
            typeLabel.setText(activityType);
            numTracks.setText(StringUtils.valueInParentheses(String.valueOf(aggregatedStatistic.getCountTracks())));

            Pair<String, String> parts = DistanceFormatter.Builder()
                    .setUnit(unitSystem)
                    .build(context).getDistanceParts(aggregatedStatistic.getTrackStatistics().getTotalDistance());
            distance.setText(parts.first);
            distanceUnit.setText(parts.second);

            time.setText(StringUtils.formatElapsedTime(aggregatedStatistic.getTrackStatistics().getMovingTime()));
        }

        private int getIcon(AggregatedStatistics.AggregatedStatistic aggregatedStatistic) {
            String localizedActivityType = aggregatedStatistic.getActivityTypeLocalized();
            return ActivityType.findByLocalizedString(context, localizedActivityType)
                    .getIconDrawableId();
        }
    }
}
