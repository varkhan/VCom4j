package net.varkhan.core.component.plugin;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Hashtable;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/12
 * @time 8:07 PM
 */
public class JMXPlugin<T> extends DelegatePlugin<T> implements DynamicMBean {
    protected final MBeanServer serv;
    protected final String[]    path;
    protected final String      name;

    public JMXPlugin(String name, Plugin<T> plugin, String ... path) {
        this(plugin, ManagementFactory.getPlatformMBeanServer(), name, path);
    }

    public JMXPlugin(Plugin<T> plugin, MBeanServer server, String name, String ... path) {
        super(plugin);
        this.name = name;
        this.serv = server;
        this.path = path.clone();
    }

    public MBeanInfo getMBeanInfo() {
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[]{
                new MBeanAttributeInfo("name", "java.lang.String", "The name of the plugin", true, false, false),
                new MBeanAttributeInfo("target", "java.lang.Class", "The target class of the plugin", true, false, false),
                new MBeanAttributeInfo("updated", "boolean", "Whether the currently loaded plugin implementation is up to date.", true, false, true),
                new MBeanAttributeInfo("available", "boolean", "Whether the last attempt to load the plugin implementation was successful.", true, false, true),
                new MBeanAttributeInfo("failure", "java.lang.Throwable", "The exception thrown during the last loading attempt, if any.", true, false, false),
        };
        MBeanOperationInfo[] operations = new MBeanOperationInfo[]{
                new MBeanOperationInfo("update", "Update the implementation of the plugin, if a new one is available.", new MBeanParameterInfo[]{}, "void", MBeanOperationInfo.ACTION),
        };
        return new MBeanInfo(this.getClass().getSimpleName(), "Plugin Management", attributes, null, operations, null);
    }

    public Object getAttribute(String attribute)
            throws AttributeNotFoundException, MBeanException,
            ReflectionException {
        if ("name".equals(attribute))
            return name;
        if ("target".equals(attribute))
            return plugin.target()==null?"??":plugin.target().getCanonicalName();
        if ("updated".equals(attribute))
            return plugin.isUpdated();
        if ("available".equals(attribute))
            return plugin.isAvailable();
        if ("failure".equals(attribute))
            return plugin.failure();
        return null;
    }

    public AttributeList getAttributes(String[] attributes) {
        AttributeList list = new AttributeList();
        for (String attribute : attributes) {
            try {
                list.add(new Attribute(attribute, getAttribute(attribute)));
            } catch (Exception e) { /* ignore */ }
        }
        return list;
    }

    public void setAttribute(Attribute attribute)
            throws AttributeNotFoundException,
            InvalidAttributeValueException, MBeanException,
            ReflectionException {
        //not supported
    }

    public AttributeList setAttributes(AttributeList attributes) {
        AttributeList list = new AttributeList();
        for (Object attribute : attributes) {
            try {
                setAttribute((Attribute) attribute);
                list.add(attribute);
            } catch (Exception e) { /* ignore */ }
        }
        return list;
    }

    public Object invoke(String actionName, Object[] params,
                         String[] signature) throws MBeanException,
            ReflectionException {
        if ("update".equals(actionName))
            plugin.updateIfAvailable();
        return null;
    }

    public void register(String... path) throws IllegalArgumentException {
        if(path==null||path.length==0) throw new IllegalArgumentException("Invalid MBean name: realm and type must be specified");
        String realm=path[0];
        Hashtable<String,String> objkeys=new Hashtable<String,String>();
        if(path.length>1) objkeys.put("type", path[1].replace(':','_').replace(' ','_'));
        final String pluginName=name.replace(':','_').replace(' ','_');
        if(path.length>2) objkeys.put("name", pluginName);
        ObjectName objectName;
        try {
            objectName=ObjectName.getInstance(realm, objkeys);
        }
        catch(MalformedObjectNameException e) {
            throw new IllegalArgumentException("Invalid MBean name: "+Arrays.toString(path)+" :"+pluginName);
        }
        try {
            serv.registerMBean(this, objectName);
        }
        catch(OperationsException e) {
            throw new IllegalArgumentException("Invalid MBean name: "+Arrays.toString(path)+" :"+pluginName, e);
        }
        catch(MBeanRegistrationException e) {
            throw new IllegalArgumentException("Invalid MBean name: "+Arrays.toString(path)+" :"+pluginName, e);
        }
    }

}
