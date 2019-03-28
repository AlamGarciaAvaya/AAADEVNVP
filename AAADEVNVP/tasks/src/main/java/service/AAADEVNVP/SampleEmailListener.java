package service.AAADEVNVP;

import com.avaya.collaboration.email.EmailListener;
import com.avaya.collaboration.email.EmailResponse;

public class SampleEmailListener implements EmailListener
{
   @Override
  public void responseReceived(final EmailResponse emailResponse)
  {
      System.out.println("receiveResponse status=" + emailResponse.getStatus());
      System.out.println("receiveResponse detail=" + emailResponse.getDetail());
      for (final String recipient : emailResponse.getInvalidAddresses())
      {
          System.out.println("invalid address " + recipient);
      }
      for (final String recipient : emailResponse.getValidUnsentAddresses())
      {
          System.out.println("could not send to " + recipient);
      }
      for (final String recipient : emailResponse.getValidSentAddresses())
      {
          System.out.println("sent to " + recipient);
      }
  }
}
