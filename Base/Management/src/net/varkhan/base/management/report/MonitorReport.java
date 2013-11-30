/**
 *
 */
package net.varkhan.base.management.report;

import net.varkhan.base.management.monitor.Monitor;


/**
 * <b>Produces reports on monitor values.</b>
 * <p/>
 *
 * @author varkhan
 * @date Jun 24, 2009
 * @time 11:52:44 PM
 */
public interface MonitorReport<M extends Monitor<?>> {

    public void add(String name, M m);

    public void del(String name);

    public M get(String name);

    public Iterable<String> names();

}
