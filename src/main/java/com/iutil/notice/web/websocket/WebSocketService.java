package com.iutil.notice.web.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * WebSocket service
 * @author Wenyi Feng
 * @since 2018-12-08
 */
@ServerEndpoint("/api/site/mq/web-socket")
@Component
@Slf4j
public class WebSocketService extends BaseMsg implements IMsgService {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //在线用户列表
    private static final ConcurrentMap<String, Session> sessions;

    static {
        sessions = new ConcurrentHashMap<>();
    }

    @Override
    public boolean sendMsgToAllUser(String message) {
        boolean result = false;
        for (ConcurrentMap.Entry<String, Session> entry : sessions.entrySet()) {
            Session session = entry.getValue();
            try {
                sendMsg(session, message);
                result = true;
            } catch (IOException e) {
                result = false;
                log.error(e.getMessage());
            }
        }
        return result;
    }


    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        String sessionId = session.getId();

        sessions.put(sessionId, session);
        addOnlineCount();           //在线数加1
        log.info("有新连接加入, Session Id=" + sessionId + "！当前在线人数为" + getOnlineCount());
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        String sessionId = session.getId();
        log.info("sessionId:" + sessionId);
        sessions.remove(sessionId);
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误, sessionId:" + session.getId());
        error.printStackTrace();
    }


    public static synchronized int getOnlineCount() {
        Integer sessionsKeyNum = sessions.keySet().size();
        if (onlineCount == sessionsKeyNum) return onlineCount;
        onlineCount = sessionsKeyNum;
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }
}
