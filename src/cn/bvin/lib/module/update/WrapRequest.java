package cn.bvin.lib.module.update;

import cn.bvin.lib.module.net.Request;
import cn.bvin.lib.module.net.RequestParam;
/**
 * 对NetworkModule里Request包装，以后统一
 * 用NetworkModule里的Request
 */
public class WrapRequest extends Request{

	public WrapRequest(String url) {
		super(url);
	}

	public WrapRequest(String url, RequestParam param) {
		super(url, param);
	}

}
