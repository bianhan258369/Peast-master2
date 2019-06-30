package UI;

public class Clock {
	
	private String name;
	private String unit;
	private int clockType;
	
	public static int PHYSICAL=0;
	public static int LOGICAL=1;
	
	private String resolution=null;
	private String max=null;
	private String offset=null;
	private String domainText = null;

	/*
	public Clock() {
		name="Default";
		clockType=PHYSICAL;
		unit="s";
	}\
	*/

	public Clock(String domainText) {
		this.name="Default";
		this.clockType=PHYSICAL;
		this.unit="s";
		this.domainText = domainText;
	}

	public Clock(String name,int type,String unit,String domainText){
		this.name=name;
		this.clockType=type;
		this.unit=unit;
		this.domainText = domainText;
	}

	public String getDomainText(){
		return this.domainText;
	}
	
	public void updateName(String name){
		this.name=name;
	}
	
	public void updateType(int type){
		this.clockType=type;
	}
	
	public void updateUnit(String unit){
		this.unit=unit;
	}
	
	public void setResolution(String res){
		this.resolution=res;
	}
	
	public void setMax(String max){
		this.max=max;
	}
	
	public void setOffset(String offset){
		this.offset=offset;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getClockType(){
		return this.clockType;
	}
	
	public String getUnit(){
		return this.unit;
	}
	
	public String getResolution(){
		return this.resolution;
	}
	
	public String getMax(){
		return this.max;
	}
	
	public String getOffset(){
		return this.offset;
	}
}
