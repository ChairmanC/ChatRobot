package com.Chairman.robot.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import com.google.gson.Gson;
import com.Chairman.robot.bean.ChatMessage;
import com.Chairman.robot.bean.CommonException;
import com.Chairman.robot.bean.FlightResult;
import com.Chairman.robot.bean.NewsResult;
import com.Chairman.robot.bean.OrderResult;
import com.Chairman.robot.bean.Result;
import com.Chairman.robot.bean.TrainResult;
/**
 * Created by youdelu on 2015/3/23.
 */
public class GetMessage{
    private static String API_KEY = "d0600b1cd6bdb4c4ed210bf867f41c79";
    private static String URL = "http://www.tuling123.com/openapi/api";
    private static String USERID = "67015";
    private static String NAME = "大白";
    private static String ERROR = "很抱歉，网络连接异常，请检查您的网络是否连通！";

    private static String menu = "您好，我是聊天机器人"+NAME+"，很高兴为您服务！"
            + "\n\n回复 “笑话” 讲笑话"
            + "\n回复 “故事” 讲故事"
            + "\n回复 “新闻” 看新闻"
            + "\n回复 “谜语” 猜谜语"
            + "\n回复 “歌词+歌名”查歌词"
            + "\n回复 “归属地+手机号”查归属地"
            + "\n回复 “歇后语”看歇后语"
            + "\n回复 “星座”看运势"
            + "\n回复 “诗词”查诗词"
            + "\n回复 “天气+地区”查天气情况"
            + "\n回复 “地区+公交路线”查公交"
            + "\n回复 “地区+附近的酒店”找酒店"
            + "\n回复 “下载+软件名”找软件"
            + "\n回复 “哪里到哪里的列车”查列车"
            + "\n回复 “哪里飞哪里的飞机”查航班"
            + "\n回复 “视频+名称”找视频"
            + "\n回复 “菜谱+名称”找菜谱"
            + "\n回复 “脑筋急转弯”脑筋急转弯游戏"
            + "\n回复 “地区 或 事物 或 人名”看简介"
            + "\n回复 “算数表达式”计算结果（如：1+2=?）"
            + "\n回复 “翻译+短语+成中文/英文”中英翻译"
            + "\n回复 “解梦+内容”解梦"
            + "\n回复 “问题+内容”回答问题"
            + "\n回复 “百科+内容”查询百科"
            + "\n回复 “成语接龙”成语接龙游戏"
            + "\n回复 “时间”校时"
            + "\n回复 “退出”结束聊天"
            + "\n\n回复其它则陪聊。";
    /**
     * 发送一个消息，并得到返回的消息
     * @param msg
     * @return
     */
    public static ChatMessage sendMsg(String msg) {
        ChatMessage message = new ChatMessage();
        message.setType(ChatMessage.Type.INPUT);
        message.setDate(new Date());
        String value = "";
        if(msg.trim().equals("")
                || msg.trim().equals("menu")
                ||msg.trim().equals("菜单")
                || msg.trim().equals("?")){
            message.setMsg(menu);
            return message;
        }
        String url = setParams(msg);
        String res = doGet(url);
        Gson gson = new Gson();
        Result result = gson.fromJson(res, Result.class);

        if (result.getText() == null || result.getText().trim().equals("")
                ||result.getCode()==40004||result.getCode()==40005
                ||result.getCode()==40006||result.getCode()==40007){
            message.setMsg("没听明白，能说得自然一点吗？");
            return message;
        }
        if(result.getText().contains("未找出结果")||result.getText().contains("有道词典")||result.getText().contains("没有找到与您查询的")){
            message.setMsg("我也不太清楚哦");
            return message;
        }
        if(result.getCode()==200000){
            value =result.getText();
            value += result.getUrl();
        }else if(result.getCode()==302000){
            //新闻
            NewsResult news = gson.fromJson(res,NewsResult.class);
            value = news.getText();
            for(int i = 0 ;i < news.getList().size();i++){
                value += "\n"+news.getList().get(i).getSource();
                value += ":"+  news.getList().get(i).getArticle();
                value += news.getList().get(i).getDetailurl();
            }
        }else if(result.getCode()==305000){
            //列车
            TrainResult news = gson.fromJson(res,TrainResult.class);
            value = news.getText();
            for(int i = 0 ;i < news.getList().size();i++){
                value += "\n车次："+ news.getList().get(i).getTrainnum();
                value +="\n起始站："+ news.getList().get(i).getStart();
                value +="\n到达站："+ news.getList().get(i).getTerminal();
                value +="\n开始时间："+ news.getList().get(i).getStarttime();
                value +="\n到达时间："+ news.getList().get(i).getEndtime();
                value +="\n详情："+ news.getList().get(i).getDetailurl();
            }
        }else if(result.getCode()==306000){
            //航班
            FlightResult news = gson.fromJson(res,FlightResult.class);
            value = news.getText();
            for(int i = 0 ;i < news.getList().size();i++){
                value +="\n航班："+ news.getList().get(i).getFlight();
                value +="\n航班路线："+ news.getList().get(i).getRoute();
                value +="\n起飞时间："+ news.getList().get(i).getStarttime();
                value +="\n到达时间："+ news.getList().get(i).getEndtime();
                value +="\n航班状态："+ news.getList().get(i).getState();
                value +="\n详情："+ news.getList().get(i).getDetailurl();
            }
        }else if(result.getCode()==309000){
            //酒店
            OrderResult news = gson.fromJson(res,OrderResult.class);
            value = news.getText();
            for(int i = 0 ;i < news.getList().size();i++){
                value +="\n酒店名称："+ news.getList().get(i).getName();
                value +="\n价格："+ news.getList().get(i).getPrice();
                value +="\n满意度："+ news.getList().get(i).getSatisfaction();
                value +="\n数量："+ news.getList().get(i).getCount();
                value +="\n详情："+ news.getList().get(i).getDetailurl();
            }
        }else if(result.getCode()==304000){
            OrderResult news = gson.fromJson(res,OrderResult.class);
            value = news.getText();
            for(int i = 0 ;i < news.getList().size();i++){
                value +="\n软件名称："+ news.getList().get(i).getName();
                value +="\n下载量："+ news.getList().get(i).getCount();
                value +="\n详情："+ news.getList().get(i).getDetailurl();
            }
        }else if(result.getCode()==308000){
            OrderResult news = gson.fromJson(res,OrderResult.class);
            value = news.getText();
            for(int i = 0 ;i < news.getList().size();i++){
                value +="\n名称："+ news.getList().get(i).getName();
                value +="\n详情："+ news.getList().get(i).getInfo();
                value +="\n观看地址："+ news.getList().get(i).getDetailurl();
            }
        }else if(result.getCode()==311000){
            OrderResult news = gson.fromJson(res,OrderResult.class);
            value = news.getText();
            for(int i = 0 ;i < news.getList().size();i++){
                value +="\n名称："+ news.getList().get(i).getName();
                value +="\n价格："+ news.getList().get(i).getPrice();
                value +="\n详情："+ news.getList().get(i).getDetailurl();
            }
        }else{
            value=  result.getText();
        }
        if(value.contains("图灵机器人")){
            value.replace("图灵机器人",NAME);
        }
        if(value.contains("3岁了")){
            value.replace("3岁了",NAME+"两岁了");
        }
        if(value.contains("CUIT大二学生")){
            value.replace("CUIT大二学生","的开发者是陈泽贤同学");
        }else if(value.contains("CUIT")){
            value.replace("CUIT","陈泽贤同学");
        }else if(value.contains("CUIT")){
            value.replace("CUIT","陈泽贤同学");
        }
        message.setMsg(value);
        return message;
    }

    /**
     * 拼接Url
     * @param msg
     * @return
     */
    private static String setParams(String msg)
    {
        if(msg.contains(NAME)){
            msg.replaceAll(NAME,"图灵机器人");
        }
        try{
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e)	{
            e.printStackTrace();
        }
        return URL + "?key=" + API_KEY + "&info=" + msg+"&userid="+USERID;
    }

    /**
     * Get请求，获得返回数据
     * @param urlStr
     * @return
     */
    private static String doGet(String urlStr){
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try{
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200){
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1){
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else{
                throw new CommonException(ERROR);
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new CommonException(ERROR);
        } finally{
            try{
                if (is != null)
                    is.close();
            } catch (IOException e){
                e.printStackTrace();
            }

            try{
                if (baos != null)
                    baos.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            conn.disconnect();
        }
    }

}
