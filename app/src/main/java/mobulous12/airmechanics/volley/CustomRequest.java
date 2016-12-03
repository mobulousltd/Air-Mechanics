package mobulous12.airmechanics.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by mobulous8 on 10/3/16.
 */
public class CustomRequest extends Request<JSONObject>
{
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;
    private static final String BASE_URL = "http://mobulous.co.in/airMechanics/";
    private HttpEntity mHttpEntity;
    private MultipartEntityBuilder entity;
    private MultipartEntity mentity;

//    Use this constructor for GET Method...
    public CustomRequest(String url, Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener, Context ctx)
    {
        super(Request.Method.GET, url, errorListener);
        this.listener = reponseListener;
    }


    //parameters in form of String in Map
//    We can use this method when we have to uploade on string data after enabling getParams() method... ******************
    public CustomRequest(int method, String url, Map<String, String> params,
                         Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener, Context ctx)
    {
        super(method, BASE_URL + url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }
    public CustomRequest(String url, Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener)
    {
        super(Request.Method.GET, url, errorListener);
        this.listener = reponseListener;
    }
    //parameters in form of Multipart
    public CustomRequest(String url, MultipartEntityBuilder entity, Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener, Context ctx)
    {
        super(Request.Method.POST, BASE_URL + url, errorListener);
        this.listener = reponseListener;
        this.entity = entity;
    }
//    public CustomRequest(String url, StringEntity entity, Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener, Context ctx)
//    {
//        super(Request.Method.POST, BASE_URL + url, errorListener);
//        this.listener = reponseListener;
//        this.entity = entity;
//    }
    public CustomRequest(String url, MultipartEntity entity, Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener, Context ctx)
    {
        super(Request.Method.POST, BASE_URL + url, errorListener);
        this.listener = reponseListener;
        this.mentity = entity;
    }
//    If you need to send string data only
    protected Map<String, String> getParams() throws AuthFailureError
    {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    //    If you need to send any type of data
    @Override
    public String getBodyContentType()
    {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            mHttpEntity = entity.build();
            mHttpEntity.writeTo(bos);
        }
        catch (IOException e)
        {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
            Log.e("CustomRequest",e.getMessage());
        }
        return bos.toByteArray();
    }


/* To send back response to particular activity/fragment */

    /** A Response encapsulates a parsed response for delivery, for a given type (such as string, image, or JSON)
     * Volley calls parseNetworkResponse() from a worker thread.
     * **/
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        }
        catch (JSONException je)
        {
            return Response.error(new ParseError(je));
        }
    }

    /* Volley calls you back on the main thread with the object you returned in parseNetworkResponse() method.
     * Most requests invoke a callback interface here  **
     */
    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
}
