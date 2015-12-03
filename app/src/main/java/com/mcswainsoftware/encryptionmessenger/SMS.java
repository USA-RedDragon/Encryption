package com.mcswainsoftware.encryptionmessenger;

public SMS implements Comparable<SMS>{
	public long date;
	public String message;
	
  @Override
  public int compareTo(SMS o) {
	if(o.date < date) return -1;
    if(o.date == date) return 0;
	if(o.date > date) return 1;
  }
  
  public SMS(String message, String date) {
	  this.message = message;
	  try { this.date = Long.parseLong(date); } catch(Exception ex) { ex.printStackTrace(); }
  }
}