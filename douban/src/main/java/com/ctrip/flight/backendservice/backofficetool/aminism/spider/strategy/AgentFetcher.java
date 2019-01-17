package com.ctrip.flight.backendservice.backofficetool.aminism.spider.strategy;

import com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool.SpiderTool;
import com.ctrip.flight.backendservice.backofficetool.spider.log.CLogManager;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * Created by hello world on 2017/1/12.
 */
public class AgentFetcher implements Runnable{
    private static Elements hostports;
    private static Document doc;
    private static final Logger log = LoggerFactory.getLogger(AgentFetcher.class);
    @Override
    public void run() {
        try {
            SpiderTool.setAgent(InetAddress.getLocalHost().getHostAddress().toString(),"80");
            //AgentFetcher.getKuaidailiAgent();
           // AgentFetcher.getXicidailiAgent();
        } catch (UnknownHostException e) {
            log.info(e.getMessage());
        }
    }

    public void getKuaidailiAgent(){
        String[] agentUrl1s={"http://www.kuaidaili.com/free/inha/","http://www.kuaidaili.com/free/intr/","http://www.kuaidaili.com/free/outha/","http://www.kuaidaili.com/free/outtr/"};
        int maxnum=10;
        for (String agentUrl:agentUrl1s) {
            for (int i = 1; i <=maxnum; i++) {
                String url=agentUrl+i;
                try{
                    doc= SpiderTool.Getdoc(url,3,false);
                    if(doc==null)
                        break;
                    if(i==1)
                        maxnum=getKuaidaiMaxnum(doc);
                    hostports=doc.select("tbody tr");
                    for(Element hp:hostports){
                        Elements tds=hp.select("td");
                        String host=tds.get(0).text();
                        String port=tds.get(1).text();
                        String time=SpiderTool.removeZh(tds.get(5).text());
                        if(isIPAdress(host) && isNumeric(port)){
                            try{
                                if((int)Float.parseFloat(time)<=1)
                                    SpiderTool.setAgent(host,port);
                            }catch (Exception e){
                                log.warn(e.getMessage());
                            }
                        }
                        else{
                            getHostPort(tds);
                        }
                    }
                }catch (Exception e){
                    CLogManager.error(e);
                }
            }
        }
    }

    public void getXicidailiAgent(){
        String[] agentUrl1s={"http://www.xicidaili.com/nn/","http://www.xicidaili.com/nt/","http://www.xicidaili.com/wn/","http://www.xicidaili.com/wt/"};
        int maxnum=10;
        for (String agentUrl:agentUrl1s) {
            for (int i = 1; i <=maxnum ; i++) {
                String url=agentUrl+i;
                try{
                    doc= SpiderTool.Getdoc(url,3,false);
                    if(doc==null)
                        break;
                    if(i==1)
                        maxnum=getXiciMaxnum(doc);
                    hostports = doc.select("table#ip_list").select("tbody tr");
                    hostports.remove(0);
                    for(Element hp:hostports) {
                        Elements tds = hp.select("td");
                        String host=tds.get(1).text();
                        String port=tds.get(2).text();
                        String time=SpiderTool.removeZh(tds.get(6).select("div.bar").attr("title"));
                        if(isIPAdress(host) && isNumeric(port)){
                            try{
                                if((int)Float.parseFloat(time)<=1)
                                    SpiderTool.setAgent(host,port);
                            }catch (Exception e){
                                log.warn(e.getMessage());
                            }
                        }
                        else{
                            getHostPort(tds);
                        }
                    }
                }catch (Exception e){
                    CLogManager.error(e);
                }
            }
        }
    }

    public void getHostPort(Elements tds){
        String host=null;
        String port=null;
        for (Element td:tds){
            if(host!=null && port!=null){
                SpiderTool.setAgent(host,port);
                break;
            }
            String str=td.text();
            if(isIPAdress(str)){
                host=str;
                continue;
            }
            if(isNumeric(str) && str.length()>1){
                port=str;
                continue;
            }
        }
    }

    public int getKuaidaiMaxnum(Document doc){
        Elements pages=doc.select("div#listnav").select("a[href]");
        return getMaxNum(pages);
    }
    public int getXiciMaxnum(Document doc){
        Elements pages=doc.select("div.pagination").select("a[href]");
        return getMaxNum(pages);
    }

    public  int getMaxNum(Elements pages){
        for (int i = pages.size()-1; i >0 ; i--) {
            String str=pages.get(i).text();
            if(isNumeric(str))
                return Integer.parseInt(str);
        }
        return 0;
    }

    public  boolean isIPAdress(String str){
        Pattern pattern=Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!pattern.matcher(str).matches())
            return false;
        return true;
    }


}
