/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.react.packagerconnection;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;

import static org.mockito.Mockito.*;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class JSPackagerClientTest {
  private static Map<String, JSPackagerClient.RequestHandler> createRH(
      String action, JSPackagerClient.RequestHandler handler) {
    Map<String, JSPackagerClient.RequestHandler> m =
      new HashMap<String, JSPackagerClient.RequestHandler>();
    m.put(action, handler);
    return m;
  }

  @Test
  public void test_onMessage_ShouldTriggerCallback() throws IOException {
    JSPackagerClient.RequestHandler handler = mock(JSPackagerClient.RequestHandler.class);
    final JSPackagerClient client = new JSPackagerClient("ws://not_needed", createRH("actionValue", handler));
    WebSocket webSocket = mock(WebSocket.class);
    ReconnectingWebSocket.WebSocketSender wbs = new ReconnectingWebSocket.WebSocketSender(webSocket);

    client.onMessage(
        wbs,
        ResponseBody.create(
          WebSocket.TEXT,
          "{\"version\": 1, \"target\": \"bridge\", \"action\": \"actionValue\"}"));
    verify(handler).onNotification(wbs);
  }

  @Test
  public void test_onMessage_WithInvalidContentType_ShouldNotTriggerCallback() throws IOException {
    JSPackagerClient.RequestHandler handler = mock(JSPackagerClient.RequestHandler.class);
    final JSPackagerClient client = new JSPackagerClient("ws://not_needed", createRH("actionValue", handler));
    WebSocket webSocket = mock(WebSocket.class);
    ReconnectingWebSocket.WebSocketSender wbs = new ReconnectingWebSocket.WebSocketSender(webSocket);

    client.onMessage(
        wbs,
        ResponseBody.create(
          WebSocket.BINARY,
          "{\"version\": 1, \"target\": \"bridge\", \"action\": \"actionValue\"}"));
    verify(handler, never()).onNotification(wbs);
  }

  @Test
  public void test_onMessage_WithoutTarget_ShouldNotTriggerCallback() throws IOException {
    JSPackagerClient.RequestHandler handler = mock(JSPackagerClient.RequestHandler.class);
    final JSPackagerClient client = new JSPackagerClient("ws://not_needed", createRH("actionValue", handler));
    WebSocket webSocket = mock(WebSocket.class);
    ReconnectingWebSocket.WebSocketSender wbs = new ReconnectingWebSocket.WebSocketSender(webSocket);

    client.onMessage(
        wbs,
        ResponseBody.create(
          WebSocket.TEXT,
          "{\"version\": 1, \"action\": \"actionValue\"}"));
    verify(handler, never()).onNotification(wbs);
  }

  @Test
  public void test_onMessage_With_Null_Target_ShouldNotTriggerCallback() throws IOException {
    JSPackagerClient.RequestHandler handler = mock(JSPackagerClient.RequestHandler.class);
    final JSPackagerClient client = new JSPackagerClient("ws://not_needed", createRH("actionValue", handler));
    WebSocket webSocket = mock(WebSocket.class);
    ReconnectingWebSocket.WebSocketSender wbs = new ReconnectingWebSocket.WebSocketSender(webSocket);

    client.onMessage(
        wbs,
        ResponseBody.create(
          WebSocket.TEXT,
          "{\"version\": 1, \"target\": null, \"action\": \"actionValue\"}"));
    verify(handler, never()).onNotification(wbs);
  }

  @Test
  public void test_onMessage_WithoutAction_ShouldNotTriggerCallback() throws IOException {
    JSPackagerClient.RequestHandler handler = mock(JSPackagerClient.RequestHandler.class);
    final JSPackagerClient client = new JSPackagerClient("ws://not_needed", createRH("actionValue", handler));
    WebSocket webSocket = mock(WebSocket.class);
    ReconnectingWebSocket.WebSocketSender wbs = new ReconnectingWebSocket.WebSocketSender(webSocket);

    client.onMessage(
        wbs,
        ResponseBody.create(
          WebSocket.TEXT,
          "{\"version\": 1, \"target\": \"bridge\"}"));
    verify(handler, never()).onNotification(wbs);
  }

  @Test
  public void test_onMessage_With_Null_Action_ShouldNotTriggerCallback() throws IOException {
    JSPackagerClient.RequestHandler handler = mock(JSPackagerClient.RequestHandler.class);
    final JSPackagerClient client = new JSPackagerClient("ws://not_needed", createRH("actionValue", handler));
    WebSocket webSocket = mock(WebSocket.class);
    ReconnectingWebSocket.WebSocketSender wbs = new ReconnectingWebSocket.WebSocketSender(webSocket);

    client.onMessage(
        wbs,
        ResponseBody.create(
          WebSocket.TEXT,
          "{\"version\": 1, \"target\": \"bridge\", \"action\": null}"));
    verify(handler, never()).onNotification(wbs);
  }

  @Test
  public void test_onMessage_WrongVersion_ShouldNotTriggerCallback() throws IOException {
    JSPackagerClient.RequestHandler handler = mock(JSPackagerClient.RequestHandler.class);
    final JSPackagerClient client = new JSPackagerClient("ws://not_needed", createRH("actionValue", handler));
    WebSocket webSocket = mock(WebSocket.class);
    ReconnectingWebSocket.WebSocketSender wbs = new ReconnectingWebSocket.WebSocketSender(webSocket);

    client.onMessage(
        wbs,
        ResponseBody.create(
          WebSocket.TEXT,
          "{\"version\": 2, \"target\": \"bridge\", \"action\": \"actionValue\"}"));
    verify(handler, never()).onNotification(wbs);
  }
}
