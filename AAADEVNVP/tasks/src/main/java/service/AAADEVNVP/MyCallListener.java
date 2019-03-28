package service.AAADEVNVP;


import java.net.URISyntaxException;

import com.avaya.collaboration.call.Call;
import com.avaya.collaboration.call.CallListenerAbstract;
import com.avaya.collaboration.call.CallTerminationCause;
import com.avaya.collaboration.call.MediaType;
import com.avaya.collaboration.call.Participant;
import com.avaya.collaboration.call.TheCallListener;
import com.avaya.collaboration.call.media.MediaFactory;
import com.avaya.collaboration.call.media.MediaServerInclusion;
import com.avaya.collaboration.call.media.MediaService;
import com.avaya.collaboration.call.media.PlayItem;

@TheCallListener
public class MyCallListener extends CallListenerAbstract {
	private final Call call;

	public MyCallListener(Call call){
		this.call = call;
	}
	@Override
	public void callIntercepted(Call call) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addParticipantFailed(Call call, Participant failedParticipant,
			CallTerminationCause cause) {
		// TODO Auto-generated method stub
		super.addParticipantFailed(call, failedParticipant, cause);
	}

	@Override
	public void callAlerting(Participant alertingParty) {
		// TODO Auto-generated method stub
		super.callAlerting(alertingParty);
	}

	@Override
	public void callAnswered(Call call) {
		call.getCallPolicies().setMediaServerInclusion(
				MediaServerInclusion.AS_NEEDED);
		String announcement = "http://breeze2-132.collaboratory.avaya.com/services/AAADEVURIEL_WAV3/ControladorGrabaciones/web/Grabaciones/Helsinki.wav";
		PlayItem playItem = null;
		try {
			playItem = MediaFactory.createPlayItem().setInterruptible(true)
					.setIterateCount(1).setSource(announcement);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final MediaService mediaService = MediaFactory.createMediaService();
		final Participant participant = call.getCalledParty();
		final MyMediaListener myMediaListener = new MyMediaListener(call);
		mediaService.play(participant, playItem, myMediaListener);
	}

	@Override
	public void callOriginated(Call call) {
		call.getCallPolicies().setMediaServerInclusion(
				MediaServerInclusion.AS_NEEDED);
		String announcement = "http://breeze2-132.collaboratory.avaya.com/services/AAADEVURIEL_WAV3/ControladorGrabaciones/web/Grabaciones/Helsinki.wav";
		PlayItem playItem = null;
		try {
			playItem = MediaFactory.createPlayItem().setInterruptible(true)
					.setIterateCount(1).setSource(announcement);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final MediaService mediaService = MediaFactory.createMediaService();
		final Participant participant = call.getCalledParty();
		final MyMediaListener myMediaListener = new MyMediaListener(call);
		mediaService.play(participant, playItem, myMediaListener);
		

	}

	@Override
	public void callTerminated(Call call, CallTerminationCause cause) {
		// TODO Auto-generated method stub
		super.callTerminated(call, cause);
	}

	@Override
	public void mediaDetected(Participant partySendingMedia,
			MediaType mediaTypeDetected) {
		// TODO Auto-generated method stub
		super.mediaDetected(partySendingMedia, mediaTypeDetected);
	}

	@Override
	public void participantDropped(Call call, Participant droppedParticipant,
			CallTerminationCause cause) {
		// TODO Auto-generated method stub
		super.participantDropped(call, droppedParticipant, cause);
	}
	

}
