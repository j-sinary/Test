package com.madvirus.async;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/enter", asyncSupported = true)
public class EnterServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processConnectionRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processConnectionRequest(req, resp);
	}

	private void processConnectionRequest(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		logger.info("Receive ENTER request");

		res.setContentType("text/html; charset=UTF-8");
		res.setHeader("Cache-Control", "private");
		res.setHeader("Pragma", "no-cache");
		res.setCharacterEncoding("UTF-8");

		PrintWriter writer = res.getWriter();
		// for IE
		writer.println("<!-- Comet is a programming technique that enables web servers to send data to the client without having any need for the client to request it. -->\n");
		writer.flush();

		AsyncContext asyncCtx = req.startAsync();
		addToChatRoom(asyncCtx);
	}

	private void addToChatRoom(AsyncContext asyncCtx) {
		asyncCtx.setTimeout(0);
		ChatRoom.getInstance().enter(asyncCtx);
		logger.info("New Client enter Room");
	}

}
