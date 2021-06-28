package cn.bvin.lib.module.update;

import cn.bvin.lib.module.net.convert.DataConvertor;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;
/**
 * CheckRequest的默认实现，负责把网络响应转换成标准的UpdateInfo
 */
public class DefaultCheckRequest extends CheckRequest{

	
	DataConvertor<UpdateInfo> convertor;
	
	public DefaultCheckRequest(WrapRequest wr,DataConvertor<UpdateInfo> convertor, CheckListener checkListener) {
		super(wr, checkListener);
		this.convertor = convertor;
	}

	public DefaultCheckRequest(int method, WrapRequest wr, DataConvertor<UpdateInfo> convertor,CheckListener checkListener) {
		super(method, wr, checkListener);
		this.convertor = convertor;
	}

	@Override
	protected Response<UpdateInfo> parseNetworkResponse(NetworkResponse arg0) {
		try {
			return Response.success(convertor.convert(arg0.data), HttpHeaderParser.parseCacheHeaders(arg0));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}

}
