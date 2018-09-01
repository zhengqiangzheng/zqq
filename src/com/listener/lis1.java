package com.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class lis1
 *
 */
@WebListener
public class lis1 implements HttpSessionListener {
	private int numonline=0;

    /**
     * Default constructor. 
     */
    public lis1() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent he)  { 
    	numonline++;
    	he.getSession().setAttribute("numonline", numonline);
    	
    	
    	
    	
        
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent se)  { 
    	numonline--;
    	se.getSession().setAttribute("numonline", numonline);
    }
	
}
