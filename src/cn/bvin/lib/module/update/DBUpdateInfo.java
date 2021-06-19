package cn.bvin.lib.module.update;

public class DBUpdateInfo extends UpdateInfo{

	public DBUpdateInfo(String version, String updateDesc, String apkUrl,
			String apkFileSize) {
		super(version, updateDesc, apkUrl, apkFileSize);
	}
	public String dbVersion;// 基础数据版本
	public String dbFileUrl;// 基础数据地址 
}
