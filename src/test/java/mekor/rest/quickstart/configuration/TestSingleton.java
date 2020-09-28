/**
 * 
 */
package mekor.rest.quickstart.configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.sse.InboundSseEvent;

/**
 * @author mekor
 *
 */
public class TestSingleton {

	private static final TestSingleton SINGLETON = new TestSingleton();

	protected Map<Long, List<InboundSseEvent>> sseEvents = new ConcurrentHashMap<>();

	public static TestSingleton get() {
		return SINGLETON;
	}

	public boolean hasEvent(Long userID) {
		List<InboundSseEvent> userEvents = sseEvents.get(userID);
		if (userEvents != null) {
			if (!userEvents.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see {@link #sseEvents}
	 */
	public Map<Long, List<InboundSseEvent>> getSseEvents() {
		return sseEvents;
	}

	/**
	 * @see {@link #sseEvents}
	 */
	public void setSseEvents(Map<Long, List<InboundSseEvent>> sseEvents) {
		this.sseEvents = sseEvents;
	}

}
