package net.varkhan.base.management.state;

import net.varkhan.base.management.logging.compat.log4j.Log;
import net.varkhan.base.management.logging.compat.log4j.LogManager;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Hashtable;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/12/11
 * @time 12:23 PM
 */
public class JMXConcurrentStateReport<L extends Level,S extends State<L,S>> implements StateReport<L,S>, DynamicMBean {
    private static final Log log=LogManager.getLogger(JMXConcurrentStateReport.class);

    private final String[]         path;
    private final ConcurrentStateReport<L,S> report;

    public JMXConcurrentStateReport(ConcurrentStateReport<L,S> report, String... path) {
        this.path=path;
        this.report=report;
        MBeanServer mbs=ManagementFactory.getPlatformMBeanServer();
        try {
            mbs.registerMBean(this, getReportMBeanName());
        }
        catch(Exception e) {
            log.warn("Registering MBean for report failed",e);
        }
        for(StateCheck<L,S> check: report.checks()) {
            try {
                mbs.registerMBean(new JMXStateCheck<L,S>(check), getCheckMBeanName(check.name()));
            }
            catch(Exception e) {
                log.warn("Registering MBean for "+check.name()+" failed",e);
            }
        }
    }

    public void add(StateCheck<L,S> check) {
        JMXStateCheck<L,S> jc=new JMXStateCheck<L,S>(check);
        MBeanServer mbs=ManagementFactory.getPlatformMBeanServer();
        try {
            mbs.registerMBean(jc, getCheckMBeanName(check.name()));
        }
        catch(Exception e) {
            log.warn("Registering MBean for "+check.name()+" failed", e);
        }
        report.add(jc);
    }

    @Override
    public S state() {
        return report.state();
    }

    @Override
    public Collection<StateCheck<L,S>> checks() {
        return report.checks();
    }

    @Override
    public void update() {
        report.update();
    }

    private ObjectName getCheckMBeanName(String name) throws MalformedObjectNameException {
        if(path==null||path.length==0) return null;
        String realm=path[0];
        Hashtable<String,String> objkeys=new Hashtable<String,String>();
        if(path.length>1) objkeys.put("type", path[1]);
        if(path.length>2) objkeys.put("name", path[2]);
        objkeys.put("id", name);
        return ObjectName.getInstance(realm, objkeys);
    }

    private ObjectName getReportMBeanName() throws MalformedObjectNameException {
        if(path==null||path.length==0) return null;
        String realm=path[0];
        Hashtable<String,String> objkeys=new Hashtable<String,String>();
        if(path.length>1) objkeys.put("type", path[1]);
        if(path.length>2) objkeys.put("name", path[2]);
        return ObjectName.getInstance(realm, objkeys);
    }

    private static class JMXStateCheck<L extends Level,S extends State<L,S>> extends WrapperStateCheck<L,S> implements DynamicMBean {

        private JMXStateCheck(StateCheck<L,S> hc) { super(hc); }

        @Override
        public S state() {
            return check.state();
        }

        @Override
        public String reason() {
            return check.reason();
        }

        @Override
        public void update() {
            check.update();
        }

        public MBeanInfo getMBeanInfo() {
            MBeanAttributeInfo[] attributes=new MBeanAttributeInfo[] {
//                    new MBeanAttributeInfo("name", String.class.getName(), "The name of the health check", true, false, false),
                    new MBeanAttributeInfo("description", String.class.getName(), "A description of the health check", true, false, false),
                    new MBeanAttributeInfo("status", String.class.getName(), "The current health check status", true, false, false),
                    new MBeanAttributeInfo("reason", String.class.getName(), "The reason for the current health check status", true, false, false),
            };
            MBeanOperationInfo[] operations=new MBeanOperationInfo[] {
                    new MBeanOperationInfo("update", "Updates internal memory, running the health check", new MBeanParameterInfo[] {
                    }, "void", MBeanOperationInfo.ACTION),
            };
            return new MBeanInfo(this.getClass().getSimpleName(), "Health Check", attributes, null, operations, null);
        }

        public Object getAttribute(String attribute)
                throws AttributeNotFoundException, MBeanException,
                       ReflectionException {
            if("name".equals(attribute)) return check.name();
            if("description".equals(attribute)) return check.desc();
            if("status".equals(attribute)) return check.state().toString();
            if("reason".equals(attribute)) return check.reason();
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
        }

        public AttributeList setAttributes(AttributeList attributes) {
            return new AttributeList();
        }

        public Object invoke(String actionName, Object[] params,
                             String[] signature) throws MBeanException,
                                                        ReflectionException {
            if("update".equals(actionName)) {
                try { check.update(); } catch(Exception e) { throw new MBeanException(e); }
            }
            return null;
        }

    }


    public MBeanInfo getMBeanInfo() {
        MBeanAttributeInfo[] attributes=new MBeanAttributeInfo[] {
                new MBeanAttributeInfo("status", String.class.getName(), "The current health report status", true, false, false),
        };
        MBeanOperationInfo[] operations=new MBeanOperationInfo[] {
                new MBeanOperationInfo("update", "Updates internal memory, running the health checks in the report", new MBeanParameterInfo[] {
                }, "void", MBeanOperationInfo.ACTION),
        };
        return new MBeanInfo(this.getClass().getSimpleName(), "Health Report", attributes, null, operations, null);
    }

    public Object getAttribute(String attribute)
            throws AttributeNotFoundException, MBeanException,
                   ReflectionException {
        if("status".equals(attribute)) return report.state().toString();
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
    }

    public AttributeList setAttributes(AttributeList attributes) {
        return new AttributeList();
    }

    public Object invoke(String actionName, Object[] params,
                         String[] signature) throws MBeanException,
                                                    ReflectionException {
        if("update".equals(actionName)) {
            try { report.update(); } catch(Exception e) { throw new MBeanException(e); }
        }
        return null;
    }

}
