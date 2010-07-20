package es.udc.cartolab.gvsig.elle.gui.wizard.save;

public class LayerProperties {


	private String schema, tablename, layername;
	private String shownname = "", group = "";
	private boolean save = true, visible = false;
	private double maxScale = -1, minScale = -1;
	private int position;


	public LayerProperties(String schema, String tablename, String layername) {
		this.schema = schema;
		this.tablename = tablename;
		this.layername = layername;
	}

	public String getSchema() {
		return schema;
	}

	public String getTablename() {
		return tablename;
	}

	/**
	 * It returns the layer name on the current view.
	 * @return
	 */
	public String getLayername() {
		return layername;
	}

	public boolean save() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	public boolean visible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * It returns the name of the layer as it'll be saved on the map.
	 * @return
	 */
	public String getShownname() {
		return shownname;
	}

	public void setShownname(String shownname) {
		this.shownname = shownname;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public double getMaxScale() {
		return maxScale;
	}

	public void setMaxScale(double maxScale) {
		this.maxScale = maxScale;
	}

	public double getMinScale() {
		return minScale;
	}

	public void setMinScale(double minScale) {
		this.minScale = minScale;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
