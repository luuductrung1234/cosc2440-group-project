package oss.cosc2440.rmit.domain;

/**
 * @author Luu Duc Trung - S3951127
 */
public interface Gift {
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
