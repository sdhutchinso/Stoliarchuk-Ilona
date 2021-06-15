package cn.bvin.lib.module.update;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;

public abstract class CheckRequest extends Request<UpdateInfo>{

	private CheckListener checkListener;
	private WrapRequest wrapRequest;
	
	/**
	 * 检查更新请求
	 * @param method Http请求方法[GET/POST]
	 * @param wr 请求包装体，包括请求地址url和请求参数
	 * @param checkListener 检测监听器
	 */
	public CheckRequest(int method,WrapRequest wr, CheckListener checkListener) {
		super(method, wr.getUrl(),checkListener.dumpError());
		this.checkListener = checkListener;
		this.wrapRequest = wr;
	}
	
	/**
	 *  检查更新请求，无method形参，将自动根据WrapRequest有没有参数来使用什么方法，
	 *  有参数用POST，没有参数就用GET
	 * @param wr 请求包装体，包括请求地址url和请求参数
	 * @param checkListener 检测监听器
	 */
	public CheckRequest(WrapRequest wr,CheckListener checkListener) {
		super(wr.getUrl(), checkListener.dumpError());
		this.checkListener = checkListener;
		this.wrapRequest = wr;
	}


	@Override
	protected void deliverResponse(UpdateInfo arg0) {
		if (arg0!=null&&checkListener!=null) {
			this.checkListener.onCheckSuccess(arg0);
		}
	}


	@Override
	public byte[] getBody() throws AuthFailureError {
		//如果有参数就把
		if (wrapRequest!=null&&wrapRequest.getParam()!=null) {
			//建议使用MapParams参数形式
			if (wrapRequest.getParam().getMapParams()!=null) {
				return wrapRequest.getParam().getBytes();
			}
		}
		return super.getBody();
	}

	
}
