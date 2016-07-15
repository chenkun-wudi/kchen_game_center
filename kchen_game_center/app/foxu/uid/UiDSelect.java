package foxu.uid;

import mustang.field.FieldValue;

/**
 * Uid初始化 生成器
 * 
 * @author kunchen
 *
 */
public class UiDSelect {
	/** plat:1-127 area:1-255 serverid:1-65535 */
	public long selectMinId(int plat, int area, int serverid) {
		long minId = (plat << 24) + (area << 16) + serverid;
		return minId << 32;
	}

	public long selectMaxId(int plat, int area, int serverid) {
		long maxId = (plat << 24) + (area << 16) + serverid;
		return (maxId << 32) + Integer.MAX_VALUE * (long) 2;
	}

	public long selectMinId(int plat) {
		long minId = plat << 24;
		return minId << 32;
	}

	public long selectMaxId(int plat) {
		long maxId = plat << 24;
		return (maxId << 32) + Integer.MAX_VALUE * (long) 2;
	}

	public long selectMinId(FieldValue plat_value, FieldValue area_value, FieldValue serverid_value) {
		int plat = (Integer) plat_value.getValue();
		int area = (Integer) area_value.getValue();
		int serverid = (Integer) serverid_value.getValue();
		long minId = (plat << 24) + (area << 16) + serverid;
		return minId << 32;
	}

	public long selectMaxId(FieldValue plat_value, FieldValue area_value, FieldValue serverid_value) {
		int plat = (Integer) plat_value.getValue();
		int area = (Integer) area_value.getValue();
		int serverid = (Integer) serverid_value.getValue();
		long maxId = (plat << 24) + (area << 16) + serverid;
		return (maxId << 32) + Integer.MAX_VALUE * (long) 2;
	}

	public long selectMinId(FieldValue plat_value) {
		int plat = (Integer) plat_value.getValue();
		long minId = plat << 24;
		return minId << 32;
	}

	public long selectMaxId(FieldValue plat_value) {
		int plat = (Integer) plat_value.getValue();
		long maxId = plat << 24;
		return (maxId << 32) + Integer.MAX_VALUE * (long) 2;
	}
}
