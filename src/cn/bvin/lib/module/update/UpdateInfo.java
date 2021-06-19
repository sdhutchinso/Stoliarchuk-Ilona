package cn.bvin.lib.module.update;

public class UpdateInfo {
	
	/**
	 * 检测更新信息
	 * @param version 版本号
	 * @param updateDesc 更新内容
	 * @param apkUrl 更新文件地址
	 * @param apkFileSize 更新文件大小
	 */
	public UpdateInfo(String version, String updateDesc, String apkUrl,
			String apkFileSize) {
		super();
		this.version = version;
		this.updateDesc = updateDesc;
		this.apkUrl = apkUrl;
		this.apkFileSize = apkFileSize;
	}
	
	public String version;// 版本号
	public String updateDesc;// 更新内容
	public String apkUrl;// 更新文件地址
	public String apkFileSize;// 更新文件大小
}
