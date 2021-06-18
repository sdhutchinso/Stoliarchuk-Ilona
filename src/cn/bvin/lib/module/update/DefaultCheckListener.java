package cn.bvin.lib.module.update;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import cn.bvin.lib.module.net.RequestParam;
import cn.bvin.lib.module.utils.VersionUtils;
/**
 * 基于CheckListener实现app本地version和服务器version比较，
 * 如果服务器版本大于本地app版本就触发onUpdateFound(UpdateInfo updateInfo)
 * 方法，否则就触发onUpdateNotfoud方法
 */
public abstract class DefaultCheckListener extends CheckListener{

	/**
	 * 发现新版本，服务器版本大于app本地版本
	 * @param updateInfo 更新信息
	 */
	public abstract void onUpdateFound(UpdateInfo updateInfo);
	
	/**
	 * 没有发现新版，说明当前安装版本是最新版本
	 */
	public abstract void onUpdateNotfoud();
	
	/**
	 * 获取上下文
	 * @return 这个上下文用来获取版本号
	 */
	public abstract Context getContext();
	
	
	private int localVersion;
	
	
	
	@Override
	public void onCheckStart(String url, RequestParam param) {
		try {
			this.localVersion = getLocalAppVersion(getContext());
		} catch (NameNotFoundException e) {
			onCheckFailure(e);
		}
	}
	@Override
	public void onCheckSuccess(UpdateInfo updateInfo) {
		try {
			int serverVersion = Integer.parseInt(updateInfo.version);
			if (serverVersion>localVersion) {
				onUpdateFound(updateInfo);
			}else {
				onUpdateNotfoud();
			}
		} catch (NumberFormatException e) {
			onCheckFailure(e);
		}
	}

	private int getLocalAppVersion(Context context) throws NameNotFoundException {
		return VersionUtils.getApplicationVersion(context);
	}
}
