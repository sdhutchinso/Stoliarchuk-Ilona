#AppUpdateModule
包含检查更新功能和下载安装，依赖NetworkModule和UtilsModule两个库。检查更新是使用NetworkModule当中volley去发送请求，而下载是通过DownloadModule去下载服务器新版本。提供一些默认的UI表现形式，也可以完全自己定制，只需要实现相关监听事件即可。
说明
--------
用到UtilsModule,主要有ToastUtils可能需要弹出先一些状态信息提示，StringUtils从Map集合中生存URL形式的字符串，以及版获取app版本的工具方法

检测更新使用UpdateManager.checkVersion(String url,Map<String, Object> params,CheckListener checkListener)方法，url是检测更新的接口地址，params是检查更新接口的参数(为了兼容不同接口，有的接口可能需要传一些用户id或者当前版本的数据)，checkListener就是检测更新状态监听器，如果不传就会以一个默认的形式去展现UI，如果需要自己定义UI则完全可以自己实现这个监听器，在监听器里面实现自己风格的UI。

cn.bvin.lib.module.update.UpdateInfo:
    更新信息模型，任何接口最好都按照此模型来定制接口数据形式
    ```
    public String version;// 版本号
	public String updateDesc;// 更新内容
	public String apkUrl;// 更新文件地址
	public String apkFileSize;// 更新文件大小
    ```
    DBUpdateInfo是继承UpdateInfo的一个类，因为有的检测更新接口可能包含一个基础数据的版本更新在里面。

cn.bvin.lib.module.update.CheckRequest:
    继承Request<UpdateInfo>，主要构造参数是需要一个包含请求地址和请求参数的WrapRequest对象和一个检测更新状态的监听器。
    ```
    @Override
	protected void deliverResponse(UpdateInfo arg0) {
		if (arg0!=null&&checkListener!=null) {
            //交付响应结果
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
	}码
    ```
DefaultCheckRequest是一个默认的接口返回内容转换成UpdateInfo对象的实现如有需要可以自己实现，因为可能不同接口返回数据形式是不一样的。

cn.bvin.lib.module.update.CheckListener:
    检查更新异步监听器，抽象类。为了接口能够有部分实现，并且需要与Volley中的Request类和ErrorListener融合妥协的结果，本来应该是interface用起来会更方便，java中implements不限制，但extends只能继承一个。
    ```
    /**
	 * 开始检测更新，是一个耗时的网络请求的过程
	 * @param context 用以获取app本地版本号
	 * @param url 检测更新的接口地址
	 * @param param 更新接口需要传的参数
	 */
    public abstract void onCheckStart(Context context, String url, RequestParam param);
	
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
    ```
DefaultCheckListener是基于CheckListener实现了版本比较的功能。
    ```
    **
	 * 发现新版本，服务器版本大于app本地版本
	 * @param updateInfo 更新信息
	 */
	public abstract void onUpdateFound(UpdateInfo updateInfo);
	
	/**
	 * 没有发现新版，说明当前安装版本是最新版本
	 */
	public abstract void onUpdateNotfoud();
    ```
使用
--------
UpdateManager.with(Context)
.checkVersion(url,Map<String, Object>,DataConvertor<UpdateInfo> convertor)
.listenUpdateConfirm(UpdateConfirmListener listener);  

UpdateManager.with(Context):通过Context返回一个UpdateManager实例  
checkVersion(请求地址，map参数，接口数据转换器)
.listenUpdateConfirm(UpdateConfirmListener listener)监听确认更新  

```
        Map<String, Object> params = new HashMap<String, Object>();
		params.put("Method", "UpdateApk");
		UpdateManager.with(this).checkVersion(url, params,new GsonDataConvertor<UpdateInfo>() {
			//转换器的作用主要是让监听器得到数据是准确的
			@Override
			public UpdateInfo convertFromGson(byte[] data, Class clazz) {
				String json = new String(data);
				Gson gson = new Gson();
				UpdateModel modal = gson.fromJson(json, clazz);
				UpdateInfo info = new UpdateInfo(modal.version,modal.note,modal.url,modal.lenth);
				return info;
			}

			@Override
			public Class jsonModel() {
				return UpdateModel.class;
			}
		}).listenUpdateConfirm(new UpdateConfirmListener() {
			
			@Override
			public void onUpdateConfirm(UpdateInfo info) {
				SimpleLogger.log_e("onUpdateConfirm", currentDownloadId<0);
				if (currentDownloadId>0) {
					startDownload(info);
				}else {
					resureDownload();
				}
				showDownloadDialog();
			}
		});
```