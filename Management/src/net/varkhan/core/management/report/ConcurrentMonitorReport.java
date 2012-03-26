/**
 *
 */
package net.varkhan.core.management.report;

import net.varkhan.core.management.monitor.Monitor;
import net.varkhan.core.management.monitor.MonitorAggregate;
import net.varkhan.core.management.monitor.MonitorParameter;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author varkhan
 * @date Jun 25, 2009
 * @time 2:41:05 AM
 */
public class ConcurrentMonitorReport<M extends Monitor<?>> implements MonitorReport<M> {

    private final String[] path;

    private final ConcurrentMap<String,M> monitors=new ConcurrentHashMap<String,M>();

    /**
     *
     */
    public ConcurrentMonitorReport(String... path) {
        this.path=path;
    }

    public void add(String name, M m) {
        monitors.putIfAbsent(name, m);
    }

    public void del(String name) {
        monitors.remove(name);
    }

    public M get(String name) { return monitors.get(name); }

    @Override
    public Iterable<String> names() {
        return monitors.keySet();
    }


    public Object getAttribute(String attribute)
            throws AttributeNotFoundException, MBeanException,
                   ReflectionException {
        if(attribute.equals("name")) {
            if(path==null || path.length==0) return "";
            StringBuilder name = new StringBuilder();
            for(int i=0;i<path.length;i++) {
                if(i>0) name.append('.');
                name.append(path[i]);
            }
            return name.toString();
        }
        Object m=monitors.get(attribute);
        if(m!=null) return m;
        int dot=attribute.indexOf('.');
        if(dot>0) {
            String name=attribute.substring(0, dot);
            String attr=attribute.substring(dot+1);
            m=monitors.get(name);
            if(m!=null) {
                if(m instanceof MonitorParameter) {
                    m=((MonitorParameter<?,?,?>) m).parameter(attr);
                    if(m!=null) return m;
                }
                if(m instanceof MonitorAggregate) {
                    m=((MonitorAggregate<?,?,?>) m).component(attr);
                    if(m!=null) return m;
                }
            }
        }
        throw new AttributeNotFoundException();
    }

    public AttributeList getAttributes(String[] attributes) {
        AttributeList list=new AttributeList();
        for(String attribute : attributes) {
            try { list.add(new Attribute(attribute, getAttribute(attribute))); }
            catch(Exception e) { /* ignore */ }
        }
        return list;
    }

    public void setAttribute(Attribute attribute)
            throws AttributeNotFoundException, InvalidAttributeValueException,
                   MBeanException, ReflectionException {
        String name=attribute.getName();
        Object m=monitors.get(name);
        if(m!=null) return;
        int dot=name.indexOf('.');
        if(dot>0) {
            name=attribute.getName().substring(0, dot);
//			String attr = attribute.getName().substring(dot+1);
            m=monitors.get(name);
            if(m!=null) {
                if(m instanceof MonitorParameter) {
//					try { ((MonitorParameter<?,?,?>)m).parameter(attr,attribute.getValue());
//					if(m!=null) return m;
                }
                if(m instanceof MonitorAggregate) {
//					m = ((MonitorAggregate<?,?,?>)m).component(attr,attribute.getValue());
//					if(m!=null) return m;
                }
            }
        }
        throw new AttributeNotFoundException();
    }

    public AttributeList setAttributes(AttributeList attributes) {
        AttributeList list=new AttributeList();
        for(Object attribute : attributes) {
            try {
                setAttribute((Attribute) attribute);
                list.add(attribute);
            }
            catch(Exception e) { /* ignore */ }
        }
        return list;
    }

    public Object invoke(String actionName, Object params[], String signature[]) throws MBeanException, ReflectionException {
        if("reset".equals(actionName)) {
            for(Monitor<?> m : monitors.values()) try { m.reset(); } catch(Exception e) { /* ignore */ }
        }
        else if("update".equals(actionName)) {
            for(Monitor<?> m : monitors.values()) try { m.update(); } catch(Exception e) { /* ignore */ }
        }
        return null;
    }

    public MBeanInfo getMBeanInfo() {
        try {
            MBeanAttributeInfo[] attributes=new MBeanAttributeInfo[1+monitors.size()];
            int pos=0;
            attributes[pos++]=new MBeanAttributeInfo("name", String.class.getName(),
                                                     "Report display name",
                                                     true, false, false);
            for(String name : monitors.keySet()) {
                attributes[pos++]=new MBeanAttributeInfo("- "+name, Monitor.class.getName(),
                                                         "StatPoint "+name,
                                                         true, false, false);
            }
            MBeanOperationInfo[] operations=new MBeanOperationInfo[] {
                    new MBeanOperationInfo("reset", "Clears internal memory, and resets the value of all the Monitors to initialization state", new MBeanParameterInfo[] {
                    }, "void", MBeanOperationInfo.ACTION),
                    new MBeanOperationInfo("update", "Updates internal memory, updating the value of all the Monitors to reflect the monitored process", new MBeanParameterInfo[] {
                    }, "void", MBeanOperationInfo.ACTION),
            };
            return new MBeanInfo(this.getClass().getSimpleName(), "Monitor Report", attributes, null, operations, null);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


}
