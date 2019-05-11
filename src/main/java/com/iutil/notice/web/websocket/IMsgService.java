package com.iutil.notice.web.websocket;

/**
 * @author Wenyi Feng
 * @since 2018-09-13
 */
public interface IMsgService {

    // 群发
    boolean sendMsgToAllUser(String message);

}
