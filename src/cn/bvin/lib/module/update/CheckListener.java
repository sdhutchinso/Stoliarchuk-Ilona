package cn.bvin.lib.module.update;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

import cn.bvin.lib.module.net.RequestParam;
/**
 * 检查更新异步监听器
 */
public abstract class CheckListener{

	/**
	 * 开始检测更新，是一个耗时的网络请求的过程
	 * @param url 检测更新的接口地址
	 * @param param 更新接口需要传的参数
	 */
	public abstract void onCheckStart(String url, RequestParam param);
	
	/**
	 * 检测更新成功
	 * @param updateInfo 服务器返回的信息
	 */
	public abstract void onCheckSuccess(UpdateInfo updateInfo);
	
	/**
	 * 检测更新失败
	 * @param e 失败原因
	 */
	public abstract void onCheckFailure(Throwable e);
	
	/**
	 * 暂时要迁就Volley
	 * ErrorListener的默认实现，用以回调标准的onCheckFailure(Throwable e)方法
	 * @return: ErrorListener 返回一个Volley的ErrorListener
	 */
	public ErrorListener dumpError() {
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				onCheckFailure(arg0);
			}};
	};
}
