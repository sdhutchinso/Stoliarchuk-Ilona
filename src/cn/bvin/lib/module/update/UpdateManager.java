package cn.bvin.lib.module.update;

import java.util.Map;

import com.android.volley.Request.Method;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import cn.bvin.lib.module.net.MapParam;
import cn.bvin.lib.module.net.RequestParam;
import cn.bvin.lib.module.net.convert.DataConvertor;
import cn.bvin.lib.module.net.volley.RequestManager;
import cn.bvin.lib.module.update.model.UpdateModel;
import cn.bvin.lib.module.utils.ToastUtils;
import cn.bvin.library.debug.DebugeHelper;
import cn.bvin.library.debug.SimpleLogger;
import cn.bvin.library.utils.StringUtils;

public class UpdateManager {

	private Context context;
	
	private CheckMode checkMode = CheckMode.ForeGround;
	
	private ToastUtils toast;
	private Dialog dialog;
	
	/**
	 * 检查更新模式
	 */
	public enum CheckMode{
		
		/** 前台模式，检测会弹出 对话框*/
		ForeGround,
		/** 后台模式，检测只弹出toast文字提示*/
		BackGrouud;
		
	}
	
	/**
	 * 更新模式
	 */
	public enum UpdateMode{
		
		/** 前台模式，检测会弹出更新确认 对话框*/
		ForeGround,
		/** 后台模式，在后台下载*/
		BackGrouud;
		
	}
	
	
	
	private UpdateManager() {
	}

	private UpdateManager(Context context) {
		this.context = context;
		toast = new ToastUtils(context);
	}

	/**
	 * 获取一个UpdateManager对象
	 * @param context 只有用上下文才能用默认的CheckListener
	 * @return 返回一个UpdateManager对象
	 */
	public static UpdateManager with(Context context) {
		return new UpdateManager(context);
	}
	
	/**
	 * 如果完全自定义UI，就不需要Context
	 * @return 返回一个UpdateManager对象
	 */
	public static UpdateManager getInstance() {
		return new UpdateManager();
	}
	
	/**
	 * 检测更新，需要自己实现对检测状态的响应，如此也可以自己扩展符合自己项目的UI风格
	 * @param url 检测更新地址
	 * @param params 参数
	 * @param checkListener 检测更新状态监听器
	 */
	public void checkVersion(String url,Map<String, Object> params,DataConvertor<UpdateInfo> convertor,CheckListener checkListener) {
		WrapRequest wr = new WrapRequest(url,  new RequestParam(new MapParam(params)));
		CheckRequest cr = new DefaultCheckRequest(Method.POST,wr,convertor, checkListener);
		//添加请求前触发onCheckStart方法
		checkListener.onCheckStart(url, wr.getParam());
		RequestManager.addRequest(cr, cr.getUrl());
	}
	
	/**
	 * 以默认的方式来显示检测更新状态
	 * @param url 检测更新地址
	 * @param params 参数
	 */
	public void checkVersion(String url,Map<String, Object> params) {
		checkVersion(url, params, new UpdateInfoConvertor(),defaultCheckListener);
	}
	
	/**
	 * 检测更新，默认监听器，自定义接口数据转换器
	 * @param url 检测更新地址
	 * @param params 参数
	 * @param convertor 接口返回数据格式对应的转换器
	 */
	public void checkVersion(String url,Map<String, Object> params,DataConvertor<UpdateInfo> convertor) {
		checkVersion(url, params, convertor,defaultCheckListener);
	}
	
	private CheckListener defaultCheckListener = new DefaultCheckListener() {
		
		
		@Override
		public void onCheckStart(String url, RequestParam param) {
			super.onCheckStart( url, param);
			if (checkMode==CheckMode.BackGrouud) {
				toast.show(Config.START_CHECK_TIPS);
			}else if (checkMode==CheckMode.ForeGround) {
				showCkeckDialog();
			}
			SimpleLogger.log_e("onCheckStart", url+"?"+StringUtils.generateUrlString(param.getMapParams().get()));
		}
		

		@Override
		public void onCheckFailure(Throwable e) {
			if (checkMode==CheckMode.BackGrouud) {
				if (TextUtils.isEmpty(e.getLocalizedMessage())) {
					toast.show(Config.CHECK_FAIL_TIPS);
				}else {
					toast.show(Config.CHECK_FAIL_TIPS+e.getLocalizedMessage());
				}
				
			}else if (checkMode==CheckMode.ForeGround) {
				closeCkeckDialog();
				SimpleLogger.log_e("onCheckFailure", "没有吗："+e.getLocalizedMessage());
			}
		}
		
		@Override
		public void onUpdateNotfoud() {
			if (checkMode==CheckMode.BackGrouud) {
				toast.show(Config.CHECK_NO_UPDATE);
			}else if (checkMode==CheckMode.ForeGround) {
				
			}
		}
		
		@Override
		public void onUpdateFound(UpdateInfo updateInfo) {
			DebugeHelper.ALog().tag_e(updateInfo.updateDesc);
			if (checkMode==CheckMode.BackGrouud) {
				//TODO 启动更新确认对话框
			}else if (checkMode==CheckMode.ForeGround) {
				closeCkeckDialog();
			}
		}

		@Override
		public Context getContext() {
			return context;
		}
	};
	
	private void showCkeckDialog(){
		final AlertDialog.Builder builder = new Builder(context);
		dialog = builder.setTitle(Config.CHECK_DIALOG_TITLE)
				.setMessage(Config.CHECK_DIALOG_MESSAGE)
				.create();
		dialog.show();
	}
	
	private void closeCkeckDialog() {
		dialog.dismiss();
	}
	
	/*创建更新确认对话框*/
	private void buildUpdateConfirmDialog() {
		new Builder(context).setTitle(Config.CHECK_DIALOG_TITLE)
			.setMessage(Config.CONFIRM_UPDATE_DIALOG_MESSAGE)
			.setPositiveButton(Config.CONFIRM_UPDATE_DIALOG_POSITIVE, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			})
			.setNegativeButton(Config.CONFIRM_UPDATE_DIALOG_NEGATIVE, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create()
			.show();
	}
}
