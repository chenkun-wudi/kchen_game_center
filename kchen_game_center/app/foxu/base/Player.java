package foxu.base;

import mustang.io.ByteBuffer;

/**
 * 角色简要信息
 * 
 * @author kunchen
 *
 */
public class Player {
	/* fields */
	/** 唯一id */
	long id;
	/** 所属账户id */
	long user_id;
	/** 运营商编号(创建) */
	int plat_id;
	/** 大区编号(创建) */
	int area_id;
	/** 服务器编号(创建) */
	int server_id;
	/** 名字 */
	String name;
	/** 等级 */
	int lv;
	/** 脸 */
	int face;
	/** vip */
	int vip;

	/* methods */
	@Override
	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("id:"+id+",");
		sb.append("user_id:"+user_id+",");
		sb.append("plat_id:"+plat_id+",");
		sb.append("area_id:"+area_id+",");
		sb.append("server_id:"+server_id+",");
		sb.append("name:"+name+",");
		sb.append("lv:"+lv+",");
		sb.append("face:"+face+",");
		sb.append("vip:"+vip);
		return sb.toString();
	}
	
	/** 显示序列化 */
	public void bytesWrite(ByteBuffer data)
	{
		data.writeByte(plat_id);
		data.writeShort(area_id);
		data.writeInt(server_id);
		data.writeUTF(name);
		data.writeShort(lv);
		data.writeShort(face);
		data.writeByte(vip);
	}
	
	/* properties */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public int getPlat_id() {
		return plat_id;
	}

	public void setPlat_id(int plat_id) {
		this.plat_id = plat_id;
	}

	public int getArea_id() {
		return area_id;
	}

	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}

	public int getServer_id() {
		return server_id;
	}

	public void setServer_id(int server_id) {
		this.server_id = server_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public int getFace() {
		return face;
	}

	public void setFace(int face) {
		this.face = face;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

}
