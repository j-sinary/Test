package com.madvirus.async;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ChatRoomLifeCycleManager implements ServletContextListener {

//	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ChatRoom.getInstance().init();
	}

//	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ChatRoom.getInstance().close();
	}

}
