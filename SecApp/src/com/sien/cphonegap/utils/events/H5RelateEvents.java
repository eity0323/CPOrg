package com.sien.cphonegap.utils.events;

import com.sien.cphonegap.model.beans.H5RelateData;

public class H5RelateEvents {
	/**加载h5配置数据*/
	public static class loadH5RelateEvent{
		public static int STATUS_SUCCESS = 0;
		public static int STATUS_FAIL = 1;
		
		private int status;
		private H5RelateData item;
		
		public loadH5RelateEvent(int status, H5RelateData data) {
			this.status = status;
			this.item = data;
		}

		public int getStatus() {
			return status;
		}

		public H5RelateData getItem() {
			return item;
		}
	}
	
	/**刷新h5页面数据*/
	public static class refreshH5RelateEvent{
		public static int STATUS_SUCCESS = 0;
		public static int STATUS_FAIL = 1;
		
		private int status;
		private String item;
		
		public refreshH5RelateEvent(int status, String data) {
			this.status = status;
			this.item = data;
		}

		public int getStatus() {
			return status;
		}

		public String getItem() {
			return item;
		}
	}
}
