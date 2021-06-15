package cn.bvin.lib.module.update;

import cn.bvin.lib.module.net.MapParam;
import cn.bvin.lib.module.net.RequestParam;
import cn.bvin.lib.module.net.volley.RequestManager;

import com.android.volley.Request.Method;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String url = "";
		MapParam mp = new MapParam();
		mp.put("p1", "v1");
		mp.put("p2", "v2");
		WrapRequest wr = new WrapRequest(url,  new RequestParam(mp));
		CheckRequest request = new DefaultCheckRequest(Method.POST, wr,new DefaultCheckListener() {

			@Override
			public void onUpdateFound(UpdateInfo updateInfo) {
				
			}

			@Override
			public void onUpdateNotfoud() {
				
			}

			@Override
			public void onCheckFailure(Throwable e) {
				
			}
			
		});
		RequestManager.addRequest(request, request.getUrl());
	}
	
}
