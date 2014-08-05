package com.madvirus.async;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/sendMessage")
public class SendMessageServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.info("Receive SEND request");
		
		res.setContentType("text/plain");
		res.setHeader("Cache-Control", "private");
		res.setHeader("Pragma", "no-cache");
		req.setCharacterEncoding("UTF-8");

		ChatRoom.getInstance().sendMessageToAll(req.getParameter("message"));

		res.getWriter().print("OK");
	}

}
