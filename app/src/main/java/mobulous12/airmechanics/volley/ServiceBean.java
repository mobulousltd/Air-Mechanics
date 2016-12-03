package mobulous12.airmechanics.volley;

import android.app.Activity;
import android.support.v4.app.Fragment;

import org.json.JSONObject;

import java.util.Map;

public class ServiceBean
{
    private Activity activity;
    private JSONObject jsonObject;
    private String methodName;
    private Fragment fragment;
    private boolean isLoader = true;
    private String LoadingMessage="";
    private Map<String, String> params;
    private boolean retrypolicy=true;
    private ApiListener apilistener;

    public ApiListener getApilistener() {
        return apilistener;
    }

    public void setApilistener(ApiListener apilistener) {
        this.apilistener = apilistener;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getLoadingMessage() {
        return LoadingMessage;
    }

    public void setLoadingMessage(String loadingMessage) {
        LoadingMessage = loadingMessage;
    }

    public boolean isLoader() {
        return isLoader;
    }

    public void setIsLoader(boolean isLoader) {
        this.isLoader = isLoader;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isRetrypolicy() {
        return retrypolicy;
    }

    public void setRetrypolicy(boolean retrypolicy) {
        this.retrypolicy = retrypolicy;
    }
}
