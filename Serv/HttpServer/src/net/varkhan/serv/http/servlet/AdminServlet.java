package net.varkhan.serv.http.servlet;

import net.varkhan.base.management.state.health.HealthLevel;
import net.varkhan.base.management.state.health.HealthState;
import net.varkhan.base.management.state.lifecycle.LifeLevel;
import net.varkhan.base.management.state.lifecycle.LifeState;
import net.varkhan.core.pres.format.CssFormatter;
import net.varkhan.core.pres.format.HtmlFormatter;
import net.varkhan.core.pres.widget.menu.*;
import net.varkhan.core.pres.widget.page.HtmlWidget;
import net.varkhan.core.pres.widget.page.Page;
import net.varkhan.serv.Monitored;
import net.varkhan.serv.http.page.StateReportPage;

import java.io.IOException;
import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/17/12
 * @time 6:30 PM
 */
public class AdminServlet extends HtmlMenuServlet {

    private final Monitored monitored;

    public AdminServlet(Monitored m) {
        super(
                new HtmlWidget<HtmlFormatter,String[],Map<String,String[]>>("home").setName("AdminHome").setTitle("Admin home").setDesc("Administration: Home page"),
                new Menu<Page<HtmlFormatter,String[],Map<String,String[]>>>(),
                new HMenuSelectorRenderer<Page<HtmlFormatter,String[],Map<String,String[]>>,Map<String,String[]>>(
                        HMenuSelectorRenderer.Layout.HOVER,
                        new VMenuSelectorRenderer<Page<HtmlFormatter,String[],Map<String,String[]>>, Map<String, String[]>>(VMenuSelectorRenderer.Layout.HOVER)
                ));
        monitored=m;
        StringBuilder buf = new StringBuilder();
        CssFormatter css = new CssFormatter(buf);
        try {
            css.open();
            css.style(".menu");
            css.style(".menu.bar","background","#404040","box-shadow","2px 2px 4px #404040","margin","15px","border","1px #D0D0D0 solid");
            css.style("TABLE.menu.block","border","0","padding","0","margin","0","border-spacing", "0","background","#404040","color","#D0D0D0");
            css.style("TR.menu.block","border","0","padding","0","margin","0");
            css.style("TD.menu.block","border","0","padding","0","margin","0");
            css.style(".menu.item");
            css.style("DIV.menu.item","padding","10px","border-right","1px #D0D0D0 solid","font-weight","bold");
            css.style("DIV.menu.item.selected","background","#202020");
            css.style("DIV.menu.item A","color","#D0D0D0","text-decoration","none");
            css.style(".menu.item:hover A","text-decoration","underline");
            css.style(".menu.subs","opacity","0.9","filter","alpha(opacity=90)","box-shadow","2px 2px 4px #404040");
            css.style(".menu.subs DIV.menu.item","border","0");
            css.style(".menu.page","padding","15px");
            css.close();
        }
        catch(IOException e) { /* can't happen */ }
        page.addLink("cssdef",buf.toString());
        HtmlWidget<HtmlFormatter,String[],Map<String,String[]>> stat=new HtmlWidget<HtmlFormatter,String[],Map<String,String[]>>("stat");
        stat.setName("AdminStatus");
        stat.setTitle("Admin status");
        stat.setDesc("Administration: Status report");
        menu.addItem(stat, stat.id());
        StateReportPage<LifeLevel,LifeState,HtmlFormatter,Map<String,String[]>> status=buildStatusReport();
        menu.addItem(status, stat.id(), status.id());
        StateReportPage<HealthLevel,HealthState,HtmlFormatter,Map<String,String[]>> health=buildHealthReport();
        menu.addItem(health, stat.id(), health.id());
        HtmlWidget<HtmlFormatter,String[],Map<String,String[]>> tools=new HtmlWidget<HtmlFormatter,String[],Map<String,String[]>>("tools");
        tools.setName("AdminTools");
        tools.setTitle("Admin tools");
        tools.setDesc("Administration: Tools");
        menu.addItem(tools, tools.id());

    }

    private StateReportPage<HealthLevel,HealthState,HtmlFormatter,Map<String,String[]>> buildHealthReport() {
        StateReportPage<HealthLevel,HealthState,HtmlFormatter,Map<String,String[]>> health=new StateReportPage<HealthLevel,HealthState,HtmlFormatter,Map<String,String[]>>("health",monitored.health());
        health.setName("AdminHealthChecks");
        health.setTitle("Admin health checks");
        health.setDesc("Administration: Health Check reports");
        StringBuilder buf = new StringBuilder();
        CssFormatter css = new CssFormatter(buf);
        try {
            css.open();
            css.style("TABLE.state.report","border","1px #D0D0D0 solid","padding","0","margin","0","border-spacing", "0","background","#404040","color","#D0D0D0");
            css.style(".state.report TD.global", "padding", "2px", "padding-left", "10px");
            css.style(".state.report TD.check", "padding", "2px", "padding-left", "10px");
            css.style(".state.report TD.header", "padding", "2px", "padding-left", "10px", "background", "#E0E0E0", "color", "#A0A0A0", "font-weight", "bold");
            css.style(".state.report .name", "font-weight", "bold");
            css.style(".state.report .state", "font-weight", "bold");
            css.style(".state.report .level", "font-weight", "bold");

            css.style(".state.report .SYSTEM .level", "color", "slateblue");
            css.style(".state.report .MODULE .level", "color", "turquoise");
            css.style(".state.report .MAJOR .level", "color", "#202020");
            css.style(".state.report .MINOR .level", "color", "#808080");
            css.style(".state.report .ADVISORY .level", "color", "#A0A0A0");

            css.style(".state.report .RUNNING .state", "color", "forestgreen");
            css.style(".state.report .STARTING .state", "color", "goldenrod");
            css.style(".state.report .STOPPING .state", "color", "goldenrod");
            css.style(".state.report .STOPPED .state", "color", "firebrick");
            css.style(".state.report .HEALTHY .state", "color", "forestgreen");
            css.style(".state.report .UNSTABLE .state", "color", "goldenrod");
            css.style(".state.report .FAILED .state", "color", "firebrick");
            css.style(".state.report TR.check.block", "border", "1px #D0D0D0 solid", "background", "#E0E0E0", "color", "#202020");
            css.style(".state.report DIV.global", "border", "1px #404040 solid", "background", "#D0D0D0", "color", "#202020");
            css.close();
        }
        catch(IOException e) { /* can't happen */ }
        health.addLink("cssdef",buf.toString());
        return health;
    }

    protected StateReportPage<LifeLevel,LifeState,HtmlFormatter,Map<String,String[]>> buildStatusReport() {
        StateReportPage<LifeLevel,LifeState,HtmlFormatter,Map<String,String[]>> life=new StateReportPage<LifeLevel,LifeState,HtmlFormatter,Map<String,String[]>>("life",monitored.status());
        life.setName("AdminLifeCycle");
        life.setTitle("Admin life cycle");
        life.setDesc("Administration: LifeCyle state");
        StringBuilder buf = new StringBuilder();
        CssFormatter css = new CssFormatter(buf);
        try {
            css.open();
            css.style("TABLE.state.report","border","1px #D0D0D0 solid","padding","0","margin","0","border-spacing", "0","background","#404040","color","#D0D0D0");
            css.style(".state.report TD.global", "padding", "2px", "padding-left", "10px");
            css.style(".state.report TD.check", "padding", "2px", "padding-left", "10px");
            css.style(".state.report TD.header", "padding", "2px", "padding-left", "10px", "background", "#E0E0E0", "color", "#A0A0A0", "font-weight", "bold");
            css.style(".state.report .name", "font-weight", "bold");
            css.style(".state.report .state", "font-weight", "bold");
            css.style(".state.report .level", "font-weight", "bold");

            css.style(".state.report .SYSTEM .level", "color", "slateblue");
            css.style(".state.report .MODULE .level", "color", "turquoise");
            css.style(".state.report .MAJOR .level", "color", "#202020");
            css.style(".state.report .MINOR .level", "color", "#808080");
            css.style(".state.report .ADVISORY .level", "color", "#A0A0A0");

            css.style(".state.report .RUNNING .state", "color", "forestgreen");
            css.style(".state.report .STARTING .state", "color", "goldenrod");
            css.style(".state.report .STOPPING .state", "color", "goldenrod");
            css.style(".state.report .STOPPED .state", "color", "firebrick");
            css.style(".state.report .HEALTHY .state", "color", "forestgreen");
            css.style(".state.report .UNSTABLE .state", "color", "goldenrod");
            css.style(".state.report .FAILED .state", "color", "firebrick");
            css.style(".state.report TR.check.block", "border", "1px #D0D0D0 solid", "background", "#E0E0E0", "color", "#202020");
            css.style(".state.report DIV.global", "border", "1px #404040 solid", "background", "#D0D0D0", "color", "#202020");
            css.close();
        }
        catch(IOException e) { /* can't happen */ }
        life.addLink("cssdef",buf.toString());
        return life;
    }
}
