package mario.com.br.velocita.holder;

import android.location.Location;

import mario.com.br.velocita.cluster.ProgressClusterItem;

public class Data {

    private long startTime;
    private long endTime;
    private long lastTime, currentTime;
    private long elapsedTime;
    private Location currentLocation;
    private Location lastLocation;
    private int speed, maxSpeed, avgSpeed;
    private double distance;
    private double accuracy;
    private float bearing;
    private Indicator currentIndicator;
    private boolean first;
    private ProgressClusterItem startMarker, endMarker;

    private OnGpsServiceUpdate onGpsServiceUpdate;

    public Data(OnGpsServiceUpdate onGpsServiceUpdate) {
        this.onGpsServiceUpdate = onGpsServiceUpdate;
        this.reset();
    }

    public void elapsedTime() {
        this.setElapsedTime(currentTime - lastTime);
    }

    public boolean hasStartMarker() {
        return startMarker != null;
    }

    public long getTotalElapsedTime() {
        return endTime - startTime;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public float getBearing() {
        return bearing;
    }

    public interface OnGpsServiceUpdate {
        void update();
    }

    /**
     * Atualiza a view com os dados do GPS
     */
    public void update() {

        if (this.onGpsServiceUpdate != null)
            this.onGpsServiceUpdate.update();
    }

    public void reset() {
        this.startTime = 0;
        this.endTime = 0;
        this.lastTime = 0;
        this.currentTime = 0;
        this.elapsedTime = 0;
        this.lastLocation = null;
        this.currentLocation = null;
        this.maxSpeed = 0;
        this.avgSpeed = 0;
        this.distance = 0d;
        this.bearing = 0;
        this.currentIndicator = Indicator.STANDBY;
        this.first = true;
        this.startMarker = null;
        this.endMarker = null;
    }

    public boolean isProgress() {
        return currentIndicator == Indicator.PROGRESS;
    }

    public boolean isNavigate() {
        return currentIndicator == Indicator.STANDBY;
    }

    public boolean isPendingProgress() {
        return currentIndicator == Indicator.PENDING_PROGRESS;
    }

    /**
     * Incrementa a distância atual
     *
     * @param distance
     */
    public void addDistance(double distance) {
        this.distance += distance;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(int avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Indicator getCurrentIndicator() {
        return currentIndicator;
    }

    public void setCurrentIndicator(Indicator currentIndicator) {
        this.currentIndicator = currentIndicator;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public ProgressClusterItem getStartMarker() {
        return startMarker;
    }

    public void setStartMarker(ProgressClusterItem startMarker) {
        this.startMarker = startMarker;
    }

    public ProgressClusterItem getEndMarker() {
        return endMarker;
    }

    public void setEndMarker(ProgressClusterItem endMarker) {
        this.endMarker = endMarker;
    }
}
