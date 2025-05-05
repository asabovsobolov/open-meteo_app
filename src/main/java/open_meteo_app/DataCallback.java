package open_meteo_app;

public interface DataCallback {
    void onSuccess(String data);
    void onFailure(Exception e);
}
