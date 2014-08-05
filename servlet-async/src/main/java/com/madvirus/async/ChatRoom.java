package com.madvirus.async;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import org.apache.log4j.Logger;

public class ChatRoom {

	private static ChatRoom INSTANCE = new ChatRoom();

	public static ChatRoom getInstance() {
		return INSTANCE;
	}

	private Logger logger = Logger.getLogger(getClass());
	private List<AsyncContext> clients = new LinkedList<AsyncContext>();
	private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();

	private Thread messageHandlerThread;
	private boolean running;

	private ChatRoom() {
	}

	public void init() {
		running = true;
		Runnable handler = new Runnable() {
//			@Override
			public void run() {
				logger.info("Started Message Handler.");
				while (running) {
					try {
						String message = messageQueue.take();
						logger.info("Take message [" + message
								+ "] from messageQueue");

						sendMessageToAllInternal(message);
					} catch (InterruptedException ex) {
						break;
					}
				}
			}
		};
		messageHandlerThread = new Thread(handler);
		messageHandlerThread.start();
	}

	public void enter(final AsyncContext asyncCtx) {
		asyncCtx.addListener(new AsyncListener() {
			public void onTimeout(AsyncEvent event) throws IOException {
				logger.info("onTimeout");
				clients.remove(asyncCtx);
			}

			public void onError(AsyncEvent event) throws IOException {
				logger.info("onError");
				clients.remove(asyncCtx);
			}

			public void onStartAsync(AsyncEvent event) throws IOException {
			}

			public void onComplete(AsyncEvent event) throws IOException {
				logger.info("onComplete");
			}
		});
		try {
			sendMessageTo(asyncCtx, "Welcome! ChatRoom");
			clients.add(asyncCtx);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessageToAll(String message) {
		try {
			messageQueue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("Add message [" + message + "] to messageQueue");
	}

	private void sendMessageToAllInternal(String message) {
		for (AsyncContext ac : clients) {
			try {
				sendMessageTo(ac, message);
			} catch (IOException e) {
				clients.remove(ac);
			}
		}
		logger.info("Send message [" + message + "] to all clients");
	}

	private void sendMessageTo(AsyncContext ac, String message)
			throws IOException {
		PrintWriter acWriter = ac.getResponse().getWriter();
		acWriter.println(toJSAppendCoomand(message));
		acWriter.flush();
	}

	private String toJSAppendCoomand(String message) {
		return "<script type='text/javascript'>\n"
				+ "window.parent.chatapp.append({ message: \""
				+ EscapeUtil.escape(message) + "\" });\n" + "</script>\n";
	}

	public void close() {
		running = false;
		messageHandlerThread.interrupt();
		logger.info("Stopped Message Handler.");

		for (AsyncContext ac : clients) {
			ac.complete();
		}
		logger.info("Complete All Client AsyncContext.");
	}
}
