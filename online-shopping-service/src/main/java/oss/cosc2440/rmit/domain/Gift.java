package oss.cosc2440.rmit.domain;

/**
* @author Group 8
*/

public interface Gift {
  /**
   * identify an object is a gift or not
   */
  boolean isGift();

  /**
   * set the greeting message of the gift product
   * @param msg greeting message
   */
  void setMessage(String msg);

  /**
   * retrieve the greeting message of the gift product
   */
  String getMessage();
}
