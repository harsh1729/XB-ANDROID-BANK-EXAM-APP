package com.xercesblue.onlinebankexampo;

import java.io.Serializable;
import java.util.ArrayList;

public class Object_QuesCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	public int id;
	public int displayOrder;
	public String name;
	public byte[] iconImage;
	public int totalQues;
	public int durationHours;
	public int durationMins;
	public float marksEachQues;
	public float negativeMarkEackQues;
	public int isParentCategory;
	public ArrayList<Object_QuesCategory> arrayChildrenCat = null;
	
}
