package org.bibsonomy.pingback;

import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

public class ThreadedPingback extends SimplePingback implements Runnable {

	private static final Log log = LogFactory.getLog(ThreadedPingback.class);

	private final Queue<Post<? extends Resource>> queue = new ConcurrentLinkedQueue();
	private final long waitTime = 1000L;

	public String sendPingback(Post<? extends Resource> post) {
		this.queue.add(post);
		return null;
	}

	public void run() {
		try {
			while (true) {
				clearQueue();
				getClass();
				Thread.sleep(1000L);
			}
		} catch (InterruptedException ex) {
			log.warn("pingback interupted, still " + this.queue.size()
					+ " URLs in queue");
		}

	}

	public void clearQueue() throws InterruptedException {
		log.debug("clearing queue (size = " + this.queue.size() + ")");
		while (!this.queue.isEmpty()) {
			super.sendPingback((Post) this.queue.poll());
			Thread.sleep(100L);
		}
		log.debug("finished");
	}

	protected Collection<Post<? extends Resource>> getQueue() {
		return Collections.unmodifiableCollection(this.queue);
	}
}