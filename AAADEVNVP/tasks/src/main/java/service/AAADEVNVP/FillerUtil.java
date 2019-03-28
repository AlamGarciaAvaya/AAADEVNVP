package service.AAADEVNVP;

import java.util.ArrayList;
import java.util.List;

public class FillerUtil {
	
	private static FillerUtil instance = null;
	
	public static FillerUtil getInstance()
	  {
	    if (instance == null) {
	      synchronized (FillerUtil.class)
	      {
	        if (instance == null) {
	          instance = new FillerUtil();
	        }
	      }
	    }
	    return instance;
	  }
	
	public List<String> checkBoxEmail()
	  {
	    List<String> contentTypeList = new ArrayList();
	    contentTypeList.add("Yes");
	    contentTypeList.add("No");
	    return contentTypeList;
	  }
	
	public List<String> checkSMS()
	  {
	    List<String> contentTypeSMS = new ArrayList();
	    contentTypeSMS.add("Yes");
	    contentTypeSMS.add("No");
	    return contentTypeSMS;
	  }
	
	public List<String> checkCall()
	  {
	    List<String> contentTypeCall = new ArrayList();
	    contentTypeCall.add("Yes");
	    contentTypeCall.add("No");
	    return contentTypeCall;
	  }

}
