package cn.bvin.lib.module.update;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
/**
 * CheckRequest的默认实现，负责把网络响应转换成标准的UpdateInfo
 */
public class DefaultCheckRequest extends CheckRequest{

	
	
	public DefaultCheckRequest(WrapRequest wr, CheckListener checkListener) {
		super(wr, checkListener);
	}

	public DefaultCheckRequest(int method, WrapRequest wr, CheckListener checkListener) {
		super(method, wr, checkListener);
	}

	@Override
	protected Response<UpdateInfo> parseNetworkResponse(NetworkResponse arg0) {
		try {
			String json = new String(arg0.data, HttpHeaderParser.parseCharset(arg0.headers));
			Gson gson = new Gson();
			return Response.success(gson.fromJson(json, UpdateInfo.class), HttpHeaderParser.parseCacheHeaders(arg0));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new VolleyError(e));
		} catch (JsonSyntaxException e) {
			 return Response.error(new ParseError(e));
		}
	}

}
