package foxu.uid;

import java.io.File;

import mustang.io.ByteKit;
import mustang.io.FileKit;
import mustang.text.TextKit;
import mustang.log.LogFactory;
import mustang.log.Logger;

/**
 * 唯一编号文件(修改版)
 * 
 * @author kunchen
 *
 */
public class UidFile {
	/** 数字大小常量 */
	public static final int NUMBER_SIZE = 16;
	/** 日志记录 */
	private static final Logger log = LogFactory.getLogger(UidFile.class);

	/** 正数唯一编号 */
	private long plusUid;
	/** 正数唯一编号的最小值 */
	private long plusUidMin;
	/** 正数唯一编号的最大值 */
	private long plusUidMax;
	/** 负数唯一编号 */
	private long minusUid;
	/** 负数唯一编号的最小值 */
	private long minusUidMin;
	/** 负数唯一编号的最大值 */
	private long minusUidMax;

	/** 保存编号的文件 */
	File file;
	/** 保存编号的备份文件 */
	File bakFile;
	/** 编号数据 */
	byte[] data = new byte[NUMBER_SIZE + NUMBER_SIZE];
	/** 正数唯一编号同步对象 */
	private Object plusLock=new Object();
	/** 负数唯一编号同步对象 */
	private Object minusLock=new Object();
	
	/** 设置正数唯一编号的区间 */
	public void setPlusUid(long min, long max) {
		plusUidMin = min;
		plusUidMax = max;
		if (plusUid < min)
			plusUid = min;
		if (plusUid > max)
			plusUid = max;
	}

	/** 设置负数唯一编号的区间 */
	public void setMinusUid(long min, long max) {
		minusUidMin = min;
		minusUidMax = max;
		if (minusUid < min)
			minusUid = min;
		if (minusUid > max)
			minusUid = max;
	}

	/** 根据文件初始化编号 */
	public void initFile(String f, String bf) {
		if (f == null)
			throw new IllegalArgumentException(getClass().getName() + " initFile, null file");
		file = new File(f);
		if (bf != null)
			bakFile = new File(bf);
		boolean exist = file.exists();
		if (exist) {
			long size = file.length();
			if (data.length != size)
				throw new IllegalArgumentException(
						getClass().getName() + " initFile, invalid file=" + f + ", size=" + size);
			try {
				byte[] temp = FileKit.readFile(file);
				if (data.length != temp.length)
					throw new IllegalArgumentException(
							getClass().getName() + " initFile, invalid file=" + f + ", data=" + TextKit.toString(temp));
				System.arraycopy(temp, 0, data, 0, data.length);
				plusUid = ByteKit.readLong(data, 0);
				minusUid = ByteKit.readLong(data, NUMBER_SIZE);
				if (plusUid < plusUidMin)
					plusUid = plusUidMin;
				if (plusUid > plusUidMax)
					plusUid = plusUidMax;
				if (minusUid < minusUidMin)
					minusUid = minusUidMin;
				if (minusUid > minusUidMax)
					minusUid = minusUidMax;
				if (bakFile != null)
					FileKit.writeFile(bakFile, data);
				log.info("initFile, load, file=" + file + ", bakFile=" + bakFile + ", plusUid=" + plusUid
						+ ", plusUidMin=" + plusUidMin + ", plusUidMax=" + plusUidMax + ", minusUid=" + minusUid
						+ ", minusUidMin=" + minusUidMin + ", minusUidMax=" + minusUidMax);
			} catch (Exception e) {
				if (log.isWarnEnabled())
					log.warn("init error, file=" + f + ", bakFile=" + bf, e);
			}
		} else {
			try {
				ByteKit.writeLong(plusUid, data, 0);
				ByteKit.writeLong(minusUid, data, NUMBER_SIZE);
				FileKit.writeFile(file, data);
				if (bakFile != null)
					FileKit.writeFile(bakFile, data);
				log.info("initFile, save, file=" + file + ", bakFile=" + bakFile + ", plusUid=" + plusUid
						+ ", plusUidMin=" + plusUidMin + ", plusUidMax=" + plusUidMax + ", minusUid=" + minusUid
						+ ", minusUidMin=" + minusUidMin + ", minusUidMax=" + minusUidMax);
			} catch (Exception e) {
				if (log.isWarnEnabled())
					log.warn("init error, file=" + f + ", bakFile=" + bf, e);
			}
		}
	}

	/** 获得唯一的正数编号 */
	public long getPlusUid() {
		synchronized (plusLock) {
			plusUid++;
			if (plusUid > plusUidMax)
				plusUid = plusUidMin;
			save();
			return plusUid;
		}
	}

	/** 获得唯一的负数编号 */
	public long getMinusUid() {
		synchronized (minusLock) {
			minusUid--;
			if (minusUid < minusUidMin)
				minusUid = minusUidMax;
			save();
			return minusUid;
		}
	}

	/** 获得指定数量的正数编号 */
	public long getPlusUid(int count) {
		if (count <= 0)
			throw new IllegalArgumentException(this + " getPlusUid, invalid count:" + count);
		synchronized (plusLock) {
			plusUid += count;
			if (plusUid > plusUidMax)
				plusUid = plusUidMin + count;
			save();
			return plusUid;
		}
	}

	/** 获得指定数量的负数编号 */
	public long getMinusUid(int count) {
		if (count <= 0)
			throw new IllegalArgumentException(this + " getMinusUid, invalid count:" + count);
		synchronized (minusLock) {
			minusUid -= count;
			if (minusUid < minusUidMin)
				minusUid = minusUidMax - count;
			save();
			return minusUid;
		}
	}

	/** 获得编号到文件 */
	public synchronized boolean save() {
		if (file == null)
			return false;
		ByteKit.writeLong(plusUid, data, 0);
		ByteKit.writeLong(minusUid, data, NUMBER_SIZE);
		try {
			FileKit.writeFile(file, data);
			if (bakFile != null)
				FileKit.writeFile(bakFile, data);
			return true;
		} catch (Exception e) {
			if (log.isWarnEnabled())
				log.warn("save error, file=" + file + ", bakFile=" + bakFile, e);
		}
		return false;
	}
}
