package net.varkhan.pres.widget.page;

import net.varkhan.pres.format.HtmlFormatter;

import java.io.IOException;
import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/28/12
 * @time 4:23 PM
 */
public class HtmlTable<F extends HtmlFormatter,L,P> extends ContainerWidget<L,P> implements HtmlPage<F,L,P> {
    protected HtmlPage<F,L,P>[][] pages = null;

    public HtmlTable(String id) {
        super(id);
    }

    public Collection<HtmlPage<F,L,P>> widgets() {
        List<HtmlPage<F,L,P>> pp = new ArrayList<HtmlPage<F,L,P>>();
        if(pages!=null) for(HtmlPage<F,L,P>[] prow: pages) {
            if(prow!=null) for(HtmlPage<F,L,P> page: prow) pp.add(page);
        }
        return pp;
    }

    public void render(F fmt, L loc, P par) throws IOException {
        fmt.tb_("id", id);
        if(pages!=null) for(int i=0;i<pages.length;i++) {
            HtmlPage<F,L,P>[] prow=pages[i];
            fmt.tr_("id", id+"_"+i);
            if(prow!=null) for(int j=0;j<prow.length;j++) {
                HtmlPage<F,L,P> page=prow[j];
                fmt.td_("id", id+"_"+i+"_"+j);
                if(page!=null) page.render(fmt, loc, par);
                fmt._td();
            }
            fmt._tr();
        }
        fmt._tb();
    }

    public String getLoc(L loc, P par) {
        return "#"+name;
    }

    public void setPage(HtmlPage<F,L,P> page, int i, int j) {
        if(pages==null) {
            pages = new HtmlPage[j+1][];
        }
        else if(pages.length<=j) {
            pages = Arrays.copyOf(pages,j+1);
        }
        HtmlPage<F,L,P>[] prow = pages[j];
        if(prow==null) {
            pages[j] = prow = new HtmlPage[i+1];
        }
        else if(prow.length<=i) {
            pages[j] = prow = Arrays.copyOf(prow,i+1);
        }
        prow[i] = page;
    }

    public void addPageRow(HtmlPage<F,L,P> page, int j) {
        if(pages==null) {
            pages = new HtmlPage[j+1][];
        }
        else if(pages.length<=j) {
            pages = Arrays.copyOf(pages,j+1);
        }
        HtmlPage<F,L,P>[] prow = pages[j];
        if(prow==null) {
            pages[j] = prow = new HtmlPage[1];
            prow[0] = page;
        }
        else {
            int i = prow.length;
            while(i>0) {
                i--;
                if(prow[i]!=null) {
                    i ++;
                    if(i>=prow.length) pages[j]=prow=Arrays.copyOf(prow,i+1);
                    prow[i] = page;
                    return;
                }
            }
        }
    }

    public void addPageCol(HtmlPage<F,L,P> page, int i) {
        if(pages==null) {
            pages = new HtmlPage[1][];
            HtmlPage<F,L,P>[] prow = pages[1] = new HtmlPage[1];
            prow[0] = page;
            return;
        }
        int j = pages.length;
        while(j>0) {
            j--;
            HtmlPage<F,L,P>[] prow = pages[j];
            if(prow!=null && i<prow.length && prow[i]!=null) {
                j ++;
                if(pages.length<=j) pages = Arrays.copyOf(pages,j+1);
                prow = pages[j];
                if(prow==null) {
                    pages[j] = prow = new HtmlPage[i+1];
                }
                else if(prow.length>=i) {
                    pages[j] = prow = Arrays.copyOf(prow,i+1);
                }
                prow[i] = page;
                return;
            }
        }
    }

}
