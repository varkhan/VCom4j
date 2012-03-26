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
public class JMXMonitorReport<M extends Monitor<?>> implements DynamicMBean, MonitorReport<M> {

    private final String[] path;

    private final ConcurrentMap<String,M> monitors=new ConcurrentHashMap<String,M>();

    /**
     *
     */
    public JMXMonitorReport(String... path) {
        this.path=path;
    }

    private ObjectName getMBeanName(String name) throws MalformedObjectNameException {
        if(path==null||path.length==0) return null;
        String realm=path[0];
        Hashtable<String,String> objkeys=new Hashtable<String,String>();
        if(path.length>1) objkeys.put("type", path[1]);
        if(path.length>2) objkeys.put("name", path[2]);
        objkeys.put("id", name);
        return ObjectName.getInstance(realm, objkeys);
    }

    private static class JMXMonitor<V> implements DynamicMBean {

        private final Monitor<V> m;

        /**
         *
         */
        public JMXMonitor(Monitor<V> m) {
            this.m=m;
        }

        public MBeanInfo getMBeanInfo() {
            int params=(m instanceof MonitorParameter) ? ((MonitorParameter<?,?,?>) m).parameters().length : 0;
            int compnd=(m instanceof MonitorAggregate) ? ((MonitorAggregate<?,?,?>) m).components().length : 0;
            MBeanAttributeInfo[] attributes=new MBeanAttributeInfo[1+params+compnd];
            int pos=0;
            attributes[pos++]=new MBeanAttributeInfo("value", Object.class.getName(), "The current monitor value", true, false, false);
            if(m instanceof MonitorParameter) for(Enum<?> e : ((MonitorParameter<?,?,?>) m).parameters()) {
                attributes[pos++]=new MBeanAttributeInfo(e.name(), Object.class.getName(), e.name()+" parameter", true, true, false);
            }
            if(m instanceof MonitorAggregate) for(Enum<?> e : ((MonitorAggregate<?,?,?>) m).components()) {
                attributes[pos++]=new MBeanAttributeInfo(e.name(), Object.class.getName(), e.name()+" component", true, false, false);
            }
            MBeanOperationInfo[] operations=new MBeanOperationInfo[] {
                    new MBeanOperationInfo("reset", "Clears internal memory, and resets the value of the Monitor to initialization state", new MBeanParameterInfo[] {
                    }, "void", MBeanOperationInfo.ACTION),
                    new MBeanOperationInfo("update", "Updates internal memory, updating the value of the Monitor to reflect the monitored process", new MBeanParameterInfo[] {
                    }, "void", MBeanOperationInfo.ACTION),
            };
            return new MBeanInfo(this.getClass().getSimpleName(), "Monitor Report", attributes, null, operations, null);
        }

        public Object getAttribute(String attribute)
                throws AttributeNotFoundException, MBeanException,
                       ReflectionException {
            if("value".equals(attribute)) return m.value();
            Object o;
            if(m instanceof MonitorParameter) {
                o=((MonitorParameter<?,?,?>) m).parameter(attribute);
                if(o!=null) return o;
            }
            if(m instanceof MonitorAggregate) {
                o=((MonitorAggregate<?,?,?>) m).component(attribute);
                if(o!=null) return o;
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

        @SuppressWarnings("unchecked")
        public void setAttribute(Attribute attribute)
                throws AttributeNotFoundException,
                       InvalidAttributeValueException, MBeanException,
                       ReflectionException {
            if(m instanceof MonitorParameter) {
                ((MonitorParameter<?,?,Object>) m).parameter(attribute.getName(), attribute.getValue());
            }
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

        public Object invoke(String actionName, Object[] params,
                             String[] signature) throws MBeanException,
                                                        ReflectionException {
            if("reset".equals(actionName)) {
                try { m.reset(); } catch(Exception e) { throw new MBeanException(e); }
            }
            else if("update".equals(actionName)) {
                try { m.update(); } catch(Exception e) { throw new MBeanException(e); }
            }
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    private Object getMBeanImpl(M m) {
        return new JMXMonitor<Object>((Monitor<Object>) m);
    }

    public void add(String name, M m) {
        if(monitors.putIfAbsent(name, m)==null) {
            synchronized(monitors) {
                try {
                    ManagementFactory.getPlatformMBeanServer().registerMBean(getMBeanImpl(m), getMBeanName(name));
                    return;
                }
                catch(Exception e) {
                    // ignore for now, get out of the sync block and remove the entry
                }
            }
            // If we were not able to add this monitor to the MBean server
            monitors.remove(name);
        }
    }

    public void del(String name) {
        if(monitors.get(name)!=null) {
            synchronized(monitors) {
                try {
                    ManagementFactory.getPlatformMBeanServer().unregisterMBean(getMBeanName(name));
                }
                catch(Exception e) {
                    // ignore, do not remove the entry and return
                    return;
                }
            }
            // If we were able to remove this monitor from the MBean server
            monitors.remove(name);
        }
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
