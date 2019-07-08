package com.frxs.order.model;

import java.io.Serializable;

/**
 * Item definition including the section.
 */
public class SectionListItem implements Serializable{
    
    private static final long serialVersionUID = 8608088691917366363L;
    
	public static final int ITEM = 0;
	public static final int SECTION = 1;
	
	
	public Object item;
	public final int type;
	public String section;
	public int sectionId;
	
	public int sectionPosition;
	public int listPosition;
	
	private boolean needDelete = false;
	
    public SectionListItem(final Object item, final int type, String section) {
        super();
        this.item = item;
        this.type = type;
        this.section = section;
    }
    
    public SectionListItem(final Object item, final int type, String section, int sectionId) {
        super();
        this.item = item;
        this.type = type;
        this.section = section;
        this.sectionId = sectionId;
    }
    
    public Object getItem()
    {
    	return item;
    }

    public void setItem(Object item)
    {
    	this.item = item;
    }

	
    public int getType()
    {
    	return type;
    }
    
	
    public String getSection()
    {
    	return section;
    }

    public int getSectionId()
    {
    	return sectionId;
    }

	
    public void setSectionId(int sectionId)
    {
    	this.sectionId = sectionId;
    }

	public void setSection(String section)
    {
    	this.section = section;
    }

    
    public boolean isNeedDelete()
    {
    	return needDelete;
    }

    public void setNeedDelete(boolean needDelete)
    {
    	this.needDelete = needDelete;
    }

	@Override
    public String toString() {
        return item.toString();
    }
	
}
