package service.AAADEVNVP;


import java.util.UUID;


import com.avaya.collaboration.call.Call;
import com.avaya.collaboration.call.media.DigitCollectorOperationCause;
import com.avaya.collaboration.call.media.MediaListenerAbstract;
import com.avaya.collaboration.call.media.PlayOperationCause;
import com.avaya.collaboration.call.media.RecordOperationCause;
import com.avaya.collaboration.call.media.SendDigitsOperationCause;
import com.avaya.collaboration.util.logger.Logger;

public class MyMediaListener extends MediaListenerAbstract{
	private final Call call;
    private final Logger logger = Logger.getLogger(getClass());
    
    /*
     * Constructor
     */
    public MyMediaListener(final Call call)
    {
        this.call = call;
    }
    
    @Override
    public void playCompleted(final UUID requestId, final PlayOperationCause cause)
    {
        logger.info("MyMediaListener, Play completed with the cause " + cause);
        logger.info("MyMediaListener, requestId: " + requestId);
        

    }
    
    @Override
    public void digitsCollected(final UUID requestId, final String digits, final DigitCollectorOperationCause cause)
    {
    	logger.info("digitsCollected not expected, requestId = " + requestId);
    }
    
    @Override
    public void sendDigitsCompleted(final UUID requestId, final SendDigitsOperationCause cause)
    {
        logger.info("sendDigitsCompleted not expected, requestId = " + requestId);
    }
    
    @Override
    public void recordCompleted(final UUID requestId, final RecordOperationCause cause)
    {
        logger.info("recordCompleted not expected, requestId = " + requestId);
    }

}
