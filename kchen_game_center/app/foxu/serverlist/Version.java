package foxu.serverlist;

import mustang.io.ByteBuffer;
import mustang.util.Sample;

/**
 * 渠道版本
 * @author kunchen
 *
 */
public class Version extends Sample{
	/** 渠道名 */
	String name;
	/** 渠道版本 */
	String ver;
	/** 包下载地址 */
	String download_url;
	/** 游戏分享地址 */
	String share_url;

	public void showBytesWrite(ByteBuffer data) {
		data.writeUTF(ver);
		data.writeUTF(download_url);
		data.writeUTF(share_url);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getDownload_url() {
		return download_url;
	}

	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}
}
