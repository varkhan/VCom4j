package net.varkhan.core.pres.format;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/13/11
 * @time 6:53 PM
 */
public class XmlDocFormatter extends XmlFormatter {

    private String version = "1.0";
    private String encoding= "utf-8";
    private String docname = "xml";

    public XmlDocFormatter(Appendable out) { super(out); }

    public XmlDocFormatter(Appendable out, boolean forceCloseTags) { super(out, forceCloseTags); }

    public void setVersion(String version) {
        if(opened) throw new IllegalStateException("Cannot set headers after document has been opened");
        this.version=version;
    }

    public void setEncoding(String encoding) {
        if(opened) throw new IllegalStateException("Cannot set headers after document has been opened");
        this.encoding=encoding;
    }

    public void setDocName(String docname) {
        if(opened) throw new IllegalStateException("Cannot set headers after document has been opened");
        this.docname=docname;
    }

    public void open() throws IOException, IllegalStateException {
        super.open();
        writeXmlDeclaration(this, version, encoding);
        if(docname!=null) writeXmlDoctype(this, docname, null, null, null);
    }

    public void close() throws IOException, IllegalStateException { super.close(); }


    public static void writeXmlDeclaration(Appendable out, String version, String encoding) throws IOException {
        if(version==null) version = "1.0";
        if(encoding==null) encoding = "utf-8";
        out.append("<?xml version=\"").append(version).append("\" encoding=\"").append(encoding).append("\"?>");
    }

    public static void writeXmlDoctype(Appendable out, String name, String pub, String sys, String sub) throws IOException {
        out.append("<!DOCTYPE ").append(name);
        if(pub!=null) {
            out.append(" PUBLIC \"").append(pub).append("\"");
            if(sys!=null) out.append(" \"").append(sys).append("\"");
        }
        else if(sys!=null) {
            out.append(" SYSTEM \"").append(sys).append("\"");
        }
        if(sub!=null) {
            out.append(" [\n").append(sub).append("\n]");
        }
        out.append('>');
    }
}
