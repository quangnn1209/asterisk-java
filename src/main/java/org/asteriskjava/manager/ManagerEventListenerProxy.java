package org.asteriskjava.manager;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.util.DaemonThreadFactory;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import java.util.concurrent.*;

/**
 * Proxies a ManagerEventListener and dispatches events asynchronously by using
 * a single threaded executor.<p>
 * Use this proxy to prevent the reader thread from being blocked while your
 * application processes {@link org.asteriskjava.manager.event.ManagerEvent}s.
 * If you want to use the {@link org.asteriskjava.manager.ManagerConnection} for
 * sending actions in your {@link org.asteriskjava.manager.ManagerEventListener}
 * using a proxy like this one is mandatory; otherwise you will always run into
 * a timeout because the reader thread that is supposed to read the response to
 * your action is still blocked processing the event.<p>
 * If in doubt use the proxy as it won't hurt.<p>
 * Example:
 * <pre>
 * ManagerConnection connection;
 * ManagerEventListener myListener;
 * ...
 * connection.addEventListener(new ManagerEventListenerProxy(myListener));
 * </pre>
 *
 * @author srt
 * @author fink
 * @since 0.3
 */
public class ManagerEventListenerProxy implements ManagerEventListener {
    protected Log logger = LogFactory.getLog(getClass());
    private final ThreadPoolExecutor executor;
    private final ManagerEventListener target;


    /**
     * Creates a new ManagerEventListenerProxy that notifies the given target
     * asynchronously when new events are received.
     *
     * @param target the target listener to invoke.
     * @see Executors#newSingleThreadExecutor(ThreadFactory)
     */
    public ManagerEventListenerProxy(ManagerEventListener target) {
        executor = new ThreadPoolExecutor(100, 10000, 500, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), new DaemonThreadFactory());

        this.target = target;
        if (target == null) {
            throw new NullPointerException("ManagerEventListener target is null!");
        }
    }//new


    @Override
    public void onManagerEvent(final ManagerEvent event) {
        executor.execute(() -> {
            try {
                target.onManagerEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        int queueSize = executor.getQueue().size();
        if (queueSize > 50) {
            logger.info(executor.getQueue().size() + " threads in queue");
        }
    }//onManagerEvent


    public void shutdown() {
        executor.shutdown();
    }
}
