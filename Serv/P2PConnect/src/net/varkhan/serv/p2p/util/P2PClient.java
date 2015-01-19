package net.varkhan.serv.p2p.util;

import net.varkhan.base.containers.array.Arrays;
import net.varkhan.base.management.report.JMXAverageMonitorReport;
import net.varkhan.serv.p2p.PeerHub;
import net.varkhan.serv.p2p.message.dispatch.MesgSender;
import net.varkhan.serv.p2p.network.P2PGroup;
import net.varkhan.serv.p2p.connect.*;
import net.varkhan.serv.p2p.message.dispatch.MesgDispatcher;
import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;
import net.varkhan.serv.p2p.message.dispatch.SingleReceiver;
import net.varkhan.serv.p2p.message.protocol.BinaryPayload;
import net.varkhan.serv.p2p.message.dispatch.AsyncDispatcher;
import net.varkhan.serv.p2p.network.P2PHub;
import net.varkhan.serv.p2p.connect.config.MapProperties;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * <b>A peering monitoring and testing shell that gives command-line access to the basic operations of this protocol.</b>
 * <p/>
 * This class implements a command shell, that creates a peer hub (set up according to parameters
 * specified as arguments), and interactively prompts for commands, that give access to administrative
 * operations on the node, monitoring and reporting tools, and communication primitives to other nodes
 * or groups.
 * <p/>
 * A peer is a <i>Host</i>, that advertises an availability status and <i>Group</i> memberships (a set of group
 * names and properties). Communication between peers can be one-to-one or one-to-many, based on the definition
 * of named groups (in this case, a multicast group specified by a name, and address and port number). The global
 * set of nodes (the <i>Hub</i>) is represented by the <b>*</b> group, that all nodes must join in order to participate.
 * In addition, each group (that has at least one member in the hub) may use several multicast addresses and ports
 * to reach its members. A distributed set of shared variables (String to String mapping) is maintained across the hub.
 * <p/>
 * Each node maintains a view of the state of all other nodes in the hub, updated as other nodes advertise
 * their state changes, including status and group membership. The &lt;<u>return</u>&gt; key on the command
 * line, and the <u>info</u> command give respectively a concise and verbose report of this view. The <u>join</u>
 * and leave commands can be used to manipulate the group memberships of the local node.
 * <p/>
 * A node also maintains a local cache of the distributed set of variables, updated as other nodes request changes
 * in those bindings. The var command can be used to query or update the global consensus on this set of variables.
 * <p/>
 * In addition, the send and call commands can be used to communicate with individual nodes (identified by their
 * node ID) or groups (identified by their group name). send does not expect a reply, while call will display the
 * response received from the target node or nodes (in case of a group call).
 * <p/>
 * <h3>Startup arguments</h3>
 * <p/>
 * <div><b>PeerClient</b>
 * [<b>-n</b> <i>node_name</i>]
 * [<b>-b</b> <i>bind_addr</i>]
 * [<b>-a</b> <i>hub_addr</i>]
 * [<b>-c</b> <i>hub_port</i>]
 * &nbsp; [<i>target_ID</i>] | [<b>send</b>|<b>call</b> <i>target_ID</i> <i>service_ID</i> <i>message</i>]</div>
 * <p/>
 * <table>
 * <tr><td><b>-n</b> <i>node_name</i>
 * </td><td> specify the name to be advertised by the local peer
 * </td></tr>
 * <tr><td><b>-b</b> <i>bind_addr</i>
 * </td><td> specify the local address this node binds to
 * </td></tr>
 * <tr><td><b>-a</b> <i>hub_addr</i>
 * </td><td> specify the multicast address used by the hub
 * </td></tr>
 * <tr><td><b>-c</b> <i>hub_port</i>
 * </td><td> specify the multicast port used by the hub
 * </td></tr>
 * <tr><td><i>name</i>
 * </td><td> the name of a specific peer or group to monitor
 * </td></tr>
 * <tr><td><b>call</b> <i>name</i> <i>service_ID</i> <i>message</i>
 * </td><td> transmits a CALL message to a named peer, wait for the reply, then exits
 * </td></tr>
 * <tr><td><b>send</b> <i>name</i> <i>service_ID</i> <i>message</i>
 * </td><td> transmits a SEND message to a named peer, then exits
 * </td></tr>
 * </table>
 * <p/>
 * <h3>Shell commands</h3>
 * <table>
 * <tr><td><b>help</b>
 * </td><td> this help
 * </td></tr>
 * <tr><td><b>exit</b>
 * </td><td> terminates this shell
 * </td></tr>
 * <tr><td><b>&lt;none&gt;</b> (the &lt;return&gt; key)
 * </td><td> displays abbreviated node and group information for the local hub
 * </td></tr>
 * <tr><td><b>info</b>
 * </td><td> displays detailed node and group information for the local hub
 * </td></tr>
 * <tr><td><b>join</b> <i>group_name</i> <i>port_number</i>
 * </td><td> joins a group, with the specified multicast port
 * </td></tr>
 * <tr><td><b>leave</b> <i>group_name</i>
 * </td><td> leaves a group
 * </td></tr>
 * <tr><td><b>call</b> <i>target_ID</i> <i>service_ID</i> <i>message</i>
 * </td><td> transmits a CALL message to a target, and wait for the reply
 * </td></tr>
 * <tr><td><b>send</b> <i>target_ID</i> <i>service_ID</i> <i>message</i>
 * </td><td> transmits a SEND message to a target
 * </td></tr>
 * <tr><td><b>var</b> <i>name</i> [<i>value</i>]
 * </td><td> gets (or sets, if a value is provided) a shared variable value
 * </td></tr>
 * </table>
 * <p/>
 * @author varkhan
 * @date 2/24/11
 * @time 9:32 PM
 */
public class P2PClient {

    private static void makePing(PeerHub hub, String name) {
        System.out.println("PING");
        System.out.println("--------------------------------------------------");
        System.out.println("To: @"+(name==null?"*":name));
        ping(hub, name);
    }

    private static void ping(PeerHub hub, String name) {
        PeerAddress node=hub.resolve(name);
        hub.ping(node);
    }

    private static void makeCall(PeerHub hub, String name, String method, String message) {
        System.out.println("CALL");
        System.out.println("--------------------------------------------------");
        System.out.println("To: @"+name+"/"+method);
        System.out.println(message);
        MesgReceiver rh = new MesgReceiver() {
            private long time = System.currentTimeMillis();

            public void receive(PeerAddress src, PeerAddress dst, String method, MesgPayload message) {
                System.out.println("--------------------------------------------------");
                System.out.println("From: "+src.name());
                try {
                    System.out.println(new String(message.getDataAsArray(),"UTF-8"));
                } catch (IOException e) {
                    // ignore
                }
                System.out.println("--------------------------------------------------");
                System.out.println("Time: "+(System.currentTimeMillis()-time)+"ms");
                System.out.println();
            }

            public void release() {
            }

            public boolean finished() {
                return System.currentTimeMillis()-time>getMaxDelay();
            }

            public long getMaxDelay() {
                // Expires in 10s
                return 10000;
            }

            public long getMaxCount() {
                return Integer.MAX_VALUE;
            }
        };
        call(hub, name, method, message, rh);
    }

    private static void waitCall(PeerHub hub, String name, String method, String message) {
        System.out.println("CALL");
        System.out.println("--------------------------------------------------");
        System.out.println("To: @"+name+"/"+method);
        System.out.println(message);
        System.out.println("--------------------------------------------------");
        SingleReceiver rh = new SingleReceiver(300000);
        long time = System.currentTimeMillis();
        call(hub,name,method, message, rh);
        if(!rh.isReceived()) {
            System.out.println("Call failed");
            return;
        }
        System.out.println("From: "+rh.getSrc().name());
        try {
            System.out.println(new String(rh.getMessage().getDataAsArray()));
        } catch (IOException e) {
            // ignore
        }
        System.out.println("--------------------------------------------------");
        System.out.println("Time: "+(System.currentTimeMillis()-time)+"ms");
    }

    private static void call(PeerHub hub, final String pointName, final String method, String message, final MesgReceiver hdlr) {
        if(pointName==null) return;
        final BinaryPayload msg=new BinaryPayload();
        msg.setData(Charset.forName("UTF-8").encode(message));
        final PeerNode node = hub.resolve(pointName).as(PeerNode.class);
        if(node!=null) try {
            node.call(method, msg, hdlr);
        } catch (IOException e) {
            // Ignore exceptions
        }
        else {
            hub.world().addListener(new PeerGroup.GroupListener() {
                private volatile boolean sent=false;

                public void release() {
                }

                public boolean finished() {
                    return sent;
                }

                public void update(PeerGroup group) {
                    // Nothing there
                }

                public void addHost(PeerGroup group, PeerHost point) {
                    PeerNode addr=point.as(PeerNode.class);
                    if(!sent&&addr!=null&&pointName.equals(point.name())) {
                        waitPointHeartBeat(point);
                        try {
                            addr.call(method, msg, hdlr);
                        }
                        catch(IOException e) {
                            // Ignore exception
                        }
                        sent=true;
                    }
                }

                public void delHost(PeerGroup group, PeerHost point) {
                    // Nothing there
                }
            });
        }
    }

    private static void makeSend(PeerHub hub, final String name, String method, String message) {
        System.out.println("SEND");
        System.out.println("--------------------------------------------------");
        System.out.println("To: @"+name+"/"+method);
        System.out.println(message);
        send(hub,name,method, message);
    }

    private static void send(PeerHub hub, final String name, final String method, String message) {
        if(name==null) return;
        final BinaryPayload msg=new BinaryPayload();
        msg.setData(Charset.forName("UTF-8").encode(message));
        final PeerNode node = hub.resolve(name).as(PeerNode.class);
        if(node!=null) try {
            node.call(method, msg, null);
        } catch (IOException e) {
            // Ignore exceptions
        }
        else {
            hub.world().addListener(new PeerGroup.GroupListener() {
                private volatile boolean sent=false;

                public void release() {
                }

                public boolean finished() {
                    return sent;
                }

                public void update(PeerGroup group) {
                    // Nothing there
                }

                public void addHost(PeerGroup group, PeerHost point) {
                    PeerNode addr=point.as(PeerNode.class);
                    if(!sent&&addr!=null&&name.equals(point.name())) {
                        waitPointHeartBeat(point);
                        try {
                            addr.call(method, msg, null);
                        }
                        catch(IOException e) {
                            // Ignore exception
                        }
                        sent=true;
                    }
                }

                public void delHost(PeerGroup group, PeerHost point) {
                    // Nothing there
                }
            });
        }
    }

    private static void makeInfo(PeerHub hub, String name) {
        PeerAddress peer = hub.resolve(name);
        if(peer!=null) {
            if(peer instanceof PeerHub) {
                printHubDetail("", (PeerHub) peer);
            }
            else if(peer instanceof PeerGroup) {
                printGroupDetail("", (PeerGroup) peer);
            }
            else if(peer instanceof PeerHost) {
                printHostDetail("", (PeerHost) peer);
            }
            else {
                System.out.println("Peer "+name+" not a recognized type");
            }
        }
        else System.out.println("Peer "+name+" not found");
    }

    private static void printHubDetail(String t, PeerHub w) {
        System.out.println(t+w.name()+" [");
        for(PeerGroup g: w.world().groups()) {
            printGroupDetail(t+"\t", g);
            System.out.println();
        }
        System.out.println(t+"]");
        System.out.println(t+"\t"+w.toString().replace("\n", "\n"+t+"\t"));
    }

    private static void printGroupInfo(String t, PeerGroup g) {
        System.out.println(t+g.name()+"\t"+g.state());
    }

    private static void printGroupDetail(String t, PeerGroup g) {
        System.out.println(t+g.name()+" {");
        for(PeerAddress n: g.hosts()) {
            if(n instanceof PeerGroup) printGroupInfo(t+"\t", (PeerGroup) n);
            else if(n instanceof PeerHost) printHostInfo(t+"\t", (PeerHost) n);
        }
        System.out.println(t+"}");
        System.out.println(t+"\t"+g.toString().replace("\n", "\n"+t+"\t"));
    }

    private static void printHostInfo(String t, PeerHost h) {
        System.out.println(t+h.name()+"\t"+h.state());
    }

    private static void printHostDetail(String t, PeerHost h) {
        System.out.println(t+h.name()+"\t"+h.state()+" [");
        for(PeerGroup g: h.groups()) {
             printGroupDetail(t+"\t", g);
        }
        System.out.println(t+"]");
        System.out.println(t+"\t"+h.toString().replace("\n", "\n"+t+"\t"));
    }

    private static void waitPointHeartBeat(PeerAddress point) {
        long delay = 0;
        PeerHost host = point.as(PeerHost.class);
        if(host!=null) delay = host.getHeartBeat();
        else {
            PeerGroup group = point.as(PeerGroup.class);
            if(group!=null) for(PeerAddress p: group.hosts()) {
                host = p.as(PeerHost.class);
                if(host!=null && host.getHeartBeat()>delay) delay = host.getHeartBeat();
            }
        }
        if(delay>0) try {
            Thread.sleep(delay);
        }
        catch(InterruptedException e) {
            // ignore
        }
    }

    private static void makeCons(final PeerHub hub, final String key, final String value) {
        Thread t = new Thread() {
            public void run() {
                if(value==null) System.out.println(hub.get(key));
                else System.out.println(hub.set(key, value));
            }
        };
        t.start();
    }

    private static String getStatus(PeerHub hub) {
        if(hub==null) return "ERROR[no hub]";
        StringBuilder buf = new StringBuilder();
        PeerNode localNode = hub.local();
        buf.append("<ServerStatus name=\"").append(localNode.name()).append("\" state=\"").append(localNode.state().name()).append("\">\n");
        buf.append("\t").append("<Nodes>\n");
        for(PeerAddress n: hub.world().hosts()) {
            if(n instanceof PeerHost) buf.append("\t\t").append("<Node name=\"").append(n.name()).append("\" state=\"").append(((PeerHost) n).state().name()).append("\"/>\n");
        }
        buf.append("\t").append("</Nodes>\n");
        buf.append("\t").append("<Groups>\n");
        for(PeerAddress g: hub.world().hosts()) {
            buf.append("\t\t").append("<Group name=\"").append(g.name()).append("\">\n");
            if(g instanceof PeerGroup) {
                for(PeerAddress n: ((PeerGroup) g).hosts()) {
                    if(n instanceof PeerHost) buf.append("\t\t\t").append("<Node name=\"").append(n.name()).append("\" state=\"").append(((PeerHost) n).state().name()).append("\"/>\n");
                }
            }
            buf.append("\t\t").append("</Group>\n");
        }
        buf.append("\t").append("</Groups>\n");
        buf.append("</ServerStatus>\n");
        return buf.toString();
    }

    private static interface PointReporter {
        public void report(String why, PeerAddress point);

        public boolean isMute();

        public void setMute(boolean mute);
    }

    private static PointGroupListener report(PeerHub hub, final String targetId, final PointReporter tr) {
        if(targetId!=null) {
            PointGroupListener ul = new PointGroupListener(targetId, tr);
            hub.world().addListener(ul);
            return ul;
        }
        return null;
    }

    private static final PointReporter stdoutTR=new PointReporter() {
        public boolean mute=true;

        public void report(String why, PeerAddress target) {
            if(mute) return;
            if(target instanceof PeerHost)
                System.out.append(why).append("\t").append(target.name()).append(": ").println(target.toString());
            else if(target instanceof PeerGroup) {
                System.out.append(why).append("\t").append(target.name()).println(" {");
                for(PeerAddress node : ((PeerGroup) target).hosts())
                    if(node instanceof PeerHost)
                        System.out.append("\t").append(node.name()).append(": ").println(((PeerHost) node).state());
                System.out.println("}");
            }
        }

        public boolean isMute() {
            return mute;
        }

        public void setMute(boolean mute) {
            this.mute=mute;
        }
    };


    private static void shorthelp(PrintStream pr) {
        pr.append("Usage: ").append(P2PClient.class.getSimpleName())
                .append(" [-n local_name] [-b bind_addr] [-a hub_addr] [-c hub_port] [name] | [send|call name method message]  ").println();
        pr.println();
    }

    private static void inlinehelp(PrintStream pr) {
        pr.append(P2PClient.class.getSimpleName()).append(" commands:").println();
        pr.append("help                       ").append("\t- ").append("this help").println();
        pr.append("exit                       ").append("\t- ").append("terminates this shell").println();
        pr.append("<none>       (<return>)    ").append("\t- ").append("displays abbreviated node and group information for the local hub").println();
        pr.append("info                       ").append("\t- ").append("displays detailed node and group information for the local hub").println();
        pr.append("join group_name port_number").append("\t- ").append("joins a group, with the specified multicast port").println();
        pr.append("leave group_name           ").append("\t- ").append("leaves a group").println();
        pr.append("call name method message   ").append("\t- ").append("transmits a CALL message to a named peer, and wait for the reply").println();
        pr.append("send name method message   ").append("\t- ").append("transmits a SEND message to a named peer").println();
        pr.append("var name [value]           ").append("\t- ").append("gets (or sets, if a value is provided) a shared variable value").println();
        pr.println();
    }

    private static void longhelp(PrintStream pr) {
        pr.append(P2PClient.class.getSimpleName()).append(" options:").println();
        pr.append("-n local_name              ").append("\t- ").append("specify the name to be advertised by this node").println();
        pr.append("-b bind_addr               ").append("\t- ").append("specify the local address this node binds to").println();
        pr.append("-a hub_addr                ").append("\t- ").append("specify the multicast address used by the hub").println();
        pr.append("-c hub_port                ").append("\t- ").append("specify the multicast port used by the hub").println();
        pr.append("name                       ").append("\t- ").append("the name of a specific peer to monitor").println();
        pr.append("call name method message   ").append("\t- ").append("transmit a CALL message to a named peer, wait for the reply, then exits").println();
        pr.append("send name method message   ").append("\t- ").append("transmit a SEND message to a named peer, then exits").println();
        pr.println();
    }

    public static void main(String[] args) throws Exception {
        String cname="admin";
        String sname="PeerHubReport";
        InetAddress gaddr=InetAddress.getByName("225.6.7.8");
        InetAddress laddr=null;
        int gport=1337;
        int lport=8001;
        long hearbeat=10000;
        int verb=0;
        List<String> al=new ArrayList<String>();
        int a=0;
        while(a<args.length) {
            if("-h".equals(args[a])||"--help".equals(args[a])) {
                shorthelp(System.out);
                longhelp(System.out);
                System.exit(0);
            }
            else if("-n".equals(args[a])||"--name".equals(args[a])) {
                a++;
                if(a>=args.length) {
                    shorthelp(System.err);
                    System.err.println("Node name not specified");
                    System.exit(5);
                }
                sname=args[a++];
            }
            else if("-c".equals(args[a])||"--hub-port".equals(args[a])) {
                a++;
                if(a>=args.length) {
                    shorthelp(System.err);
                    System.err.println("PeerHub port not specified");
                    System.exit(5);
                }
                try {
                    lport=Integer.parseInt(args[a]);
                }
                catch(NumberFormatException e) {
                    shorthelp(System.err);
                    System.err.println("Invalid hub port: "+args[a]);
                    System.exit(5);
                }
                a++;
            }
            else if("-a".equals(args[a])||"--hub-addr".equals(args[a])) {
                a++;
                if(a>=args.length) {
                    shorthelp(System.err);
                    System.err.println("PeerHub address not specified");
                    System.exit(5);
                }
                try {
                    gaddr=InetAddress.getByName(args[a]);
                }
                catch(UnknownHostException e) {
                    shorthelp(System.err);
                    System.err.println("Invalid hub address: "+args[a]);
                    System.exit(5);
                }
                a++;
            }
            else if("-b".equals(args[a])||"--bind".equals(args[a])) {
                a++;
                if(a>=args.length) {
                    shorthelp(System.err);
                    System.err.println("Local address not specified");
                    System.exit(5);
                }
                try {
                    laddr=InetAddress.getByName(args[a]);
                }
                catch(UnknownHostException e) {
                    shorthelp(System.err);
                    System.err.println("Invalid local address: "+args[a]);
                    System.exit(5);
                }
//                try {
//                    if(NetworkInterface.getByInetAddress(laddr)==null) {
//                        shorthelp(System.err);
//                        System.err.println("No such local address: "+args[a]);
//                        System.exit(5);
//                    }
//                }
//                catch(SocketException e) {
//                    shorthelp(System.err);
//                    System.err.println("Local address unavailable: "+args[a]);
//                    e.printStackTrace(System.err);
//                    System.exit(5);
//                }
                a++;
            }
            else if("-v".equals(args[a])||"--verbose".equals(args[a])) {
                a++;
                verb++;
            }
            else {
                al.add(args[a++]);
            }
        }
        String tname=cname+'.'+sname+'-'+Long.toHexString(System.nanoTime()).toUpperCase();
        final P2PHub hub;
        try {
            ThreadPoolExecutor exec=new ThreadPoolExecutor(5, 10, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100));
            MesgDispatcher disp=new AsyncDispatcher(exec, new JMXAverageMonitorReport(10, "Peer", "dispatch", tname), 10);
            TraceMessageExecutor trace=new TraceMessageExecutor(sname, disp);
            hub=new P2PHub(tname, trace, laddr, lport, gaddr, gport, hearbeat);
            trace.setHub(hub);
        }
        catch(IOException e) {
            System.err.println("IO error creating "+tname+":");
            e.printStackTrace();
            System.exit(2);
            return;
        }
        hub.start();
//        hub.join(cname, new MapProperties());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                hub.stop();
            }
        });
//        hub.start();
        if(al.size()>3) {
            final String type=al.get(0);
            final String targetId=al.get(1);
            final String serviceId=al.get(2);
            final String message=al.get(3);
            if("call".equalsIgnoreCase(type)) waitCall(hub, targetId, serviceId, message);
            else if("send".equalsIgnoreCase(type)) makeSend(hub, targetId, serviceId, message);
            else {
                shorthelp(System.err);
                System.err.println("Invalid call type: "+type);
                System.exit(5);
            }
            System.exit(0);
        }
        if(al.size()>0) report(hub, al.get(0), stdoutTR);
        PushbackInputStream ins=new PushbackInputStream(System.in);
        BufferedReader inr=new BufferedReader(new InputStreamReader(ins));
        System.out.flush();
        System.err.flush();
        System.out.print("> ");
        System.out.flush();
        for(;;) {
            try {
                synchronized(System.in) {
                    System.in.wait(100);
                }
            }
            catch(InterruptedException e) {
                // Ignore
            }
//            try {
//                if(ins.available()==0) continue;
//                int b = ins.read();
//                // Exit if we reached EOF or the user typed Ctrl-D (EOT=004)
//                if(b<0 || b==4) break;
//                ins.unread(b);
//            }
//            catch(IOException e) {
//                continue;
//            }
            String line;
            try {
                line=inr.readLine();
            }
            catch(IOException e) {
                line=null;
            }
            if(line!=null) {
                String[] cmdargs=Command.splitCmd(line.trim());
                if(verb>2) System.err.println("CMD: \""+Arrays.join("\" \"", cmdargs)+"\"");
                if(cmdargs.length>0&&cmdargs[0].length()>0) try {
                    final String cmd=cmdargs[0];
                    if(cmd.equalsIgnoreCase("exit")) System.exit(0);
                    else if(cmd.equalsIgnoreCase("help")||cmd.equalsIgnoreCase("h")||cmd.equalsIgnoreCase("?"))
                        inlinehelp(System.out);
                    else if(cmd.equalsIgnoreCase("info")) {
                        if(cmdargs.length<2) makeInfo(hub, null);
                        else makeInfo(hub, cmdargs[1]);
                    }
                    else if(cmd.equalsIgnoreCase("verb")) {
                        if(cmdargs.length<2) {
                            inlinehelp(System.err);
                            System.err.println("Too few arguments for command "+cmd);
                        }
                        else {
                            if(cmdargs[1].equals("0")||cmdargs[1].equalsIgnoreCase("false")) stdoutTR.setMute(true);
                            else if(cmdargs[1].equals("1")||cmdargs[1].equalsIgnoreCase("true"))
                                stdoutTR.setMute(false);
                        }
                    }
                    else if(cmd.equalsIgnoreCase("ping")) {
                        if(cmdargs.length<2) {
                            makePing(hub, null);
                        }
                        else {
                            makePing(hub, cmdargs[1]);
                        }
                        // Wait half a second to get reply before showing prompt again
                        try {
                            Thread.sleep(250);
                        }
                        catch(InterruptedException e) {
                            // ignore
                        }
                    }
                    else if(cmd.equalsIgnoreCase("call")) {
                        if(cmdargs.length<4) {
                            inlinehelp(System.err);
                            System.err.println("Too few arguments for command "+cmd);
                        }
                        else {
                            makeCall(hub, cmdargs[1], cmdargs[2], cmdargs[3]);
                            // Wait half a second to get reply before showing prompt again
                            try {
                                Thread.sleep(250);
                            }
                            catch(InterruptedException e) {
                                // ignore
                            }
                        }
                    }
                    else if(cmd.equalsIgnoreCase("send")) {
                        if(cmdargs.length<4) {
                            inlinehelp(System.err);
                            System.err.println("Too few arguments for command "+cmd);
                        }
                        else makeSend(hub, cmdargs[1], cmdargs[2], cmdargs[3]);
                    }
                    else if(cmd.equalsIgnoreCase("join")) {
                        if(cmdargs.length<4) {
                            inlinehelp(System.err);
                            System.err.println("Too few arguments for command "+cmd);
                        }
                        else try {
                            String groupName=cmdargs[1];
                            InetAddress groupAddr="-".equals(cmdargs[2]) ? gaddr : InetAddress.getByName(cmdargs[2]);
                            int groupPort=Integer.parseInt(cmdargs[3]);
                            hub.join(groupName, P2PGroup.getProperties(new MapProperties(), groupName, groupAddr, groupPort));
                        }
                        catch(NumberFormatException e) {
                            inlinehelp(System.err);
                            System.err.println("Invalid port number for "+cmd);
                        }
                    }
                    else if(cmd.equalsIgnoreCase("leave")) {
                        if(cmdargs.length<2) {
                            inlinehelp(System.err);
                            System.err.println("Too few arguments for command "+cmd);
                        }
                        else {
                            hub.leave(cmdargs[1]);
                        }
                    }
                    else if(cmd.equalsIgnoreCase("var")) {
                        if(cmdargs.length<2) {
                            inlinehelp(System.err);
                            System.err.println("Too few arguments for command "+cmd);
                        }
                        else {
                            makeCons(hub, cmdargs[1], cmdargs.length>2 ? cmdargs[2] : null);
                        }
                        // Wait half a second to get reply before showing prompt again
                        try {
                            Thread.sleep(250);
                        }
                        catch(InterruptedException e) {
                            // ignore
                        }
                    }
                    else if(cmd.equalsIgnoreCase("exit")) System.exit(0);
                    else {
                        System.err.println("Unknown command "+cmd);
                        System.err.println("Type 'help' for a listing of available commands");
                    }
                }
                catch(Throwable t) {
                    System.err.println("Command "+cmdargs[0]+" failed");
                    t.printStackTrace(System.err);
                }
                else {
                    System.out.println("--------------------------------------------------");
                    // FIXME add printout
//                    hub.showClusters(System.out);
//                    System.out.println();
//                    hub.showNodes(System.out);
                    System.out.println();
                }
                System.out.flush();
                System.err.flush();
                System.out.print("> ");
                System.out.flush();
            }
        }
    }


    private static class TraceMessageExecutor implements MesgDispatcher {
        private final String  hdr;
        private       PeerHub hub;
        private boolean mute=false;


        public synchronized boolean isMute() {
            return mute;
        }

        public synchronized void setMute(boolean mute) {
            this.mute=mute;
        }

        public TraceMessageExecutor(String hdr, MesgDispatcher disp) {
            this.hdr=hdr;
        }

        public PeerHub getHub() {
            return hub;
        }

        public void setHub(PeerHub hub) {
            this.hub=hub;
        }

        public void repl(PeerAddress src, PeerAddress dst, String method, MesgPayload repl, long sequence) {
            synchronized(this) { if(mute) return; }
            try {
                System.err.println(hdr+" REPL "
                                   +dst.name()+":"+method
                                   +" from "+src.name()+": \""
                                   +new String(repl.getDataAsArray(), Charset.forName("UTF-8")).replace("\"", "\\\"")+"\"");
            }
            catch(IOException e) {
                System.err.println(hdr+" REPL "+dst.name()+":"+method+" from "+src.name()+" >> ERROR "+e);
            }
        }

        @Override
        public void call(PeerAddress src, PeerAddress dst, String method, MesgPayload call, MesgPayload repl, long sequence, MesgSender send) {
            if("getStatus".equals(method)) {
                if(repl instanceof BinaryPayload)
                    ((BinaryPayload) repl).setData(getStatus(hub).getBytes(Charset.forName("UTF-8")));
                return;
            }
            synchronized(this) { if(mute) return; }
            try {
                System.err.println(hdr+" CALL "
                                   +dst.name()+":"+method
                                   +" from "+src.name()+": \""
                                   +new String(repl.getDataAsArray(), Charset.forName("UTF-8")).replace("\"", "\\\"")+"\"");
                if(repl instanceof BinaryPayload) {
                    String ans="> "+new String(repl.getDataAsArray(), Charset.forName("UTF-8"));
                    ((BinaryPayload) repl).setData(ans.getBytes(Charset.forName("UTF-8")));
                }
            }
            catch(IOException e) {
                System.err.println(hdr+" CALL "+dst.name()+":"+method+" from "+src.name()+" >> ERROR "+e);
            }
        }

        private final AtomicLong nextSequence=new AtomicLong(0);

        public long register(MesgReceiver handler) {
            long sequence=nextSequence.incrementAndGet();
//            registry.put(sequence, handler);
            return sequence;
        }

        public void unregister(long sequence) {
//            CommReceiver receiver = registry.remove(sequence);
//            if(receiver!=null) receiver.release();
        }
    }


    private static class PointGroupListener implements PeerGroup.GroupListener {
        private final String        targetId;
        private final PointReporter tr;
        private boolean end=false;

        public PointGroupListener(String targetId, PointReporter tr) {
            this.targetId=targetId;
            this.tr=tr;
        }

        @Override
        public void update(PeerGroup target) {
            if(targetId.equals(target.name())) tr.report("UPD", target);
        }

        @Override
        public void addHost(PeerGroup target, PeerHost point) {
            if(targetId.equals(target.name())) {
                tr.report("ADD", target);
                target.addListener(this);
            }
        }

        @Override
        public void delHost(PeerGroup target, PeerHost point) {
            if(targetId.equals(target.name())) {
                tr.report("DEL", target);
                end=true;
            }
        }

        @Override
        public void release() { }

        @Override
        public boolean finished() {
            return end;
        }

    }


}
