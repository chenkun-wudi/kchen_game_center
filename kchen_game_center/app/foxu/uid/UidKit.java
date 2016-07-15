package foxu.uid;

/**
 * uid管理器
 * 
 * @author kunchen
 *
 */
public class UidKit {
	/* static fields */
	/** 数量常量 */
	public static final int COUNT=100;

	/* fields */
	/** 每次获取唯一编号的数量 */
	int count=COUNT;
	UidFile uidFile;
	/** 正数唯一编号 */
	private long plusUid1;
	/** 正数唯一编号的最小值 */
	private long plusUid2;
	/** 负数唯一编号 */
	private long minusUid1;
	/** 负数唯一编号的最小值 */
	private long minusUid2;
	/** 正数唯一编号同步对象 */
	private Object plusLock=new Object();
	/** 负数唯一编号同步对象 */
	private Object minusLock=new Object();
	/* methods */
	/** 获得唯一的正数编号 */
	public long getPlusUid()
	{
		synchronized(plusLock)
		{
			if(plusUid1>=plusUid2)
			{
				plusUid2=uidFile.getPlusUid(count);
				plusUid1=plusUid2-count;
			}
			return ++plusUid1;
		}
	}
	
	/** 获得唯一的负数编号 */
	public long getMinusUid()
	{
		synchronized(minusLock)
		{
			if(minusUid1<=minusUid2)
			{
				minusUid2=uidFile.getMinusUid(count);
				minusUid1=minusUid2+count;
			}
			return --minusUid1;
		}
	}
	
	public UidFile getUidFile() {
		return uidFile;
	}

	public void setUidFile(UidFile uidFile) {
		this.uidFile = uidFile;
	}

}
