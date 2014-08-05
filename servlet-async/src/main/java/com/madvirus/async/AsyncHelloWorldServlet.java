package com.madvirus.async;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/hello", asyncSupported = true)
public class AsyncHelloWorldServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		final AsyncContext asyncContext = req.startAsync();
		
		new Thread(new Runnable() {
			
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				
				try {
					response.getWriter().println("HELLO");
				} catch (IOException e) {
					e.printStackTrace();
				}
				logger.info("complete response");
				asyncContext.complete();
			}
		}).start();
		
		logger.info("doGet return");
	}

}
