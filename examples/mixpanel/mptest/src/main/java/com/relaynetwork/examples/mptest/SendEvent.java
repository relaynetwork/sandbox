package com.relaynetwork.examples.mptest;

import com.mixpanel.mixpanelapi.MixpanelAPI;
import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MessageBuilder;

import org.json.JSONObject;

public class SendEvent { 
  public static void main( String[] args ) {

    try { 
      if (args.length < 1) {
        System.out.println("Error: you must pass the mixpanel token.");
        System.exit(1);
      }

      String token = args[0],
             distinctId = "jdk-api-test-event-distinct-id",
             eventName = "JDK API Test Event";
      System.out.println("Using mixpanel token: " + token);

      MixpanelAPI api = new MixpanelAPI();
      MessageBuilder builder = new MessageBuilder(token);

      ClientDelivery delivery = new ClientDelivery();
      JSONObject props = new JSONObject();
      props.put("size", "gigantic");
      props.put("time", new java.util.Date().toString());

      delivery.addMessage(builder.event(
            distinctId,
            eventName,
            props));
      System.out.println("invoking deliver...");
      api.deliver(delivery);
      System.out.println("api.delivery returned.");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }
}
