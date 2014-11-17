package net.varkhan.serv.http.page;

import net.varkhan.base.management.state.Level;
import net.varkhan.base.management.state.StateCheck;
import net.varkhan.base.management.state.StateReport;
import net.varkhan.base.management.state.State;
import net.varkhan.core.pres.format.HtmlFormatter;
import net.varkhan.core.pres.widget.page.HtmlPage;
import net.varkhan.core.pres.widget.page.HtmlWidget;

import java.io.IOException;

import static net.varkhan.core.pres.format.HtmlFormatter.ATR_CLASS;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/25/12
 * @time 12:26 AM
 */
public class StateReportPage<L extends Level,S extends State<L,S>,F extends HtmlFormatter,P> extends HtmlWidget<F,String[],P> implements HtmlPage<F,String[],P> {

    private StateReport<L,S> rep;

    public StateReportPage(String id, StateReport<L,S> rep) {
        super(id);
        this.rep = rep;
    }

    @Override
    public void render(F fmt, String[] loc, P par) throws IOException {
        rep.update();
        fmt.tb_(ATR_CLASS, "state report");
        fmt.tr_(ATR_CLASS, "header block");
        fmt.td_(ATR_CLASS, "header state","colspan","5").div("Global state")._td();
        fmt._tr();
        fmt.tr_(ATR_CLASS, "global block "+rep.state().name());
        fmt.td_(ATR_CLASS, "global state","colspan","5").div(rep.state().name())._td();
        fmt._tr();
        fmt.tr_(ATR_CLASS, "header block");
        fmt.td_(ATR_CLASS, "header name").div("Name")._td();
        fmt.td_(ATR_CLASS, "header level").div("Level")._td();
        fmt.td_(ATR_CLASS, "header desc").div("Description")._td();
        fmt.td_(ATR_CLASS, "header state").div("State")._td();
        fmt.td_(ATR_CLASS, "header reason").div("Reason")._td();
        fmt._tr();
        for(StateCheck<L,S> chk: rep.checks()) {
            fmt.tr_(ATR_CLASS, "check block "+chk.level().name()+" "+chk.state().name());
            fmt.td_(ATR_CLASS, "check name").div(chk.name())._td();
            fmt.td_(ATR_CLASS, "check level").div(chk.level().name())._td();
            fmt.td_(ATR_CLASS, "check desc").div(chk.desc())._td();
            fmt.td_(ATR_CLASS, "check state").div(chk.state().name())._td();
            fmt.td_(ATR_CLASS, "check reason").div(chk.reason())._td();
            fmt._tr();
        }
        fmt._tb();
    }

    @Override
    public String getLoc(String[] loc, P par) {
        if(loc==null || loc.length==0) return "";
        return "#"+loc[0];
    }
}
