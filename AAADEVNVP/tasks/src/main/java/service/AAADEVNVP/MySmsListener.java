package service.AAADEVNVP;

import com.avaya.collaboration.sms.SmsListener;
import com.avaya.collaboration.sms.SmsRequest;
import com.avaya.collaboration.sms.SmsResponse;
import com.avaya.collaboration.util.logger.Logger;

public final class MySmsListener implements SmsListener
{

    private final SmsRequest smsRequest;
    private final Logger logger = Logger.getLogger(MySmsListener.class);

    public MySmsListener(final SmsRequest smsRequest)
    {
        this.smsRequest = smsRequest;
    }

    @Override
    public void responseReceived(final SmsResponse smsResponse)
    {
        if (logger.isFinestEnabled())
        {
            logger.finest("responseReceived: " + smsResponse.toString() + " for " + smsRequest.toString());
        }
    }
}
