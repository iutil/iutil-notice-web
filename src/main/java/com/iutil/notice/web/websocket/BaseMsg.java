package com.iutil.notice.web.websocket;

import javax.websocket.Session;
import java.io.IOException;

/**
 * @author Wenyi Feng
 * @since 2018-09-13
 */
public class BaseMsg {

    protected void sendMsg (Session session, String message)
            throws IOException
    {
        session.getBasicRemote().sendText(message);
    }

}
