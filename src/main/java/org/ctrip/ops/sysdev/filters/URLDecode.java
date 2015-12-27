package org.ctrip.ops.sysdev.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;
import org.ctrip.ops.sysdev.Main;

public class URLDecode extends BaseFilter {
	private static final Logger logger = Logger.getLogger(Main.class.getName());

	public URLDecode(Map config) {
		super(config);
	}

	private ArrayList<String> fields;
	private String enc;

	protected void prepare() {
		this.fields = (ArrayList<String>) config.get("fields");
		if (config.containsKey("enc")) {
			this.enc = (String) config.get("enc");
		} else {
			this.enc = "UTF-8";
		}

		if (this.config.containsKey("tag_on_failure")) {
			this.tagOnFailure = (String) this.config.get("tag_on_failure");
		} else {
			this.tagOnFailure = "URLDecodefail";
		}
	};

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Map filter(final Map event) {
		boolean success = true;
		for (String f : this.fields) {
			if (event.containsKey(f)) {
				try {
					event.put(f,
							URLDecoder.decode((String) event.get(f), this.enc));
				} catch (UnsupportedEncodingException e) {
					success = false;
				}
			}
		}

		this.postProcess(event, success);

		return event;
	}
}
